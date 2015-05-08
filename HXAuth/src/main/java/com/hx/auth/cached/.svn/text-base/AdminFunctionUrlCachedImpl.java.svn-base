package com.hx.auth.cached;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.service.FunctionInfoService;
import com.hx.auth.service.FunctionUrlService;

/**
 * @author anyang
 * @date 2014-5-29 上午11:52:47
 */
@Service("adminFunctionUrlCached")
public class AdminFunctionUrlCachedImpl implements AdminFunctionUrlCached {

	/**
	 * 缓存当前用户的权限所关联的url
	 */
	protected static List<String> adminUrls = null;
	
	@Autowired
	private FunctionUrlService functionUrlService;
	@Autowired
	private FunctionInfoService functionInfoService;
	
	@Override
	public List<String> getAdminUrls(long currentRoleId) {
		if(adminUrls == null) {
			//当前用户当前角色对应的权限集
			List<FunctionInfo> functionInfos = functionInfoService.findByRoleId(currentRoleId);
			
			if(!functionInfos.isEmpty()) {
				for(FunctionInfo functionInfo : functionInfos) {
					long functionId = functionInfo.getFunctionId();
					List<FunctionUrl> functionUrls = functionUrlService.findByFunctionId(functionId);
					if(!functionUrls.isEmpty()) {
						for(FunctionUrl functionUrl : functionUrls) {
							String urlInfo = functionUrl.getUrlInfo();
							if(adminUrls != null) {
								if(!adminUrls.contains(urlInfo) && !"".equals(urlInfo)) {
									adminUrls.add(urlInfo);
								}
							}else {
								if(!"".equals(urlInfo)) {
									adminUrls = new ArrayList();
									adminUrls.add(urlInfo);
								}
							}
						}
					}
				}
			}
		}
		return adminUrls;
	}

	@Override
	public void reset(long currentRoleId) {
		adminUrls = null;
		adminUrls = this.getAdminUrls(currentRoleId);
	}
}
