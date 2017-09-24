package com.ijson.platform.generator.model;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

@Data
public class TableEntity {

    private String tableName;//表名

    private String tableAttName;//类名

    private String pKColumn;//主键列名

    private List<ColumnEntity> columns = Lists.newArrayList();

    public void setColumns(ColumnEntity column) {
        this.columns.add(column);
    }
}
