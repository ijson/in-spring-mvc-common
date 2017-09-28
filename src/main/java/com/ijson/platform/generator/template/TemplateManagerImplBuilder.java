package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.ToolsUtil;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;


public class TemplateManagerImplBuilder implements TemplateHanlder {


    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdManager(prefix, tables, config);
        createdManagerImpl(prefix, tables, config);

    }

    /**
     * 生成manager接口类
     *
     * @param prefix prefix
     * @param config configs
     * @param tables tables
     */
    private void createdManager(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String projectName = config.get("project_name");
        String managerPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/manager/";
        FileOperate.getInstance().newCreateFolder(managerPath);
        FileOperate.getInstance().newCreateFile(managerPath + "UnityBaseManager.java", TemplateUtil.getTemplate("unitybasemanager.ijson", config));

        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String tableName = table1.getTableAttName();
                Map<String, Object> map = Maps.newHashMap();
                map.put("package_name", config.get("package_name"));
                map.put("tableName", tableName);
                FileOperate.getInstance().newCreateFile(managerPath + tableName + "Manager.java", TemplateUtil.getTemplate("manager.ijson", map));
            }
        }
    }


    /**
     * 生成manager接口的实现类
     *
     * @param config 配置
     * @param tables 表
     * @param prefix 前缀
     */
    private void createdManagerImpl(String prefix, List<TableEntity> tables, Map<String, String> config) {

        String fsPath = config.get("fs_path") + "/";

        String projectName = config.get("project_name");
        String managerPath = fsPath + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/manager/impl/";
        String pluginPath = fsPath + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/manager/plugins/";

        FileOperate.getInstance().newCreateFolder(managerPath);
        FileOperate.getInstance().newCreateFolder(pluginPath);

        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String tableName = table1.getTableAttName();
                String beanIdName = ToolsUtil.toCamelNamed(table1.getTableName());
                Map<String, Object> map = Maps.newHashMap();
                map.put("package_name", config.get("package_name"));
                map.put("tableName", tableName);
                map.put("beanIdName", beanIdName);
                map.put("table", table1);
                map.put("pkey", ToolsUtil.toUpperFirst(table1.getPKColumn()));
                map.put("pkeys", table1.getPKColumn());
                FileOperate.getInstance().newCreateFile(managerPath + tableName + "ManagerImpl.java", TemplateUtil.getTemplate("managerimpl.ijson", map));
            }
        }
    }

}
