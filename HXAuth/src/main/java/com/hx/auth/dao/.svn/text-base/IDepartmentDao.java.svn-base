package com.hx.auth.dao;

import java.util.List;

import com.hx.auth.bean.Department;
import com.hx.auth.dao.MybatisDao;

/**
 * 
 * @author anyang
 * @date 2014-4-1 上午11:21:50
 *
 */
public interface IDepartmentDao extends MybatisDao<Department>{

    public Department findByCode(String departmentCode);

	public List<Department> findChildByCode(Department department);

	//public CustomerAccount findCustomerAccountByCode(String departmentCode);
	
	public List<Department> findByAdminCode(String AdminCode);
	
	public List<Department> findAllRoots(Department department);
	
	public List<Department> findAllChildByCode(String oldCode);
	
	public List<Long> isExistDepartment();

	//public List<Department> findDepartmentByNodeType(char nodetype);
}
