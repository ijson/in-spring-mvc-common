package com.ijson.platform.database.db;

import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;

import java.util.List;


/**
 * description:  持久层接口公共接口，负责定义hibernate和myibatis底层接口，不对外使用
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public interface BaseDao {

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
     * description: 获取唯一对象实例
     *
     * @param spanceName 空间名(myibatis必填)
     * @param key        执行KEY(必填,myibatis为sql的ID，hibernate为entity名称)
     * @param infoId     信息ID
     * @param cacheId    缓存ID
     * @return 返回执行后的数据对象
     */
    Object selectById(String spanceName, String key, Object infoId, String cacheId);

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
     * description:  列表查询,并转型为object
     *
     * @param param param
     * @author cuiyongxu
     * @return value
     */
    List selectByObject(MethodParam param);

    /**
     * description:  获取唯一对象实例,主要转为vo等
     *
     * @param param param
     * @author cuiyongxu
     * @return value
     */
    Object selectSingleByObject(MethodParam param);
}
