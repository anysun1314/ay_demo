package com.hx.auth.dao;

import java.util.List;

import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.dao.MybatisDao;
/**
 * @author anyang
 * @date 2014-5-12 下午4:55:44
 */
public interface IFunctionInfoDao extends MybatisDao<FunctionInfo> {

	public List<FunctionInfo> findByPid(long pFunctionId);

	public FunctionInfo findByCode(String functionCode);

	public List<FunctionInfo> findByRoleId(long roleId);

}
