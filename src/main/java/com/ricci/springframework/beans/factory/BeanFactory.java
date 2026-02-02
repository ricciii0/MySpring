package com.ricci.springframework.beans.factory;

import com.ricci.springframework.beans.BeanException;

public interface BeanFactory {
    Object getBean(String beanName) throws BeanException;

    Object getBean(String beanName, Object ...args) throws BeanException;
}
