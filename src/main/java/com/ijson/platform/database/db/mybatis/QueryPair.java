package com.ijson.platform.database.db.mybatis;

/**
 * description: 查询参数模型
 */
public class QueryPair {
    String id;
    Object o;

    public QueryPair(String id, Object o) {
        this.id = id;
        this.o = o;
    }

    public String getId() {
        return id;
    }

    public Object getO() {
        return o;
    }

}
