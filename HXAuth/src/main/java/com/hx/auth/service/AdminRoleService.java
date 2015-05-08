package com.hx.auth.service;

import java.util.List;

import com.hx.auth.bean.AdminRole;
/**
 * @author anyang
 * @date 2014-4-22 下午1:07:24
 */
public interface AdminRoleService {
	public void updateAdminRole(AdminRole adminRole);
	
	public void addAdminRole(AdminRole adminRole);

    public List<AdminRole> findAll();

    public AdminRole findById(long adminRoleId);
    
    public List<AdminRole> findByAdminId(long adminId);

	public void deleteAdminRole(AdminRole adminRole);

	public void deleteAdminRoles(String userRoleIds);
}
