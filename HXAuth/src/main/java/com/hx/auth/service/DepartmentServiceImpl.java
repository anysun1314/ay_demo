package com.hx.auth.service;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.AdminRole;
import com.hx.auth.bean.DepartAccount;
import com.hx.auth.bean.Department;
import com.hx.auth.dao.IDepartmentDao;
import com.hx.auth.util.PubMethod;

/**
 * 
 * @author anyang
 * @date 2014-4-1 上午11:41:10
 * 
 */
@Service("departmentService")
@Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	private IDepartmentDao departmentDao;
	@Autowired
	private AdminInfoService adminInfoService;
	@Autowired
	private AdminRoleService adminRoleService;
	//@Autowired
	//private CustomerInfoService customerInfoService;
	@Autowired
	private DepartAccountService departAccountService;
	//封装�?��下级部门	**单例、回收清除问题�?多线程并发访问问�?*
	//private List<Department> childrenDepartments = new ArrayList<Department>();	
	protected final static String PROPERTIES_FILE_NAME = "config.properties";
	protected final static String BPM_DEPARTMENTCODE_CREDITCENTER = "bpm_departmentCode_creditCenter";
	protected final static String BPM_DEPARTMENTCODE_OUTLETCENTER = "bpm_departmentCode_outletCenter";
	protected final static String BPM_ROLEID_EMPLOYEE = "bpm_roleId_employee";

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public void updateDepartment(Department department) {
		departmentDao.update(department);
	}

	/**
	 * 保存组织机构，客户�?及账户信�?
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public String addDepartmentAndDepartAccount(Boolean addFlag,String loginName,long adminId,Department department) throws InvocationTargetException, IllegalAccessException {
		String customerName = department.getDepartmentName();	//部门名称 customerName
		String departmentCode = department.getDepartmentCode();
		/**
		 * Map<账户Id, 账户类型>---用于记录该组织机构关联的账户信息
		 */
		Map<Long, String> customerAccount = new HashMap<Long, String>();	//TODO 调用使用者接口读取账户 customerInfoService.addCustomerInfo(departmentCode, loginName, customerName, adminId)
		if(addFlag) {
			//保存部门信息
			try {
				departmentDao.insert(department);
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}else {
			//更新部门信息
			/*try {
				departmentDao.update(department);
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}*/
			
			//判断该机构是否有关联的资金帐户，有的话，设置删除标记，后续在添加新的
		}
		
		//保存部门关联的账�?
		try {
			if(!customerAccount.isEmpty()) {
				DepartAccount departAccount = new DepartAccount();
				for(Map.Entry<Long, String> entry : customerAccount.entrySet()) {
					departAccount.setAccountType(entry.getValue());
					departAccount.setCustomerAccountId(entry.getKey());
					departAccount.setCreateTime(new Date());
					departAccount.setDepartmentCode(departmentCode);
					departAccount.setDelState(DepartAccount.DELSTATE_UNDELETED);
					departAccountService.addDepartAccount(departAccount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "customerAccount";
		}
		
		return "";
	}

	@Override
	public List<Department> findAll(char delState) {
		Map map = new HashMap();
		map.put("delState", delState);
		return departmentDao.findBy(map);
	}
	
	@Override
	public List<Department> findAllDepartment() {
		return departmentDao.findAll();
	}

	@Override
	public Department findByCode(String departmentCode) {
		return departmentDao.findByCode(departmentCode);
	}

	@Override
	public List<Department> findChildByCode(String oldCode, char delState) {
		Department department = new Department();
		department.setpDeptCode(oldCode);
		department.setDelState(delState);
		return departmentDao.findChildByCode(department);
	}
	
	@Override
	public List<Department> findAllChildByCode(String oldCode) {
		return departmentDao.findAllChildByCode(oldCode);
	}
	
	/**
	 * 通过code查询子级、孙级等下级�?��部门
	 */
	@Override
	public List<Department> findChildDepartments(String pDepartmentCode, char delState) {
		//封装�?��下级部门
		List<Department> childrenDepartments = new ArrayList<Department>();
		this.findAllChildren(childrenDepartments, pDepartmentCode, delState);
		return childrenDepartments;
	}

	private void findAllChildren(List<Department> childrenDepartments, String pDepartmentCode, char delState) {
		List<Department> child = this.findChildByCode(pDepartmentCode, delState);
		if(!child.isEmpty()) {
			for(Department childDepartment : child) {
				childrenDepartments.add(childDepartment);
				findAllChildren(childrenDepartments, childDepartment.getDepartmentCode(), delState);
			}
		}
	}

	/**
	 * 根据部门号和账户类型,递归查询该节点及其上级节点是否存在资金账�?
	 * 返回账户Id,没找到返�?0
	 */
	@Override
	public long findCustomerAccountByCodeAndAccountType(String departmentCode, String accountType) {
		long cusomerAccountId = 0;
		cusomerAccountId = this.getCusomerAccountId(departmentCode, accountType);
		if (cusomerAccountId != 0) {
			return cusomerAccountId;
		}
		return cusomerAccountId;
	}

	private long getCusomerAccountId(String departmentCode, String accountType) {
		long cusomerAccountId = 0;
		if(departmentCode == null || "".equals(departmentCode)) {
			return cusomerAccountId;
		}
		Department department = departmentDao.findByCode(departmentCode);
		DepartAccount departAccount = departAccountService.findByDepartmentCodeAndAccountType(departmentCode,accountType);
		if (department.getEnableAccount() == Department.ENBALEACCOUNT_ENABLE
				&& departAccount != null && departAccount.getCustomerAccountId() != 0) {
			return departAccount.getCustomerAccountId();
		} else {
			return getCusomerAccountId(department.getpDeptCode(),accountType);
		}
	}
	
	/**
	 * 根据部门号和账户类型,递归查询该节点及其上级节点是否存在资金账�?
	 * 存在：返回上级所有部门code；不存在：返回空list
	 */
	@Override
	public List<Department> findDepartmentsByDeptCodeAndAccountType(String departmentCode, String accountType) {
		List<Department> departments = new ArrayList<Department>();
		departments = this.getDepts(departmentCode, accountType, departments);
		return departments;
	}

	private List<Department> getDepts(String departmentCode, String accountType, List<Department> departments) {
		if(departmentCode == null || "".equals(departmentCode)) {
			return departments;
		}
		Department department = departmentDao.findByCode(departmentCode);
		DepartAccount departAccount = departAccountService.findByDepartmentCodeAndAccountType(departmentCode,accountType);
		if (department.getEnableAccount() == Department.ENBALEACCOUNT_ENABLE
				&& departAccount != null && departAccount.getCustomerAccountId() != 0) {
			departments.add(department);
			return departments;
		} else {
			if(departmentCode.equals("01")) {
				return new ArrayList<Department>();
			}else {
				departments.add(department);
				return getDepts(department.getpDeptCode(),accountType,departments);
			}
		}
	}

	/**
	 * 根据部门号，递归查询该节点及其上级节点是否存在资金账�?
	 * Map<账户Id, 账户类型>，如果没找到，返回空map
	 */
	@Override
	public Map<Long, String> findCustomerAccountByCode(String departmentCode) {
		List<DepartAccount> departAccounts = this.getCusomerAccounts(departmentCode);
		Map<Long, String> map = new HashMap<Long, String>();
		if (!departAccounts.isEmpty()) {
			for(DepartAccount departAccount : departAccounts) {
				map.put(departAccount.getCustomerAccountId(), departAccount.getAccountType());
			}
			return map;
		}
		return map;
	}

	private List<DepartAccount> getCusomerAccounts(String departmentCode) {
		List<DepartAccount> departAccounts = new ArrayList<DepartAccount>(); 
		if(departmentCode == null || "".equals(departmentCode) || "undefined".equals(departmentCode)) {
			return departAccounts;
		}
		Department department = departmentDao.findByCode(departmentCode);
		departAccounts = departAccountService.findByDepartmentCode(departmentCode);
		if (department.getEnableAccount() == Department.ENBALEACCOUNT_ENABLE
				&& !departAccounts.isEmpty()) {
			return departAccounts;
		} else {
			return getCusomerAccounts(department.getpDeptCode());
		}
	}
	
	/**
	 * 通过工号查询，返回组织机构对�?
	 */
	@Override
	public List<Department> findByAdminCode(String AdminCode) {
		return departmentDao.findByAdminCode(AdminCode);
	}

	/**
	 * 根据部门号删除单个节�?
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteDepartmentByCode(Department department) {
		departmentDao.delete(department);
	}

	/**
	 * 递归删除该节点，以及下级节点
	 */
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public String deleteDepartment(Department department) {
		return deleteAll(department);
	}

	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	private String deleteAll(Department department) {
		Map map = new HashMap();
		map.put("departmentCode", department.getDepartmentCode());
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCode(map);
		if(!adminInfos.isEmpty()) {
			return "hasAdmin";
		}
		List<Department> departments = this.findAllDepartment();
		List<String> pIds = new ArrayList();
		for (Department departmentP : departments) {
			pIds.add(departmentP.getpDeptCode());
		}
		if (pIds.contains(department.getDepartmentCode())) {
			try {
				department.setDelState(department.DELSTATE_DELETED);
				this.deleteDepartmentByCode(department);
				List<Department> childs = findChildByCode(department
						.getDepartmentCode(), Department.DELSTATE_UNDELETED);
				for (Department departmentC : childs) {
					this.deleteAll(departmentC);
				}
				return "success";
			} catch (Exception e) {
				return "false";
			}
		} else {
			department.setDelState(department.DELSTATE_DELETED);
			this.deleteDepartmentByCode(department);
			return "success";
		}

	}

	@Override
	public List<Department> findBy(Map paramters) {
		return departmentDao.findBy(paramters);
	}

	@Override
	public Department findByAdminCodeAndRoleCode(Map paramters) {
		List<Department> results = new ArrayList<Department>();
		results.addAll(findBy(paramters));
		Department department = null;
		if (!results.isEmpty())
			department = results.get(0);
		return department;
	}

	//封装组织结构的树形结构，供combotree使用
	@Override
	public String getDepartTreeStr() {
		StringBuilder stringBuilder = new StringBuilder("[");
		Department department = new Department();
		department.setDelState(Department.DELSTATE_UNDELETED);
		List<Department> departments = findAllRoots(department);	//取根节点遍历，在递归中只循环遍历单个根节点的树结�?
		stringBuilder.append(getChild(departments));
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	private String getChild(List<Department> departments) {
		StringBuilder stringBuilder = new StringBuilder("");
		int index = 1;
		int pIndex = 0;
		for (Department department : departments) {
			List<Department> childDepartments = findChildByCode(department
					.getDepartmentCode(), Department.DELSTATE_UNDELETED);
				stringBuilder.append("{");
				stringBuilder.append("\"id\":\"" + department.getDepartmentCode()
						+ "\",");
				stringBuilder.append("\"text\":\"" + department.getDepartmentName()
						+ "\"");
			if (childDepartments != null && !childDepartments.isEmpty()) {
				stringBuilder.append(",\"children\":[");
				stringBuilder.append(getChild(childDepartments));
				stringBuilder.append("]");
			}
				stringBuilder.append("}");
				if (pIndex < departments.size() - 1) {
					stringBuilder.append(",");
					pIndex++;
				}
		}
		return stringBuilder.toString();
	}

	@Override
	public List<Department> findAllRoots(Department department) {
		return departmentDao.findAllRoots(department);
	}

	@Override
	public Boolean isExistDepartment(long customerAccountId) {
		DepartAccount departAccount = departAccountService.findByCustomerAccountId(customerAccountId);
		if(departAccount == null) {
			return false;
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, readOnly = false, propagation = Propagation.REQUIRED)
	public void addDepartment(Department department) {
		departmentDao.insert(department);
	}
	
	/**
	 * 通过组织机构Code,查询组织机构树上相应的人员信息
	 */
	/*@Override
	public String findAdminInfosFromDeptTreeByDeptCode(Map map) {
		String rstJson = "";
		//构造当前部门（最上级，根节点）
		String crtDepartmentCode = map.get("departmentCode").toString();
		Department crtDepartmentObj = this.findByCode(crtDepartmentCode);
		Map<String, Object> crtDepartmentMap = new HashMap<String, Object>();
		Map<String, Object> stateMap = new HashMap<String, Object>();
		stateMap.put("opened", true);
		crtDepartmentMap.put("text", crtDepartmentObj.getDepartmentName());
		crtDepartmentMap.put("state", stateMap);
		crtDepartmentMap.put("classes", "important");
		
		List<Object> cList = new ArrayList<Object>();
		
		this.findAdminInfosFromDeptTree(map, crtDepartmentMap);
		return rstJson;
	}

	private void findAdminInfosFromDeptTree(Map map, Map<String, Object> pMap) {
		List<Department> childs = this.findChildByCode(map.get("departmentCode").toString(), (Character) map.get("delState"));
		List<Object> childList = new ArrayList<Object>();
		if(!childs.isEmpty()) {
			Map<String, Object> stateMap = new HashMap<String, Object>();
			stateMap.put("opened", true);
			
			for(Department childDepartment : childs) {
				Map<String, Object> childMap = new HashMap<String, Object>();
				childMap.put("text", childDepartment.getDepartmentName());
				childMap.put("state", stateMap);
				
				childList.add(childMap);
				pMap.put("children", childList);
				
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("departmentCode", childDepartment.getDepartmentCode());
				paramMap.put("delState", Department.DELSTATE_UNDELETED);
				findAdminInfosFromDeptTree(paramMap, pMap);
			}
			
		}
	}*/
	/**
	 * 通过组织机构Code以及筛选条件,查询组织机构树上相应的人员信息
	 * @throws IOException 
	 */
	@Override
	public String findAdminInfosFromDeptTreeByDeptCodeAndScreenCondition(Map map) throws IOException {
		StringBuilder stringBuilder = new StringBuilder("[");
		String crtDepartmentCode = map.get("departmentCode").toString();
		Department crtDepartmentObj = this.findByCode(crtDepartmentCode);
		/*char childNodeType = '0';
		List<Department> crtChildDepartments = this.findChildByCode(crtDepartmentCode, Department.DELSTATE_UNDELETED);
		if(!crtChildDepartments.isEmpty()) {
			childNodeType = crtChildDepartments.get(0).getNodeType();
		}*/
		List<Department> departments = new ArrayList<Department>();
		departments.add(crtDepartmentObj);
		//当前节点类型（判断是否需要显示门店节点【总部审核】）
		char nodeType = crtDepartmentObj.getNodeType();
		stringBuilder.append(findAdminInfosFromDeptTreeWithScreenCondition(departments,crtDepartmentCode,nodeType,map));	//, childNodeType	//"first" change to crtDepartmentCode
		stringBuilder.append("]");
		String ret = stringBuilder.toString();
		
		char[] retCharArr = ret.toCharArray();
		String cout = ret.toCharArray()[ret.length()-4] + "";
		if (",".equals(cout)) {
			//char[] ary = new char[retCharArr.length-1];
			//System.arraycopy(retCharArr, 0, ary, 0, ret.length()-4);
			//System.arraycopy(retCharArr, ret.length()-4+1, ary, ret.length()-4, ary.length-ret.length()-4);
			//retCharArr = ary;
			StringBuffer sBuffer = new StringBuffer();
			for (int i = 0; i < retCharArr.length; i++) {
				if(i != ret.length()-4)
					sBuffer.append(retCharArr[i]);
			}
			ret = sBuffer.toString();
		}
		//ret = retCharArr.toString();
		return ret;
		//return stringBuilder.toString();
	}
	
	/**
	 * modify findAdminInfosFromDeptTree[add 筛选条件]
	 * @param departments
	 * @param fst
	 * @param nodeType
	 * @return
	 * @throws IOException 
	 */
	private String findAdminInfosFromDeptTreeWithScreenCondition(List<Department> departments,String fst,char nodeType,Map<String, Object> mapCondition) throws IOException {	//, char childNodeType
		StringBuilder stringBuilder = new StringBuilder("");
		int index = 1;
		int pIndex = 0;
		for (Department department : departments) {
			//子部门
			List<Department> childDepartments = findChildByCode(department.getDepartmentCode(), Department.DELSTATE_UNDELETED);
			//当前部门下的员工
			Map map = new HashMap();
			map.put("departmentCode", department.getDepartmentCode());
			map.put("delState", AdminRole.DELSTATE_UNDELETED);
			List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCodeConcat(map);	
			
			//部门号为信审中心时，去掉角色为组员的员工
			Properties pro = PropertiesLoaderUtils.loadAllProperties(PROPERTIES_FILE_NAME);
			String bpm_departmentCode_creditCenter = pro.getProperty(BPM_DEPARTMENTCODE_CREDITCENTER, "");
			if(bpm_departmentCode_creditCenter.equals(fst)) {
				List<AdminInfo> adminInfosToRemove = new ArrayList<AdminInfo>();
				String bpm_roleId_employee = pro.getProperty(BPM_ROLEID_EMPLOYEE, "");
				for (AdminInfo adminInfo : adminInfos) {
					List<AdminRole> adminRoles = adminRoleService.findByAdminId(adminInfo.getAdminId());
					for (AdminRole adminRole : adminRoles) {
						if(bpm_roleId_employee.equals(adminRole.getRoleId() + "") && adminInfos.contains(adminInfo)) {
							adminInfosToRemove.add(adminInfo);
						}
					}
				}
				
				adminInfos.removeAll(adminInfosToRemove);
			}
			
			if(nodeType == Department.NODETYPE_BPM_OUTLET) {
				stringBuilder.append("{");
				/*stringBuilder.append("\"id\":\"" + department.getDepartmentCode()
					+ "\",");*/
				stringBuilder.append("\"text\":\"" + department.getDepartmentName() + "\",");
				stringBuilder.append("\"state\":{\"opened\":\"true\"}");
				if("first".equals(fst)) {
					stringBuilder.append(",\"classes\":\"important\"");
					fst = "";
				}
			}else {
				if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append("{");
					/*stringBuilder.append("\"id\":\"" + department.getDepartmentCode()
						+ "\",");*/
					stringBuilder.append("\"text\":\"" + department.getDepartmentName() + "\",");
					stringBuilder.append("\"state\":{\"opened\":\"true\"}");
					if("first".equals(fst)) {
						stringBuilder.append(",\"classes\":\"important\"");
						fst = "";
					}
				}
			}
				
			if ((childDepartments != null && !childDepartments.isEmpty()) || (adminInfos != null && !adminInfos.isEmpty())) {
				if(nodeType == Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append(",\"children\":[");
					
					//遍历部门下对应的员工
					//if(nodeType == Department.NODETYPE_BPM_OUTLET) {
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						if(adminInfos != null && !adminInfos.isEmpty()) {
							//int j = 0;
							for (int i = 0; i < adminInfos.size(); i++) {
								if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == false) {	//不进行筛选
									Map<String, Object> mapAdmin = new HashMap<String, Object>();
									mapAdmin.put("id", adminInfos.get(i).getAdminId());
									mapAdmin.put("text", adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]");
									list.add(mapAdmin);
								}else if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == true) {	//进行筛选
									List<String> escapeIdList = (List<String>) mapCondition.get("condition");	//筛选条件
									if(!PubMethod.isEmpty(escapeIdList)) {	//筛选条件不为空-筛选
										int crtId = (int) adminInfos.get(i).getAdminId();
										if(!escapeIdList.contains(crtId)) {	//要筛选id中不包含当前id才会添加
											Map<String, Object> mapAdmin = new HashMap<String, Object>();
											mapAdmin.put("id", adminInfos.get(i).getAdminId());
											mapAdmin.put("text", adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]");
											list.add(mapAdmin);
										}
									}else {	//筛选条件为空-继续添加
										Map<String, Object> mapAdmin = new HashMap<String, Object>();
										mapAdmin.put("id", adminInfos.get(i).getAdminId());
										mapAdmin.put("text", adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]");
										list.add(mapAdmin);
									}
								}
								/*if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == false) {	//不进行筛选
									if(i == 0) {
										stringBuilder.append("{");
									}else {
										stringBuilder.append(",{");
									}
									stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
									stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
									stringBuilder.append("}");
								} else if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == true) {	//进行筛选
									List<String> escapeIdList = (List<String>) mapCondition.get("condition");	//筛选条件
									if(!PubMethod.isEmpty(escapeIdList)) {	//筛选条件不为空-筛选
										int crtId = (int) adminInfos.get(i).getAdminId();
										if(!escapeIdList.contains(crtId)) {	//要筛选id中不包含当前id才会添加
											if(i == 0 || j == 1) {
												stringBuilder.append("{");
												j=2;
											}else {
												stringBuilder.append(",{");
											}
											stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
											stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
											stringBuilder.append("}");
										}else {
											if(escapeIdList.contains((int)adminInfos.get(i).getAdminId()) && i == 0)
												j++;
										}
									}else {	//筛选条件为空-继续添加
										if(i == 0) {
											stringBuilder.append("{");
										}else {
											stringBuilder.append(",{");
										}
										stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
										stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
										stringBuilder.append("}");
									}
								}*/
							}
							if(!list.isEmpty()) {
								String adminInfosStr = JSON.toJSONString(list);
								adminInfosStr = adminInfosStr.substring(1, adminInfosStr.length()-1);
								stringBuilder.append(adminInfosStr);
							}
							
						}
					/*}else {
						
					}*/
					
					//递归遍历部门（如果查找的组织类型为门店，就遍历多有。否则，只遍历到门店以上一级）
					//if(nodeType == Department.NODETYPE_BPM_OUTLET) {	
						if((childDepartments != null && !childDepartments.isEmpty()) && (adminInfos != null && !adminInfos.isEmpty() && !list.isEmpty())) {
							stringBuilder.append(",");
							stringBuilder.append(findAdminInfosFromDeptTreeWithScreenCondition(childDepartments, fst, nodeType, mapCondition));	//, department.getNodeType()
						}else if((childDepartments != null && !childDepartments.isEmpty()) && !(adminInfos != null && !adminInfos.isEmpty() && !list.isEmpty())) {
							stringBuilder.append(findAdminInfosFromDeptTreeWithScreenCondition(childDepartments, fst, nodeType, mapCondition));	//, department.getNodeType()
						}
					/*}else {
						if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
							if((childDepartments != null && !childDepartments.isEmpty()) && (adminInfos != null && !adminInfos.isEmpty())) {
								stringBuilder.append(",");
								stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, department.getNodeType()));
							}else if((childDepartments != null && !childDepartments.isEmpty()) && !(adminInfos != null && !adminInfos.isEmpty())) {
								stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, department.getNodeType()));
							}
						}
					}*/
					stringBuilder.append("]");
				}else {
					/*List<Department> crtChildDepartments = this.findChildByCode(department.getDepartmentCode(), Department.DELSTATE_UNDELETED);
					if(!crtChildDepartments.isEmpty()) {
						for (Department childDepartment : crtChildDepartments) {
							char childNodeType = childDepartment.getNodeType();*/
							if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
								stringBuilder.append(",\"children\":[");
								
								//遍历部门下对应的员工
								//if(nodeType == Department.NODETYPE_BPM_OUTLET) {
								List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
								if(adminInfos != null && !adminInfos.isEmpty()) {
									//int j = 0;
									for (int i = 0; i < adminInfos.size(); i++) {
										/*if(i == 0) {
											stringBuilder.append("{");
										}else {
											stringBuilder.append(",{");
										}
										stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
										stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
										stringBuilder.append("}");*/
										
										/*if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == false) {	//不进行筛选
											if(i == 0) {
												stringBuilder.append("{");
											}else {
												stringBuilder.append(",{");
											}
											stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
											stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
											stringBuilder.append("}");
										} else if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == true) {	//进行筛选
											List<String> escapeIdList = (List<String>) mapCondition.get("condition");	//筛选条件
											if(!PubMethod.isEmpty(escapeIdList)) {	//筛选条件不为空-筛选
												if(!escapeIdList.contains((int)adminInfos.get(i).getAdminId()))	{ //要筛选id中不包含当前id才会添加
													if(i == 0 || j == 1) {
														stringBuilder.append("{");
														j=2;
													}else {
														stringBuilder.append(",{");
													}
													stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
													stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
													stringBuilder.append("}");
												}else {
													if(escapeIdList.contains((int)adminInfos.get(i).getAdminId()) && i == 0)
														j++;
												}
											}else {	//筛选条件为空-继续添加
												if(i == 0) {
													stringBuilder.append("{");
												}else {
													stringBuilder.append(",{");
												}
												stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
												stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
												stringBuilder.append("}");
											}
										}*/
										
										if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == false) {	//不进行筛选
											Map<String, Object> mapAdmin = new HashMap<String, Object>();
											mapAdmin.put("id", adminInfos.get(i).getAdminId());
											mapAdmin.put("text", adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]");
											list.add(mapAdmin);
										}else if(mapCondition.containsKey("isScreen") && (Boolean)mapCondition.get("isScreen") == true) {	//进行筛选
											List<String> escapeIdList = (List<String>) mapCondition.get("condition");	//筛选条件
											if(!PubMethod.isEmpty(escapeIdList)) {	//筛选条件不为空-筛选
												int crtId = (int) adminInfos.get(i).getAdminId();
												if(!escapeIdList.contains(crtId)) {	//要筛选id中不包含当前id才会添加
													Map<String, Object> mapAdmin = new HashMap<String, Object>();
													mapAdmin.put("id", adminInfos.get(i).getAdminId());
													mapAdmin.put("text", adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]");
													list.add(mapAdmin);
												}
											}else {	//筛选条件为空-继续添加
												Map<String, Object> mapAdmin = new HashMap<String, Object>();
												mapAdmin.put("id", adminInfos.get(i).getAdminId());
												mapAdmin.put("text", adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]");
												list.add(mapAdmin);
											}
										}
									}
									if(!list.isEmpty()) {
										String adminInfosStr = JSON.toJSONString(list);
										adminInfosStr = adminInfosStr.substring(1, adminInfosStr.length()-1);
										stringBuilder.append(adminInfosStr);
									}
								}
								
								//递归遍历部门（如果查找的组织类型为门店，就遍历多有。否则，只遍历到门店以上一级）
								if((childDepartments != null && !childDepartments.isEmpty()) && (adminInfos != null && !adminInfos.isEmpty() && !list.isEmpty())) {
									stringBuilder.append(",");
									stringBuilder.append(findAdminInfosFromDeptTreeWithScreenCondition(childDepartments, fst, nodeType, mapCondition));	//, childDepartments.get(0).getNodeType()
								}else if((childDepartments != null && !childDepartments.isEmpty()) && !(adminInfos != null && !adminInfos.isEmpty() && !list.isEmpty())) {
									stringBuilder.append(findAdminInfosFromDeptTreeWithScreenCondition(childDepartments, fst, nodeType, mapCondition));	//, childDepartments.get(0).getNodeType()
								}
								stringBuilder.append("]");
							}/*else {
								stringBuilder.append(",\"children\":[");
								
								//遍历部门下对应的员工
								//if(nodeType == Department.NODETYPE_BPM_OUTLET) {
								if(adminInfos != null && !adminInfos.isEmpty()) {
									for (int i = 0; i < adminInfos.size(); i++) {
										if(i == 0) {
											stringBuilder.append("{");
										}else {
											stringBuilder.append(",{");
										}
										stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
										stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
										stringBuilder.append("}");
									}
								}
								
								stringBuilder.append("]");
							}*/
							
					/*	}
					}*/
				}
			}
			
			if ((childDepartments == null || childDepartments.isEmpty()) && (adminInfos == null || adminInfos.isEmpty())) {
				if(nodeType == Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append(",\"classes\":\"important\"");
					stringBuilder.append(",\"children\":[");
					stringBuilder.append("]");
				}else {
					if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
						stringBuilder.append(",\"classes\":\"important\"");
						stringBuilder.append(",\"children\":[");
						stringBuilder.append("]");
					}
				}
			}
			
			if(nodeType == Department.NODETYPE_BPM_OUTLET) {
				stringBuilder.append("}");
				if (pIndex < departments.size() - 1) {
					stringBuilder.append(",");
					pIndex++;
				}
			}else {
				if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append("}");
					if (pIndex < departments.size() - 1) {
						stringBuilder.append(",");
						pIndex++;
					}
				}else {
					//departments.remove(department);
					pIndex++;
				}
			}
			
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 通过组织机构Code,查询组织机构树上相应的人员信息
	 */
	@Override
	public String findAdminInfosFromDeptTreeByDeptCode(Map map) {
		StringBuilder stringBuilder = new StringBuilder("[");
		String crtDepartmentCode = map.get("departmentCode").toString();
		Department crtDepartmentObj = this.findByCode(crtDepartmentCode);
		/*char childNodeType = '0';
		List<Department> crtChildDepartments = this.findChildByCode(crtDepartmentCode, Department.DELSTATE_UNDELETED);
		if(!crtChildDepartments.isEmpty()) {
			childNodeType = crtChildDepartments.get(0).getNodeType();
		}*/
		List<Department> departments = new ArrayList<Department>();
		departments.add(crtDepartmentObj);
		//当前节点类型（判断是否需要显示门店节点【总部审核】）
		char nodeType = crtDepartmentObj.getNodeType();
		stringBuilder.append(findAdminInfosFromDeptTree(departments,"first",nodeType));	//, childNodeType
		stringBuilder.append("]");
		return stringBuilder.toString();
	}

	private String findAdminInfosFromDeptTree(List<Department> departments,String fst,char nodeType) {	//, char childNodeType
		StringBuilder stringBuilder = new StringBuilder("");
		int index = 1;
		int pIndex = 0;
		for (Department department : departments) {
			//子部门
			List<Department> childDepartments = findChildByCode(department.getDepartmentCode(), Department.DELSTATE_UNDELETED);
			//当前部门下的员工
			Map map = new HashMap();
			map.put("departmentCode", department.getDepartmentCode());
			map.put("delState", AdminRole.DELSTATE_UNDELETED);
			List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCodeConcat(map);
			
			if(nodeType == Department.NODETYPE_BPM_OUTLET) {
				stringBuilder.append("{");
				/*stringBuilder.append("\"id\":\"" + department.getDepartmentCode()
					+ "\",");*/
				stringBuilder.append("\"text\":\"" + department.getDepartmentName() + "\",");
				stringBuilder.append("\"state\":{\"opened\":\"true\"}");
				if("first".equals(fst)) {
					stringBuilder.append(",\"classes\":\"important\"");
					fst = "";
				}
			}else {
				if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append("{");
					/*stringBuilder.append("\"id\":\"" + department.getDepartmentCode()
						+ "\",");*/
					stringBuilder.append("\"text\":\"" + department.getDepartmentName() + "\",");
					stringBuilder.append("\"state\":{\"opened\":\"true\"}");
					if("first".equals(fst)) {
						stringBuilder.append(",\"classes\":\"important\"");
						fst = "";
					}
				}
			}
				
			if ((childDepartments != null && !childDepartments.isEmpty()) || (adminInfos != null && !adminInfos.isEmpty())) {
				if(nodeType == Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append(",\"children\":[");
					
					//遍历部门下对应的员工
					//if(nodeType == Department.NODETYPE_BPM_OUTLET) {
						if(adminInfos != null && !adminInfos.isEmpty()) {
							for (int i = 0; i < adminInfos.size(); i++) {
								if(i == 0) {
									stringBuilder.append("{");
								}else {
									stringBuilder.append(",{");
								}
								stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
								stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
								stringBuilder.append("}");
							}
						}
					/*}else {
						
					}*/
					
					//递归遍历部门（如果查找的组织类型为门店，就遍历多有。否则，只遍历到门店以上一级）
					//if(nodeType == Department.NODETYPE_BPM_OUTLET) {	
						if((childDepartments != null && !childDepartments.isEmpty()) && (adminInfos != null && !adminInfos.isEmpty())) {
							stringBuilder.append(",");
							stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, nodeType));	//, department.getNodeType()
						}else if((childDepartments != null && !childDepartments.isEmpty()) && !(adminInfos != null && !adminInfos.isEmpty())) {
							stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, nodeType));	//, department.getNodeType()
						}
					/*}else {
						if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
							if((childDepartments != null && !childDepartments.isEmpty()) && (adminInfos != null && !adminInfos.isEmpty())) {
								stringBuilder.append(",");
								stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, department.getNodeType()));
							}else if((childDepartments != null && !childDepartments.isEmpty()) && !(adminInfos != null && !adminInfos.isEmpty())) {
								stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, department.getNodeType()));
							}
						}
					}*/
					stringBuilder.append("]");
				}else {
					/*List<Department> crtChildDepartments = this.findChildByCode(department.getDepartmentCode(), Department.DELSTATE_UNDELETED);
					if(!crtChildDepartments.isEmpty()) {
						for (Department childDepartment : crtChildDepartments) {
							char childNodeType = childDepartment.getNodeType();*/
							if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
								stringBuilder.append(",\"children\":[");
								
								//遍历部门下对应的员工
								//if(nodeType == Department.NODETYPE_BPM_OUTLET) {
								if(adminInfos != null && !adminInfos.isEmpty()) {
									for (int i = 0; i < adminInfos.size(); i++) {
										if(i == 0) {
											stringBuilder.append("{");
										}else {
											stringBuilder.append(",{");
										}
										stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
										stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
										stringBuilder.append("}");
									}
								}
								
								//递归遍历部门（如果查找的组织类型为门店，就遍历多有。否则，只遍历到门店以上一级）
								if((childDepartments != null && !childDepartments.isEmpty()) && (adminInfos != null && !adminInfos.isEmpty())) {
									stringBuilder.append(",");
									stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, nodeType));	//, childDepartments.get(0).getNodeType()
								}else if((childDepartments != null && !childDepartments.isEmpty()) && !(adminInfos != null && !adminInfos.isEmpty())) {
									stringBuilder.append(findAdminInfosFromDeptTree(childDepartments, fst, nodeType));	//, childDepartments.get(0).getNodeType()
								}
								stringBuilder.append("]");
							}/*else {
								stringBuilder.append(",\"children\":[");
								
								//遍历部门下对应的员工
								//if(nodeType == Department.NODETYPE_BPM_OUTLET) {
								if(adminInfos != null && !adminInfos.isEmpty()) {
									for (int i = 0; i < adminInfos.size(); i++) {
										if(i == 0) {
											stringBuilder.append("{");
										}else {
											stringBuilder.append(",{");
										}
										stringBuilder.append("\"id\":\"" + adminInfos.get(i).getAdminId() + "\",");
										stringBuilder.append("\"text\":\"" + adminInfos.get(i).getDisplayName() + "[" + adminInfos.get(i).getRoleName() + "]" + "\"");
										stringBuilder.append("}");
									}
								}
								
								stringBuilder.append("]");
							}*/
							
					/*	}
					}*/
				}
			}
			
			if ((childDepartments == null || childDepartments.isEmpty()) && (adminInfos == null || adminInfos.isEmpty())) {
				if(nodeType == Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append(",\"classes\":\"important\"");
					stringBuilder.append(",\"children\":[");
					stringBuilder.append("]");
				}else {
					if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
						stringBuilder.append(",\"classes\":\"important\"");
						stringBuilder.append(",\"children\":[");
						stringBuilder.append("]");
					}
				}
			}
			
			if(nodeType == Department.NODETYPE_BPM_OUTLET) {
				stringBuilder.append("}");
				if (pIndex < departments.size() - 1) {
					stringBuilder.append(",");
					pIndex++;
				}
			}else {
				if(department.getNodeType() != Department.NODETYPE_BPM_OUTLET) {
					stringBuilder.append("}");
					if (pIndex < departments.size() - 1) {
						stringBuilder.append(",");
						pIndex++;
					}
				}else {
					//departments.remove(department);
					pIndex++;
				}
			}
			
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 查询门店组人员所在门店
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IntrospectionException 
	 */
	@Override
	public String findBelongsDepartByCrtDepartmentCode(String departmentCode) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if(!PubMethod.isEmpty(this.getDept(departmentCode)))
			map = this.getDept(departmentCode);
		String rst = "";
		rst = JSON.toJSONString(map);
		return rst;
	}

	private Map<String, Object> getDept(String departmentCode) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>(); 
		if(departmentCode.isEmpty() || "undefined".equals(departmentCode)) {
			return map;
		}
		Department department = departmentDao.findByCode(departmentCode);
		if(department != null) {
			char nodeType = department.getNodeType();
			if (nodeType == Department.NODETYPE_BPM_OUTLET_CHILD || nodeType == Department.NODETYPE_BPM_OUTLET) {
				map = PubMethod.convertBean(department);
				return map;
			} else {
				return getDept(department.getpDeptCode());
			}
		}else {
			return map;
		}
	}

	@Override
	public List<Department> findDepartmentByNodeType(char nodetype) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("nodeType", nodetype);
		map.put("delState", Department.DELSTATE_UNDELETED);
		List<Department> departments = departmentDao.findBy(map);
		return departments;
	}

	@Override
	public int belongToStoreOrCredit(long adminId) throws IOException {
		String crtAdminDeptCode = adminInfoService.findAdminInfoById(adminId).getDepartmentCode();
		//门店总部部门号
		Properties pro = PropertiesLoaderUtils.loadAllProperties(PROPERTIES_FILE_NAME);
		String bpm_departmentCode_creditCenter = pro.getProperty(BPM_DEPARTMENTCODE_CREDITCENTER, "");
		String bpm_departmentCode_outletCenter = pro.getProperty(BPM_DEPARTMENTCODE_OUTLETCENTER, "");
		
		List<Department> departments_creditCenter = findChildDepartments(bpm_departmentCode_creditCenter, Department.DELSTATE_UNDELETED);
		departments_creditCenter.add(departmentDao.findByCode(bpm_departmentCode_creditCenter));
		List<Department> departments_outletCenter = findChildDepartments(bpm_departmentCode_outletCenter, Department.DELSTATE_UNDELETED);
		departments_outletCenter.add(departmentDao.findByCode(bpm_departmentCode_outletCenter));
		
		List<Long> adminIds_creditCenter = new ArrayList<Long>();
		List<Long> adminIds_outletCenter = new ArrayList<Long>();
		for (Department departmentC : departments_creditCenter) {
			Map map = new HashMap();
			map.put("departmentCode", departmentC.getDepartmentCode());
			map.put("delState", AdminRole.DELSTATE_UNDELETED);
			List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCodeConcat(map);
			for (AdminInfo adminInfo : adminInfos) {
				adminIds_creditCenter.add(adminInfo.getAdminId());
			}
		}
		for (Department departmentO : departments_outletCenter) {
			Map map = new HashMap();
			map.put("departmentCode", departmentO.getDepartmentCode());
			map.put("delState", AdminRole.DELSTATE_UNDELETED);
			List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCodeConcat(map);
			for (AdminInfo adminInfo : adminInfos) {
				adminIds_outletCenter.add(adminInfo.getAdminId());
			}
		}
		
		if(adminIds_creditCenter.contains(adminId))
			return 1;
		if(adminIds_outletCenter.contains(adminId))
			return 0;
		return 0;
	}

}
