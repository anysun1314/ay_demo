package com.hx.auth.dao;

import java.util.List;

import com.hx.auth.bean.AdminRole;
import com.hx.auth.dao.MybatisDao;
/**
 * @author anyang
 * @date 2014-4-22 下午1:05:36
 */
public interface IAdminRoleDao extends MybatisDao<AdminRole> {
	public List<AdminRole> findByAdminId(long adminId);
}
