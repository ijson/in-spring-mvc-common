package com.ijson.platform.api.manager;


import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.exception.PluginConnectorException;

/**
 * description:  插件管理,需要客户端实现,并实现功能
 *
 * @author cuiyongxu 创建时间：Nov 20, 2015
 */
public interface PluginConnector {
    /**
     * description:  插件客户端实现<p>使用方式:</p>
     * <string>首先,我们需要在appliactionContext-{projectName}.xml中定义相关插件,代码如下</string>
     * <blockquote>
     * <pre>
     *  &lt;bean id="dictCateManager" class="com.ijson.dict.manager.impl.DictCateManagerImpl"&gt;
     * 	&lt;property name="plugins"&gt;
     * 		&lt;map&gt;
     * 			&lt;entry key="cateEname" value-ref="cateEnamePlugin"&gt;&lt;/entry&gt;
     * 		&lt;/map&gt;
     * 	&lt;/property>
     * &lt;/bean>
     * &lt;bean id="cateEnamePlugin" class="com.ijson.dict.manager.plugins.CateEnamePlugin"&gt;&lt;/bean&gt;
     * </pre>
     * 并且在实现类中添加以下代码:</br>
     * protected Map<String, PluginConnector> plugins;</br>
     * public void setPlugins(Map<String, PluginConnector> plugins) {</br>
     * &nbsp;&nbsp;&nbsp; this.plugins = plugins;</br>
     * }</br>
     * 由代码生成器生成代码,则存在excute方法.修改此方法:</br>
     * public Object execute(ParamsVo<DictCate> vo) {</br>
     * &nbsp;&nbsp;&nbsp;String key = vo.getMethodKey();</br>
     * &nbsp;&nbsp;&nbsp;try {</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (!Validator.isEmpty(plugins)) {</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	if (!Validator.isEmpty(plugins.get(key))) {</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		return plugins.get(key).execute(vo);</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	}</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	}</br>
     * &nbsp;&nbsp;&nbsp;} catch (Exception e) {</br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	e.printStackTrace();</br>
     * &nbsp;&nbsp;&nbsp;}</br>
     * &nbsp;&nbsp;&nbsp;return null;</br>
     * &nbsp;&nbsp;&nbsp;}</br>
     * 使用方式如下:</br>
     * &nbsp;&nbsp;&nbsp;vo.setMethodKey("cateEname");</br>
     * //当然,execute方法返回的是Object类型,根据你自己的实现,返回不同类型的参数
     * &nbsp;&nbsp;&nbsp; List<DictCate> lst = (List<DictCate>) dictCateManager.execute(vo);</br>
     * </blockquote>
     *
     * @param param
     * @return
     * @throws PluginConnectorException
     * @author cuiyongxu
     * @update Nov 20, 2015
     */
    Object execute(ParamsVo param) throws PluginConnectorException;
}
