package com.hx.auth.dao;

import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-4-8 下午3:33.
 */
public interface MybatisDao<T> {

    public void insert(T t);

    public void update(T t);

    public void delete(Object obj);

    public void deleteById(Object objKey);

    public <T> T findById(Object objKey);

    public List<T> findBy(Map map);

    public List<T> findBy(T t);


    public List<T> findAll();

    public List<T> findAll(T t);


}
