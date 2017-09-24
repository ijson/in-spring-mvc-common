package com.ijson.platform.database.db.mybatis.dialect;


import com.ijson.platform.common.util.Validator;

/**
 * description: oracle分页插件
 */
public class OracleDialect implements Dialect {

    public boolean supportsLimit() {
        return true;
    }

    public boolean supportsLimitOffset() {
        return true;
    }

    /**
     * 将sql变成分页sql语句,提供将offset及limit使用占位符(placeholder)替换.
     * <pre>
     * 如mysql
     * dialect.getLimitString(&quot;select * from user&quot;, 12, 0,&quot;:limit&quot;) 将返回
     * select * from user limit :offset,:limit
     * </pre>
     *
     * @return 包含占位符的分页sql
     */
    public String getLimitString(String sql, int startRow, int endRow, String orderby) {
        if (sql == null)
            return "";
        sql = trim(sql);
        sql = decorateSql(sql, orderby);
        StringBuilder sb = new StringBuilder(sql.length() + 20);
        sb.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (");
        sb.append(sql);
        sb.append(" )A WHERE ROWNUM <=");
        sb.append(endRow);
        sb.append(")WHERE RN >=");
        if (startRow >= 0) {
            sb.append(startRow);
        }
        return sb.toString();
    }

    /**
     * 为sql添加order by语句。
     *
     * @param sql 源sql语句
     * @return 如果有排序条件的话则返回添加了order by语句的sql，否则直接返回sql
     */
    private String decorateSql(String sql, String orderby) {
        if (Validator.isNull(sql)) {
            return sql;
        }
        StringBuilder strBuf = new StringBuilder();

        if (Validator.isNull(orderby)) {
            return sql;
        }
        strBuf.append("select * from (").append(sql).append(" order by ").append(orderby).append(") Expr1");
        return strBuf.toString();
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
            sql = sql.substring(0, sql.length() - ";".length());
        }
        return sql;
    }
}
