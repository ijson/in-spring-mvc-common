package com.ijson.platform.generator.template;

import com.ijson.platform.generator.model.ParamsVo;
import com.ijson.platform.generator.model.TableEntity;
import com.ijson.platform.generator.util.FileOperate;
import com.ijson.platform.generator.util.Validator;

import java.util.List;
import java.util.Map;


public class TemplateDaoImplBuilder implements TemplateHanlder {

    public void execute(ParamsVo<TableEntity> vo, Map<String, String> config) {
        List<TableEntity> tables = vo.getObjs();
        String prefix = Validator.getDefaultStr(String.valueOf(vo.getParams("prefix")), "src/main/");
        createdDaoImpl(prefix, tables,config);
    }

    public void createdDaoImpl(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String projectName = config.get("project_name");
        String daoPath = config.get("fs_path") + "/" + projectName + "/" + prefix + "java/"
                + config.get("package_name").replace(".", "/") + "/dao/";
        FileOperate.getInstance().newCreateFolder(daoPath);
        getTemplateStr(tables, daoPath,config);
    }

    public void getTemplateStr(List<TableEntity> tables, String daoPath,Map<String, String> config) {
        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                StringBuilder result = new StringBuilder("");
                String tableName = table1.getTableAttName();
                result.append(getImports(config));
                result.append("public class ").append(tableName).append("DaoImpl extends DaoImpl { \n\n");
                result.append(getClassMethods(table1));
                result.append("} \n");
                FileOperate.getInstance().newCreateFile(daoPath + tableName + "DaoImpl.java", result.toString());
            }
        }
    }

    /**
     * 返回类的头引入内容
     *
     * @return
     */
    private String getImports(Map<String, String> config) {
        return "package " + config.get("package_name")
                + ".dao;\n\n" + "import com.ijson.platform.database.db.DaoImpl;\n" +
                "\n \n";
    }

    /**
     * 生成类中的方法
     *
     * @return
     */
    private String getClassMethods(TableEntity table) {
        StringBuilder result = new StringBuilder("   public String getSql(int type) { \n");
        String sql = "from " + table.getTableAttName() + " where 1=1";
        result.append("      String sql = \"\";\n");
        result.append("      switch (type) {\n");
        result.append("      case 1:\n");
        result.append("           sql=\"select count(*) ").append(sql).append(" \";\n           break;\n");
        result.append("      case 2:\n");
        result.append("           sql=\" ").append(sql).append(" \";\n           break;\n");
        result.append("      default:\n");
        result.append("          sql=\"select count(*) ").append(sql).append(" \";\n    }\n");
        result.append("    return sql;\n }\n");
        result.append("\n \n");
        result.append("   public void initSystemCache() { \n");
        result.append("   }\n");
        return result.toString();
    }

}
