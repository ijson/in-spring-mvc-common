package com.ijson.platform.generator.template;

import com.ijson.platform.generator.model.ColumnEntity;
import com.ijson.platform.generator.model.ParamsVo;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.DataType;
import com.ijson.platform.generator.util.FileOperate;
import com.ijson.platform.generator.util.Validator;

import java.util.List;
import java.util.Map;


public class HibernateXmlBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdHibernateXml(prefix, tables, config);
    }

    private void createdHibernateXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "resources/hibernate/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table : tables) {
                StringBuilder result = new StringBuilder("");
                result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
                result.append("<!DOCTYPE hibernate-mapping PUBLIC \"-//Hibernate/Hibernate Mapping DTD 3.0//EN\" \n");
                result.append("\"http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd\">\n\n");
                result.append("<hibernate-mapping>\n");
                result.append("    <class name=\"").append(jarPath).append(".entity.").append(table.getTableAttName()).append("\" table=\"").append(table.getTableName()).append("\" >\n");

                result.append(getResultMap(table));

                result.append("    </class>\n");
                result.append("</hibernate-mapping>\n");
                FileOperate.getInstance().newCreateFile(xmlPath + table.getTableAttName() + ".hbm.xml",
                        result.toString());
            }
        }
    }

    private String getResultMap(TableEntity table) {
        StringBuilder result = new StringBuilder("");
        int count = table.getColumns().size();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                ColumnEntity col = table.getColumns().get(i);
                String colType = DataType.getDataType(col.getColumnTypeName(), true, col.getPrecision());

                if (col.getAttrName().equalsIgnoreCase(table.getPKColumn())) {
                    result.append("    <id name=\"").append(col.getAttrName()).append("\" type=\"").append(colType).append("\">\n");
                    result.append("        <column name=\"").append(col.getColumnName()).append("\" length=\"").append(col.getPrecision()).append("\" />\n");
                    result.append("        <generator class=\"assigned\" />\n");
                    result.append("    </id>\n");
                } else {
                    result.append("    <property name=\"").append(col.getAttrName()).append("\" type=\"").append(colType).append("\">\n");
                    result.append("        <column name=\"").append(col.getColumnName()).append("\" length=\"").append(col.getPrecision()).append("\" />\n");
                    result.append("    </property>\n");
                }
            }
        }
        return result.toString();
    }
}
