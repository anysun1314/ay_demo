package com.hx.auth.service;

import java.util.List;

import com.hx.auth.bean.RoleInfo;

/**
 * @author anyang
 * @date 2014-3-27 上午11:46:33
 */
public interface RoleInfoService {
	public void updateRoleInfo(RoleInfo roleInfo);
	
	public void addRoleInfo(RoleInfo roleInfo);

    public List<RoleInfo> findAll();

    public RoleInfo findById(long roleInfo);
    
}
