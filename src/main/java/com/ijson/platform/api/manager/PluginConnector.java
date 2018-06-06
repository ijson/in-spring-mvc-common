package com.ijson.platform.api.manager;


import com.ijson.platform.api.model.ParamsVo;
import com.ijson.platform.common.exception.PluginConnectorException;

/**
 *  插件管理,需要客户端实现,并实现功能
 *
 * @author cuiyongxu 创建时间：Nov 20, 2015
 */
public interface PluginConnector<T> {
    /**
     *   插件客户端实现&lt;p&gt;使用方式:&lt;/p&gt;
     * &lt;pre&gt;首先,我们需要在appliactionContext-{projectName}.xml中定义相关插件,代码如下&lt;/pre&gt;
     * &lt;blockquote&gt;
     * &lt;pre&gt;
     *  &lt;bean id="dictCateManager" class="com.ijson.dict.manager.impl.DictCateManagerImpl"&gt;
     * 	&lt;property name="plugins"&gt;
     * 		&lt;map&gt;
     * 			&lt;entry key="cateEname" value-ref="cateEnamePlugin"&gt;&lt;/entry&gt;
     * 		&lt;/map&gt;
     * 	&lt;/property&gt;
     * &lt;/bean&gt;
     * &lt;bean id="cateEnamePlugin" class="com.ijson.dict.manager.plugins.CateEnamePlugin"&gt;&lt;/bean&gt;
     * &lt;/pre&gt;
     * 并且在实现类中添加以下代码:&lt;/br&gt;
     * protected Map&lt;String, PluginConnector&gt; plugins;&lt;/br&gt;
     * public void setPlugins(Map&lt;String, PluginConnector&gt; plugins) {&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp; this.plugins = plugins;&lt;/br&gt;
     * }&lt;/br&gt;
     * 由代码生成器生成代码,则存在excute方法.修改此方法:&lt;/br&gt;
     * public Object execute(ParamsVo&lt;DictCate&gt; vo) {&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;String key = vo.getMethodKey();&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;try {&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;if (!Validator.isEmpty(plugins)) {&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	if (!Validator.isEmpty(plugins.get(key))) {&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		return plugins.get(key).execute(vo);&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	}&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	}&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;} catch (Exception e) {&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	e.printStackTrace();&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;}&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;return null;&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;}&lt;/br&gt;
     * 使用方式如下:&lt;/br&gt;
     * &nbsp;&nbsp;&nbsp;vo.setMethodKey("cateEname");&lt;/br&gt;
     * //当然,execute方法返回的是Object类型,根据你自己的实现,返回不同类型的参数
     * &nbsp;&nbsp;&nbsp; List&lt;DictCate&gt; lst = (List&lt;DictCate&gt;) dictCateManager.execute(vo);&lt;/br&gt;
     * &lt;/blockquote&gt;
     *
     * @param param 参数
     * @return object
     */
     T execute(ParamsVo<T> param) throws PluginConnectorException;
}
