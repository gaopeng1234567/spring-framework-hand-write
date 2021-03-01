package com.patrick.spring.config;

/**
 * @author patrick
 * @date 2021/2/28 4:12 下午
 * @Des Bean的定义
 * 最簡單的事是堅持，最難的事還是堅持
 */
public class BeanDefinition {
    private String scope;
    private boolean isLazy;
    private Class beanClass;

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isLazy() {
        return isLazy;
    }

    public void setLazy(boolean lazy) {
        isLazy = lazy;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }
}
