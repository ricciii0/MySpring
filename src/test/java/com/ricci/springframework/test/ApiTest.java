package com.ricci.springframework.test;

import com.ricci.springframework.beans.PropertyValue;
import com.ricci.springframework.beans.PropertyValues;
import com.ricci.springframework.beans.factory.config.BeanDefinition;
import com.ricci.springframework.beans.factory.config.BeanReference;
import com.ricci.springframework.beans.factory.support.DefaultListableBeanFactory;
import com.ricci.springframework.test.bean.UserDao;
import com.ricci.springframework.test.bean.UserService;
import org.junit.Test;

public class ApiTest {

    //在 IntelliJ 的 Run/Debug Configurations 里加 VM options：
    //
    //--add-opens java.base/java.lang=ALL-UNNAMED
    @Test
    public void test_BeanFactory() {
        //1.
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //2.
        beanFactory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

        //3.
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("uID", "21322"));
        propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));

        //4.
        BeanDefinition beanDefinition = new BeanDefinition(UserService.class, propertyValues);
        beanFactory.registerBeanDefinition("UserService",beanDefinition);

        //5.
        UserService userService = (UserService) beanFactory.getBean("UserService");
        userService.queryUserInfo();
    }
}
