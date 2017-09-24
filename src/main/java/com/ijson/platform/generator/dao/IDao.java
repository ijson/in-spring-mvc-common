package com.ijson.platform.generator.dao;


import com.ijson.platform.generator.model.TableEntity;

import java.util.List;
import java.util.Map;

public interface IDao {

	List<TableEntity> getTables(String[] tableNames, Map<String, String> config);
}
