package com.ijson.platform.database.db.mybatis;

import com.google.common.collect.Maps;
import com.ijson.platform.common.exception.DBServiceException;
import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Map;


/**
 * description: 实现ibatis3数据库操作统一实现类
 */
@Slf4j
public class DaoSession<O> {
    private SqlSessionFactory sessionFactory;
    protected String nameSpace;
    private SqlSession session;

    public DaoSession(String nameSpace, SqlSessionFactory sqlSession) {
        this.nameSpace = nameSpace;
        this.sessionFactory = sqlSession;
    }

    /**
     * description: 新增记录
     *
     * @param query ql执行key
     * @param args  参数
     * @return insert count
     */
    public int insert(String query, Object... args) {
        QueryPair qp = buildQueryPair("insert", query, args);
        try {
            session = getSqlSession();
            return session.insert(qp.id, qp.o);
        } catch (Exception e) {
            log.error("DaoSession insert ERROR:",e);
            throw new DBServiceException("执行insert方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 批量新增
     *
     * @param objList 对象集合
     * @return ok
     */
    public int insertBath(List<O> objList) {
        int result = 0;
        try {
            session = getSqlSession();
            for (O obj : objList) {
                QueryPair qp = buildQueryPair("insert", "Batch", obj);
                result += session.insert(qp.id, qp.o);
            }
            session.clearCache();
            return result;
        } catch (Exception e) {
            log.error("DaoSession insertBath ERROR:",e);
            throw new DBServiceException("执行insertBath方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 修改信息
     *
     * @param query sql执行key
     * @param args  sql执行参数
     * @return ok
     */
    public int update(String query, Object... args) {
        QueryPair qp = buildQueryPair("update", query, args);
        try {
            session = getSqlSession();
            return session.update(qp.id, qp.o);
        } catch (Exception e) {
            log.error("DaoSession update ERROR:",e);
            throw new DBServiceException("执行update方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 删除信息
     *
     * @param query sql执行key
     * @param args  sql执行参数
     * @return ok
     */
    public int delete(String query, Object... args) {
        QueryPair qp = buildQueryPair("delete", query, args);
        try {
            session = getSqlSession();
            return session.delete(qp.id, qp.o);
        } catch (Exception e) {
            log.error("DaoSession delete ERROR:",e);
            throw new DBServiceException("执行delete方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取单个对象
     *
     * @param query sql执行key
     * @param args  sql执行参数
     * @return Object
     */
    public O selectSingle(String query, Object... args) {
        QueryPair qp = buildQueryPair("select", query, args);
        try {
            session = getSqlSession();
            return (O) session.selectOne(qp.id, qp.o);
        } catch (Exception e) {
            log.error("DaoSession selectSingle ERROR:",e);
            throw new DBServiceException("执行selectSingle方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取记录数
     *
     * @param query sql执行key
     * @param args  sql执行参数
     * @return count
     */
    public long count(String query, Object... args) {
        QueryPair qp = buildQueryPair("count", query, args);
        try {
            session = getSqlSession();
            return (long) (Long) session.selectOne(qp.id, qp.o);
        } catch (Exception e) {
            log.error("DaoSession count ERROR:",e);
            throw new DBServiceException("执行count方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取数据集合
     *
     * @param query sql执行key
     * @param args  sql执行参数
     * @return value
     */
    public List<O> select(String query, Object... args) {
        QueryPair qp = buildQueryPair("select", query, args);
        try {
            session = getSqlSession();
            return (List<O>) session.selectList(qp.id, qp.o);
        } catch (Exception e) {
            log.error("DaoSession select ERROR:",e);
            throw new DBServiceException("执行select方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取数据集合
     *
     * @param query sql执行key
     * @param start 开始位置
     * @param end   结束位置
     * @param args  sql执行参数
     * @return list
     */
    public List<O> select(String query, int start, int end, Object... args) {
        QueryPair qp = buildQueryPair("select", query, args);
        try {
            session = getSqlSession();
            return (List<O>) session.selectList(qp.id, qp.o, new RowBounds(start, end));
        } catch (Exception e) {
            log.error("DaoSession select ERROR:",e);
            throw new DBServiceException("执行select方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
    }

    /**
     * description: 获取分页信息
     *
     * @param query sql执行key
     * @param param sql执行参数
     * @return value
     */
    public Page selectPage(String query, MethodParam param) {
        Page pagingData = new Page();
        pagingData.setPageNeeded(param.getPageIndex());
        pagingData.setPageSize(param.getPageSize());
        List<O> list;
        int totalRows = pagingData.getCount();
        if ("true".equals(pagingData.getIsNeedCount())) {
            totalRows = (int) this.count(query, param.getParams());
        }
        pagingData.setCount(totalRows);
        try {
            param.setParams("Qrder-By", param.getOrderby());
            QueryPair qp = buildQueryPair("select", query, param.getParams());
            session = getSqlSession();
            list = (List<O>) session.selectList(qp.getId(), qp.getO(), new RowBounds(pagingData.getStartRow(),
                    pagingData.getEndRow()));
            pagingData.setPageObjects(list);
            //LimitedPageData.setPagingData(pagingData, null, null);
        } catch (Exception e) {
            log.error("DaoSession selectPage ERROR:",e);
            throw new DBServiceException("执行selectPage方法出错:" + e.getMessage());
        } finally {
            closeSqlSession(session);
        }
        return pagingData;
    }

    /**
     * description: 获取执行sql的参数有效模型
     *
     * @param type  执行sql前缀
     * @param query 执行sql后缀
     * @param args  执行sql参数
     * @return value
     */
    protected QueryPair buildQueryPair(String type, String query, Object... args) {
        String id = nameSpace;
        if (!query.equals("")) {
            query = query.substring(0, 1).toUpperCase() + query.substring(1);
        }
        Object o;
        if (args.length == 0) {
            o = null;
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
