package com.ijson.platform.generator.template;

import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.util.FileOperate;
import com.ijson.platform.common.util.ToolsUtil;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.generator.model.TableEntity;

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
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/in-junit-base.xml", getInJunitBaseXml(prefix,tables,config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/ehcache.xml", getEhcacheBaseXml(prefix,tables,config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/autoconf/in-cache", getInCacheBaseXml(prefix,tables,config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/autoconf/in-db", getInDBBaseXml(prefix,tables,config));
        FileOperate.getInstance().newCreateFile(fsPath + projectName + "/" + prefix + "/resources/spring/in-spring-database.xml", getInDatabaseXml(prefix,tables,config));


        FileOperate.getInstance().newCreateFolder(managerPath);
        String baseTest = getBaseTest(config);
        FileOperate.getInstance()
                .newCreateFile(fsPath + projectName + "/" + prefix + "java/"
                        + config.get("package_name").replace(".", "/") + "/BaseTest.java", baseTest);

        if (!Validator.isEmpty(tables)) {
            for (TableEntity table1 : tables) {
                StringBuilder result = new StringBuilder("");
                String tableName = table1.getTableAttName();
                String beanIdName = ToolsUtil.toCamelNamed(table1.getTableName().replaceAll(tabPrefix, ""));
                result.append(getManagerImplImports(tableName, config));
                result.append("public class ").append(tableName).append("ManagerTest extends BaseTest { \n\n");
                result.append(getManagerImplClassMethods(tableName, beanIdName, table1.getPKColumn(), table1, config));
                result.append("} \n");
                FileOperate.getInstance()
                        .newCreateFile(managerPath + tableName + "ManagerTest.java", result.toString());
            }
        }
    }

    private String getInDBBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return "db.driver=com.mysql.jdbc.Driver\n" +
                "db.url=jdbc:mysql://127.0.0.1:3307/ijson_mvc_db?useUnicode=true&characterEncoding=utf8\n" +
                "db.username=admin\n" +
                "db.password=MSCR7KPCJMR5\n" +
                "hibernate.dialect=org.hibernate.dialect.MySQLDialect\n" +
                "\n" +
                "#db.driver=org.h2.Driver\n" +
                "#db.url=jdbc:h2:mem:test\n" +
                "#db.username=sa\n" +
                "#db.password=\n" +
                "#Hibernate Settings\n" +
                "#hibernate.dialect=org.hibernate.dialect.H2Dialect\n" +
                "\n" +
                "hibernate.show_sql=true\n" +
                "hibernate.format_sql=false\n" +
                "db.initialSize=50\n" +
                "db.maxActive=100\n" +
                "db.maxIdle=20\n";
    }

    private String getInCacheBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return "cache_type=ehcache\n" +
                "cache_default_name=ijsonCache\n";
    }

    private String getEhcacheBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return "<ehcache>\n" +
                "    <diskStore path=\"java.io.tmpdir\"/>\n" +
                "\n" +
                "    <!-- 默认缓存 -->\n" +
                "    <defaultCache\n" +
                "            maxElementsInMemory=\"10000\"\n" +
                "            eternal=\"false\"\n" +
                "            timeToIdleSeconds=\"120\"\n" +
                "            timeToLiveSeconds=\"120\"\n" +
                "            overflowToDisk=\"true\"\n" +
                "            maxElementsOnDisk=\"10000000\"\n" +
                "            diskPersistent=\"false\"\n" +
                "            diskExpiryThreadIntervalSeconds=\"120\"\n" +
                "            memoryStoreEvictionPolicy=\"LRU\"\n" +
                "    />\n" +
                "\n" +
                "    <!-- 不做数据同步时配置,框架所需,禁止删除-->\n" +
                "    <cache name=\"ijsonCache\" maxElementsInMemory=\"100000\" eternal=\"true\"\n" +
                "           overflowToDisk=\"true\" timeToIdleSeconds=\"120\" timeToLiveSeconds=\"120\"\n" +
                "           diskPersistent=\"false\" diskExpiryThreadIntervalSeconds=\"120\" diskSpoolBufferSizeMB=\"1024\"\n" +
                "           memoryStoreEvictionPolicy=\"LFU\">\n" +
                "    </cache>\n" +
                "\n" +
                "\n" +
                "    <!-- 注释内容 -->\n" +
                "    <cache\n" +
                "            name=\"example\"\n" +
                "            maxElementsInMemory=\"1000000\"\n" +
                "            eternal=\"false\"\n" +
                "            overflowToDisk=\"true\"\n" +
                "            timeToIdleSeconds=\"86400\"\n" +
                "            timeToLiveSeconds=\"86400\"\n" +
                "            memoryStoreEvictionPolicy=\"LRU\"\n" +
                "    />\n" +
                "    <!--\n" +
                "        name  缓存名称  必填。\n" +
                "        maxElementsInMemory:该缓存中允许存放的最大条目数量。\n" +
                "       eternal:缓存内容是否永久储存。\n" +
                "\n" +
                "       overflowToDisk:如果内存中的数据超过maxElementsInMemory,是否使用磁盘存储。\n" +
                "       maxElementsOnDisk 磁盘存储中允许存放的最大条目数\n" +
                "       diskPersistent:磁盘储存的条目是否永久保存。\n" +
                "\n" +
                "       timeToIdleSeconds:如果不是永久储存的缓存，那么在timeToIdleSeconds指定时间内没有访问一个条目，则移除它，单位 秒\n" +
                "       timeToLiveSeconds:如果不是永久储存的缓存，一个条目可以存在的最长时间，单位 秒\n" +
                "\n" +
                "       diskExpiryThreadIntervalSeconds:磁盘清理线程的运行时间间隔。即后台线程监测元素失效的间隔时间  默认是120秒\n" +
                "\n" +
                "       memoryStoreEvictionPolicy：缓存回收策略  即当缓存容量满了（达到maxElementsInMemory设定的最大条目数），需要为新的缓存腾出地方来时，回收已存在缓存的策略\n" +
                "       1.FIFO ,first in first out   先进先出\n" +
                "       2.LFU , Less Frequently Used  一直以来最少被使用\n" +
                "       3.LRU ,Least Recently Used 最近最少使用   默认是这种回收策略\n" +
                "     -->\n" +
                "\n" +
                "</ehcache>\n";
    }

    private String getInDatabaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n" +
                "       xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xmlns:jee=\"http://www.springframework.org/schema/jee\"\n" +
                "       xmlns:tx=\"http://www.springframework.org/schema/tx\"\n" +
                "       xmlns:aop=\"http://www.springframework.org/schema/aop\" xmlns:p=\"http://www.springframework.org/schema/p\"\n" +
                "       xsi:schemaLocation=\"\n" +
                "\thttp://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd\n" +
                "\thttp://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.0.xsd\n" +
                "\thttp://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.0.xsd\"\n" +
                "       default-lazy-init=\"true\">\n" +
                "\n" +
                "    <description>Spring公共配置</description>\n" +
                "\n" +
                "    <bean id=\"dataSource\" class=\"com.ijson.platform.database.db.DataSource\" init-method=\"initdb\"/>\n" +
                "\n" +
                "    <!--  配置参与事务的类 -->\n" +
                "    <aop:config>\n" +
                "        <aop:pointcut id=\"transactionServiceMethod\" expression=\"execution(* com.ijson..manager.*Manager.*(..))\"/>\n" +
                "        <aop:advisor pointcut-ref=\"transactionServiceMethod\" advice-ref=\"txAdvice\"/>\n" +
                "    </aop:config>\n" +
                "\n" +
                "    <tx:advice id=\"txAdvice\" transaction-manager=\"transactionManager\">\n" +
                "        <tx:attributes>\n" +
                "            <tx:method name=\"save*\" propagation=\"REQUIRED\"\n" +
                "                       rollback-for=\"com.ijson.platform.common.exception.MVCBusinessException\"/>\n" +
                "            <tx:method name=\"delete*\" propagation=\"REQUIRED\"\n" +
                "                       rollback-for=\"com.ijson.platform.common.exception.MVCBusinessException\"/>\n" +
                "            <tx:method name=\"edit*\" propagation=\"REQUIRED\"\n" +
                "                       rollback-for=\"com.ijson.platform.common.exception.MVCBusinessException\"/>\n" +
                "            <tx:method name=\"*\" propagation=\"SUPPORTS\"/>\n" +
                "        </tx:attributes>\n" +
                "    </tx:advice>\n" +
                "\n" +
                "    <!-- myibatis config -->\n" +
                "    <import resource=\"classpath*:spring/myibatis-database.xml\"/>\n" +
                "    <bean id=\"transactionManager\" class=\"org.springframework.jdbc.datasource.DataSourceTransactionManager\"\n" +
                "          p:dataSource-ref=\"dataSource\"/>\n" +
                "\n" +
                "\n" +
                "    <!--  <import resource=\"classpath*:spring/hibernate-database.xml\"/>\n" +
                "      <bean id=\"transactionManager\" class=\"org.springframework.jdbc.datasource.DataSourceTransactionManager\" p:dataSource-ref=\"dataSource\" />-->\n" +
                "\n" +
                "</beans>\n";
    }

    private String getInJunitBaseXml(String prefix, List<TableEntity> tables, Map<String, String> config) {
        String jarPath = config.get("package_name");
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<beans xmlns=\"http://www.springframework.org/schema/beans\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "       xmlns:jee=\"http://www.springframework.org/schema/jee\"  xmlns:context=\"http://www.springframework.org/schema/context\"\n" +
                "       xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-3.0.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.0.xsd\"\n" +
                "       default-autowire=\"byName\">\n" +
                "\n" +
                "    <import resource=\"classpath:spring/in-spring-biz-"+jarPath.substring(jarPath.lastIndexOf(".") + 1) +".xml\"/>\n" +
                "\n" +
                "</beans>\n" +
                "\n";
    }


    public String getBaseTest(Map<String, String> config) {
        return "package " + config.get("package_name")
                + ";\n\n"
                + "import org.junit.runner.RunWith;\n" +
                "import org.springframework.test.context.ContextConfiguration;\n" +
                "import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\n" +
                "\n \n" +
                "/**\n" +
                " * Created by cuiyongxu on 17/9/23.\n" +
                " */\n" + "@RunWith(SpringJUnit4ClassRunner.class)\n" + "@ContextConfiguration(locations = \"classpath:in-junit-base.xml\")\n" +
                "public class BaseTest {\n" +
                "}\n";
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
