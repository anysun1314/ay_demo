package com.hx.auth.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.dao.IPageDao;
/**
 * @author anyang
 * @date 2014-4-18 上午11:07:17
 */
public class AdminInfoPageDaoImpl extends SqlSessionDaoSupport implements
		IPageDao<AdminInfo> {

	@Override
	public List<AdminInfo> findByPage(int offset, int limit, AdminInfo adminInfo) {
		RowBounds rowBounds = new RowBounds(offset, limit);
        return getSqlSession().selectList("com.hx.auth.dao.IAdminInfoDao.findAll", adminInfo, rowBounds);
	}

	@Override
	public int countFind(AdminInfo adminInfo) {
		return (Integer)getSqlSession().selectOne("com.hx.auth.dao.IAdminInfoDao.countAllAdminInfo", adminInfo);	}

}
