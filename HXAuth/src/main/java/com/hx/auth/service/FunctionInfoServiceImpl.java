package com.hx.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.bean.RoleFunction;
import com.hx.auth.dao.FunctionInfoPageDaoImpl;
import com.hx.auth.dao.IFunctionInfoDao;
import com.hx.auth.dao.IFunctionUrlDao;
import com.hx.auth.dao.IRoleFunctionDao;
import com.hx.auth.page.Pagination;

/**
 * @author anyang
 * @date 2014-5-12 下午5:38:05
 */
@Service("functionInfoService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class FunctionInfoServiceImpl implements FunctionInfoService {

	@Autowired
	private IFunctionInfoDao functionInfoDao;
	@Autowired
	private IFunctionUrlDao functionUrlDao;
	@Autowired
	private IRoleFunctionDao roleFunctionDao;
	
	@Autowired
	private FunctionInfoPageDaoImpl functionInfoPageDaoImpl;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects) {
		functionInfoDao.update(functionInfo);
		List<FunctionUrl> functionUrls = functionUrlDao.findByFunctionId(functionInfo.getFunctionId());
		
		if(!urlJsonObjects.isEmpty()) {	//如果有表单提交的Url
			
			if(functionUrls.size() != 0) {	//原来保存了Url
				List<Long> formUrlIds = new ArrayList<Long>();	//封装表单提交的urlId
				for(JSONObject urlJsonObject : urlJsonObjects) {
					formUrlIds.add(urlJsonObject.getLong("urlId"));
				}
				for(FunctionUrl functionUrl : functionUrls) {
					long saveUrlId = functionUrl.getUrlId();
					if(!formUrlIds.contains(saveUrlId)) {	//判断是否有删
						functionUrlDao.delete(functionUrl);
					}
				}
			}
			
			for(JSONObject jsonObject : urlJsonObjects) {
				long urlId = 0L;
				if(!"".equals(jsonObject.getString("urlId")) && jsonObject.getString("urlId") != null) {
					urlId = Long.parseLong(jsonObject.getString("urlId"));
				}
				
				FunctionUrl functionUrl = new FunctionUrl();
				functionUrl.setFunctionId(functionInfo.getFunctionId());
				functionUrl.setUrlId(urlId);
				functionUrl.setUrlInfo(jsonObject.getString("urlInfo"));
				
				if(urlId == 0L) {	//判断是否有增
					functionUrlDao.insert(functionUrl);
				}else {
					functionUrlDao.update(functionUrl);
				}
			}
		}else {	//表单没有提交Url，原来保存了Url，执行删�?
			if(functionUrls.size() != 0) {
				for(FunctionUrl functionUrl : functionUrls) {
					functionUrlDao.delete(functionUrl);
				}
			}
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addFunctionInfo(FunctionInfo functionInfo, List<JSONObject> urlJsonObjects) {
		functionInfoDao.insert(functionInfo);
		if(!urlJsonObjects.isEmpty()) {	//如果有Url，保存Url
			for(JSONObject jsonObject : urlJsonObjects) {
				FunctionUrl functionUrl = new FunctionUrl();
				functionUrl.setFunctionId(functionInfo.getFunctionId());
				functionUrl.setUrlInfo(jsonObject.getString("urlInfo"));
				functionUrlDao.insert(functionUrl);
			}
		}
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public String deleteFunction(FunctionInfo functionInfo) {
		return deleteAll(functionInfo);
	}

	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	private String deleteAll(FunctionInfo functionInfo) {
		List<RoleFunction> roleFunctions = roleFunctionDao.findByFunctionId(functionInfo.getFunctionId());
		if(!roleFunctions.isEmpty()) {	//判断是否有角色使用该权限
			return "used";
		}
		List<FunctionInfo> functionInfos = functionInfoDao.findByPid(functionInfo.getFunctionId());
		if (functionInfos.size() > 0) {
			try {
				functionUrlDao.deleteByFunctionId(functionInfo.getFunctionId());
				functionInfoDao.delete(functionInfo);
				for (FunctionInfo CfunctionInfo : functionInfos) {
					String use = deleteAll(CfunctionInfo);	//递归删除对应的子权限
					if("used".equals(use)) {	//子节点被使用跳出
						return "used";
					}
				}
				return "success";
			} catch (Exception e) {
				return "false";
			}
		} else {
			functionUrlDao.deleteByFunctionId(functionInfo.getFunctionId());
			try {
				functionInfoDao.delete(functionInfo);
			} catch (Exception e) {
				e.printStackTrace();
				return "false";
			}
			return "success";
		}
	}
	
	
	
	@Override
	public List<FunctionInfo> findAll() {
		return functionInfoDao.findAll();
	}

	@Override
	public FunctionInfo findById(long functionInfoId) {
		return functionInfoDao.findById(functionInfoId);
	}

	@Override
	public Pagination<FunctionInfo> findAllByPage(int page, int limit,
			FunctionInfo functionInfo) {
		Pagination<FunctionInfo> pagination = new Pagination<FunctionInfo>();
		pagination.setCurrentPage(page);
		pagination.setPageSize(limit);
		List<FunctionInfo> functionInfos = functionInfoPageDaoImpl.findByPage(pagination.getOffset(), pagination.getLimit(), functionInfo);
		pagination.setRows(functionInfos);
		pagination.setTotal(functionInfoPageDaoImpl.countFind(functionInfo));
		return pagination;
	}

	@Override
	public List<FunctionInfo> findByPid(long pFunctionId) {
		return functionInfoDao.findByPid(pFunctionId);
	}

	@Override
	public FunctionInfo findByCode(String functionCode) {
		return functionInfoDao.findByCode(functionCode);
	}

	@Override
	public List<FunctionInfo> findByRoleId(long roleId) {
		return functionInfoDao.findByRoleId(roleId);
	}
}
