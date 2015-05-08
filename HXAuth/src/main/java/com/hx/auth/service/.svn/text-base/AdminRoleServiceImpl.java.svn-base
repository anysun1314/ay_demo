package com.hx.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.AdminRole;
import com.hx.auth.dao.IAdminRoleDao;
/**
 * @author anyang
 * @date 2014-4-22 下午1:14:31
 */
@Service("adminRoleService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class AdminRoleServiceImpl implements AdminRoleService {

	@Autowired
	private IAdminRoleDao adminRoleDao ;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateAdminRole(AdminRole adminRole) {
		adminRoleDao.update(adminRole);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addAdminRole(AdminRole adminRole) {
		adminRoleDao.insert(adminRole);
	}

	@Override
	public List<AdminRole> findAll() {
		return adminRoleDao.findAll();
	}

	@Override
	public AdminRole findById(long adminRoleId) {
		return adminRoleDao.findById(adminRoleId);
	}

	@Override
	public List<AdminRole> findByAdminId(long adminId) {
		return adminRoleDao.findByAdminId(adminId);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void deleteAdminRole(AdminRole adminRole) {
		adminRole.setDelState(AdminRole.DELSTATE_DELETED);
		adminRoleDao.delete(adminRole);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void deleteAdminRoles(String userRoleIds) {
		String[] userRoleIdArray = userRoleIds.split(",");
		if(userRoleIdArray != null && userRoleIdArray.length > 0) {
			AdminRole adminRole = new AdminRole();
			for(String userRoleIdStr : userRoleIdArray) {
				long userRoleId = Long.parseLong(userRoleIdStr);
				adminRole.setUserRoleId(userRoleId);
				adminRole.setDelState(AdminRole.DELSTATE_DELETED);
				adminRoleDao.delete(adminRole);
			}
		}
	}

}
