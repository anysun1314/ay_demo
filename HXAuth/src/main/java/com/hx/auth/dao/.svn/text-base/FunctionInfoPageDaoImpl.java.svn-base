package com.hx.auth.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.dao.IPageDao;

/**
 * @author anyang
 * @date 2014-5-12 下午5:55:24
 */
public class FunctionInfoPageDaoImpl extends SqlSessionDaoSupport implements
		IPageDao<FunctionInfo> {

	@Override
	public List<FunctionInfo> findByPage(int offset, int limit,
			FunctionInfo functionInfo) {
		RowBounds rowBounds = new RowBounds(offset, limit);
		return getSqlSession().selectList("com.hx.auth.dao.IFunctionInfoDao.findAll", functionInfo, rowBounds);
	}

	@Override
	public int countFind(FunctionInfo functionInfo) {
		return (Integer)getSqlSession().selectOne("com.hx.auth.dao.IFunctionInfoDao.countAllFunctionInfo", functionInfo);
	}
}
