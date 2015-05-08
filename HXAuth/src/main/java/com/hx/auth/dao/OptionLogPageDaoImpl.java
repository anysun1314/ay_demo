package com.hx.auth.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hx.auth.bean.OptionLog;
import com.hx.auth.dao.IPageDao;

/**
 * @author anyang
 * @date 2014-5-26 上午10:09:09
 */
public class OptionLogPageDaoImpl extends SqlSessionDaoSupport implements
		IPageDao<OptionLog> {

	@Override
	public List<OptionLog> findByPage(int offset, int limit, OptionLog optionLog) {
		RowBounds rowBounds = new RowBounds(offset, limit);
		return getSqlSession().selectList("com.hx.auth.dao.IOptionLogDao.findAll", optionLog, rowBounds);
	}

	@Override
	public int countFind(OptionLog optionLog) {
		return (Integer)getSqlSession().selectOne("com.hx.auth.dao.IOptionLogDao.countAllOptionLog", optionLog);
	}
}
