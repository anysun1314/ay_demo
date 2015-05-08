package com.hx.auth.service;

import com.hx.auth.bean.ConstantDefine;
import com.hx.auth.dao.MybatisDao;
import com.hx.auth.page.Pagination;

import java.util.List;

/**
 * User: Ren yulin
 * Date: 14-3-6  上午11:05
 */
public interface ConstantDefineService {
    public List<ConstantDefine> findAll();

    public Pagination<ConstantDefine> findAllByPage(int page, int limit, ConstantDefine constantDefine);


    public void addConstantDefine(ConstantDefine constantDefine);

    public ConstantDefine findById(long constantDefineId);

    public List<ConstantDefine> findsById(long constantDefineId);

    public List<ConstantDefine> getConstantDefinesByType(String typeCode);

    /**
     * 处理费用类别数据回显
     *
     * @param constantValue
     * @return
     */
    public ConstantDefine doFeesTypeEcho(String constantValue);

    public void updateConstantDefine(ConstantDefine constantDefine);

    public ConstantDefine findConstantByTypeCodeAndValue(ConstantDefine constantDefine);

    public List<ConstantDefine> findByTypeCodeAndParentConstant(String constantTypeCode, long parentConstant);


}
