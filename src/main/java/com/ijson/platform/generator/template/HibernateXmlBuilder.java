package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;


public class HibernateXmlBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdHibernateXml(prefix, tables, config);
    }

    private void createdHibernateXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "resources/hibernate/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table : tables) {
                String entityName = table.getTableAttName();
                String tableName = table.getTableName();
                Map<String,Object> maps = Maps.newHashMap();
                maps.put("entityName", entityName);
                maps.put("tableName", tableName);
                maps.put("table",table);
                maps.put("package_name", config.get("package_name"));
                FileOperate.getInstance().newCreateFile(xmlPath + table.getTableAttName() + ".hbm.xml", TemplateUtil.getTemplate("in-hibernate-template", maps));
            }
        }
    }

}
