package com.ricci.springframework.beans.factory.support;

import com.ricci.springframework.beans.factory.BeanException;
import com.ricci.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {

    private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeanException {
        Object bean = null;
        try {
            bean = createBeanInstance(beanName,beanDefinition,args);
        } catch (Exception e) {
            throw new BeanException("Instantiation of bean failed", e);
        }
        addSingleton(beanName, bean);
        return bean;
    }

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Constructor constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor ctor : declaredConstructors) {
            if (args == null) {
                if (ctor.getParameterCount() == 0) {
                    constructorToUse = ctor;
                    break;
                }
                continue;
            }
            Class<?>[] paramTypes = ctor.getParameterTypes();
            if (paramTypes.length != args.length) {
                continue;
            }
            if (isMatchParamTypes(args, paramTypes)) {
                constructorToUse = ctor;
                break;
            }
        }
        return instantiationStrategy.instantiate(beanDefinition, beanName, constructorToUse, args);
    }

    private boolean isMatchParamTypes(Object[] args, Class<?>[] paramTypes) {
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            Class<?> paramType = paramTypes[i];

            if (arg == null) {
                if (paramType.isPrimitive()) {
                    return false;
                }
                continue;
            }

            Class<?> argClass = arg.getClass();

            // 处理基本类型 <-> 包装类型
            if (paramType.isPrimitive()) {
                paramType = primitiveToWrapper(paramType);
            }

            // 允许子类/实现类向上转型
            if (!paramType.isAssignableFrom(argClass)) {
                return false;
            }
        }
        return true;
    }

    private Class<?> primitiveToWrapper(Class<?> primitiveType) {
        if (primitiveType == boolean.class) return Boolean.class;
        if (primitiveType == byte.class) return Byte.class;
        if (primitiveType == short.class) return Short.class;
        if (primitiveType == char.class) return Character.class;
        if (primitiveType == int.class) return Integer.class;
        if (primitiveType == long.class) return Long.class;
        if (primitiveType == float.class) return Float.class;
        if (primitiveType == double.class) return Double.class;
        return primitiveType; // 理论上不会走到这里
    }
}
