package com.ijson.platform.database.db.mybatis.dialect;


import com.ijson.platform.common.util.Validator;

/**
 * description: MySQL分页插件
 *
 */
public class MySQLDialect implements Dialect {

    /* (non-Javadoc)
     * @see com.kimbanx.database.db.ibatis3.dialect.Dialect#supportsLimit()
     */
    public boolean supportsLimit() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.kimbanx.database.db.ibatis3.dialect.Dialect#supportsLimitOffset()
     */
    public boolean supportsLimitOffset() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.kimbanx.database.db.ibatis3.dialect.Dialect#getLimitString(java.lang.String, int, int)
     */
    public String getLimitString(String sql, int offset, int limit, String orderby) {
        String orderByStr = "";
        if (Validator.isNotNull(orderby)) {
            orderByStr = " order by " + orderby;
        } else {
            orderByStr = "";
        }
        offset = offset - 1;
        if (offset > 0) {
            limit = limit - offset;
            return "SELECT * FROM (" + sql + ") t " + orderByStr + " limit " + offset + "," + limit;
        } else {
            return "SELECT * FROM (" + sql + ") t " + orderByStr + " limit " + limit;
        }
    }

}
