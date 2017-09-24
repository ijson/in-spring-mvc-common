package com.ijson.platform.database;

import com.ijson.platform.database.interceptor.MethodCacheInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cuiyongxu on 17/9/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/in-spring-database.xml")
public class DatabaseTest {

    @Autowired
    private MethodCacheInterceptor resultCacheInterceptor;

    @Test
    public void create(){

    }
}
