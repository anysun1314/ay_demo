package com.hx.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.auth.bean.DepartAccount;
import com.hx.auth.dao.IDepartAccountDao;

/**
 * @author anyang
 * @date 2014-6-18 下午2:55:25
 */
@Service("departAccountService")
public class DepartAccountServiceImpl implements DepartAccountService{
	@Autowired
	private IDepartAccountDao departAccountDao;

	/**
	 * 更新部门对应的账户信�?
	 */
	@Override
	public void updateDepartAccount(DepartAccount departAccount) {
		departAccountDao.update(departAccount);
	}

	/**
	 * 保存部门对应的账户信�?
	 */
	@Override
	public void addDepartAccount(DepartAccount departAccount) {
		departAccountDao.insert(departAccount);
	}

	@Override
	public List<DepartAccount> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DepartAccount findById(long departAccount) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 通过部门code查询关联的账�?
	 */
	@Override
	public List<DepartAccount> findByDepartmentCode(String departmentCode) {
		return departAccountDao.findByDepartmentCode(departmentCode);
	}

	/**
	 * 通过客户账户Id查询相应的账�?
	 */
	@Override
	public DepartAccount findByCustomerAccountId(long customerAccountId) {
		return departAccountDao.findByCustomerAccountId(customerAccountId);
	}

	/**
	 * 通过部门code和账户类型，查询关联的账�?
	 */
	@Override
	public DepartAccount findByDepartmentCodeAndAccountType(
			String departmentCode, String accountType) {
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("accountType", accountType);
		map.put("delState", DepartAccount.DELSTATE_UNDELETED);
		return departAccountDao.findBy(map).isEmpty() ? null : departAccountDao.findBy(map).get(0);
	}
}
