package com.ijson.platform.generator.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ColumnEntity {

    private String columnName;//列名
    //获得指定列的数据类型名
    private String columnTypeName = "";
    //对应数据类型的类
    private String columnClassName;
    //在数据库中类型的最大字符个数
    private int precision;
    //是否为空
    private int isNullable;//0为不可空,1为可空

    private String attrName;//属性名
    private String jdbcType;//数据类型

}
