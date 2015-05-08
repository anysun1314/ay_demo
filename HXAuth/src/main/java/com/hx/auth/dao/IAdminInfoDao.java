package com.hx.auth.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.dao.MybatisDao;
/**
 * @author anyang
 * @date 2014-4-18 下午2:37:04
 */
public interface IAdminInfoDao extends MybatisDao<AdminInfo> {
	
	public AdminInfo findByCodeAndName(@Param("adminCode") String adminCode,@Param("adminName") String adminName);
	
	public List<AdminInfo> findById(long adminId);
	
	public List<AdminInfo> findByCode(String adminCode);

	public List<AdminInfo> findByLoginName(String loginName);

	public List<AdminInfo> findByDepartmentCode(Map map);
	
	public List<AdminInfo> findByAdminIdCode(String departmentCode);

	public List<AdminInfo> findByDepartmentCodeConcat(Map map);
	
	public AdminInfo findByEmail(String email);
	
	public AdminInfo findByTelPhone(String telPhone);
	
	public List<AdminInfo> findAllAdminWithAccount();

	public AdminInfo findAdminInfoById(long adminId);

	public List<AdminInfo> findAdminInfosByRoleCode(String roleCode);

	public List<AdminInfo> findAdminInfosByAdminCode(String adminCode);

	public List<AdminInfo> findAdminInfosByDepartmentCodeAndRoleId(Map map);
	
	public List<AdminInfo> findAdminInfosByDepartmentCodeAndRoleCode(Map map);
}
