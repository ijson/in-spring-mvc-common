package com.ijson.platform.generator.dao;

import com.google.common.collect.Lists;

import com.ijson.platform.common.util.ToolsUtil;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.ColumnEntity;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.ConnctionData;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MysqlDaoImpl implements IDao {

    public List<TableEntity> getTables(String[] tableNames, Map<String, String> config) {
        ConnctionData conn = ConnctionData.getInstance(config.get("jdbc.url"), config.get("jdbc.driver"), config.get("jdbc.user"), config.get("jdbc.password"));

        List<TableEntity> result = Lists.newArrayList();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = conn.getConnection();
            if (!Validator.isEmpty(connection)) {
                log.info("连接数据库成功...");
            }
            for (String tableName1 : tableNames) {
                String sql = "select * from " + tableName1.toLowerCase();
                stmt = connection.prepareStatement(sql);
                rs = stmt.executeQuery(sql);
                ResultSetMetaData data = rs.getMetaData();
                String pkColumn = getPKColumn(tableName1, connection);
                TableEntity table = new TableEntity();
                String tableName = tableName1.toLowerCase();
                log.info("当前表名:{}", tableName);
                //if (rs.next()) {
                int ccnum = data.getColumnCount();
                for (int i = 1; i <= ccnum; i++) {
                    ColumnEntity column = new ColumnEntity();
                    //获得所有列的数目及实际列数
                    //	int columnCount = data.getColumnCount();
                    //获得指定列的列名
                    String columnName = data.getColumnName(i).toLowerCase();/////
                    //获得指定列的列值
                    //	String columnValue = rs.getString(i);
                    //获得指定列的数据类型
                    //	int columnType = data.getColumnType(i);
                    //获得指定列的数据类型名
                    String columnTypeName = data.getColumnTypeName(i);////
                    //所在的Catalog名字
                    //	String catalogName = data.getCatalogName(i);
                    //对应数据类型的类
                    //	String columnClassName = data.getColumnClassName(i);
                    //在数据库中类型的最大字符个数
                    int columnDisplaySize = data.getColumnDisplaySize(i);////
                    //默认的列的标题
                    //	String columnLabel = data.getColumnLabel(i);
                    //获得列的模式
                    //	String schemaName = data.getSchemaName(i);
                    //某列类型的精确度(类型的长度)
                    //	int precision = data.getPrecision(i);
                    //小数点后的位数
                    //	int scale = data.getScale(i);
                    //获取某列对应的表名
                    //tableName = data.getTableName(i);
                    // 是否自动递增
                    //boolean isAutoInctement = data.isAutoIncrement(i);
                    //在数据库中是否为货币型
                    //	boolean isCurrency = data.isCurrency(i);
                    //是否为空
                    int isNullable = data.isNullable(i);/////
                    column.setColumnName(columnName);
                    column.setIsNullable(isNullable);
                    column.setColumnTypeName(columnTypeName);
                    column.setPrecision(columnDisplaySize);
                    column.setAttrName(ToolsUtil.toCamelNamed(columnName));
                    //column.set
                    //是否为只读
                    //	boolean isReadOnly = data.isReadOnly(i);
                    //能否出现在where中
                    //boolean isSearchable = data.isSearchable(i);
                    //System.out.println(columnCount);
                    //System.out.println("----->获得列" + i + "的字段名称:" + columnName);
                    //System.out.println("获得列" + i + "的数据类型名:" + columnTypeName);
                    //System.out.println("获得列" + i + "在数据库中类型的最大字符个数:" + columnDisplaySize);
                    //System.out.println("获得列" + i + "对应的表名:" + tableName);
                    //System.out.println("获得列" + i + "是否为空:" + isNullable);
                    table.setColumns(column);
                }
                //}
                if (Validator.isNotNull(pkColumn)) {
                    table.setPKColumn(ToolsUtil.toCamelNamed(pkColumn));
                }
                table.setTableAttName(ToolsUtil.toUpperFirst(tableName));
                table.setTableName(tableName);
                result.add(table);
            }
        } catch (SQLException e) {
            log.error("数据库链接失败:", e);
        } finally {
            conn.close(connection, stmt, rs);
        }
        return result;
    }

    /**
     * 获取主键列名称,推荐string
     */
    private String getPKColumn(String tableName, Connection conn) {
        String pkColumn = "";
        DatabaseMetaData dbMeta;
        ResultSet pkRSet = null;
        try {
            dbMeta = conn.getMetaData();
            pkRSet = dbMeta.getPrimaryKeys(null, null, tableName.toLowerCase());
            if (pkRSet.next()) {
                pkColumn = pkRSet.getString("column_name").toLowerCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != pkRSet) {
                    pkRSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pkColumn;
    }
}
