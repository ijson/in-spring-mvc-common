package com.ijson.platform.database.db.hibernate;

import com.google.common.collect.Lists;
import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.impl.LoadCacheFactory;
import com.ijson.platform.common.exception.DBServiceException;
import com.ijson.platform.common.util.ReflectDB;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.database.db.BaseDao;
import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * description: hibernate 持久层的实现
 *
 * @author cuiyongxu 创建时间：Nov 18, 2015
 */
@Slf4j
public class DaoHibernateImpl extends HibernateDaoSupport implements BaseDao {

    private CacheManager cache;

    public DaoHibernateImpl() {
        cache = LoadCacheFactory.getInstance().getCacheManager("");
    }

    public CacheManager getCache() {
        return cache;
    }

    /**
     * description: 批量删除对象
     *
     * @param param 方法参数模型
     * @return true为删除成功；false为删除失败
     */
    public boolean deleteBath(MethodParam param) {
        List list = (List) param.getVaule();
        try {
            this.getHibernateTemplate().deleteAll(list);
        } catch (Exception e) {
            log.error("DaoHibernateImpl deleteBath ERROR:", e);
            throw new DBServiceException("执行deleteBath方法出错:" + e.getMessage());
        }
        return true;
    }

    /**
     * description: 批量修改对象值
     *
     * @param param 方法参数模型
     * @return true为修改成功；false为修改失败
     */
    public boolean editBath(MethodParam param) {
        List list = (List) param.getVaule();
        try {
            for (Object po : list) {
                this.getHibernateTemplate().update(po);
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl editBath ERROR:", e);
            throw new DBServiceException("执行editBath方法出错:" + e.getMessage());
        }
        return true;
    }

    /**
     * description: 批量新增
     *
     * @param param 方法参数模型
     * @return true为新增成功；false为新增失败
     */
    public boolean insertBath(MethodParam param) {
        List list = (List) param.getVaule();
        try {
            this.getHibernateTemplate().save(list);
        } catch (Exception e) {
            log.error("insertBath:", e);
            log.error("DaoHibernateImpl insertBath ERROR:", e);
            throw new DBServiceException("执行insertBath方法出错:" + e.getMessage());
        }
        return true;
    }

    /**
     * description: 获取指定sql的count值
     *
     * @param param 方法参数模型
     * @return 返回sql执行的记录数
     */
    public long count(MethodParam param) {
        int count = 0;
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Query query = session.createQuery(param.getSqlStr());
            getParamClass(query, param.getParams());
            Object obj = query.uniqueResult();
            if (Validator.isEmpty(obj))
                return 0;
            if ("List".equals(obj.getClass().getSimpleName())) {
                count = ((List) obj).size();
            } else {
                count = Integer.parseInt(obj.toString());
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl count ERROR:", e);
            throw new DBServiceException("执行count方法出错");
        } finally {
            session.flush();
            session.close();
        }
        return count;
    }

    /**
     * description: 删除指定对象
     *
     * @param param 方法参数模型
     * @return true为删除成功；false为删除失败
     */
    public boolean delete(MethodParam param) {
        try {
            if (Validator.isNotNull(param.getSqlStr())) {//用hql批量删除
                editOrDelForHql(param);
            } else {
                this.getHibernateTemplate().delete(param.getVaule());
                if (Validator.isNotNull(param.getCacheId())) {
                    cache.removeCacheObject(param.getCacheId());
                }
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl delete ERROR:", e);
            throw new DBServiceException("执行delete方法出错");
        }
        return true;
    }

    /**
     * description: 修改对象值
     *
     * @param param 方法参数模型
     * @return true为修改成功；false为修改失败
     */
    public boolean edit(MethodParam param) {
        try {
            this.getHibernateTemplate().clear();
            if (Validator.isNotNull(param.getSqlStr())) {//用hql批量修改
                editOrDelForHql(param);
            } else {
                this.getHibernateTemplate().update(param.getVaule());
                if (Validator.isNotNull(param.getCacheId())) {
                    cache.createCacheObject(param.getCacheId(), param.getVaule());
                }
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl edit ERROR:", e);
            throw new DBServiceException("执行edit方法出错");
        }
        return true;
    }

    /**
     * description: 新增单个对象
     *
     * @param param 方法参数模型
     * @return true为新增成功；false为新增失败
     */
    public boolean insert(MethodParam param) {
        try {
            this.getHibernateTemplate().save(param.getVaule());
            if (Validator.isNotNull(param.getCacheId())) {
                cache.createCacheObject(param.getCacheId(), param.getVaule());
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl insert ERROR:", e);
            throw new DBServiceException("执行insert方法出错:" + e.getMessage());
        }
        return true;
    }

    /**
     * description: 分页查询
     *
     * @param param 方法参数模型
     * @return 返回page对象
     */
    public Page pageSelect(MethodParam param) {
        Page page = new Page();
        int startResult = 0;
        if (param.getPageIndex() > 1) {
            startResult = param.getPageSize() * (param.getPageIndex() - 1);
        } else {
            startResult = 0;
        }
        page.setPageNeeded(param.getPageIndex());
        page.setPageSize(param.getPageSize());
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Query query = session.createQuery(param.getSqlStr());
            getParamClass(query, param.getParams());
            int rsCount = (int) param.getPageCount();
            if (rsCount == 0)
                rsCount = query.list().size();
            query.setFirstResult(startResult);
            query.setMaxResults(param.getPageSize());
            List list = query.list();
            if (!Validator.isEmpty(list)) {
                page.setPageObjects(list);
                page.setCount(rsCount);
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl pageSelect ERROR:", e);
            throw new DBServiceException("执行pageSelect方法出错:" + e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
        return page;
    }

    /**
     * description: 列表查询
     *
     * @param param 方法参数模型
     * @return 返回sql执行的结果集
     */
    public List select(MethodParam param) {
        List list = null;
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Query query = session.createQuery(param.getSqlStr());
            getParamClass(query, param.getParams());
            list = query.list();
        } catch (Exception e) {
            log.error("DaoHibernateImpl select ERROR:", e);
            throw new DBServiceException("执行select方法出错:" + e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
        return list;
    }

    /**
     * description: 获取唯一对象实例
     *
     * @param param 方法参数模型
     * @return 返回sql执行后的数据对象
     */
    @Override
    public Object selectSingle(MethodParam param) {
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Object obj = null;
            if (Validator.isNotNull(param.getCacheId())) {
                obj = cache.getCacheCloneByKey(param.getCacheId());
            }
            if (Validator.isEmpty(obj)) {
                Query query = session.createQuery(param.getSqlStr());
                getParamClass(query, param.getParams());
                obj = query.uniqueResult();
            }
            return obj;
        } catch (Exception e) {
            log.error("DaoHibernateImpl selectSingle ERROR:", e);
            throw new DBServiceException("执行selectSingle方法出错:" + e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
    }

    public Object selectById(String spanceName, String key, String infoId, String cacheId) {
        Object obj = null;
        if (Validator.isNotNull(cacheId)) {
            obj = cache.getCacheCloneByKey(cacheId);
        }
        try {
            if (Validator.isEmpty(obj)) {
                obj = this.getHibernateTemplate().get(spanceName, infoId);
                if (!Validator.isEmpty(obj) && Validator.isNotNull(cacheId))
                    cache.createCacheObject(cacheId, obj);
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl selectById ERROR:", e);
            throw new DBServiceException("执行selectById方法出错:" + e.getMessage());
        }
        return obj;
    }

    private void getParamClass(Query query, Object... args) {
        if (Validator.isNull(args) || args.length <= 0) {

        } else {
            Class queryclass = query.getClass();
            for (int k = 0; k < args.length; k++) {
                try {
                    Map<String, Object> map = (Map) args[k];
                    for (String key : map.keySet()) {
                        Object o = map.get(key);
                        String oName = o.getClass().getSimpleName();
                        Method qm = queryclass.getMethod("set" + oName, new Class[]{String.class, o.getClass()});
                        Method valueof = o.getClass().getMethod("valueOf", new Class[]{char[].class});
                        if ("".equals(key))
                            qm.invoke(query, k, valueof.invoke(null, o.toString().toCharArray()));
                        else
                            qm.invoke(query, key, valueof.invoke(null, o.toString().toCharArray()));
                    }

                } catch (Exception e) {
                    log.error("DaoHibernateImpl getParamClass ERROR:", e);
                    throw new DBServiceException("执行selectById方法出错:" + e.getMessage());
                }
            }
        }
    }

    /**
     * description: 对自定义sql进行执行
     *
     * @param param
     * @return
     * @author cuiyongxu
     */
    private int editOrDelForHql(MethodParam param) throws Exception {
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Query query = session.createQuery(param.getSqlStr());
            getParamClass(query, param.getParams());
            return query.executeUpdate();
        } catch (Exception e) {
            log.error("DaoHibernateImpl editOrDelForHql ERROR:", e);
            throw new DBServiceException("执行editOrDelForHql方法出错:" + e.getMessage());
        } finally {
            session.flush();
            session.close();
        }
    }

    /**
     * TODO 查询vo对象,主要针对关联表查询,返回Object&lt;class&gt;
     */
    @Override
    public Object selectSingleByObject(MethodParam param) {
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Object obj = null;
            Object objInstance = null;
            Object objClone = null;
            if (Validator.isNotNull(param.getCacheId())) {
                objClone = cache.getCacheCloneByKey(param.getCacheId());
            }
            if (Validator.isEmpty(objClone)) {
                Query query = session.createQuery(param.getSqlStr());
                getParamClass(query, param.getParams());
                obj = query.uniqueResult();
                String spanceName = param.getSpanceName();
                Class clazz = Class.forName(spanceName);
                objInstance = clazz.newInstance();
                if (!Validator.isEmpty(obj)) {
                    objClone = ReflectDB.getInstance().cloneObj(obj, objInstance);
                }
            }
            return objClone;
        } catch (Exception e) {
            log.error("DaoHibernateImpl selectSingleByObject ERROR:", e);
            throw new DBServiceException("执行selectSingle方法出错");
        } finally {
            session.flush();
            session.close();
        }
    }

    /**
     * TODO 查询vo对象,主要针对关联表查询,返回list&lt;class&gt;
     */
    @Override
    public List selectByObject(MethodParam param) {
        List list;
        List<Object> objList = null;
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        try {
            Query query = session.createQuery(param.getSqlStr());
            getParamClass(query, param.getParams());
            list = query.list();
            String spanceName = param.getSpanceName();
            Class clazz = Class.forName(spanceName);
            Object obj = clazz.newInstance();
            if (!Validator.isEmpty(list)) {
                objList = Lists.newArrayList();
                for (Object aList : list) {
                    Object objClone = ReflectDB.getInstance().cloneObj(aList, obj);
                    objList.add(objClone);
                }
            }
        } catch (Exception e) {
            log.error("DaoHibernateImpl selectByObject ERROR:", e);
            throw new DBServiceException("执行select方法出错");
        } finally {
            session.flush();
            session.close();
        }
        return objList;
    }

}
