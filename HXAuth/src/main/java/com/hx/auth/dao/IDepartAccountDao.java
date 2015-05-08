package com.hx.auth.dao;

import java.util.List;

import com.hx.auth.bean.DepartAccount;
import com.hx.auth.dao.MybatisDao;
/**
 * @author anyang
 * @date 2014-6-18 上午11:21:50
 */
public interface IDepartAccountDao extends MybatisDao<DepartAccount> {

	List<DepartAccount> findByDepartmentCode(String departmentCode);

	DepartAccount findByCustomerAccountId(long customerAccountId);
	
}
