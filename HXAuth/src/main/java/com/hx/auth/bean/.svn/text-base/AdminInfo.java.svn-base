package com.hx.auth.bean;

import java.util.Date;
/**
 * @author anyang
 * @date 2014-4-18 下午2:37:48
 */
public class AdminInfo {

	private long adminId;
	private String displayName;
	private String adminCode;
	private String loginName;
	private String loginPwd;
	private Date createTime;
	private char adminState;
	private String telPhone;
	private String adminIDCode;
	private String email;
	
	/**
     * 用户状�?�?为无效，1为有效，2为离�?
     */
	public static final char ENBALEADMIN_DISABLE='0';
    public static final char ENBALEADMIN_ENABLE='1';
    public static final char ENBALEADMIN_GONE='2';
    
    /**
     * SESSION中记录的当前用户信息
     * 
     * LOGINUSER ：记录当前用户对�?
     * CURRENTDEPARTMENT ：记录当前部门对�?
     * CURRENTPARENTDEPARTMENT ：记录当前部门的父级部门对象
     * CURRENTCHILDDEPARTMENTS ：记录当前部门的子级部门对象�?
     * CURRENTPOSITION ：记录当前职位对�?
     * CURRENTROLE ：记录当前角色对�?
     * CURRENTPERMISSION ：记录当前角色对应的权限code�?
     * USERINFOS : 用户对象集（包含部门权限信息�?
     * USERROLEID : 当前用户角色关联id
     * USERFUNCTIONURL : 当前用户权限url
     */
    public static final String LOGINUSER = "loginUser";
    public static final String CURRENTDEPARTMENT = "currentDepartment";
    public static final String CURRENTPARENTDEPARTMENT = "currentParentDepartment";
    public static final String CURRENTCHILDDEPARTMENTS = "currentChildDepartments";
    public static final String CURRENTPOSITION = "currentPosition";
    public static final String CURRENTROLE = "currentRole";
    public static final String CURRENTPERMISSION = "currentPermission";
    public static final String USERINFOS = "userInfos";
    public static final String USERROLEID = "userRoleId";
    public static final String USERFUNCTIONURL = "userFunctionUrl";

    /**
     * 默认用户�?2为默认的代理人，-1为系统定时任务默认的操作�? 0为平台自动垫付时的操作员�?为系统初始化管理员，
     */
    public static final long DEFAULT_AGENT = -2L;
    public static final long DEFAULT_ADMIN = -1L;
    public static final long DEFAULT_SYSTEM = 0L;
    /**
     * 系统初化化默认密�?
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 系统默认代理人的工号
     */
    public static final String ADMINCODE_AGENT="theAgent";
    
	public String getTelPhone() {
		return telPhone;
	}
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getAdminCode() {
		return adminCode;
	}
	public void setAdminCode(String adminCode) {
		this.adminCode = adminCode;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getLoginPwd() {
		return loginPwd;
	}
	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public char getAdminState() {
		return adminState;
	}
	public void setAdminState(char adminState) {
		this.adminState = adminState;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdminIDCode() {
		return adminIDCode;
	}
	public void setAdminIDCode(String adminIDCode) {
		this.adminIDCode = adminIDCode;
	}
	
	
	/**
     * 以下为VO字段
     */
    private String departmentCode;
    private String roleCode;
    private long roleId;
    private long positionId;
    
    private String departmentName;
    private String roleName;
    private String positionName;

    private long userRoleId;
	private String userRoleIds;
	
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
    
	public String getUserRoleIds() {
		return userRoleIds;
	}
	public void setUserRoleIds(String userRoleIds) {
		this.userRoleIds = userRoleIds;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public long getPositionId() {
		return positionId;
	}
	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}
	
	
	
}
