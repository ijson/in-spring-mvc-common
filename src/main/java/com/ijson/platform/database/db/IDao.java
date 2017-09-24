package com.ijson.platform.database.db;

import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;

import java.util.List;


/**
 * description: 持久层接口,对外使用
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public interface IDao {
    /**
     * description: 新增单个对象
     *
     * @param param 方法参数模型
     * @return true为新增成功；false为新增失败
     */
    boolean insert(MethodParam param);

    /**
     * description: 批量新增
     *
     * @param param 方法参数模型
     * @return true为新增成功；false为新增失败
     */
    boolean insertBath(MethodParam param);

    /**
     * description: 修改对象值
     *
     * @param param 方法参数模型
     * @return true为修改成功；false为修改失败
     */
    boolean edit(MethodParam param);

    /**
     * description: 批量修改对象值
     *
     * @param param 方法参数模型
     * @return true为修改成功；false为修改失败
     */
    boolean editBath(MethodParam param);

    /**
     * description: 删除指定对象
     *
     * @param param 方法参数模型
     * @return true为删除成功；false为删除失败
     */
    boolean delete(MethodParam param);

    /**
     * description: 批量删除对象
     *
     * @param param 方法参数模型
     * @return true为删除成功；false为删除失败
     */
    boolean deleteBath(MethodParam param);

    /**
     * description: 获取指定sql的count值
     *
     * @param param 方法参数模型
     * @return 返回sql执行的记录数
     */
    long count(MethodParam param);

    /**
     * description: 获取唯一对象实例
     *
     * @param param 方法参数模型
     * @return 返回sql执行后的数据对象
     */
    Object selectSingle(MethodParam param);

    /**
     * description: 按信息ID获取唯一对象实例
     *
     * @param param 方法参数模型
     * @return 返回sql执行后的数据对象
     */
    Object selectById(MethodParam param);

    /**
     * description: 分页查询
     *
     * @param param 方法参数模型
     * @return 返回page对象
     */
    Page pageSelect(MethodParam param);

    /**
     * description: 列表查询
     *
     * @param param 方法参数模型
     * @return 返回sql执行的结果集
     */
    List select(MethodParam param);

    /**
     * description:获取要执行的sql
     *
     * @param type 标记
     * @return
     */
    String getSql(int type);

    /***
     * description: 系统启动是初始缓存
     */
    //void initSystemCache();

    /**
     * description:  列表查询,并转型为object
     *
     * @param param
     * @return
     * @author cuiyongxu
     * @update Dec 11, 2015
     */
    List selectByObject(MethodParam param);

    /**
     * description:  获取唯一对象实例,主要转为vo等
     *
     * @param param
     * @return
     * @author cuiyongxu
     * @update Dec 11, 2015
     */
    Object selectSingleByObject(MethodParam param);
}
