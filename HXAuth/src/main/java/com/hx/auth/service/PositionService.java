package com.hx.auth.service;

import java.util.List;

import com.hx.auth.bean.Position;

/**
 * @author anyang
 * @date 2014-4-21 下午7:38:54
 */
public interface PositionService {
	public void updatePosition(Position position);
	
	public void addPosition(Position position);

    public List<Position> findAll();

    public Position findById(long position);
}
