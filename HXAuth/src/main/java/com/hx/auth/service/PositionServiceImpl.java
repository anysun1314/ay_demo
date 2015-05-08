package com.hx.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hx.auth.bean.Position;
import com.hx.auth.dao.IPositionDao;
/**
 * @author anyang
 * @date 2014-4-21 下午7:42:34
 */
@Service("positionService")
@Transactional(rollbackFor=Exception.class,propagation=Propagation.SUPPORTS)
public class PositionServiceImpl implements PositionService {

	@Autowired
	private IPositionDao positionDao;
	
	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void updatePosition(Position position) {
		positionDao.update(position);
	}

	@Override
	@Transactional(rollbackFor=Exception.class,readOnly=false,propagation=Propagation.REQUIRED)
	public void addPosition(Position position) {
		positionDao.insert(position);
	}

	@Override
	public List<Position> findAll() {
		return positionDao.findAll();
	}

	@Override
	public Position findById(long positionId) {
		return positionDao.findById(positionId);
	}

}
