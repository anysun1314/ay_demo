package com.hx.auth.bean;

import java.util.Date;

/**
 * @author anyang
 * @date 2014-6-18 下午2:22:39
 */
public class DepartAccount {
	
	private long departAccountId;	//组织机构平台账户Id(pk)
	private String departmentCode;	//组织机构编码(fk关联组织机构)
	private long customerAccountId;	//客户资金账户Id
	private String accountType;		//账户类型
	private Date createTime;		//创建时间
	private char delState;			//删除状�?
	
	/**
     * 删除标记�?为未删除�?为已删除
     */
    public static final char DELSTATE_DELETED = '1';
    public static final char DELSTATE_UNDELETED = '0';
	
	public long getDepartAccountId() {
		return departAccountId;
	}
	public void setDepartAccountId(long departAccountId) {
		this.departAccountId = departAccountId;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public long getCustomerAccountId() {
		return customerAccountId;
	}
	public void setCustomerAccountId(long customerAccountId) {
		this.customerAccountId = customerAccountId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public char getDelState() {
		return delState;
	}
	public void setDelState(char delState) {
		this.delState = delState;
	}
	
	
}
