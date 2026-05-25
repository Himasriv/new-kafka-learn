package com.example.demo;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PrototypeBean implements DisposableBean {
    public PrototypeBean() {
        System.out.println("2. PrototypeBean called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("3. PrototypeBean destroyed");
    }
    // Prototype logic
}

