package com.ijson.platform.database.db;

import com.ijson.platform.common.util.Validator;
import com.ijson.platform.database.db.hibernate.DaoHibernateImpl;
import com.ijson.platform.database.db.mybatis.DaoIbatisImpl;
import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;

import java.util.List;

/**
 * description: 持久层的实现 可以重写此类的方法来满足特定的业务要求
 */
public abstract class DaoImpl implements IDao {

    protected DaoHibernateImpl hibernateDao;

    protected DaoIbatisImpl ibatisDao;

    protected boolean isHibernateDao() {//判断是启用Hibernate还是ibatis
        if (Validator.isEmpty(ibatisDao)) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#count(com.persistenceLayer.core.model.MethodParam)
     */
    public long count(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.count(param);
        } else
            return ibatisDao.count(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#delete(com.persistenceLayer.core.model.MethodParam)
     */
    public boolean delete(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.delete(param);
        } else
            return ibatisDao.delete(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#deleteBath(com.persistenceLayer.core.model.MethodParam)
     */
    public boolean deleteBath(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.deleteBath(param);
        } else
            return ibatisDao.deleteBath(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#edit(com.persistenceLayer.core.model.MethodParam)
     */
    public boolean edit(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.edit(param);
        } else
            return ibatisDao.edit(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#editBath(com.persistenceLayer.core.model.MethodParam)
     */
    public boolean editBath(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.editBath(param);
        } else
            return ibatisDao.editBath(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#insert(com.persistenceLayer.core.model.MethodParam)
     */
    public boolean insert(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.insert(param);
        } else
            return ibatisDao.insert(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#insertBath(com.persistenceLayer.core.model.MethodParam)
     */
    public boolean insertBath(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.insertBath(param);
        } else
            return ibatisDao.insertBath(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#pageSelect(com.persistenceLayer.core.model.MethodParam)
     */
    public Page pageSelect(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.pageSelect(param);
        } else
            return ibatisDao.pageSelect(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#select(com.persistenceLayer.core.model.MethodParam)
     */
    public List select(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.select(param);
        } else
            return ibatisDao.select(param);
    }

    /* (non-Javadoc)
     * @see com.persistenceLayer.core.db.IDao#selectSingle(com.persistenceLayer.core.model.MethodParam)
     */
    public Object selectSingle(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao.selectSingle(param);
        } else
            return ibatisDao.selectSingle(param);
    }

    /* (non-Javadoc)
     */
    public Object selectById(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao
                    .selectById(param.getSpanceName(), param.getKey(), param.getInfoId(), param.getCacheId());
        } else
            return ibatisDao.selectById(param.getSpanceName(), param.getKey(), param.getInfoId(), param.getCacheId());
    }

    /**
     * description:  列表查询,并转型为object
     *
     * @param param
     * @return
     * @author cuiyongxu
     * @update Dec 11, 2015
     */
    @Override
    public List selectByObject(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao
                    .selectByObject(param);
        } else
            return ibatisDao.selectByObject(param);

    }

    /**
     * description:  获取唯一对象实例,主要转为vo等
     *
     * @param param
     * @return
     * @author cuiyongxu
     * @update Dec 11, 2015
     */
    @Override
    public Object selectSingleByObject(MethodParam param) {
        if (isHibernateDao()) {
            return hibernateDao
                    .selectSingleByObject(param);
        } else
            return ibatisDao.selectSingleByObject(param);
    }

    public void setHibernateDao(DaoHibernateImpl hibernateDao) {
        this.hibernateDao = hibernateDao;
    }

    public void setIbatisDao(DaoIbatisImpl ibatisDao) {
        this.ibatisDao = ibatisDao;
    }

}
