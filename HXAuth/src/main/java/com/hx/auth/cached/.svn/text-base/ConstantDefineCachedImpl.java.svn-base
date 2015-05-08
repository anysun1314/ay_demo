package com.hx.auth.cached;

import com.hx.auth.bean.ConstantDefine;
import com.hx.auth.service.ConstantDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Renyulin on 14-4-11 上午11:59.
 */
@Service("constantDefineCached")
public class ConstantDefineCachedImpl implements ConstantDefineCached {
    protected static List<ConstantDefine> constantDefinesAll = null;


    @Autowired
    private ConstantDefineService constantDefineService;

    @Override
    public List<ConstantDefine> getAll() {
        if (constantDefinesAll == null) {
            constantDefinesAll = constantDefineService.findAll();
        }
        return constantDefinesAll;
    }

    @Override
    public List<ConstantDefine> getByTypeCode(String typeCode) {
        List<ConstantDefine> defineList = new ArrayList<ConstantDefine>();
        constantDefinesAll = getAll();
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine == null || constantDefine.getConstantTypeCode() == null || typeCode == null) {
                System.out.println("temp");
            }
            if (constantDefine.getConstantTypeCode().equals(typeCode)) {
                defineList.add(constantDefine);
            }
        }
        return defineList;
    }

    @Override
    public List<ConstantDefine> getByTypeCodeAndParent(String typeCode, long pConstantDefine) {
        List<ConstantDefine> defineList = new ArrayList<ConstantDefine>();
        constantDefinesAll = getAll();
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine.getParentConstant() == pConstantDefine && constantDefine.getConstantTypeCode().equals(typeCode)) {
                defineList.add(constantDefine);
            }
        }
        return defineList;
    }

    @Override
    public ConstantDefine getpConstantByChild(String typeCode, String typeValue) {
        ConstantDefine theConstantDefine = null;
        constantDefinesAll = getAll();
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine.getConstantValue().equals(typeValue) && constantDefine.getConstantTypeCode().equals(typeCode)) {
                theConstantDefine = constantDefine;
                break;
            }
        }
        for (ConstantDefine constantDefine : constantDefinesAll) {
            if (constantDefine.getConstantDefineId() == theConstantDefine.getParentConstant()) {
                return constantDefine;
            }
        }
        return null;
    }

    public void reset() {
        constantDefinesAll = null;
        constantDefinesAll = getAll();
    }
}
