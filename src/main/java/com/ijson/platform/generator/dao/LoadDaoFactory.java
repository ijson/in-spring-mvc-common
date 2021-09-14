package com.ijson.platform.generator.dao;

public class LoadDaoFactory {

    private static LoadDaoFactory instance;

    private IDao mysqlDao;

    private LoadDaoFactory() {

    }

    public static LoadDaoFactory getInstance() {
        if (null == instance) {
            instance = new LoadDaoFactory();
        }
        return instance;
    }

    /**
     * description:  暂时默认使用mysql
     *
     * @param databaseType databaseType
     * @return IDao
     * @author cuiyongxu
     */
    public IDao getDao(String databaseType) {
        if ("Mysql".equalsIgnoreCase(databaseType)) {
            return getMysqlDao();
        }
        return null;
    }

    private IDao getMysqlDao() {
        if (null == mysqlDao) {
            mysqlDao = new MysqlDaoImpl();
        }
        return mysqlDao;
    }

}
