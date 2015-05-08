package com.hx.auth.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.page.Pagination;

/**
 * @author anyang
 * @date 2014-5-12 下午5:07:53
 */
public interface FunctionInfoService {
	public void updateFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects);
	
	/**
	 * 添加权限
	 * @param functionInfo
	 * @param urlJsonObjects 添加的Url信息
	 */
	public void addFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects);

    public List<FunctionInfo> findAll();

    public FunctionInfo findById(long functionInfoId);
    
    public Pagination<FunctionInfo> findAllByPage(int page, int limit, FunctionInfo functionInfo);

	public List<FunctionInfo> findByPid(long pFunctionId);

	public FunctionInfo findByCode(String functionCode);

	public String deleteFunction(FunctionInfo functionInfo);
	
	public List<FunctionInfo> findByRoleId(long roleId);
}
