package com.ricci.springframework.beans.factory.support;

import com.ricci.springframework.beans.BeanException;
import com.ricci.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SimpleInstantiationStrategy implements InstantiationStrategy{

    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeanException {
        Class clazz=beanDefinition.getBeanClass();
        try {
            if (ctor!=null){
                return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
            }else {
                return clazz.getDeclaredConstructor().newInstance(args);
            }
        }catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e){
            throw new BeanException("Failed to instantiate ["+clazz.getName()+"]",e);
        }
    }
}
