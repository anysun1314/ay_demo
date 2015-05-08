package com.hx.auth.dao;

import com.hx.auth.bean.ConstantDefine;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Renyulin on 14-4-11 下午7:36.
 */
@Service("constantDefinePageDao")
public class ConstantDefinePageDaoImpl<ConstantDefine> extends SqlSessionDaoSupport implements IPageDao<ConstantDefine> {
    @Override
    public List<ConstantDefine> findByPage(int offset, int limit, ConstantDefine parameter) {
        RowBounds rowBounds = new RowBounds(offset, limit);
        return getSqlSession().selectList("com.hx.dao.IConstantDefineDao.findAll", parameter, rowBounds);
    }

    @Override
    public int countFind(ConstantDefine parameter) {
        return (Integer)getSqlSession().selectOne("com.hx.dao.IConstantDefineDao.countFindAllConstantDefine", parameter);
    }
}
