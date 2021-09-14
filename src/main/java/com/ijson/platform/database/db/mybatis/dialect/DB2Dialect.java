package com.ijson.platform.database.db.mybatis.dialect;


import com.ijson.platform.common.util.Validator;

/**
 * description: DB2分页插件
 *
 */
public class DB2Dialect implements Dialect {

    /* (non-Javadoc)
     * @see com.kimbanx.database.db.ibatis3.dialect.Dialect#supportsLimit()
     */
    @Override
    public boolean supportsLimit() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.kimbanx.database.db.ibatis3.dialect.Dialect#supportsLimitOffset()
     */
    @Override
    public boolean supportsLimitOffset() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.kimbanx.database.db.ibatis3.dialect.Dialect#getLimitString(java.lang.String, int, int)
     */
    @Override
    public String getLimitString(String sql, int offset, int limit, String orderby) {
        if (sql == null) {
            return "";
        }
        sql = trim(sql);
        String rowNumSql = getRowNumSql(orderby);
        StringBuilder sb = new StringBuilder(sql.length() + 20);
        sb.append("SELECT * FROM (SELECT A.*, ").append(rowNumSql).append("as ROWNUM from (");
        sb.append(sql);
        sb.append(" )A ) WHERE ROWNUM <");
        sb.append(limit + 1);
        sb.append(" and ROWNUM >");
        if (offset - 1 >= 0) {
            sb.append(offset - 1);
        }
        return sb.toString();
    }

    /**
     * 为sql添加order by语句。
     *
     * @return 如果有排序条件的话则返回添加了order by语句的sql，否则直接返回sql
     */
    private String getRowNumSql(String orderby) {
        if (Validator.isNull(orderby)) {
            return "";
        }
        return "ROW_NUMBER() OVER(ORDER BY  " + orderby + " )";
    }

    /**
     * 去除空格，如果该字符串以";"结尾则去掉。
     *
     * @param sql 源串
     */
    private String trim(String sql) {
        if (Validator.isNull(sql)) {
            return sql;
        }
        sql = sql.trim();
        if (sql.endsWith(";")) {
            sql = sql.substring(0, sql.length() - 1);
        }
        return sql;
    }
}
