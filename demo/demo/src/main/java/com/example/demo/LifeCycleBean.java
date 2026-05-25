package com.example.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.stereotype.Component;

@Component
public class LifeCycleBean implements 
        BeanNameAware, BeanFactoryAware,
        InitializingBean, DisposableBean {

    public LifeCycleBean() {
        System.out.println("1. Constructor called");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("2. BeanNameAware: " + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("3. BeanFactoryAware called");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("4. @PostConstruct called");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("5. InitializingBean.afterPropertiesSet()");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("6. @PreDestroy called");
    }

    @Override
    public void destroy() {
        System.out.println("7. DisposableBean.destroy()");
    }
}