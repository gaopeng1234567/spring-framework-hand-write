package com.patrick.spring.context;

import com.alibaba.fastjson.JSON;
import com.patrick.spring.annotation.*;
import com.patrick.spring.config.BeanDefinition;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author patrick
 * @date 2021/2/28 3:24 下午
 * @Des bean容器
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class PatrickApplicationContext {

    private Class config;

    //BeanDefinition map
    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap();
    //单例池
    private Map<String, Object> singletonObjectMap = new HashMap();

    public PatrickApplicationContext(Class config) {
        this.config = config;

        //扫描
        scan(config);
        //创建非懒加载的单例bean
        Map<String, BeanDefinition> collect = beanDefinitionMap.entrySet()
                .stream()
                .filter((e) -> "singleton".equals(e.getValue().getScope()) && !e.getValue().isLazy())
                //创建一个bean
                .peek((e) -> singletonObjectMap.put(e.getKey(), createBean(e.getValue())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println(JSON.toJSONString(collect, true));
    }

    //开始bean的生命周期
    private Object createBean(BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        try {
            Object instance = beanClass.getDeclaredConstructor().newInstance();
            //依赖注入
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    //byType or byName
                    Object bean = getBean(field.getName());
                    field.setAccessible(true);
                    field.set(instance, bean);
                }
            }
            return instance;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Object getBean(String beanName) {
        if (!beanDefinitionMap.containsKey(beanName)) {
            throw new NullPointerException();
        } else {
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            Optional.ofNullable(beanDefinition)
                    .orElseThrow(NullPointerException::new);
            if ("singleton".equals(beanDefinition.getScope())) {
                //单例池
                return singletonObjectMap.get(beanName);
            }
            if ("prototype".equals(beanDefinition.getScope())) {
                return createBean(beanDefinition);
            }
        }
        return null;
    }

    private void scan(Class config) {
        if (config.isAnnotationPresent(ComponentScan.class)) {
            ComponentScan componentScan = (ComponentScan) config.getAnnotation(ComponentScan.class);
            String path = componentScan.value();//扫描路径
            //通过 AppClassLoader获取class路径
            ClassLoader loader = PatrickApplicationContext.class
                    .getClassLoader();
            URL resource = loader.getResource(path.replace(".", "/"));
            File file = new File(resource.getFile());
            //扫描class
            for (File listFile : file.listFiles()) {
                String absolutePath = listFile.getAbsolutePath();
                if (absolutePath.endsWith(".class")) {
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"))
                            .replace("/", ".");
                    try {
                        Class<?> aClass = loader.loadClass(absolutePath);
                        System.out.println(aClass);
                        //是否加载bean
                        if (aClass.isAnnotationPresent(Component.class)) {
                            //bean
                            Component annotation = aClass.getAnnotation(Component.class);
                            String beanName = annotation.value();
                            //创建BeanDefinition对象，对象的定义
                            BeanDefinition beanDefinition = new BeanDefinition();
                            beanDefinition.setBeanClass(aClass);
                            //是否懒加载
                            if (aClass.isAnnotationPresent(Lazy.class)) {
                                beanDefinition.setLazy(true);
                            }
                            //是否原型
                            if (aClass.isAnnotationPresent(Scope.class)) {
                                Scope scope = aClass.getAnnotation(Scope.class);
                                beanDefinition.setScope(scope.value());
                            } else {
                                //单列
                                beanDefinition.setScope("singleton");
                            }
                            //放入map,避免每次都加载
                            beanDefinitionMap.put(beanName, beanDefinition);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
//
            }
        }
    }


}
