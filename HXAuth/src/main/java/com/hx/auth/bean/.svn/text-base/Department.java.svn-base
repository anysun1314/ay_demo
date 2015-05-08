package com.hx.auth.bean;

/**
 * @author anyang
 * @date 2014-4-1 上午11:11:50
 */
public class Department {
    /*
     * 2014-6-18   平台账户更新至单独的关联表[DepartAccount]�?
     */
    //private long customerAccountId;	//客户资金账户ID可为空，默认值为0(�?��在sqlMap屏蔽<关联>)
    private String departmentCode;        //当前部门code
    private String departmentName;        //当前部门名称
    private String deptDesc;            //描述
    private String pDeptCode;            //上级部门code
    private char nodeType;                //节点类型
    private String nodeValue;            //类型�?
    private char enableAccount;            //启用资金账户标记
    private char delState;                //删除部门标记

    /**
     * 账户启用状�?�?为不启用�?为启�?
     */
    public static final char ENBALEACCOUNT_ENABLE = '1';
    public static final char ENBALEACCOUNT_DISABLE = '0';

    /**
     * 删除标记�?为未删除�?为已删除
     */
    public static final char DELSTATE_DELETED = '1';
    public static final char DELSTATE_UNDELETED = '0';

    public static final String DEPARTMENTCODE_AGENT = "010201";//默认的代理人的组织机构节点，�?��的代理人都挂在此节点�?

    public static final char NODETYPE_BPM_OUTLET = '1';	//默认总门店的节点类型
    public static final char NODETYPE_BPM_OUTLET_CHILD = '5';	//默认各个分店的节点类型
    
    public char getNodeType() {
        return nodeType;
    }

    public void setNodeType(char nodeType) {
        this.nodeType = nodeType;
    }

    public char getEnableAccount() {
        return enableAccount;
    }

    public void setEnableAccount(char enableAccount) {
        this.enableAccount = enableAccount;
    }

    public char getDelState() {
        return delState;
    }

    public void setDelState(char delState) {
        this.delState = delState;
    }

    /*public long getCustomerAccountId() {
        return customerAccountId;
    }
    public void setCustomerAccountId(long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }*/
    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getNodeValue() {
        return nodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public String getpDeptCode() {
        return pDeptCode;
    }

    public void setpDeptCode(String pDeptCode) {
        this.pDeptCode = pDeptCode;
    }

}
