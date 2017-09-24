package com.ijson.platform.database.db.mybatis.dialect;

/**
 * description: 分页插件接口
 */
public interface Dialect {

    /**
     * 是否支持分页
     *
     * @return value
     */
    boolean supportsLimit();

    /**
     * @return value
     */
    boolean supportsLimitOffset();

    /**
     * 将sql变成分页sql语句,直接使用offset,limit的值作为占位符,<br> 源代码为:
     * getLimitString(sql,offset,String.valueOf(offset),limit,String.valueOf(limit))
     *
     * @param limit   limit
     * @param offset  offset
     * @param orderby orderby
     * @param sql     sql
     * @return value
     */
    String getLimitString(String sql, int offset, int limit, String orderby);

}
