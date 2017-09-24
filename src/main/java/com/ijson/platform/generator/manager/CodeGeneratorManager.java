package com.ijson.platform.generator.manager;


import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.generator.model.TableEntity;

import java.util.Map;

/**
 * description: 代码
 *
 * @author cuiyongxu 创建时间：Nov 17, 2015
 */
public interface CodeGeneratorManager {

    /**
     * 代码统一生成器入口方法
     *
     * @param config config
     * @param vo 方法参数
     */
    void execute(ParamsVo<TableEntity> vo, Map<String, String> config);
}
