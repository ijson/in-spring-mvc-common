package com.ijson.platform.generator.template;

import com.ijson.platform.generator.model.ParamsVo;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.FileOperate;
import com.ijson.platform.generator.util.ToolsUtil;
import com.ijson.platform.generator.util.Validator;

import java.util.List;
import java.util.Map;


public class SpringXmlBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createSpringXml(prefix, tables, config);
    }

    public void createSpringXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String projectName = config.get("project_name");
        String xmlPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "resources/spring/";
        FileOperate.getInstance().newCreateFolder(xmlPath);
        if (!Validator.isEmpty(tables)) {
            StringBuilder result = new StringBuilder("");
            StringBuilder resultDao = new StringBuilder("");
            StringBuilder resultManager = new StringBuilder("");
            StringBuilder resultService = new StringBuilder("");
            result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            result
                    .append("<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
            result
                    .append("    xmlns:jee=\"http://www.springframework.org/schema/jee\"  xmlns:context=\"http://www.springframework.org/schema/context\"\n");
            result
                    .append("    xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd\"\n");
            result.append("    default-autowire=\"byName\"> \n\n");
            for (TableEntity table : tables) {
                resultDao.append(getDaoConfig(jarPath, table.getTableName(), config));
                resultManager.append(getManagerConfig(jarPath, table.getTableName(), config));
                resultService.append(getServiceConfig(jarPath, table.getTableName(), config));
            }
            result.append("   <!-- dao config start -->\n").append(resultDao.toString()).append("   <!-- dao config stop -->\n\n");
            result.append("   <!-- manager config start -->\n").append(resultManager.toString()).append("   <!-- manager config stop -->\n\n");
            //			result.append("   <!-- service config start -->\n" + resultService.toString()
            //					+ "   <!-- service config stop -->\n\n");
            result.append("</beans>\n");
            FileOperate.getInstance().newCreateFile(
                    xmlPath + "in-spring-biz-" + jarPath.substring(jarPath.lastIndexOf(".") + 1) + ".xml",
                    result.toString());
        }
    }

    private String getDaoConfig(String jarPath, String tableName, Map<String, String> config) {
        StringBuilder result = new StringBuilder("");
        String tabPrefix = config.get("table_prefix").toLowerCase();
        String id = ToolsUtil.toCamelNamed(tableName.replaceAll(tabPrefix, "")) + "Dao";
        String name = ".dao." + ToolsUtil.toUpperFirst(tableName.replaceAll(tabPrefix, "")) + "DaoImpl";
        result.append("   <bean id=\"").append(id).append("\" class=\"").append(jarPath).append(name).append("\" parent=\"abstractDao\"/>\n");
        return result.toString();
    }

    private String getManagerConfig(String jarPath, String tableName, Map<String, String> config) {
        StringBuilder result = new StringBuilder("");
        String tabPrefix = config.get("table_prefix").toLowerCase();
        String id = ToolsUtil.toCamelNamed(tableName.replaceAll(tabPrefix, "")) + "Manager";
        String name = ToolsUtil.toUpperFirst(tableName.replaceAll(tabPrefix, "")) + "ManagerImpl";
        result.append("   <bean id=\"").append(id).append("\" class=\"").append(jarPath).append(".manager.impl.").append(name).append("\"></bean>\n");
        return result.toString();
    }

    private String getServiceConfig(String jarPath, String tableName, Map<String, String> config) {
        StringBuilder result = new StringBuilder("");
        String tabPrefix = config.get("table_prefix").toLowerCase();
        String id = ToolsUtil.toCamelNamed(tableName.replaceAll(tabPrefix, "")) + "Service";
        String name = ToolsUtil.toUpperFirst(tableName.replaceAll(tabPrefix, "")) + "ServiceImpl";
        result.append("   <bean id=\"").append(id).append("\" class=\"").append(jarPath).append(".manager.").append(name).append("\"></bean>\n");
        return result.toString();
    }
}
