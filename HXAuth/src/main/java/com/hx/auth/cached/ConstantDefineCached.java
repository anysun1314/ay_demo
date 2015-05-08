package com.hx.auth.cached;

import com.hx.auth.bean.ConstantDefine;

import java.util.List;

/**
 * Created by Renyulin on 14-4-11 上午11:54.
 */
public interface ConstantDefineCached {

    public List<ConstantDefine> getAll();

    public List<ConstantDefine> getByTypeCode(String typeCode);

    public List<ConstantDefine> getByTypeCodeAndParent(String typeCode, long pConstantDefine);

    public ConstantDefine getpConstantByChild(String typeCode, String typeValue);

    public void reset();

}
