package com.hx.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.dao.IFunctionUrlDao;

/**
 * @author anyang
 * @date 2014-5-12 下午5:24:09
 */
@Service("functionUrlService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class FunctionUrlServiceImpl implements FunctionUrlService {

	@Autowired
	private IFunctionUrlDao functionUrlDao;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateFunctionUrl(FunctionUrl functionUrl) {
		functionUrlDao.update(functionUrl);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addFunctionUrl(FunctionUrl functionUrl) {
		functionUrlDao.insert(functionUrl);
	}

	@Override
	public List<FunctionUrl> findAll() {
		return functionUrlDao.findAll();
	}

	@Override
	public FunctionUrl findById(long urlId) {
		return functionUrlDao.findById(urlId);
	}

	@Override
	public List<FunctionUrl> findByFunctionId(long functionId) {
		return functionUrlDao.findByFunctionId(functionId);
	}

	@Override
	public List<FunctionUrl> findByUrlInfo(String urlInfo) {
		return functionUrlDao.findByUrlInfo(urlInfo);
	}

	@Override
	public List<String> findAllUrls() {
		return functionUrlDao.findAllUrls();
	}
}
