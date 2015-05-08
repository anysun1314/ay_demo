package com.hx.auth.bean;
/**
 * @author anyang
 * @date 2014-4-22 下午1:01:11
 */
public class AdminRole {
	private long userRoleId;
	private String departmentCode;
	private long roleId;
	private long adminId;
	private long positionId;
	private char delState;
	
	/**
	 * 删除标记
	 */
	public static final char DELSTATE_DELETED = '1';
    public static final char DELSTATE_UNDELETED = '0';
	
	public char getDelState() {
		return delState;
	}
	public void setDelState(char delState) {
		this.delState = delState;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
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
	public long getAdminId() {
		return adminId;
	}
	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}
	public long getPositionId() {
		return positionId;
	}
	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}
}
