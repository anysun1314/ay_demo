package com.hx.auth.dao;

import java.util.List;

import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.dao.MybatisDao;
/**
 * @author anyang
 * @date 2014-5-12 下午4:56:31
 */
public interface IFunctionUrlDao extends MybatisDao<FunctionUrl> {

	public List<FunctionUrl> findByFunctionId(long functionId);
	
	public List<FunctionUrl> findByUrlInfo(String urlInfo);

	public void deleteByFunctionId(long functionId);

	public List<String> findAllUrls();

}
