package com.hx.auth.dao;

import java.util.List;

import com.hx.auth.bean.RoleFunction;
import com.hx.auth.dao.MybatisDao;

/**
 * @author anyang
 * @date 2014-5-12 下午4:53:08
 */
public interface IRoleFunctionDao extends MybatisDao<RoleFunction>{

	public List<RoleFunction> findByFunctionId(long editingId);

	public List<RoleFunction> findByRoleId(long roleId);
	
}
