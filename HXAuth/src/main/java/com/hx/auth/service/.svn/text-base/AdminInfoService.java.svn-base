package com.hx.auth.service;

import java.util.List;
import java.util.Map;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.page.Pagination;

/**
 * 
 * @author anyang
 * @date 2014-4-17 下午4:35:55
 *
 */



public interface AdminInfoService {

	public void updateAdminInfo(AdminInfo adminInfo);
	
	public void addAdminInfo(AdminInfo adminInfo);

    public List<AdminInfo> findAll();

    /**
     * 根据id查询员工，及关联的部门信�?
     * @param adminInfo
     * @return
     */
    public List<AdminInfo> findById(long adminInfo);
    
    /**
     * 根据id查询员工信息
     * @param adminId
     * @return
     */
    public AdminInfo findAdminInfoById(long adminId);
    
    public Pagination<AdminInfo> findAllByPage(int page, int limit, AdminInfo adminInfo);
	

	public AdminInfo findByCodeAndName(String adminCode,String adminName) throws Exception;
	
	public List<AdminInfo> findByCode(String adminCode) throws Exception;

	public List<AdminInfo> findByLoginName(String loginName);
	
	public void changePwd(AdminInfo adminInfo);

	public void updateRoleInfo(AdminInfo adminInfo, Long userRoleId);

    public List<AdminInfo> findByDepartmentCode(Map map);
    
    public List<AdminInfo> findByAdminIdCode(String adminIdCode);
    
    public AdminInfo findByEmail(String email);
    
    public AdminInfo findByTelPhone(String telPhone);

    /**
     * 组织机构中查看人�?
     * @param map
     * @return
     */
	public List<AdminInfo> findByDepartmentCodeConcat(Map map);
	
	/**
	 * 查找�?��有组织机构且平台账户启用的员�?
	 * @return
	 */
	public List<AdminInfo> findAllAdminWithAccount();
	
	/**
	 * 通过角色code查询员工信息
	 * @param roleCode
	 * @return
	 */
	public List<AdminInfo> findAdminInfosByRoleCode(String roleCode);
	
	/**
	 * 通过员工code查询员工信息
	 * @param adminCode
	 * @return
	 */
	public List<AdminInfo> findAdminInfosByAdminCode(String adminCode);
	
	/**
	 * 根据组织机构编码和角色ID查人员
	 */
	public List<AdminInfo> findAdminInfosByDepartmentCodeAndRoleId(Map map);
	/**
	 * 根据组织机构编码和角色Code查人员
	 */
	List<AdminInfo> findAdminInfosByDepartmentCodeAndRoleCode(Map map);
}
