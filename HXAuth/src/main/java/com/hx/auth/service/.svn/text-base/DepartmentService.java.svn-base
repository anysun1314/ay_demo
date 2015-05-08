package com.hx.auth.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.hx.auth.bean.Department;

/**
 * 
 * @author anyang
 * @date 2014-4-1 上午11:39:37
 *
 */
public interface DepartmentService {
	public void updateDepartment(Department department);
	
	/**
	 * 保存组织机构信息
	 * @param department
	 */
	public void addDepartment(Department department);

    public List<Department> findAll(char delState);

    public Department findByCode(String departmentCode);

	public List<Department> findChildByCode(String oldCode, char delState);
	
	public List<Department> findAllChildByCode(String oldCode);

	/**
	 * 根据部门号和账户类型，查询相应的客户账户
	 * @param departmentCode,accountType
	 * @return Long CustomerAccountId
	 */
	public long findCustomerAccountByCodeAndAccountType(String departmentCode,String accountType);
	
	/**
	 * 根据部门号，查询相应的客户账�?
	 * @param departmentCode
	 * @return Map<账户Id, 账户类型>，如果没找到，返回空map
	 */
	public Map<Long, String> findCustomerAccountByCode(String departmentCode);
	
	/**
	 * 根据员工号查询部�?
	 * @param AdminCode
	 * @return
	 */
	public List<Department> findByAdminCode(String AdminCode);

	public void deleteDepartmentByCode(Department department);
	
	public String deleteDepartment(Department department);
	
	public List<Department> findBy(Map paramters);
	public Department findByAdminCodeAndRoleCode(Map paramters);


    public String getDepartTreeStr();
    
    /**
     * 得到�?��的根节点对象
     * @param department
     * @return
     */
    public List<Department> findAllRoots(Department department);

	public List<Department> findAllDepartment();

	/**
	 * 通过code查询，子级�?孙级等下�?�?��部门
	 */
	public List<Department> findChildDepartments(String pDepartmentCode,char delState);
	
	/**
	 * 查找是否存在该平台账户的组织机构
	 * @param customerAccountId
	 * @return
	 */
	public Boolean isExistDepartment(long customerAccountId);
	
	/**
	 * 保存组织机构、关联账�?
	 * @param loginName			登录�?
	 * @param adminId			登录用户id
	 * @param department		组织机构对象
	 */
	public String addDepartmentAndDepartAccount(Boolean addFlag,String loginName,long adminId, Department department) throws InvocationTargetException, IllegalAccessException;

	/**
	 * 找到上级有资金帐户的部门，返回原部门上级到该部门的所有部�?
	 * @param departmentCode
	 * @param accountType
	 * @return
	 */
	public List<Department> findDepartmentsByDeptCodeAndAccountType(String departmentCode, String accountType);

	/**
	 * For bpm
	 * 通过组织机构Code,查询组织机构树上相应的人员信息
	 * @param departmentCode
	 * @return json格式数据：
	 */
	public String findAdminInfosFromDeptTreeByDeptCode(Map map);

	/**
	 * For bpm
	 * 通过当前组织机构Code,查询门店组人员所在门店
	 * @param departmentCode
	 * @return json格式数据：
	 * @throws Exception 
	 */
	String findBelongsDepartByCrtDepartmentCode(String departmentCode) throws Exception;

	/**
	 * 通过节点类型查询部门
	 * @param nodetype
	 * @return
	 */
	public List<Department> findDepartmentByNodeType(char nodetype);

	/**
	 * For bpm
	 * 通过组织机构Code,查询组织机构树上相应的人员信息
	 * @param departmentCode[部门号],isScreen[是否筛选],condition[筛选条件]
	 * @return json格式数据
	 */
	public String findAdminInfosFromDeptTreeByDeptCodeAndScreenCondition(Map map) throws Exception;

	/**
	 * For bpm
     * 判断当前人员属于[门店]还是[信审中心]
     * @param adminId
     * @return	
	 * @throws IOException 
     */
	public int belongToStoreOrCredit(long parseLong) throws IOException;

}
