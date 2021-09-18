package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ExtPv;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;


public class SpringXmlBuilder implements TemplateHanlder {

    @Override
    public void execute(ExtPv<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createSpringXml(prefix, tables, config);
    }

    private void createSpringXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "resources/spring/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        if (!Validator.isEmpty(tables)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("list", tables);
            map.put("package_name", jarPath);
            String result = TemplateUtil.getTemplate("springbizxml.ijson", map);
            FileOperate.getInstance().newCreateFile(xmlPath + "in-spring-biz-" + jarPath.substring(jarPath.lastIndexOf(".") + 1) + ".xml", result);
        }
    }
}
