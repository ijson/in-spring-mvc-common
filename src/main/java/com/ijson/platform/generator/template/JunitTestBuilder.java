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
 * Created by cuiyongxu on 17/9/23.
 */
public class JunitTestBuilder implements TemplateHanlder {


    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = "src/test/";
        createdManagerImpl(prefix, tables, config);

    }

    /**
     * 生成manager接口的实现类
     *
     * @param config config
     * @param prefix prefix
     * @param tables tables
     */
    private void createdManagerImpl(String prefix, List<TableEntity> tables, Map<String, String> config) {

        String fsPath = config.get("fs_path") + "/";

        String projectName = config.get("project_name");
        String managerPath = fsPath + projectName + "/" + prefix + "java/"
                + config.get("package_name").replace(".", "/") + "/manager/";
        FileOperate.getInstance().newCreateFolder(fsPath + projectName + "/" + prefix + "/resources/");
        FileOperate.getInstance().newCreateFolder(fsPath + projectName + "/" + prefix + "/resources/autoconf/");
        FileOperate.getInstance().newCreateFolder(fsPath + projectName + "/" + prefix + "/resources/spring/");
        //in-junit-base.xml
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/in-junit-base.xml", getInJunitBaseXml(prefix, tables, config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/ehcache.xml", getEhcacheBaseXml(prefix, tables, config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/autoconf/in-common-cache", getInCacheBaseXml(prefix, tables, config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/autoconf/in-common-db", getInDBBaseXml(prefix, tables, config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/spring/in-spring-database.xml", getInDatabaseXml(prefix, tables, config));


        FileOperate.getInstance().newCreateFolder(managerPath);
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/BaseTest.java", getBaseTest(config));

        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String tableName = table1.getTableAttName();
                Map<String, Object> map = Maps.newHashMap();
                map.put("package_name", config.get("package_name"));
                map.put("tableName", tableName);
                map.put("tableId", TemplateUtil.toLowerCaseFirstOne(tableName));
                String string = TemplateUtil.getTemplate("in-junit-manager-template", map);
                FileOperate.getInstance().newCreateFile(managerPath + tableName + "ManagerTest.java", string);
            }
        }
    }

    private String getInDBBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        Map dbConfig = Maps.newHashMap();
        dbConfig.put("driver", config.get("jdbc.driver"));
        dbConfig.put("url", config.get("jdbc.url"));
        dbConfig.put("user", config.get("jdbc.user"));
        dbConfig.put("password", config.get("jdbc.password"));
        return TemplateUtil.getTemplate("in-junit-database-config-template", dbConfig);
    }

    private String getInCacheBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return TemplateUtil.getTemplate("in-common-cache-template", Maps.newHashMap());
    }

    private String getEhcacheBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String cacheName = jarPath.substring(jarPath.lastIndexOf(".") + 1);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("cacheName", cacheName);
        return TemplateUtil.getTemplate("in-junit-ehcache-template", maps);
    }

    private String getInDatabaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return TemplateUtil.getTemplate("in-junit-spring-database-template", Maps.newHashMap());
    }

    private String getInJunitBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String bizName = jarPath.substring(jarPath.lastIndexOf(".") + 1);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("bizName", bizName);
        return TemplateUtil.getTemplate("in-junit-base-xml-template", maps);
    }


    private String getBaseTest(Map<String, String> config) {
        return TemplateUtil.getTemplate("in-junit-base-template", config);
    }
}

