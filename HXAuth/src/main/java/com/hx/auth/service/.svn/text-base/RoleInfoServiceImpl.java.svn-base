package com.hx.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.RoleInfo;
import com.hx.auth.dao.IRoleInfoDao;

/**
 * @author anyang
 * @date 2014-3-27 上午11:48:42
 */
@Service("roleInfoService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class RoleInfoServiceImpl implements RoleInfoService {
	@Autowired
	private IRoleInfoDao roleInfoDao;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateRoleInfo(RoleInfo roleInfo) {
		roleInfoDao.update(roleInfo);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addRoleInfo(RoleInfo roleInfo) {
		roleInfoDao.insert(roleInfo);
	}

	@Override
	public List<RoleInfo> findAll() {
		return roleInfoDao.findAll();
	}

	@Override
	public RoleInfo findById(long roleInfoId) {
		return roleInfoDao.findById(roleInfoId);
	}


}
