package com.ijson.platform.database.interceptor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ijson.platform.cache.CacheManager;
import com.ijson.platform.cache.impl.LoadCacheFactory;
import com.ijson.platform.common.util.Validator;
import com.ijson.platform.database.model.MethodParam;
import com.ijson.platform.database.model.Page;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * description:  拦截器,用于缓存方法返回结果
 *
 * @author cuiyongxu 创建时间：Oct 27, 2015
 */
public class MethodCacheInterceptor implements MethodInterceptor {

    private CacheManager cache;

    private CacheManager getCache() {
        if (cache == null) {
            cache = LoadCacheFactory.getInstance().getCacheManager("defaultCache");
        }
        return cache;
    }

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
     */
    public void afterPropertiesSet() throws Exception {

    }

    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String targetName = invocation.getThis().getClass().getName();
        String methodName = invocation.getMethod().getName();
        Object result;
        if ("getSql".equals(methodName) || "initSystemCache".equals(methodName)) {
            result = invocation.proceed();
            return result;
        }
        MethodParam arguments = (MethodParam) invocation.getArguments()[0];
        if (Validator.isNotNull(arguments.getSpanceName())) {
            targetName = arguments.getSpanceName();
        }
        boolean mark = "pageSelect".equalsIgnoreCase(methodName);
        if ("select".equalsIgnoreCase(methodName) || mark) {
            String cacheKey = getCacheKey(methodName, arguments);
            result = getCache().getCacheObjectByKey(cacheKey);
            if (null == result) {
                result = invocation.proceed();
                if (!Validator.isEmpty(result)) {
                    if (mark) {
                        Page page = (Page) result;
                        Map<String, Object> hm = Maps.newHashMap();
                        hm.put("list", page.getPageObjects());
                        hm.put("pageIndex", page.getPageNeeded());
                        hm.put("pageSize", page.getPageSize());
                        hm.put("count", page.getCount());
                        getCache().createCacheObject(cacheKey, hm);
                    } else {
                        getCache().createCacheObject(cacheKey, result);
                    }
                }
            } else {
                if (mark) {
                    Page page = new Page();
                    HashMap<String, Object> hm = (HashMap<String, Object>) result;
                    page.setCount((Integer) hm.get("count"));
                    page.setPageNeeded((Integer) hm.get("pageIndex"));
                    page.setPageSize((Integer) hm.get("pageSize"));
                    page.setPageObjects((List) hm.get("list"));
                    return page;
                }
            }
            writeCache(cacheKey, targetName, true);
        } else {
            result = invocation.proceed();
            if (methodName.startsWith("insert") || methodName.startsWith("edit") || methodName.startsWith("delete")) {//清掉缓存
                writeCache("", targetName, false);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void writeCache(String key, String targetName, boolean iswrite) {
        List<String> list = (List<String>) getCache().getCacheObjectByKey(targetName);
        if (iswrite) {
            if (Validator.isEmpty(list)) {
                list = Lists.newArrayList();
            }
            if (!list.contains(key)) {
                list.add(key);
                getCache().createCacheObject(targetName, list);
            }
        } else {
            if (!Validator.isEmpty(list)) {
                int count = list.size();
                for (String aList : list) {
                    getCache().removeCacheObject(aList);
                }
                getCache().removeCacheObject(targetName);
            }
        }
    }

    /**
     * 生成缓存的KEY
     *
     * @param methodName 方法名
     * @param arguments  参数
     * @return
     */
    private String getCacheKey(String methodName, MethodParam arguments) {
        StringBuilder sb = new StringBuilder();
        if (Validator.isNull(arguments.getSpanceName())) {
            sb.append(arguments.getSqlStr());
        } else {
            sb.append(arguments.getSpanceName()).append(".").append(arguments.getKey());
        }
        Map<String, Object> param = arguments.getParams();
        if (param != null && !param.isEmpty()) {
            for (String key : param.keySet()) {
                sb.append(".").append(param.get(key));
            }
        }
        if ("pageSelect".equalsIgnoreCase(methodName)) {
            sb.append(".").append(arguments.getPageIndex()).append(".").append(arguments.getPageSize());
        }
        return sb.toString();
    }
}
