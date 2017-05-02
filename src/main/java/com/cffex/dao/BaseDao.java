package com.cffex.dao;

/**
 * Created by Ming on 2017/4/21.
 */
public interface BaseDao<T> {
    void add(T t);
    void delete(T t);
    void update(T t);
    T select(T t);
}
