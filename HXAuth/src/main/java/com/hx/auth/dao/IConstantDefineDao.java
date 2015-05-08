package com.hx.auth.dao;

import com.hx.auth.bean.ConstantDefine;

import java.util.List;

/**
 * User: Ren yulin
 * Date: 14-3-6  上午10:53
 */
public interface IConstantDefineDao extends MybatisDao<ConstantDefine> {

    public List<ConstantDefine> findAll();

    public List<ConstantDefine> findsById(long constantDefineId);

    public List<ConstantDefine> getConstantDefinesByType(String typeCode);

    /**
     * 处理费用类别数据回显
     *
     * @param constantValue
     * @return
     */
    public ConstantDefine doFeesTypeEcho(String constantValue);

    public ConstantDefine findConstantByTypeCodeAndValue(ConstantDefine constantDefine);

    public List<ConstantDefine> findByTypeCodeAndParentConstant(ConstantDefine constantDefine);


}
