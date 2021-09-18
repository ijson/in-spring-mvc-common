package com.ijson.platform.api;


import com.ijson.platform.api.model.ExtPv;
import com.ijson.platform.common.exception.PluginConnectorException;

/**
 * 插件管理,需要客户端实现,并实现功能
 *
 * @author cuiyongxu 创建时间：Nov 20, 2015
 */
public interface PluginConnector {
    /**
     * 插件客户端实现<p>使用方式:</p>
     * <pre>首先,我们需要在appliactionContext-{projectName}.xml中定义相关插件,代码如下</pre>
     * <blockquote>
     * <pre>
     * <bean id="dictCateManager" class="com.ijson.dict.manager.impl.DictCateManagerImpl">
     *     <property name="plugins">
     *         <map>
     *             <entry key="cateEname" value-ref="cateEnamePlugin"></entry> *
     *         </map>
     *         </property>
     *
     *  </bean>
     *
     *  <bean id="cateEnamePlugin" class="com.ijson.dict.manager.plugins.CateEnamePlugin"></bean>
     *
     * @param param 参数
     * @return object
     */
    Object execute(ExtPv param) throws PluginConnectorException;


    /**
     * 插件标识
     *
     * @return
     */
    String pluginKey();
}
