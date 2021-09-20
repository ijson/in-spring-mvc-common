package com.ijson.platform.api;

import com.ijson.platform.api.entity.BaseEntity;
import com.ijson.platform.api.model.ExtPv;
import com.ijson.platform.common.exception.DBServiceException;

import java.util.List;


/**
 * description:  公共manager接口
 * <pre>此方法主要为manager实现类.其中execute为插件提供,默认情况下不会存在执行操作,只有在使用时才会用到方法;</pre>
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public interface BaseManager<E extends BaseEntity> {
    /**
     * description:  保存
     *
     * @param vo 方法参数
     * @return 成功返回信息ID，失败返回空
     * @return id
     */
    String saveInfo(ExtPv<E> vo) throws DBServiceException;

    /**
     * description:  修改
     *
     * @param vo 方法参数
     * @return 成功返回true，失败返回false
     */
    boolean editInfo(ExtPv<E> vo) throws DBServiceException;

    /**
     * description: 按信息ID获取信息对象
     *
     * @param vo 方法参数
     * @return 成功返回信息对象，失败返回空
     */
    E getInfoById(ExtPv<E> vo);

    /**
     * description:  删除
     *
     * @param vo 方法参数
     * @return 成功返回true，失败返回false
     */
    boolean deleteInfo(ExtPv<E> vo) throws DBServiceException;

    /**
     * description:  获取信息数据可执行方法
     *
     * @param vo 方法参数
     * @return 返回要获取的信息数据
     */
    Object execute(ExtPv<E> vo);


    /**
     * description:  根据条件获取信息列表
     *
     * @param vo 方法参数
     * @return 成功返回信息列表，失败返回空
     */
    List<E> getList(ExtPv<E> vo);

    /**
     * description:  根据条件获取信息数量
     *
     * @param vo 方法参数
     * @return 返回信息数量
     */
    long countInfo(ExtPv<E> vo);

}
