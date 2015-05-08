package com.hx.auth.dao;

import java.util.List;

/**
 * User: Ren yulin
 * Date: 14-3-6  上午10:53
 */
public interface IPageDao<T> {


    public List<T> findByPage(int offset, int limit, T parameter);

    public int  countFind(T parameter);

}
