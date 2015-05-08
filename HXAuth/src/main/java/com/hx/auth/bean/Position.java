package com.hx.auth.bean;
/**
 * @author anyang
 * @date 2014-4-21 下午7:37:45
 */
public class Position {
	private long positionId;
	private String positionName;
	private String positionDesc;
	
	public long getPositionId() {
		return positionId;
	}
	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getPositionDesc() {
		return positionDesc;
	}
	public void setPositionDesc(String positionDesc) {
		this.positionDesc = positionDesc;
	}
	
}
