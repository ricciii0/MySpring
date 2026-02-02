package com.ricci.springframework.beans.factory.support;

import com.ricci.springframework.beans.BeanException;
import com.ricci.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public interface InstantiationStrategy {
    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeanException;
}
