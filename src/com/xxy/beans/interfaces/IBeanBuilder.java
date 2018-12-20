package com.xxy.beans.interfaces;

public interface IBeanBuilder {
    Object getObject(Class<?> klass);
    <T> T getproxy(Class<?> klass);
}
