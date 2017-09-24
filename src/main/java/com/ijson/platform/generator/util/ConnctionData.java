package com.ijson.platform.generator.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.*;

public class ConnctionData {

    private static ConnctionData instance;
    private String url = "jdbc:mysql://localhost:3306/nuth?useUnicode=true&characterEncoding=utf8";
    private String name = "com.mysql.jdbc.Driver";
    private String user = "root";
    private String password = "root";
    private DruidDataSource ds = null;

    private ConnctionData(String jdbcUrl, String jdbcDriver, String user, String password) {
        this.url = jdbcUrl;
        this.name = jdbcDriver;
        this.user = user;
        this.password = password;
        init();
    }

    private void init() {
        ds = new DruidDataSource();
        ds.setDriverClassName(name);
        ds.setUrl(url);
        ds.setUsername(user);
        ds.setPassword(password);

        ds.setInitialSize(20);
        ds.setMinIdle(15);

        /**
         * 最大活动连接数
         * 同时进行的数据库连接数不超过这个数目
         * */
        ds.setMaxActive(20);
        /**
         * 最大空闲连接数
         * 当释放数据库连接后,空闲连接数超过这个数目时关闭一些空闲连接
         */
        //ds.setMaxIdle(4);
        /**
         * 是否预编译SQL语句
         * */
        ds.setPoolPreparedStatements(true);
    }

    public synchronized static ConnctionData getInstance(String jdbcUrl, String jdbcDriver, String user, String password) {
        if (null == instance) {
            instance = new ConnctionData(jdbcUrl, jdbcDriver, user, password);
        }
        return instance;
    }

    /**
     * 执行一条简单的更新语句
     * 执行成功则返回true
     *
     * @param sql sql
     * @return value
     * @throws SQLException 异常
     */

    public boolean execute(String sql) throws SQLException {
        boolean v = false;
        Connection conn = getConnection();//conn用于连接数据库
        PreparedStatement stmt = null;//stmt用于发送sql语句到数据库并执行sql语句
        try {
            stmt = conn.prepareStatement(sql);
            v = stmt.execute(sql);
        } finally {
            if (null != stmt) {
                stmt.close();
            }
            if (null != conn) {
                conn.close();
            }
        }
        return v;
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


    public void close(Connection con, PreparedStatement stmt, ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
            if (null != stmt) {
                stmt.close();
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
