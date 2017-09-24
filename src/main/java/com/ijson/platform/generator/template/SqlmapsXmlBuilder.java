
package com.ijson.platform.generator.template;

import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.ColumnEntity;
import com.ijson.platform.generator.model.TableEntity;

import java.util.List;
import java.util.Map;

/**
 * 生成myibatis的sqlmaps的xml模板
 */
public class SqlmapsXmlBuilder implements TemplateHanlder {

    private String pkId = "";

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdSqlmapsXml(prefix, tables, config);
    }

    /**
     * description: 生成sqlmaps.xml文件
     */
    public void createdSqlmapsXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "resources/ibatis/sqlmaps/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table : tables) {
                StringBuilder result = new StringBuilder("");
                result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                result.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \n");
                result.append("    \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n");
                result.append("<mapper namespace=\"").append(jarPath).append(".entity.").append(table.getTableAttName()).append("\">\n\n");

                result.append(getResultMap(jarPath, table));

                result.append(getInsertSql(jarPath, table));

                result.append(getUpdateSql(jarPath, table));

                result.append(getIbatisSql(jarPath, table));

                result.append("</mapper>\n");
                FileOperate.getInstance().newCreateFile(xmlPath + table.getTableAttName() + "Mapper.xml",
                        result.toString());
            }
        }
    }

    /**
     * description: 返回resultMap定义
     */
    private String getResultMap(String jarPath, TableEntity table) {
        StringBuilder result = new StringBuilder("");
        StringBuilder cols = new StringBuilder("");//存放列信息
        StringBuilder variable = new StringBuilder("");//存放定义变量
        StringBuilder where = new StringBuilder("");
        int count = table.getColumns().size();
        if (count > 0) {
            String tableName = table.getTableAttName();
            cols.append("  <sql id=\"selectSql").append(tableName).append("\">\n    ");
            variable.append("  <sql id=\"variableSql").append(tableName).append("\">\n    ");
            where.append("  <sql id=\"whereSql").append(tableName).append("\">\n    where 1=1 \n    ");
            result.append("  <resultMap id=\"result").append(tableName).append("\" type=\"").append(jarPath).append(".entity.").append(tableName).append("\">\n");
            for (int i = 0; i < count; i++) {
                ColumnEntity col = table.getColumns().get(i);
                String dian = ",";
                if (i == count - 1) {
                    dian = "";
                }
                String colType = getJdbcType(col.getColumnTypeName(), col.getPrecision());//字段类型

                result.append("      <result property=\"").append(col.getAttrName()).append("\" column=\"").append(col.getColumnName()).append("\" jdbcType=\"").append(colType).append("\"/>\n");
                cols.append(col.getColumnName()).append(dian);
                variable.append("#{").append(col.getAttrName()).append(",jdbcType=").append(colType).append("}").append(dian);
                where.append("<if test=\"").append(col.getAttrName()).append(" != null and ").append(col.getAttrName()).append(" != ''\"> and ").append(col.getColumnName()).append("=#{").append(col.getAttrName()).append("}</if>\n    ");
            }
            result.append("  </resultMap>\n\n");
            cols.append("\n  </sql>\n");
            variable.append("\n  </sql>\n");
            where.append("</sql>\n");
            result.append(cols.toString());
            result.append(variable.toString());
            result.append(where.toString());
        }
        return result.toString();
    }

    /**
     * description: 生成insert的sql
     */
    private String getInsertSql(String jarPath, TableEntity table) {

        return "" + "  <insert id=\"insert" + table.getTableAttName() + "\" parameterType=\"" + jarPath + ".entity."
                + table.getTableAttName() + "\">\n" +
                "       INSERT INTO " + table.getTableName() + " ( <include refid=\"selectSql"
                + table.getTableAttName() + "\"/>\n" +
                "       ) VALUES ( <include refid=\"variableSql" + table.getTableAttName() + "\"/>)\n" +
                "   </insert>\n";
    }

    /**
     * description: 生成update的sql
     */
    private String getUpdateSql(String jarPath, TableEntity table) {
        StringBuilder result = new StringBuilder("");
        result.append("  <update id=\"update").append(table.getTableAttName()).append("\" parameterType=\"").append(jarPath).append(".entity.").append(table.getTableAttName()).append("\">\n");
        int count = table.getColumns().size();
        if (count > 0) {
            result.append("       UPDATE ").append(table.getTableName()).append(" SET \n     ");
            StringBuilder isNotNull = new StringBuilder("");
            StringBuilder isNull = new StringBuilder("     ");
            for (int i = 0; i < count; i++) {
                ColumnEntity col = table.getColumns().get(i);
                if (col.getAttrName().equalsIgnoreCase(table.getPKColumn())) {
                    pkId = col.getColumnName();
                    continue;
                }
                if (0 == col.getIsNullable()) {
                    isNotNull.append(col.getColumnName()).append("=#{").append(col.getAttrName()).append("},\n");
                } else {
                    isNull.append("<if test=\"").append(col.getAttrName()).append(" != null and ").append(col.getAttrName()).append(" != ''\">,").append(col.getColumnName()).append("=#{").append(col.getAttrName()).append("}</if>\n     ");
                }
            }
            int isNotNullLength = isNotNull.length();
            if (isNotNullLength > 2) {
                result.append(isNotNull.substring(0, isNotNullLength - 2)).append("\n     ");
            }
            result.append(isNull.toString());
            result.append("\n      WHERE ").append(pkId).append(" = #{").append(table.getPKColumn()).append("}\n");
        }
        result.append("   </update>\n");
        return result.toString();
    }

    /**
     * description: 生成delete和select的sql
     */
    private String getIbatisSql(String jarPath, TableEntity table) {
        StringBuilder result = new StringBuilder("");
        if (Validator.isNotNull(table.getPKColumn())) {
            result.append("  <delete id=\"deleteById\" parameterType=\"java.util.Map\">\n");
            result.append("       DELETE FROM ").append(table.getTableName());
            result.append(" WHERE ").append(pkId).append(" = #{").append(table.getPKColumn()).append("}\n");
            result.append("   </delete>\n");

            result.append("  <select id=\"selectById\" resultMap=\"result").append(table.getTableAttName()).append("\" parameterType=\"java.util.Map\">\n");
            result.append("       select <include refid=\"selectSql").append(table.getTableAttName()).append("\"/> from ").append(table.getTableName());
            result.append(" WHERE ").append(pkId).append(" = #{").append(table.getPKColumn()).append("}\n");
            result.append("   </select>\n");
        }
        result.append("  <select id=\"countByProperty\" resultType=\"java.lang.Long\"  parameterType=\"java.util.Map\">\n");
        result.append("       SELECT count(*) FROM ").append(table.getTableName()).append(" <include refid=\"whereSql").append(table.getTableAttName()).append("\"/>\n");
        result.append("   </select>\n");

        result.append("  <select id=\"selectByProperty\" resultMap=\"result").append(table.getTableAttName()).append("\"  parameterType=\"java.util.Map\">\n");
        result.append("       SELECT <include refid=\"selectSql").append(table.getTableAttName()).append("\"/> FROM ").append(table.getTableName()).append(" <include refid=\"whereSql").append(table.getTableAttName()).append("\"/>\n");
        result.append("   </select>\n");

        return result.toString();
    }

    /**
     * description: 获取数据类型
     */
    private String getJdbcType(String columnType, int precision) {
        String colType = columnType.toUpperCase();
        if ("INT".equals(colType) || "TINYINT".equals(colType) || "SMALLINT".equals(colType)) {
            colType = "INTEGER";
        } else if ("NUMBER".equals(colType)) {
            if (precision > 20) {
                colType = "DOUBLE";
            } else {
                colType = "BIGINT";
            }
        } else if ("VARCHAR2".equals(colType) || "TEXT".equals(colType) || "CHAR".equals(colType)) {
            colType = "VARCHAR";
        } else if ("TIMESTAMP".equalsIgnoreCase(colType)) {
            colType = "TIMESTAMP";
        } else if ("DATE".equals(colType) || "TIME".equals(colType)) {
            colType = "DATETIME";
        }
        return colType;
    }
}
