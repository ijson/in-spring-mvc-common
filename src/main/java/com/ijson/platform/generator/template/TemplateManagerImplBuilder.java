package com.ijson.platform.generator.template;

import com.google.common.collect.Maps;
import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.ToolsUtil;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.ColumnEntity;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.DataType;
import com.ijson.platform.generator.util.TemplateUtil;

import java.util.List;
import java.util.Map;


public class TemplateManagerImplBuilder implements TemplateHanlder {

    private String tabPrefix;
    private String useCache;

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        tabPrefix = config.get("table_prefix").toLowerCase();
        useCache = config.get("use_cache").toUpperCase();
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
                Map<String,Object> map = Maps.newHashMap();
                map.put("package_name",config.get("package_name"));
                map.put("tableName",tableName);
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
    public void createdManagerImpl(String prefix, List<TableEntity> tables, Map<String, String> config) {

        String fsPath = config.get("fs_path") + "/";

        String projectName = config.get("project_name");
        String managerPath = fsPath + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/manager/impl/";
        String pluginPath = fsPath + projectName + "/" + prefix + "java/" + config.get("package_name").replace(".", "/") + "/manager/plugins/";

        FileOperate.getInstance().newCreateFolder(managerPath);
        FileOperate.getInstance().newCreateFolder(pluginPath);

        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                String tableName = table1.getTableAttName();
                String beanIdName = ToolsUtil.toCamelNamed(table1.getTableName().replaceAll(tabPrefix, ""));
                Map<String,Object> map = Maps.newHashMap();
                map.put("package_name",config.get("package_name"));
                map.put("tableName",tableName);
                map.put("beanIdName",beanIdName);
                map.put("table",table1);
                map.put("pkey",ToolsUtil.toUpperFirst(table1.getPKColumn()));
                map.put("pkeys",table1.getPKColumn());
                FileOperate.getInstance().newCreateFile(managerPath + tableName + "ManagerImpl.java", TemplateUtil.getTemplate("managerimpl.ijson", map));
            }
        }
    }

    /**
     * 返回类的头引入内容
     *
     * @param config 配置
     * @return 头信息
     */
    private String getManagerImplImports(String tableName, Map<String, String> config) {

        return "package " + config.get("package_name")
                + ".manager.impl;\n\n" + "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "import com.ijson.platform.database.model.MethodParam;\n" +
                "import com.ijson.platform.database.model.Page;\n" +
                "import com.ijson.platform.database.db.IDao;\n" +
                "import com.ijson.platform.api.model.ParamsVo;\n" +
                "import com.ijson.platform.common.util.Validator;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;;\n" +
                "import com.ijson.platform.api.manager.PluginConnector;\n" +
                "import " + config.get("package_name") + ".entity." + tableName
                + ";\n" +
                "import " + config.get("package_name") + ".manager." + tableName
                + "Manager;\n" +
                "import com.ijson.platform.common.exception.MVCBusinessException;\n" +
                "\n \n";
    }

    /**
     * 生成类中的方法
     *
     * @param config 配置
     * @return value
     */
    private String getManagerImplClassMethods(String tableName, String beanIdName, String pkCol, TableEntity table, Map<String, String> config) {
        StringBuilder result = new StringBuilder();
        String dao = beanIdName + "Dao";
        String jarPath = config.get("package_name");
        result.append("    @Autowired\n");
        result.append("    protected IDao ").append(dao).append(";\n");
        result.append("    protected Map<String, PluginConnector" + "> plugins;\n");

        result.append("    protected final String entityName = \"").append(jarPath).append(".entity.").append(tableName).append("\";\n\n");
        String pkCols = ToolsUtil.toUpperFirst(pkCol);
        ///添加spring注入方法
        result.append("    public void set").append(tableName).append("Dao(IDao ").append(dao).append(") { \n");
        result.append("      this.").append(dao).append("=").append(dao).append(";\n   }\n");

        result.append("	   public void setPlugins(Map<String, PluginConnector> plugins) {\n");
        result.append("		 this.plugins = plugins;\n");
        result.append("	   }\n");

        ///新增方法的实现
        result.append(saveInfo(dao, tableName, beanIdName, pkCols));
        ///修改方法的实现
        result.append(editInfo(dao, tableName, beanIdName, pkCols));
        //删除方法的实现
        result.append(deleteInfo(dao, tableName, beanIdName, pkCol));
        //按ID获取信息的实现
        result.append(infoById(dao, tableName, beanIdName, pkCols));

        result.append("    public Object execute(ParamsVo<").append(tableName).append("> vo){ \n");
        result.append("		  String key = vo.getMethodKey();\n");
        result.append("		  try {\n");
        result.append("			if (!Validator.isEmpty(plugins)) {\n");
        result.append("				if (!Validator.isEmpty(plugins.get(key))) {\n");
        result.append("					return plugins.get(key).execute(vo);\n");
        result.append("				}\n");
        result.append("	  		  }\n");
        result.append("		  } catch (Exception e) {\n");
        result.append("			e.printStackTrace();\n");
        result.append("		  }\n");
        result.append(" 	return null; \n   }\n");

        result.append("    public Page getPageInfo(ParamsVo<").append(tableName).append("> vo){ \n");
        result.append("      MethodParam param = setMethodParams(vo, 2);\n");
        result
                .append("      int pageSize = Integer.valueOf(Validator.getDefaultStr(String.valueOf(vo.getParams(\"pageSize\")), \"10\"));\n");
        result
                .append("      int pageIndex = Integer.valueOf(Validator.getDefaultStr(String.valueOf(vo.getParams(\"pageIndex\")), \"1\"));\n");
        result.append("      param.setPageIndex(pageIndex);\n");
        result.append("      param.setPageSize(pageSize);\n");
        result.append("      Page page = ").append(dao).append(".pageSelect(param);\n      return page; \n   }\n");

        result.append("    public List<").append(tableName).append("> getList(ParamsVo<").append(tableName).append("> vo){ \n");
        result.append("      MethodParam param = setMethodParams(vo, 2);\n");
        result.append("      return ").append(dao).append(".select(param);\n   }\n");

        result.append("    public long countInfo(ParamsVo<").append(tableName).append("> vo){ \n");
        result.append("      MethodParam param = setMethodParams(vo, 1);\n");
        result.append("      return ").append(dao).append(".count(param); \n   }\n");

        result.append(setMethodParams(tableName, dao, table, beanIdName, pkCol));

        return result.toString();
    }

    private String setMethodParams(String tableName, String dao, TableEntity table, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        StringBuilder params = new StringBuilder();
        result.append("    private MethodParam setMethodParams(ParamsVo<").append(tableName).append("> vo, int type) { \n");
        result.append("      String methodKey = Validator.getDefaultStr(vo.getMethodKey(), \"ByProperty\");\n");
        result.append("      StringBuffer sb = new StringBuffer(").append(dao).append(".getSql(type));\n");
        result.append("      MethodParam param = new MethodParam(methodKey, \"\", \"\", entityName);\n");
        if (!Validator.isEmpty(table.getColumns())) {
            int count = table.getColumns().size();
            params.append("      ").append(tableName).append(" ").append(beanIdName).append(" = vo.getObj();\n");
            for (int i = 0; i < count; i++) {
                ColumnEntity column = table.getColumns().get(i);
                if (column.getAttrName().equals(pkCol)) {
                    continue;
                }
                String getMethod = beanIdName + ".get" + ToolsUtil.toUpperFirst(column.getAttrName()) + "()";
                if (DataType.isStringType(column.getColumnTypeName())) {
                    params.append("      if(Validator.isNotNull(").append(getMethod).append(")) { \n");
                    params.append("           sb.append(\" and ").append(column.getAttrName()).append(" = :").append(column.getAttrName()).append("\");\n");
                    params.append("           param.setParams(\"").append(column.getAttrName()).append("\", ").append(getMethod).append("); \n");
                    params.append("      }\n");
                }
            }
        }
        result.append(params.toString());
        result.append("      \n      param.setSqlStr(sb.toString());\n");
        result.append("      return param; \n   }\n");
        return result.toString();
    }

    /**
     * 新增方法的实现
     *
     * @param daoStr     daoStr
     * @param tableName  tableName
     * @param pkCol      pkCol
     * @param beanIdName beanIdName
     */
    private String saveInfo(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    public String saveInfo(ParamsVo<").append(tableName).append("> vo) throws MVCBusinessException { \n");
        result.append("      ").append(tableName).append(" ").append(beanIdName).append(" = vo.getObj();\n");
        //		result
        //				.append("      String infoId = Validator.generate();\n      //定义对象缓存KEY,如果不需要缓存对象请不要对变量赋值，如果要缓存对象建议使用infoId\n");
        result.append("      String infoId = Validator.generate();\n");

        if ("T".equals(useCache)) {
            result.append("      String cacheKey=\"").append(beanIdName).append("_\"+infoId;\n");
        } else {
            result.append("      String cacheKey=\"\";\n");
        }

        result.append("      ").append(beanIdName).append(".set").append(pkCol).append("(infoId);\n");
        result.append("      MethodParam param = new MethodParam(\"").append(tableName).append("\", cacheKey, \"\", entityName);\n");
        result.append("      param.setVaule(").append(beanIdName).append(");\n");
        result.append("      if (").append(daoStr).append(".insert(param)) { \n         return infoId;\n      }\n");
        result.append("      return \"\"; \n   }\n");
        return result.toString();
    }

    /**
     * 修改方法的实现
     */
    private String editInfo(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    public boolean editInfo(ParamsVo<").append(tableName).append("> vo) throws MVCBusinessException { \n");
        result.append("      ").append(tableName).append(" ").append(beanIdName).append(" = vo.getObj();\n");
        result.append("      if (Validator.isNotNull(").append(beanIdName).append(".get").append(pkCol).append("())) {\n");

        //		result.append("         String cacheKey=\"\";\n");
        //		result.append("      //String cacheKey=\"" + beanIdName + "_\"+" + beanIdName + ".get" + pkCol + "();\n");

        if ("T".equals(useCache)) {
            result.append("      String cacheKey=\"").append(beanIdName).append("_\"+").append(beanIdName).append(".get").append(pkCol).append("();\n");
        } else {
            result.append("         String cacheKey=\"\";\n");
        }

        result.append("         MethodParam param = new MethodParam(\"").append(tableName).append("\", cacheKey, \"\", entityName);\n");
        result.append("         param.setVaule(").append(beanIdName).append(");\n         return ").append(daoStr).append(".edit(param);\n      }\n");
        result.append("      return false; \n   }\n");
        return result.toString();
    }

    /**
     * 按ID获取信息的实现
     */
    private String deleteInfo(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    public boolean deleteInfo(ParamsVo<").append(tableName).append("> vo) throws MVCBusinessException { \n");
        result.append("      String infoId = vo.getInfoId();\n");
        result.append("      if (Validator.isNull(infoId)){\n          return false; \n      }\n");
        //		result.append("      String cacheKey=\"\";\n");
        //		result.append("      //String cacheKey=\"" + beanIdName + "_\"+infoId;\n");

        if ("T".equals(useCache)) {
            result.append("      String cacheKey=\"").append(beanIdName).append("_\"+infoId;\n");
        } else {
            result.append("      String cacheKey=\"\";\n");
        }

        result
                .append("      String mark = Validator.getDefaultStr(String.valueOf(vo.getParams(\"isDelete\")), \"true\");\n");
        result.append("      MethodParam param = new MethodParam(\"ById\", cacheKey, \"\", entityName);\n");
        result.append("      param.setInfoId(infoId);\n");
        result.append("      ").append(tableName).append(" info = (").append(tableName).append(") ").append(daoStr).append(".selectById(param);\n");
        result.append("      if (Validator.isEmpty(info)) {\n          return false; \n      }\n");
        result.append("      param.setVaule(info);//此处需要先将状态值赋值为删除状态\n");
        result.append("      if(\"false\".equals(mark)){//逻辑删除\n         param.setKey(\"").append(tableName).append("\");\n");
        result.append("         return ").append(daoStr).append(".edit(param);\n      } else{\n");
        result.append("         param.setParams(\"").append(pkCol).append("\",infoId);\n");
        result.append("         param.setDelete(true);\n");
        result.append("         return ").append(daoStr).append(".delete(param);\n      }\n   }\n");
        return result.toString();
    }

    /**
     * 按ID获取信息的实现
     */
    private String infoById(String daoStr, String tableName, String beanIdName, String pkCol) {
        StringBuilder result = new StringBuilder();
        result.append("    public Object getInfoById(ParamsVo<").append(tableName).append("> vo){ \n");
        result.append("      String infoId = vo.getInfoId();\n");
        //		result.append("      String cacheKey=\"\";\n");
        //		result.append("      //String cacheKey=\"" + beanIdName + "_\"+infoId;\n");

        if ("T".equals(useCache)) {
            result.append("      String cacheKey=\"").append(beanIdName).append("_\"+infoId;\n");
        } else {
            result.append("      String cacheKey=\"\";\n");
        }

        result.append("      if (Validator.isNull(infoId)) {\n         return null;\n      }\n");
        result.append("      MethodParam param = new MethodParam(\"ById\", cacheKey, \"\", entityName);\n");
        result.append("      param.setInfoId(infoId);\n");
        result.append("      return ").append(daoStr).append(".selectById(param); \n   }\n");
        return result.toString();
    }

}
