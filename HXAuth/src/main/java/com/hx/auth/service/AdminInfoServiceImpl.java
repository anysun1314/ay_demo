package com.hx.auth.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.AdminRole;
import com.hx.auth.dao.AdminInfoPageDaoImpl;
import com.hx.auth.dao.IAdminInfoDao;
import com.hx.auth.dao.IAdminRoleDao;
import com.hx.auth.page.Pagination;
import com.hx.auth.util.MD5Key;

/**
 * @author anyang
 * @date 2014-4-17 下午4:59:01
 */
@Service("adminInfoService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class AdminInfoServiceImpl implements AdminInfoService{

	@Autowired
	private IAdminInfoDao adminInfoDao;
	
	@Autowired
	private AdminInfoPageDaoImpl adminInfoPageDaoImpl;
	
	@Autowired
	private IAdminRoleDao adminRoleDao;

	@Override
	public List<AdminInfo> findAll() {
		return adminInfoDao.findAll();
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateRoleInfo(AdminInfo adminInfo, Long userRoleId) {
		//保存关联到AdminRole
		AdminRole adminRole = new AdminRole();
		adminRole.setUserRoleId(userRoleId);
		adminRole.setAdminId(adminInfo.getAdminId());
		adminRole.setDepartmentCode(adminInfo.getDepartmentCode());
		adminRole.setPositionId(adminInfo.getPositionId());
		adminRole.setRoleId(adminInfo.getRoleId());
		adminRole.setDelState(AdminRole.DELSTATE_UNDELETED);
		adminRoleDao.update(adminRole);
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateAdminInfo(AdminInfo adminInfo) {
		String adminCode = adminInfo.getAdminCode();
		adminInfo.setLoginName(adminCode);
		adminInfo.setLoginPwd(adminInfoDao.findAdminInfoById(adminInfo.getAdminId()).getLoginPwd());
		//adminInfo.setEmail(adminInfo.getEmail() + "@hexindai.com");
		adminInfoDao.update(adminInfo);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addAdminInfo(AdminInfo adminInfo) {
		if(adminInfoDao.findByCode(adminInfo.getAdminCode()).size() == 0) {
			String adminCode = adminInfo.getAdminCode();
			String loginPwd = adminInfo.getLoginPwd();
			//对密码进行加�?
			MD5Key md5Key = new MD5Key();
			String md5Pwd = md5Key.getkeyBeanofStr(loginPwd);
			adminInfo.setLoginPwd(md5Pwd);
			//工号作为登录�?
			adminInfo.setLoginName(adminCode);
			adminInfo.setCreateTime(new Date());
			//adminInfo.setEmail(adminInfo.getEmail() + "@hexindai.com");
			adminInfoDao.insert(adminInfo);
		}
		
		//保存关联到AdminRole
		AdminRole adminRole = new AdminRole();
		adminRole.setAdminId(adminInfo.getAdminId());
		adminRole.setDepartmentCode(adminInfo.getDepartmentCode());
		adminRole.setPositionId(adminInfo.getPositionId());
		adminRole.setRoleId(adminInfo.getRoleId());
		adminRole.setDelState(AdminRole.DELSTATE_UNDELETED);
		if(adminRole.getDepartmentCode() != null && adminRole.getPositionId() != 0 && adminRole.getRoleId() != 0) {
			adminRoleDao.insert(adminRole);
		}

	}

	@Override
	public List<AdminInfo> findById(long adminId) {
		return adminInfoDao.findById(adminId);
	}

	@Override
	public AdminInfo findByCodeAndName(String adminCode,String adminName) throws Exception {
		return adminInfoDao.findByCodeAndName(adminCode,adminName);
	}

	@Override
	public List<AdminInfo> findByCode(String adminCode) throws Exception {
		return adminInfoDao.findByCode(adminCode);
	}

	@Override
	public Pagination<AdminInfo> findAllByPage(int page, int limit, AdminInfo adminInfo) {
		Pagination<AdminInfo> pagination = new Pagination<AdminInfo>();
        pagination.setCurrentPage(page);
        pagination.setPageSize(limit);
        List<AdminInfo> adminInfos = adminInfoPageDaoImpl.findByPage(pagination.getOffset(), pagination.getLimit(), adminInfo);
        pagination.setRows(adminInfos);
        pagination.setTotal(adminInfoPageDaoImpl.countFind(adminInfo));
        return pagination;
	}

	@Override
	public List<AdminInfo> findByLoginName(String loginName){
		return adminInfoDao.findByLoginName(loginName);
	}
	
	/**
	 * 修改密码
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void changePwd(AdminInfo adminInfo) {
		adminInfoDao.update(adminInfo);
	}
	
	@Override
	public List<AdminInfo> findByDepartmentCode(Map map) {
		return adminInfoDao.findByDepartmentCode(map);
	}

	@Override
	public List<AdminInfo> findByAdminIdCode(String adminIdCode) {
		return adminInfoDao.findByAdminIdCode(adminIdCode);
	}

	@Override
	public List<AdminInfo> findByDepartmentCodeConcat(Map map) {
		return adminInfoDao.findByDepartmentCodeConcat(map);
	}

	@Override
	public AdminInfo findByEmail(String email) {
		return adminInfoDao.findByEmail(email);
	}

	@Override
	public AdminInfo findByTelPhone(String telPhone) {
		return adminInfoDao.findByTelPhone(telPhone);
	}

	@Override
	public List<AdminInfo> findAllAdminWithAccount() {
		return adminInfoDao.findAllAdminWithAccount();
	}

	@Override
	public AdminInfo findAdminInfoById(long adminId) {
		return adminInfoDao.findAdminInfoById(adminId);
	}

	@Override
	public List<AdminInfo> findAdminInfosByRoleCode(String roleCode) {
		return adminInfoDao.findAdminInfosByRoleCode(roleCode);
	}

	@Override
	public List<AdminInfo> findAdminInfosByAdminCode(String adminCode) {
		return adminInfoDao.findAdminInfosByAdminCode(adminCode);
	}

	@Override
	public List<AdminInfo> findAdminInfosByDepartmentCodeAndRoleId(Map map) {
		return adminInfoDao.findAdminInfosByDepartmentCodeAndRoleId(map);
	}
	
	@Override
	public List<AdminInfo> findAdminInfosByDepartmentCodeAndRoleCode(Map map) {
		return adminInfoDao.findAdminInfosByDepartmentCodeAndRoleCode(map);
	}
}
