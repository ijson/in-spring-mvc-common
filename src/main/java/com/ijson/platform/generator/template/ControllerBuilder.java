package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by cuiyongxu on 17/9/24.
 */
public class ControllerBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        getTemplateStr(prefix, tables, config);
    }


    private void getTemplateStr(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String projectName = config.get("project_name");
        String classPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "java/"
                + config.get("package_name").replace(".", "/") + "/controller/";
        FileOperate.getInstance().newCreateFolder(classPath);
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String entityName = table1.getTableAttName();
                Map<String,Object> map = Maps.newHashMap();
                map.put("entityName",entityName);
                map.put("package_name",config.get("package_name"));
                map.put("columns",table1.getColumns());
                FileOperate.getInstance().newCreateFile(classPath + entityName + "Controller.java", TemplateUtil.getTemplate("controller.ijson",map));
            }
        }
    }
}
