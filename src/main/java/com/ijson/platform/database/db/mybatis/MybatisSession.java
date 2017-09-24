package com.ijson.platform.database.db.mybatis;

import com.google.common.collect.Maps;
import com.ijson.platform.common.exception.DBServiceException;
import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;


/**
 * description: 实现ibatis3数据库操作统一实现类
 */
public class MybatisSession<O> {
    private SqlSessionFactory sessionFactory;
    private SqlSession session;

    public MybatisSession(SqlSessionFactory sqlSession) {
        this.sessionFactory = sqlSession;
    }

    /**
     * description: 新增记录
     *
     * @param query     ql执行key
     * @param args      参数
     * @param nameSpace namespace
     * @return value
     */
    public int insert(String nameSpace, String query, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "insert", query, args);
        try {
            session = getSqlSession();
            return session.insert(qp.id, qp.o);
        } catch (Exception e) {
            throw new DBServiceException("执行insert方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 批量新增
     *
     * @param objList   对象集合
     * @param nameSpace namespace
     * @return value
     */
    public int insertBath(List<O> objList, String nameSpace) {
        int result = 0;
        try {
            session = getSqlSession();
            for (O obj : objList) {
                QueryPair qp = buildQueryPair(nameSpace, "insert", "Batch", obj);
                result += session.insert(qp.id, qp.o);
            }
            session.clearCache();
            return result;
        } catch (Exception e) {
            throw new DBServiceException("执行insertBath方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 修改信息
     *
     * @param query     sql执行key
     * @param args      sql执行参数
     * @param nameSpace namespace
     * @return value
     */
    public int update(String nameSpace, String query, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "update", query, args);
        try {
            session = getSqlSession();
            return session.update(qp.id, qp.o);
        } catch (Exception e) {
            throw new DBServiceException("执行update方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 删除信息
     *
     * @param query     sql执行key
     * @param nameSpace namespace
     * @param args      sql执行参数
     * @return value
     */
    public int delete(String nameSpace, String query, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "delete", query, args);
        try {
            session = getSqlSession();
            return session.delete(qp.id, qp.o);
        } catch (Exception e) {
            throw new DBServiceException("执行delete方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取单个对象
     *
     * @param query     sql执行key
     * @param args      sql执行参数
     * @param nameSpace namespace
     * @return value
     */
    public O selectSingle(String nameSpace, String query, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "select", query, args);
        try {
            session = getSqlSession();
            return (O) session.selectOne(qp.id, qp.o);
        } catch (Exception e) {
            throw new DBServiceException("执行selectSingle方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取记录数
     *
     * @param query     sql执行key
     * @param nameSpace namespace
     * @param args      sql执行参数
     * @return value
     */
    public long count(String nameSpace, String query, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "count", query, args);
        try {
            session = getSqlSession();
            return (long) (Long) session.selectOne(qp.id, qp.o);
        } catch (Exception e) {
            throw new DBServiceException("执行count方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取数据集合
     *
     * @param nameSpace namespace
     * @param query     sql执行key
     * @param args      sql执行参数
     * @return value
     */
    public List<O> select(String nameSpace, String query, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "select", query, args);
        try {
            session = getSqlSession();
            return (List<O>) session.selectList(qp.id, qp.o);
        } catch (Exception e) {
            throw new DBServiceException("执行select方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取数据集合
     *
     * @param nameSpace namespace
     * @param query     sql执行key
     * @param start     开始位置
     * @param end       结束位置
     * @param args      sql执行参数
     * @return value
     */
    public List<O> select(String nameSpace, String query, int start, int end, Object... args) {
        QueryPair qp = buildQueryPair(nameSpace, "select", query, args);
        try {
            session = getSqlSession();
            return (List<O>) session.selectList(qp.id, qp.o, new RowBounds(start, end));
        } catch (Exception e) {
            throw new DBServiceException("执行select方法出错");
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取分页信息
     *
     * @param nameSpace namespace
     * @param query     sql执行key
     * @param param     sql执行参数
     * @return value
     */
    public Page selectPage(String nameSpace, String query, MethodParam param) {
        Page pagingData = new Page();
        pagingData.setPageNeeded(param.getPageIndex());
        pagingData.setPageSize(param.getPageSize());
        List<O> list = null;
        int totalRows = pagingData.getCount();
        if ("true".equals(pagingData.getIsNeedCount())) {
            totalRows = (int) this.count(nameSpace, query, param.getParams());
        }
        pagingData.setCount(totalRows);
        try {
            param.setParams("Qrder-By", param.getOrderby());
            QueryPair qp = buildQueryPair(nameSpace, "select", query, param.getParams());
            session = getSqlSession();
            list = (List<O>) session.selectList(qp.getId(), qp.getO(), new RowBounds(pagingData.getStartRow(),
                    pagingData.getEndRow()));
            pagingData.setPageObjects(list);
            //LimitedPageData.setPagingData(pagingData, null, null);
        } catch (Exception e) {
            throw new DBServiceException("执行selectPage方法出错");
        } finally {
            closeSqlSession(session);
        }
        return pagingData;
    }

    /**
     * description: 获取执行sql的参数有效模型
     *
     * @param nameSpace namespace
     * @param type      执行sql前缀
     * @param query     执行sql后缀
     * @param args      执行sql参数
     * @return value
     */
    protected QueryPair buildQueryPair(String nameSpace, String type, String query, Object... args) {
        String id = nameSpace;
        if (!query.equals("")) {
            query = query.substring(0, 1).toUpperCase() + query.substring(1);
        }
        Object o;
        if (args.length == 0) {
            o = Maps.newHashMap();
        } else if (args.length == 1) {
            o = args[0];
        } else {
            Map<String, Object> map = Maps.newHashMap();
            String fields = query;
            if (fields.contains("By")) {
                int index = fields.indexOf("By");
                fields = fields.substring(index + 2);
            }
            String[] parts = fields.split("(By|And|Or)");
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                part = part.substring(0, 1).toLowerCase() + part.substring(1);
                map.put(part, args[i]);
            }
            o = map;
        }
        id = id + "." + type + query;
        return new QueryPair(id, o);
    }

    /**
     * description: 打开session
     *
     * @return value
     */
    private SqlSession getSqlSession() {
        return this.sessionFactory.openSession();
    }

    /**
     * description: 关闭session
     */
    private void closeSqlSession(SqlSession session) {
        if (null != session) {
            session.close();
        }
    }
}
