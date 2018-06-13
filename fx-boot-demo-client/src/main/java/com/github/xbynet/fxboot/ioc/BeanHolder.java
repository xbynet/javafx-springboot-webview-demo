package com.github.xbynet.fxboot.ioc;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author anzhou.tjw
 * @date 2018/6/13 下午3:39
 */
@Component
public class BeanHolder implements ApplicationContextAware,BeanFactoryAware,BeanPostProcessor {

    private static ApplicationContext applicationContext;

    private static BeanFactory beanFactory;

    private  ConcurrentHashMap<String,Object> bizBeanMap=new ConcurrentHashMap<>();
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanHolder.beanFactory=beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
        return o;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
        //仅注册本应用包下的bean到容器
        if(o.getClass().getName().startsWith("com.github.xbynet")){
            String name=s;
            if(s.contains(".")){
                name= StringUtils.uncapitalize(o.getClass().getSimpleName());
            }
            bizBeanMap.put(name,o);
        }
        return o;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanHolder.applicationContext=applicationContext;
    }

    public ConcurrentHashMap<String, Object> getBizBeanMap() {
        return bizBeanMap;
    }

    public static void main(String[] args) {
        String str=StringUtils.uncapitalize(BeanHolder.class.getSimpleName());
        System.out.println(str);
    }
}
