package com.hx.auth.service;

import java.util.List;

import com.hx.auth.bean.DepartAccount;
/**
 * @author anyang
 * @date 2014-6-18 下午7:38:54
 */
public interface DepartAccountService {
	public void updateDepartAccount(DepartAccount departAccount);
	
	public void addDepartAccount(DepartAccount departAccount);

    public List<DepartAccount> findAll();

    public DepartAccount findById(long departAccount);

	public List<DepartAccount> findByDepartmentCode(String departmentCode);
	
	public DepartAccount findByDepartmentCodeAndAccountType(String departmentCode, String accountType);

	public DepartAccount findByCustomerAccountId(long customerAccountId);
}
