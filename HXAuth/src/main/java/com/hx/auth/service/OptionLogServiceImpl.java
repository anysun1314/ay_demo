package com.hx.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.OptionLog;
import com.hx.auth.dao.IOptionLogDao;
import com.hx.auth.dao.OptionLogPageDaoImpl;
import com.hx.auth.page.Pagination;

/**
 * @author anyang
 * @date 2014-5-26 上午10:25:42
 */
@Service("optionLogService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class OptionLogServiceImpl implements OptionLogService {

	@Autowired
	private IOptionLogDao optionLogDao;
	@Autowired
	private OptionLogPageDaoImpl optionLogPageDaoImpl;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updateOptionLog(OptionLog optionLog) {
		optionLogDao.update(optionLog);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addOptionLog(OptionLog optionLog) {
		optionLogDao.insert(optionLog);
	}

	@Override
	public List<OptionLog> findAll() {
		return optionLogDao.findAll();
	}

	@Override
	public OptionLog findById(long optionLogId) {
		return optionLogDao.findById(optionLogId);
	}

	@Override
	public Pagination<OptionLog> findAllByPage(int pageNo, int pageSize, OptionLog optionLog) {
		Pagination<OptionLog> pagination = new Pagination<OptionLog>();
		pagination.setCurrentPage(pageNo);
		pagination.setPageSize(pageSize);
		List<OptionLog> optionLogs = optionLogPageDaoImpl.findByPage(pagination.getOffset(), pagination.getLimit(), optionLog);
		pagination.setRows(optionLogs);
		pagination.setTotal(optionLogPageDaoImpl.countFind(optionLog));
		return pagination;
	}
}
