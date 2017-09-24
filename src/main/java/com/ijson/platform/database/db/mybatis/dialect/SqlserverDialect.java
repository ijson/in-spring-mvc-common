package com.ijson.platform.database.db.mybatis.dialect;


import com.ijson.platform.common.util.Validator;

/**
 * description:
 */
public class SqlserverDialect implements Dialect {

    /* (non-Javadoc)
     */
    @Override
    public boolean supportsLimit() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     */
    @Override
    public boolean supportsLimitOffset() {
        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     */
    @Override
    public String getLimitString(String sql, int offset, int limit, String orderby) {
        sql = sql.trim();
        String orderByStr = "";
        if (Validator.isNotNull(orderByStr)) {
            orderByStr = " order by " + orderby;
        } else {
            orderByStr = "";
        }
        StringBuilder pageSql = new StringBuilder(sql.length() + 15);
        pageSql.append(sql).insert(getAfterSelectInsertPoint(sql), " top " + (offset + limit));

        // 其实这里还是有一点问题的，就是排序问题，指定死了，有解决的提供一下，等复习到Hibernate看看Hibernat内部是如何实现的。
        //		pageSql.append("select top " + (offset + limit) + " * from( ");
        //		pageSql.append(sql);
        //		pageSql.append(" ) a " + orderByStr);
        return pageSql.toString();
    }

    private int getAfterSelectInsertPoint(String sql) {
        int selectIndex = sql.toLowerCase().indexOf("select");
        final int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
        return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }
}
