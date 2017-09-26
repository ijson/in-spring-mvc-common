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

/**
 * Created by cuiyongxu on 17/9/23.
 */
public class JunitTestBuilder implements TemplateHanlder {

    private String tabPrefix;
    private String useCache;

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        tabPrefix = config.get("table_prefix").toLowerCase();
        useCache = config.get("use_cache").toUpperCase();
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
    public void createdManagerImpl(String prefix, List<TableEntity> tables, Map<String, String> config) {

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
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/autoconf/in-cache", getInCacheBaseXml(prefix, tables, config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/autoconf/in-db", getInDBBaseXml(prefix, tables, config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/spring/in-spring-database.xml", getInDatabaseXml(prefix, tables, config));


        FileOperate.getInstance().newCreateFolder(managerPath);
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/BaseTest.java", getBaseTest(config));

        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String tableName = table1.getTableAttName();
                Map map = Maps.newHashMap();
                map.put("package_name", config.get("package_name"));
                map.put("tableName", tableName);
                map.put("tableId", TemplateUtil.toLowerCaseFirstOne(tableName));
                String string = TemplateUtil.getTemplate("managertest.ijson", map);
                FileOperate.getInstance().newCreateFile(managerPath + tableName + "ManagerTest.java", string);
            }
        }
    }

    private String getInDBBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return TemplateUtil.getTemplate("indb.ijson", Maps.newHashMap());
    }

    private String getInCacheBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return TemplateUtil.getTemplate("autoincache.ijson", Maps.newHashMap());
    }

    private String getEhcacheBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String cacheName = jarPath.substring(jarPath.lastIndexOf(".") + 1);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("cacheName", cacheName);
        return TemplateUtil.getTemplate("ehcache.ijson", maps);
    }

    private String getInDatabaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return TemplateUtil.getTemplate("inspringdatabase.ijson", Maps.newHashMap());
    }

    private String getInJunitBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        String bizName = jarPath.substring(jarPath.lastIndexOf(".") + 1);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("bizName", bizName);
        return TemplateUtil.getTemplate("injunitbase.ijson", maps);
    }


    public String getBaseTest(Map<String, String> config) {
        return TemplateUtil.getTemplate("basetest.ijson", config);
    }

    /**
     * 返回类的头引入内容
     *
     * @return
     */
    private String getManagerImplImports(String tableName, Map<String, String> config) {

        return "package " + config.get("package_name")
                + ".manager;\n\n" +
                "import org.junit.Test;\n" +
                "import com.ijson.auth.BaseTest;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;;\n" +
                "import com.ijson.platform.api.model.ParamsVo;\n" +
                "import " + config.get("package_name") + ".entity." + tableName
                + ";\n" +
                "\n \n";
    }

    /**
     * 生成类中的方法
     *
     * @return
     */
    private String getManagerImplClassMethods(String tableName, String beanIdName, String pkCol, TableEntity table, Map<String, String> config) {
        StringBuilder result = new StringBuilder();
        result.append("    @Autowired\n");
        String manager = tableName + "Manager";
        result.append("    private ").append(manager).append(" ").append(tableName.toLowerCase()).append("Manager;\n\n\n");
        String dao = beanIdName + "Dao";
        String pkCols = ToolsUtil.toUpperFirst(pkCol);
        ///添加spring注入方法
        ///新增方法的实现
        result.append(saveInfo(dao, tableName, beanIdName, pkCols));
        ///修改方法的实现
        result.append(editInfo(dao, tableName, beanIdName, pkCols));
        //删除方法的实现
        result.append(deleteInfo(dao, tableName, beanIdName, pkCol));
        //按ID获取信息的实现
        result.append(infoById(dao, tableName, beanIdName, pkCols));

        return result.toString();
    }


    /**
     * 新增方法的实现
     */
    private String saveInfo(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    @Test\n");
        result.append("    public void saveInfo() { \n");
        result.append("      ").append(tableName).append(" ").append(beanIdName).append(" = new ").append(tableName + "();").append("\n");

        result.append("      ParamsVo<").append(tableName).append("> vo = new ParamsVo<").append(tableName).append(">();\n");
        result.append("      vo.setObj(").append(beanIdName).append(");\n");


        result.append("      String id = ").append(tableName.toLowerCase()).append("Manager.saveInfo(vo);\n");
        result.append("      System.out.println(id);\n");
        result.append("       \n   }\n");
        return result.toString();
    }

    /**
     * 修改方法的实现
     */
    private String editInfo(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    @Test\n");
        result.append("    public void editInfo() { \n");
        result.append("      ").append(tableName).append(" ").append(beanIdName).append(" = new ").append(tableName + "();").append("\n");

        result.append("      ParamsVo<").append(tableName).append("> vo = new ParamsVo<").append(tableName).append(">();\n");
        result.append("      vo.setObj(").append(beanIdName).append(");\n");


        result.append("      boolean isOk = ").append(tableName.toLowerCase()).append("Manager.editInfo(vo);\n");
        result.append("      System.out.println(isOk);\n");
        result.append("       \n   }\n");
        return result.toString();
    }

    /**
     * 按ID获取信息的实现
     */
    private String deleteInfo(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    @Test\n");
        result.append("    public void deleteInfo() { \n");
        result.append("      ").append(tableName).append(" ").append(beanIdName).append(" = new ").append(tableName + "();").append("\n");

        result.append("      ParamsVo<").append(tableName).append("> vo = new ParamsVo<").append(tableName).append(">();\n");
        result.append("      vo.setObj(").append(beanIdName).append(");\n");


        result.append("      boolean isOk = ").append(tableName.toLowerCase()).append("Manager.deleteInfo(vo);\n");
        result.append("      System.out.println(isOk);\n");
        result.append("       \n   }\n");
        return result.toString();
    }

    /**
     * 按ID获取信息的实现
     */
    private String infoById(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    @Test\n");
        result.append("    public void infoById() { \n");
        result.append("      ").append(tableName).append(" ").append(beanIdName).append(" = new ").append(tableName + "();").append("\n");

        result.append("      ParamsVo<").append(tableName).append("> vo = new ParamsVo<").append(tableName).append(">();\n");
        result.append("      vo.setObj(").append(beanIdName).append(");\n");


        result.append("      Object object = ").append(tableName.toLowerCase()).append("Manager.getInfoById(vo);\n");
        result.append("      System.out.println(object);\n");
        result.append("       \n   }\n");
        return result.toString();
    }

}
