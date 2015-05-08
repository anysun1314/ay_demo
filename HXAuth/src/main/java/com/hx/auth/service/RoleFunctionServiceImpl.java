package com.hx.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.RoleFunction;
import com.hx.auth.dao.IRoleFunctionDao;

/**
 * @author anyang
 * @date 2014-5-12 下午5:17:56
 */
@Service("roleFunctionService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class RoleFunctionServiceImpl implements RoleFunctionService {

	@Autowired
	private IRoleFunctionDao roleFunctionDao;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateRoleFunction(RoleFunction roleFunction) {
		roleFunctionDao.update(roleFunction);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addRoleFunction(RoleFunction roleFunction,String functionIdStr) {
		//获取选中的functionId  
		String[] functionIds;
		functionIds = functionIdStr.split("-");
		//对比表单提交的跟原来已有的，做增删操�?
		List<RoleFunction> roleFunctions = roleFunctionDao.findByRoleId(roleFunction.getRoleId());	
		List<Long> functionIdListSaved = new ArrayList();
		
		//页面选中的不包含保存的，执行删除
		List<String> functionIdList = Arrays.asList(functionIds);
		for(RoleFunction roleFunctionSaved : roleFunctions) {
			//记录保存的functionId
			functionIdListSaved.add(roleFunctionSaved.getFunctionId());
			String savedId = roleFunctionSaved.getFunctionId() + "";
			if(!functionIdList.contains(savedId)){//判断保存的是否在获取过来数据中存在，不存在干掉，存在不动，行增的添加
				roleFunctionDao.delete(roleFunctionSaved);
			}
		}
		
		if(functionIds != null && functionIds.length != 0 && !"".equals(functionIds[0])) {
			//保存的不包含页面选中�?执行新增
			for(String id : functionIds) {
				long functionId = Long.parseLong(id);
				if(!functionIdListSaved.contains(functionId)) {
					roleFunction.setFunctionId(functionId);
					roleFunctionDao.insert(roleFunction);
				}
			}
		}
	}

	@Override
	public List<RoleFunction> findAll() {
		return roleFunctionDao.findAll();
	}

	@Override
	public RoleFunction findById(long roleFunctionId) {
		return roleFunctionDao.findById(roleFunctionId);
	}

	@Override
	public List<RoleFunction> findByFunctionId(long editingId) {
		return roleFunctionDao.findByFunctionId(editingId);
	}

	@Override
	public List<RoleFunction> findByRoleId(long roleId) {
		return roleFunctionDao.findByRoleId(roleId);
	}
}
