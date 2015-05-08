package com.hx.auth.service;

import com.hx.auth.bean.ConstantDefine;
import com.hx.auth.cached.ConstantDefineCached;
import com.hx.auth.dao.IConstantDefineDao;
import com.hx.auth.dao.IPageDao;
import com.hx.auth.page.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Ren yulin
 * Date: 14-3-6  上午11:06
 */
@Service("constantDefineService")
public class ConstantDefineServiceImpl implements ConstantDefineService {
    @Autowired
    private IConstantDefineDao constantDefineDao;
    @Autowired
    private ConstantDefineCached constantDefineCached;
    @Autowired
    private IPageDao constantDefinePageDao;

    @Override
    public List<ConstantDefine> findAll() {
        return constantDefineDao.findAll();
    }

    @Override
    public Pagination<ConstantDefine> findAllByPage(int page, int limit,ConstantDefine constantDefine) {
        Pagination<ConstantDefine> pagination = new Pagination<ConstantDefine>();
        pagination.setCurrentPage(page);
        pagination.setPageSize(limit);
        List<ConstantDefine> constantDefines = constantDefinePageDao.findByPage(pagination.getOffset(), pagination.getLimit(), constantDefine);
        pagination.setRows(constantDefines);
        pagination.setTotal(constantDefinePageDao.countFind(constantDefine));
        return pagination;
    }

    @Override
    public void addConstantDefine(ConstantDefine constantDefine) {
        constantDefineDao.insert(constantDefine);
        constantDefineCached.reset();
    }

    @Override
    public void updateConstantDefine(ConstantDefine constantDefine){
        constantDefineDao.update(constantDefine);
        constantDefineCached.reset();
    }

    @Override
    public ConstantDefine findConstantByTypeCodeAndValue(ConstantDefine constantDefine) {
        return constantDefineDao.findConstantByTypeCodeAndValue(constantDefine);
    }

    @Override
    public List<ConstantDefine> findByTypeCodeAndParentConstant(String constantTypeCode,long parentConstant) {
        ConstantDefine constantDefine=new ConstantDefine();
        constantDefine.setConstantTypeCode(constantTypeCode);
        constantDefine.setParentConstant(parentConstant);
        return constantDefineDao.findByTypeCodeAndParentConstant(constantDefine);
    }


    @Override
    public ConstantDefine findById(long constantDefineId) {
        return constantDefineDao.findById(constantDefineId);
    }

	@Override
	public List<ConstantDefine> findsById(long constantDefineId) {
		return constantDefineDao.findsById(constantDefineId);
	}

    @Override
    public List<ConstantDefine> getConstantDefinesByType(String typeCode) {
        return constantDefineDao.getConstantDefinesByType(typeCode);
    }

    @Override
    public ConstantDefine doFeesTypeEcho(String constantValue) {
        return constantDefineDao.doFeesTypeEcho(constantValue);
    }



}
