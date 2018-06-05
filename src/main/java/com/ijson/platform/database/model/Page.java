package com.ijson.platform.database.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * description: 分页实体Bean，包含分页逻辑和前台显示代码
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
@Setter
@Getter
public class Page<T> implements Serializable {

    private int count = 0; // 记录总数
    private int pageSize = -1; // 每页记录数
    private int pageCount = 0; // 总页数
    private int pageNeeded = 1; // 当前页码
    private int startRow = 1; // 记录开始数
    private int endRow = 15; // 记录结束数
    private String sortFieldName; // 排序字段
    private String sortByAD; // 排序方式
    private String isNeedCount = "true"; // 是否需要重新从数据库中获取记录总数
    private static int maxPageSize = 100; // 默认每页的最大数
    public static final String PAGER_IMAGE_LOCATION_PATH = "pagerImageLocationPath"; // 图片存放路径常量字符串

    private List<T> pageObjects;

    /**
     * 排序sql字符串,例如现在要用字段fiedl_1进行倒序,field_2 进行顺序排序.则该字符串的内容为: "field_1
     * asc,field_2 asc" 如果只有单个排序字段则直接写成:"field_1 asc"
     */
    private String orderByStr;

    public Page() {
    }

    public Page(String sortFieldName, String sortByAD) {
        this.sortFieldName = sortFieldName;
        this.sortByAD = sortByAD;
    }

    /**
     * 设置总记录数,同时根据总记录数计算总页数,开始记录数,截止记录数
     * @param totalRows totalRows
     */
    public void setCount(int totalRows) {
        if (pageSize != 0) {
            pageCount = totalRows / pageSize;
            if (totalRows % pageSize != 0) {
                pageCount++;
            }
        }
        this.count = totalRows;

        if (pageCount < pageNeeded) {
            pageNeeded = 1;
        }

        startRow = (getPageNeeded() - 1) * getPageSize() + 1;
        endRow = getPageSize() * getPageNeeded();
        if (endRow > totalRows)
            endRow = totalRows;

    }



    /**
     * 设置排序sql字符串
     * <p>
     * 字符串传递规则:例如现在要用字段fiedl_1进行倒序,field_2 进行顺序排序. 则该字符串的内容为: "field_1
     * asc,field_2 asc" 如果只有单个排序字段则直接写成:"field_1 asc"
     *
     * @param orderByStr orderByStr
     */
    public void setOrderByStr(String orderByStr) {
        this.orderByStr = orderByStr;
    }

}
