package com.hx.auth.service;

import java.util.List;

import com.hx.auth.bean.FunctionUrl;

/**
 * @author anyang
 * @date 2014-5-12 下午5:14:30
 */
public interface FunctionUrlService {
	public void updateFunctionUrl(FunctionUrl functionUrl);
	
	public void addFunctionUrl(FunctionUrl functionUrl);

    public List<FunctionUrl> findAll();

    public FunctionUrl findById(long functionUrlId);

	public List<FunctionUrl> findByFunctionId(long functionId);
	
	public List<FunctionUrl> findByUrlInfo(String urlInfo);
	
	public List<String> findAllUrls();
}
