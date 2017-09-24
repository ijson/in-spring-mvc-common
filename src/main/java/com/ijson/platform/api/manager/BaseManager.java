package com.ijson.platform.api.manager;

import com.ijson.platform.common.exception.DBServiceException;
import com.ijson.platform.api.model.ParamsVo;

import java.util.List;


/**
 * description:  公共manager接口
 * <string>此方法主要为manager实现类.其中execute为插件提供,默认情况下不会存在执行操作,只有在使用时才会用到方法;</string>
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public interface BaseManager<E> {
    /**
     * description:  保存
     *
     * @param vo 方法参数
     * @return 成功返回信息ID，失败返回空
     * @throws DBServiceException
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    String saveInfo(ParamsVo<E> vo) throws DBServiceException;

    /**
     * description:  修改
     *
     * @param vo 方法参数
     * @return 成功返回true，失败返回false
     * @throws DBServiceException
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    boolean editInfo(ParamsVo<E> vo) throws DBServiceException;

    /**
     * description:  删除
     *
     * @param vo 方法参数
     * @return 成功返回true，失败返回false
     * @throws DBServiceException
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    boolean deleteInfo(ParamsVo<E> vo) throws DBServiceException;

    /**
     * description:  获取信息数据可执行方法
     *
     * @param vo 方法参数
     * @return 返回要获取的信息数据
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    Object execute(ParamsVo<E> vo);

    /**
     * description: 按信息ID获取信息对象
     *
     * @param vo 方法参数
     * @return 成功返回信息对象，失败返回空
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    Object getInfoById(ParamsVo<E> vo);

    /**
     * description:  根据条件获取信息列表
     *
     * @param vo 方法参数
     * @return 成功返回信息列表，失败返回空
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    List<E> getList(ParamsVo<E> vo);

    /**
     * description:  根据条件获取信息数量
     *
     * @param vo 方法参数
     * @return 返回信息数量
     * @author cuiyongxu
     * @update Nov 23, 2015
     */
    long countInfo(ParamsVo<E> vo);

}
