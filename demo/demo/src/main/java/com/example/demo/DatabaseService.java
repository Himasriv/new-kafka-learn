package com.example.demo;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class DatabaseService implements
        BeanNameAware, ApplicationContextAware,
        InitializingBean, DisposableBean {

    @Autowired
    private DataSource dataSource;

    // ── Phase 2 ──────────────────────────────
    public DatabaseService() {
        System.out.println("1. Instantiated via constructor");
    }

    // ── Phase 3 ──────────────────────────────
    @Autowired
    public void setDataSource(DataSource dataSource) {
        System.out.println("2. DataSource injected via setter");
        this.dataSource = dataSource;
    }

    // ── Phase 4 ──────────────────────────────
    @Override
    public void setBeanName(String name) {
        System.out.println("3. Bean name set: " + name);
    }

   // @Override
   // public void setApplicationContext(ApplicationContext ctx) {
//        System.out.println("4. ApplicationContext injected");
//    }

    // ── Phase 6 ──────────────────────────────
    @PostConstruct
    public void postConstruct() {
        System.out.println("5. @PostConstruct — opening connection pool");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("6. afterPropertiesSet — validating config");
    }

    // ── Phase 9 ──────────────────────────────
    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy — closing connection pool");
    }

    @Override
    public void destroy() {
        System.out.println("8. destroy() — final cleanup");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
               System.out.println("4. ApplicationContext injected");

    }
}