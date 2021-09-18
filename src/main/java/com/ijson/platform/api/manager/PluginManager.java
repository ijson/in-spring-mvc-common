package com.ijson.platform.api.manager;

import com.google.common.base.Strings;
import com.ijson.platform.api.PluginConnector;
import com.ijson.platform.api.model.ExtPv;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * desc:
 * author: cuiyongxu
 * create_time: 2021/9/18-2:38 PM
 **/
@Component
public class PluginManager extends ApplicationObjectSupport {

    private Map<String, PluginConnector> handlers;

    @PostConstruct
    public void init() {
        Map<String, PluginConnector> taskCompleteHandler = getApplicationContext().getBeansOfType(PluginConnector.class);
        handlers = taskCompleteHandler.values().stream().collect(Collectors.toMap(PluginConnector::pluginKey, item -> item));
    }


    public Object execute(ExtPv<?> paramsVo) {
        String pluginKey = paramsVo.getPluginKey();
        if (Strings.isNullOrEmpty(pluginKey)) {
            return null;
        }
        return handlers.get(pluginKey).execute(paramsVo);
    }
}
