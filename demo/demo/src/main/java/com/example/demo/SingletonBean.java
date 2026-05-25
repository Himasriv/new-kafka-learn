package com.example.demo;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SingletonBean {
   /*     @Autowired
    private ObjectFactory<PrototypeBean> prototypeBeanFactory;
   // private PrototypeBean prototypeBean;

//    public PrototypeBean getPrototypeBean() {
//        System.out.println("4. singleton called");
//        return prototypeBean;
//    }

    public void usePrototypeBean() {
        PrototypeBean prototypeBean = prototypeBeanFactory.getObject();
        // Each call gets a new instance
    }*/

  /*  @Lookup
    public PrototypeBean getPrototypeBean() {
        // Spring overrides this method to return a new instance
        return null;
    }

    public void usePrototypeBean() {
        PrototypeBean prototypeBean = getPrototypeBean();
        // Each call gets a new instance
    }
*/

    @Autowired
    private ApplicationContext applicationContext;

    public void usePrototypeBean() {
        PrototypeBean prototypeBean = applicationContext.getBean(PrototypeBean.class);
        System.out.println("PrototypeBean: " + prototypeBean);
        System.out.println("destroying prototypeBean");
        try {
           // prototypeBean.destroy();
            System.out.println("PrototypeBean destroyed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // Each call gets a new instance
    }
}
