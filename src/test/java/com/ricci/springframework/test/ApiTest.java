package com.ricci.springframework.test;

import com.ricci.springframework.beans.factory.config.BeanDefinition;
import com.ricci.springframework.beans.factory.BeanFactory;
import com.ricci.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.ricci.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    //在 IntelliJ 的 Run/Debug Configurations 里加 VM options：
    //
    //--add-opens java.base/java.lang=ALL-UNNAMED
    @Test
    public void test_BeanFactory(){
        DefaultListableBeanFactory beanFactory= new DefaultListableBeanFactory();

        BeanDefinition beanDefinition=new BeanDefinition(UserService.class);
        beanFactory.registryBeanDefinition("UserService",beanDefinition);

        UserService userService= (UserService) beanFactory.getBean("UserService","Ricci");
        userService.queryUserInfo();
    }
}
