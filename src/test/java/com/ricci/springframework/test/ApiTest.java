package com.ricci.springframework.test;

import com.ricci.springframework.beans.factory.config.BeanDefinition;
import com.ricci.springframework.beans.factory.BeanFactory;
import com.ricci.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.ricci.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    @Test
    public void test_BeanFactory(){
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();

        BeanDefinition beanDefinition=new BeanDefinition(UserService.class);
        beanFactory.registryBeanDefinition("UserService",beanDefinition);

        UserService userService=(UserService)beanFactory.getBean("UserService");
        userService.queryUserInfo();

        UserService userService_singleton=(UserService) beanFactory.getBean("UserService");
        userService_singleton.queryUserInfo();
    }
}
