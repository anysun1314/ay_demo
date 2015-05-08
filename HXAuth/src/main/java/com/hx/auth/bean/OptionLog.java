package com.hx.auth.bean;

import java.util.Date;

/**
 * @author anyang
 * @date 2014-5-26 上午10:03:23
 */
public class OptionLog {
	
	private long optionLogId;
	private long userRoleId;
	private Date optionTime;
	private String loginIP;
	private String logInfo;
	
	public long getOptionLogId() {
		return optionLogId;
	}
	public void setOptionLogId(long optionLogId) {
		this.optionLogId = optionLogId;
	}
	public long getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(long userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Date getOptionTime() {
		return optionTime;
	}
	public void setOptionTime(Date optionTime) {
		this.optionTime = optionTime;
	}
	public String getLoginIP() {
		return loginIP;
	}
	public void setLoginIP(String loginIP) {
		this.loginIP = loginIP;
	}
	public String getLogInfo() {
		return logInfo;
	}
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}
	
	
	
	/**
	 * vo字段
	 */
	private String departmentName;
	private String positionName;
	private String roleName;
	private String adminName;

	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
}
