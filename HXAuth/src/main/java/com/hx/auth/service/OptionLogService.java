package com.hx.auth.service;

import java.util.List;

import com.hx.auth.bean.OptionLog;
import com.hx.auth.page.Pagination;

/**
 * @author anyang
 * @date 2014-5-26 上午10:16:09
 */
public interface OptionLogService {
	public void updateOptionLog(OptionLog optionLog);
	
	public void addOptionLog(OptionLog optionLog);

    public List<OptionLog> findAll();

    public OptionLog findById(long optionLogId);

	public Pagination<OptionLog> findAllByPage(int pageNo, int pageSize, OptionLog optionLog);

}
