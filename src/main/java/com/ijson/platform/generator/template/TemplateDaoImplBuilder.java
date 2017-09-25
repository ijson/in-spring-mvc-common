package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;


public class TemplateDaoImplBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdDaoImpl(prefix, tables, config);
    }

    private void createdDaoImpl(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String projectName = config.get("project_name");
        String daoPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "java/"
                + config.get("package_name").replace(".", "/") + "/dao/";
        FileOperate.getInstance().newCreateFolder(daoPath);
        getTemplateStr(tables, daoPath, config);
    }

    private void getTemplateStr(List<TableEntity> tables, String daoPath, Map<String, String> config) {
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String tableName = table1.getTableAttName();
                Map<String, Object> maps = Maps.newHashMap();
                maps.put("tableName", tableName);
                maps.put("package_name",config.get("package_name"));
                FileOperate.getInstance().newCreateFile(daoPath + tableName + "DaoImpl.java", TemplateUtil.getTemplate("daoImpl.ijson", maps));
            }
        }
    }
}
