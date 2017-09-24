package com.ijson.platform.database.db.mybatis;

import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.impl.LoadCacheFactory;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.database.db.BaseDao;
import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * description: ibatis 持久层的实现
 */
public class DaoIbatisImpl implements BaseDao {

    private CacheManager cache;
    private SqlSessionFactory sqlSessionFactory;//需要注入
    private Map<String, DaoSession> dao = new HashMap<String, DaoSession>();//不需要注入

    public DaoIbatisImpl() {
        cache = LoadCacheFactory.getInstance().getCacheManager("");
    }

    public CacheManager getCache() {
        return cache;
    }

    //	public void init(String className) {
    //		if (Validator.isEmpty(dao.get(className))) {
    //			DaoSession selectTemplate = new DaoSession(className, sqlSessionFactory);
    //			dao.put(className, selectTemplate);
    //		}
    //	}

    @SuppressWarnings("rawtypes")
    public MybatisSession getSession() {
        return new MybatisSession(sqlSessionFactory);
    }

    //	public DaoSession getSession(String className) {
    //		init(className);
    //		return dao.get(className);
    //	}

    /**
     * description: 批量删除对象
     *
     * @param param 方法参数模型
     * @return true为删除成功；false为删除失败
     */
    public boolean deleteBath(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //init(param.getSpanceName());
            int count = 0;
            List list = (List) param.getVaule();
            for (Object aList : list) {
                //count += dao.get(param.getSpanceName()).delete(param.getKey(), list.get(i));
                count += getSession().delete(param.getSpanceName(), param.getKey(), aList);
            }
            if (count > 0)
                return true;
        }
        return false;
    }

    /**
     * description: 批量修改对象值
     *
     * @param param 方法参数模型
     * @return true为修改成功；false为修改失败
     */
    public boolean editBath(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //init(param.getSpanceName());
            int count = 0;
            List list = (List) param.getVaule();
            for (Object aList : list) {
                //count += dao.get(param.getSpanceName()).update(param.getKey(), list.get(i));
                count += getSession().update(param.getSpanceName(), param.getKey(), aList);
            }
            if (count > 0)
                return true;
        }
        return false;
    }

    /**
     * description: 批量新增
     *
     * @param param 方法参数模型
     * @return true为新增成功；false为新增失败
     */
    public boolean insertBath(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //init(param.getSpanceName());
            int count = 0;
            List list = (List) param.getVaule();
            //count = dao.get(param.getSpanceName()).insertBath(list);
            count = getSession().insertBath(list, param.getSpanceName());
            if (count > 0)
                return true;
        }
        return false;
    }

    /**
     * description: 获取指定sql的count值
     *
     * @param param 方法参数模型
     * @return 返回sql执行的记录数
     */
    public long count(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //init(param.getSpanceName());
            //return dao.get(param.getSpanceName()).count(param.getKey(), param.getParams());
            return getSession().count(param.getSpanceName(), param.getKey(), param.getParams());
        }
        return 0;
    }

    /**
     * description: 删除指定对象
     *
     * @param param 方法参数模型
     * @return true为删除成功；false为删除失败
     */
    public boolean delete(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //init(param.getSpanceName());
            int count = 0;
            if (param.isDelete())
                //count = dao.get(param.getSpanceName()).delete(param.getKey(), param.getParams());
                count = getSession().delete(param.getSpanceName(), param.getKey(), param.getParams());
            else
                //count = dao.get(param.getSpanceName()).update(param.getKey(), param.getParams());
                count = getSession().update(param.getSpanceName(), param.getKey(), param.getParams());
            if (count > 0) {
                if (Validator.isNotNull(param.getCacheId())) {
                    cache.removeCacheObject(param.getCacheId());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * description: 修改对象值
     *
     * @param param 方法参数模型
     * @return true为修改成功；false为修改失败
     */
    public boolean edit(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //	init(param.getSpanceName());
            int count = 0;
            if (Validator.isEmpty(param.getVaule()))
                //count = dao.get(param.getSpanceName()).update(param.getKey(), param.getParams());
                count = getSession().update(param.getSpanceName(), param.getKey(), param.getParams());
            else
                //count = dao.get(param.getSpanceName()).update(param.getKey(), param.getVaule());
                count = getSession().update(param.getSpanceName(), param.getKey(), param.getVaule());
            if (count > 0) {
                if (Validator.isNotNull(param.getCacheId())) {
                    if (Validator.isEmpty(param.getVaule())) {
                        cache.removeCacheObject(param.getCacheId());
                    } else {
                        cache.createCacheObject(param.getCacheId(), param.getVaule());
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * description: 新增单个对象
     *
     * @param param 方法参数模型
     * @return true为新增成功；false为新增失败
     */
    public boolean insert(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //	init(param.getSpanceName());
            //	int count = dao.get(param.getSpanceName()).insert(param.getKey(), param.getVaule());
            int count = getSession().insert(param.getSpanceName(), param.getKey(), param.getVaule());
            if (count > 0) {
                if (Validator.isNotNull(param.getCacheId())) {
                    cache.createCacheObject(param.getCacheId(), param.getVaule());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * description: 分页查询
     *
     * @param param 方法参数模型
     * @return 返回page对象
     */
    public Page pageSelect(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //	init(param.getSpanceName());
            //return dao.get(param.getSpanceName()).selectPage(param.getKey(), param);
            return getSession().selectPage(param.getSpanceName(), param.getKey(), param);
        }
        return new Page();
    }

    /**
     * description: 列表查询
     *
     * @param param 方法参数模型
     * @return 返回sql执行的结果集
     */
    public List select(MethodParam param) {
        if (Validator.isNotNull(param.getSpanceName())) {
            //init(param.getSpanceName());
            //return dao.get(param.getSpanceName()).select(param.getKey(), param.getParams());
            return getSession().select(param.getSpanceName(), param.getKey(), param.getParams());
        }
        return new ArrayList();
    }

    /**
     * description:  列表查询,并转型为object
     *
     * @param param
     * @author cuiyongxu
     * @update Dec 11, 2015
     */
    @Override
    public List selectByObject(MethodParam param) {
        //TODO
        return null;
    }

    /**
     * description:  获取唯一对象实例,主要转为vo等
     *
     * @param param
     * @author cuiyongxu
     * @update Dec 11, 2015
     */
    @Override
    public Object selectSingleByObject(MethodParam param) {
        //TODO
        return null;
    }

    /**
     * description: 获取唯一对象实例
     *
     * @param param 方法参数模型
     * @return 返回sql执行后的数据对象
     */
    public Object selectSingle(MethodParam param) {
        Object obj = null;
        if (Validator.isNotNull(param.getSpanceName())) {
            //	init(param.getSpanceName());
            if (Validator.isNotNull(param.getCacheId())) {
                obj = cache.getCacheCloneByKey(param.getCacheId());
            }
            if (Validator.isEmpty(obj)) {
                //obj = dao.get(param.getSpanceName()).selectSingle(param.getKey(), param.getParams());
                obj = getSession().selectSingle(param.getSpanceName(), param.getKey(), param.getParams());
            }
        }
        return obj;
    }

    public Object selectById(String spanceName, String key, String infoId, String cacheId) {
        Object obj = null;
        if (Validator.isNotNull(spanceName)) {
            //	init(spanceName);
            if (Validator.isNotNull(cacheId)) {
                obj = cache.getCacheCloneByKey(cacheId);
            }
            if (Validator.isEmpty(obj)) {
                //obj = dao.get(spanceName).selectSingle(key, infoId);
                obj = getSession().selectSingle(spanceName, key, infoId);
                if (!Validator.isEmpty(obj) && Validator.isNotNull(cacheId))
                    cache.createCacheObject(cacheId, obj);
            }
        }
        return obj;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

}
