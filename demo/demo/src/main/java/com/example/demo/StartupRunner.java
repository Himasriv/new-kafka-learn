package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Autowired
    private SingletonBean singletonBean;

    @Override
    public void run(String... args) {
       // singletonBean.getPrototypeBean();
        singletonBean.usePrototypeBean();
    }


}