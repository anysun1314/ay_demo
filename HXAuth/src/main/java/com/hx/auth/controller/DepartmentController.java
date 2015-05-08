package com.hx.auth.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.AdminRole;
import com.hx.auth.bean.DepartAccount;
import com.hx.auth.bean.Department;
import com.hx.auth.service.AdminInfoService;
import com.hx.auth.service.AdminRoleService;
import com.hx.auth.service.DepartAccountService;
import com.hx.auth.service.DepartmentService;
import com.hx.auth.bean.ConstantDefine;
import com.hx.auth.service.ConstantDefineService;
//import com.hx.customer.bean.AccountType;
//import com.hx.customer.bean.CustomerInfo;
//import com.hx.customer.service.CustomerInfoService;

/**
 * 
 * @author anyang
 * @date 2014-4-1 上午11:51:00
 *
 */
@Controller
@RequestMapping("/jsp/department")
public class DepartmentController extends BaseController {
	
	@Autowired
	private DepartmentService departmentService;
	
	/*@Autowired
	private CustomerInfoService customerInfoService;*/
	
	@Autowired
	private ConstantDefineService constantDefineService;
	
	@Autowired
	private AdminInfoService adminInfoService;

	@Autowired
	private AdminRoleService adminRoleService;
	
	@Autowired
	private DepartAccountService departAccountService;
	
    @RequestMapping("/test.do")
    public ModelAndView test(HttpServletRequest request) {
        String deptCode = request.getParameter("deptCode");
        System.out.println(departmentService.findCustomerAccountByCode(deptCode));
        return new ModelAndView();
    }
	
    /**
     * 转向组织机构管理列表页面
     * @return
     */
	@RequestMapping("/departmentsList.do")
	public ModelAndView departmentsList() {
		return new ModelAndView();
	}
	
	/**
	 * 封装组织机构树结构，返回json格式数据，供easyUI-treegrid使用
	 * @return 封装成的json格式数据,格式：{"rows":[{"id":x,"name":"xxx","_parentId":x,"state":"closed"}]...}
	 */
	@RequestMapping("/list.do")
	@ResponseBody
	public Object departmentsTree() {
		List<Department> departments = departmentService.findAll(Department.DELSTATE_UNDELETED);
		
		List pIds = new ArrayList();
		for(Department department : departments) {
			pIds.add(department.getpDeptCode());
		}
		
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(Department department : departments) {
			String id = department.getDepartmentCode();
			String text = department.getDepartmentName();
			String parentId = department.getpDeptCode();
			//if(Department.DELSTATE_DELETED != department.getDelState()) {
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(!id.equals("01")) {
					map.put("_parentId", parentId);
				}
				list.add(map);
			//}
		}
		pMap.put("rows",list);
		return pMap;
	}
	
	/**
	 * 转向组织机构添加页面
	 * @param editingId
	 * @param editingPid
	 * @return
	 */
	@RequestMapping(value="/addDepartment.do")
	public ModelAndView addDepartment(@RequestParam("editingId") String editingId,
									  @RequestParam("editingPid") String editingPid) {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("add", true);
		modelAndView.addObject("departmentCode", editingId);
		modelAndView.addObject("pDeptCode", editingPid);
		
		//显示节点信息用
		String childNode = departmentService.findByCode(editingId).getDepartmentName();
		String parentCode = departmentService.findByCode(editingId).getpDeptCode();
		String parentNode = "";
		if(!"".equals(parentCode) && parentCode != null && !"undefined".equals(parentCode)) {
			parentNode = departmentService.findByCode(parentCode).getDepartmentName();
		}
		modelAndView.addObject("childNode", childNode);
		modelAndView.addObject("parentNode", parentNode);
		
		return modelAndView;
	}
	
	/**
	 * 保存、更新组织机构信息
	 * @param addFlag
	 * @param department
	 * @param loginName
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/saveDepartment.do", method=RequestMethod.POST)
	@ResponseBody
	public Object saveDepartment(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("department") Department department,
			@ModelAttribute("loginName") String loginName,	//操作员输入[登录名]
			HttpSession session
			) throws InvocationTargetException, IllegalAccessException {
		//用于封装返回值{a:x,b:xx}
		Map map = new HashMap();
		
		//判断部门名称是否为空
		if("".equals(department.getDepartmentName())) {
			map.put("departName", "null");
			return map;
		}
		
		//判断有节点类型，无类型值的情况
		if(department.getNodeType() != 0) {
			if("".equals(department.getNodeValue()) || department.getNodeValue() == null){
				List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("departmentNodeType");
				for(int i=0; i<constantDefines.size(); i++){
					ConstantDefine constantDefine = constantDefines.get(i);
					String nodeTypeString = department.getNodeType() + "";
					if(nodeTypeString.equals(constantDefine.getConstantValue())) {
						if(constantDefineService.findsById(constantDefine.getConstantDefineId()).size() > 0){
								map.put("nodeValueNone", "none");
								return map;
						}
					}
				}
			}
		}
		
		String oldCode = department.getDepartmentCode(); 	//当前选中节点code，如果新增，该code为新增节点的父级code
		char enableAccount = department.getEnableAccount();	//是否启用资金账户
		
		//判断是否启用资金帐户？继续 ：判断上级是否开启，上级没有的话，就不允许创建
		if(Department.ENBALEACCOUNT_DISABLE == enableAccount) {
			if(department.getpDeptCode() != null) {
				Map<Long, String> accountMap = new HashMap<Long, String>();
				//根节点的话直接查找该节点账户是否存在
				if("undefined".equals(department.getpDeptCode())) {	
					accountMap = departmentService.findCustomerAccountByCode(oldCode);
				//非根节点，查找上级资金账户是否存在
				}else {	
					accountMap = departmentService.findCustomerAccountByCode(department.getpDeptCode());
				}
				if(accountMap.isEmpty()) {
					map.put("enableAccountWrong", true);
					return map;
				}
			}else {
				map.put("enableAccountWrong", true);
				return map;
			}
		}
		
		//操作员id
		long adminId;
		AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute("loginUser");
		if(sessionAdminInfo != null) {
			adminId = sessionAdminInfo.getAdminId() ;
		}else {
			adminId = 0L;
		}
		
		//新增组织机构
		if(addFlag) {
			//根据当前节点oldCode，判断是否有子节点，并构造新的子节点newCode
			List<Department> departments = departmentService.findAllChildByCode(oldCode);
			String newCode;
			if(departments.size() > 0) {
				List<Long> childsList = new ArrayList();
				for(int i=0; i<=departments.size()-1; i++) {
					childsList.add(Long.parseLong(departments.get(i).getDepartmentCode()));
				}
				Collections.sort(childsList);
				
				long lastChildCode = childsList.get(childsList.size()-1);	//得到子节点code最后一位的code
				long newCodeLong = lastChildCode+1;
				newCode = "0" + newCodeLong;
			}else {
				newCode = oldCode + "01";
			}
			
			//如果启用资金账户
			if(Department.ENBALEACCOUNT_ENABLE == enableAccount) {
				if(!"".equals(loginName)) {
					//判断登录名是否存在
					//TODO CustomerInfo savedCustomerInfo = customerInfoService.findBySystemLoginName(loginName);
					String savedCustomerInfo = ""; //TODO 调用外部接口获取客户
					if(savedCustomerInfo != null) {
						map.put("loginNameExist", true);
						return map;
					}
					//try {
						department.setDepartmentCode(newCode);
						department.setpDeptCode(oldCode);
						department.setDelState(Department.DELSTATE_UNDELETED);
						//保存组织结构信息，调用客户接口保存客户及账户信息
						String msg = departmentService.addDepartmentAndDepartAccount(addFlag,loginName,adminId,department);
						if(!"".equals(msg)) {
							map.put(msg, true);
							return map;
						}
					/*} catch (Exception e) {
						e.printStackTrace();
						map.put("customerAccount", true);
						return map;
					}*/
				}else {
					map.put("loginNameNone", true);
					return map;
				}
			//没启用资金账户
			}else {
				try {
					department.setDepartmentCode(newCode);
					department.setpDeptCode(oldCode);
					department.setDelState(Department.DELSTATE_UNDELETED);
					departmentService.addDepartment(department);
				} catch (Exception e) {
					e.printStackTrace();
					map.put("error", true);
					return map;
				}
			}
			map.put("success", true);
			map.put("newCode", newCode);
			
		//修改组织机构
		}else {
			//启用资金帐户
			if(Department.ENBALEACCOUNT_ENABLE == enableAccount) {
				//根据部门code查询资金帐户
				List<DepartAccount> departAccounts = departAccountService.findByDepartmentCode(department.getDepartmentCode());
				//之前没有平台账户
				if(departAccounts.isEmpty()) {	
					if(!"".equals(loginName)) {
						//判断登录名是否存在
//						CustomerInfo savedCustomerInfo = customerInfoService.findBySystemLoginName(loginName);
						//TODO CustomerInfo savedCustomerInfo = customerInfoService.findBySystemLoginName(loginName);
						String savedCustomerInfo = ""; //TODO 调用外部接口获取客户
						if(savedCustomerInfo != null) {
							map.put("loginNameExist", true);
							return map;
						}
						
						//调用客户接口保存客户及账户信息
						String msg = departmentService.addDepartmentAndDepartAccount(addFlag,loginName,adminId,department);
						if(!"".equals(msg)) {
							map.put(msg, true);
							return map;
						}
					}else {
						map.put("loginNameNone", true);
						return map;
					}
				//之前有平台账户
				}else {	
					if(!"".equals(loginName)) {
						//通过登录名查询保存的客户信息
//						CustomerInfo savedCustomerInfo = customerInfoService.findBySystemLoginName(loginName);
						//通过账户查询客户信息
//						CustomerInfo customerInfo = customerInfoService.findByAccountId(departAccounts.get(0).getCustomerAccountId());
						//判断登录名是否存在
						//TODO 调用外部接口获取客户信息
						String savedCustomerInfo = "";
						if(savedCustomerInfo != null) {	// && savedCustomerInfo.getCustomerId() != customerInfo.getCustomerId()
							map.put("loginNameExist", true);
							return map;
						}
						String storeLoginName = "test";	//TODO customerInfo.getSystemLoginName()
						if(storeLoginName == null) {
							storeLoginName = "";
						}
						if(!storeLoginName.equals(loginName)){	//是否跟原来一样 ? 一样-采用原来的 : 不一样-新增（返回的平台id，保证一一对应）
							//将原来的设置为不可用
							List<DepartAccount> departAccountsToDel = departAccountService.findByDepartmentCode(department.getDepartmentCode());
							if(!departAccountsToDel.isEmpty()) {
								try {
									for(DepartAccount departAccount : departAccountsToDel) {
										departAccount.setDelState(DepartAccount.DELSTATE_DELETED);
										departAccountService.updateDepartAccount(departAccount);
									}
								} catch (Exception e) {
									e.printStackTrace();
									map.put("error", true);
									return map;
								}
							}
							
							//调用客户接口保存客户及账户信息
							String msg = departmentService.addDepartmentAndDepartAccount(addFlag,loginName,adminId,department);
							if(!"".equals(msg)) {
								map.put(msg, true);
								return map;
							}
						}
					}else {
						map.put("loginNameNone", true);
						return map;
					}
				}
			}
			//之前有,标记为不启用时，只修改启用状态码。下次点启用默认读出来，再保存时，执行上面‘之前有’操作
			
			//更新组织结构信息
			if("undefined".equals(department.getpDeptCode())) {
				department.setpDeptCode("");
			}
			department.setDelState(Department.DELSTATE_UNDELETED);
			try {
				departmentService.updateDepartment(department);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", true);
				return map;
			}
			map.put("success", true);
		}
		return map;
		
	}
	
	@RequestMapping(value="/showDepartment.do")
	public ModelAndView showDepartment(@RequestParam("editingId") String editingId) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = departmentService.findByCode(editingId);
		
		/*try {
			long test1 = departmentService.findCustomerAccountByCodeAndAccountType("0101", "00010301");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<Long, String> test = departmentService.findCustomerAccountByCode(department.getDepartmentCode());
		*/
		String nodeTypeName = this.transName(department);
		String loginName = "";
		List<DepartAccount> departAccounts = departAccountService.findByDepartmentCode(department.getDepartmentCode());
		if(!departAccounts.isEmpty()){
			//TODO 调用外部接口
			//loginName = customerInfoService.findByAccountId(departAccounts.get(0).getCustomerAccountId()).getSystemLoginName();
		}
		//显示节点信息用
		Department department2 = departmentService.findByCode(editingId);
		String childNode = departmentService.findByCode(editingId).getDepartmentName();
		String parentCode = departmentService.findByCode(editingId).getpDeptCode();
		String parentNode = "";
		if(!"".equals(parentCode) && parentCode != null && !"undefined".equals(parentCode)) {
			parentNode = departmentService.findByCode(parentCode).getDepartmentName();
		}
		modelAndView.addObject("childNode", childNode);
		modelAndView.addObject("parentNode", parentNode);
		
		modelAndView.addObject("loginName", loginName);
		modelAndView.addObject("nodeTypeName", nodeTypeName);
		modelAndView.addObject(department);
		modelAndView.setViewName("jsp/department/showDepartment");
		return modelAndView;
	}

	/**
	 * 转换节点类型名称、节点类型值名称，设置节点类型值名称
	 * @param department
	 * @return	节点类型名称
	 */
	private String transName(Department department) {
		String nodeTypeName = "";
		long constantDefineId = 0L;
		String constantValueName = "";
		//获取节点类型名
		List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("departmentNodeType");
		for(ConstantDefine constantDefine : constantDefines) {
			String constantValue = constantDefine.getConstantValue();
			String nodeType =  department.getNodeType() + "";
			if(constantValue.equals(nodeType)){
				nodeTypeName = constantDefine.getConstantName();
				constantDefineId = constantDefine.getConstantDefineId();
			}
		}
		//获取、设置节点类型值名
		constantDefines = constantDefineService.getConstantDefinesByType("departmentNodeValue");
		for(ConstantDefine constantDefine : constantDefines) {
			if(department.getNodeValue()!=null) {
				if(department.getNodeValue().equals(constantDefine.getConstantValue()) && constantDefineId==constantDefine.getParentConstant()) {
					constantValueName = constantDefine.getConstantName();
					department.setNodeValue(constantValueName);
				}
			}
		}
		return nodeTypeName;
	}
	
	@RequestMapping(value="/editDepartment.do")
	public Object editdepartment(@RequestParam("editingId") String editingId,
								 @RequestParam("editingPid") String editingPid) {
		ModelAndView modelAndView = new ModelAndView();
		Department department = departmentService.findByCode(editingId);
		//获取当前节点类型的id，用来查询该节点对应节点类型值
		long nodeValueId = 0L;
		String flag = "";
		if(department.getNodeType() != 0) {
			List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("departmentNodeType");
			for(ConstantDefine constantDefine : constantDefines) {
				if(constantDefine.getConstantValue().equals(department.getNodeType() + "")) {
					nodeValueId = constantDefine.getConstantDefineId();
				}
			}
		}else {
			flag = "0";
		}
		String loginName = "";
		List<DepartAccount> departAccounts = departAccountService.findByDepartmentCode(department.getDepartmentCode());
		if(!departAccounts.isEmpty()){
			//TODO 调用外部接口
			//loginName = customerInfoService.findByAccountId(departAccounts.get(0).getCustomerAccountId()).getSystemLoginName();
		}
		
		//显示节点信息用
		String childNode = departmentService.findByCode(editingId).getDepartmentName();
		String parentCode = departmentService.findByCode(editingId).getpDeptCode();
		String parentNode = "";
		if(!"".equals(parentCode) && parentCode != null) {
			parentNode = departmentService.findByCode(parentCode).getDepartmentName();
		}
		modelAndView.addObject("childNode", childNode);
		modelAndView.addObject("parentNode", parentNode);
		
		modelAndView.addObject("loginName", loginName);
		modelAndView.addObject("nodeValueId", nodeValueId);
		modelAndView.addObject(department);
		modelAndView.addObject("add", false);
		modelAndView.addObject("flag",flag);
		modelAndView.addObject("departmentCode", editingId);
		modelAndView.addObject("pDeptCode", editingPid);
		modelAndView.setViewName("jsp/department/addDepartment");
		return modelAndView;
	}
	
	@RequestMapping(value="/deleteDepartment.do")
	@ResponseBody
	public Object deleteDepartment(@RequestParam("editingId") String editingId) {
		Department department = departmentService.findByCode(editingId);
		return departmentService.deleteDepartment(department);
	}

	/**
	 * 查询常量表，封装节点类型
	 * @return	json格式的字符串 eg:[{"id":1,"text":"text1" },{"id":2,"text":"text2" }]
	 */
	@RequestMapping(value="/findNodeType.do")
	@ResponseBody
	public Object findNodeType() {
		List nodeTypes = new ArrayList();
		List<ConstantDefine> constantDefines = constantDefineService.getConstantDefinesByType("departmentNodeType");
		for(int i=0; i<constantDefines.size(); i++){
			Map map = new HashMap();
			if(i==0) {
				Map mapdef = new HashMap();
				mapdef.put("id", "0");
				mapdef.put("text", "请选择");
				mapdef.put("consId", "0");
				nodeTypes.add(mapdef);
			}
			ConstantDefine constantDefine = constantDefines.get(i);
			map.put("id", constantDefine.getConstantValue());
			map.put("text", constantDefine.getConstantName());
			map.put("consId", constantDefine.getConstantDefineId());
			nodeTypes.add(map);
		}
		return nodeTypes;
	}
	
	/**
	 * 查询常量表，封装节点类型值
	 * @return	json格式的字符串
	 */
	@RequestMapping(value="/findNodeValue.do")
	@ResponseBody
	public Object findNodeValue(@RequestParam("id") String id) {
		List nodeValues = new ArrayList();
		List<ConstantDefine> constantDefines = constantDefineService.findsById(Long.parseLong(id));
		
		for(int i=0; i<constantDefines.size(); i++){
			Map map = new HashMap();
			ConstantDefine constantDefine = constantDefines.get(i);
			if(constantDefine.getConstantTypeCode().equals("departmentNodeValue")) {
				map.put("id", constantDefine.getConstantValue());
				map.put("text", constantDefine.getConstantName());
				nodeValues.add(map);
			}
		}
		return nodeValues;
	}

	/**
	 * 用于转到显示，并选择组织结构节点页面
	 */
	@RequestMapping("/showDepartmentTree.do")
	@ResponseBody
	public Object showDepartmentTree(){
		return new ModelAndView();
	}
	
	/**
	 * combotree 封装json串
	 * @return 封装成的json格式数据,格式：{[{"id":x,"text:"xxx","children":"[{"id":x,"text":xx}]","state":"closed"}]...}
	 */
	@RequestMapping("/departmentTreeByChildren.do")
	@ResponseBody
	public Object departmentTreeByChildren() {
		String treeString = departmentService.getDepartTreeStr();
		return treeString;
	}
	
	
	/**
	 * 以下为组织机构模块的员工管理模块
	 * 
	 * 跳转到员工管理页
	 */
	@RequestMapping("/showAdmins.do")
	public ModelAndView showAdmins(@RequestParam("editingId") String editingId) {
		ModelAndView modelAndView = new ModelAndView();
		String childNode = departmentService.findByCode(editingId).getDepartmentName();
		String parentCode = departmentService.findByCode(editingId).getpDeptCode();
		String parentNode = "";
		if(!"".equals(parentCode) && parentCode != null && !"undefined".equals(parentCode)) {
			parentNode = departmentService.findByCode(parentCode).getDepartmentName();
		}
		modelAndView.addObject("childNode", childNode);
		modelAndView.addObject("parentNode", parentNode);
		modelAndView.addObject("departmentCode", editingId);
		return modelAndView;
	}
	
	@RequestMapping(value="/showAdminInfo.do")
	@ResponseBody
	public Object showAdminInfo(@RequestParam("departmentCode") String departmentCode) {
		//机构下关联的人员
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCodeConcat(map);
		return adminInfos;
	}
	
	@RequestMapping("/addAdmin.do")
	public ModelAndView addAdmin(@RequestParam("departmentCode") String departmentCode) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("departmentCode", departmentCode);
		return modelAndView;
	}
	
	@RequestMapping(value="/searchAdmin.do")
	@ResponseBody
	public Object searchAdmin(@RequestParam("adminCode") String adminCode) {
		List<AdminInfo> adminInfos;
		try {
			adminInfos = adminInfoService.findByCode(adminCode);
			if(adminInfos.size() == 0) {
				return "empty";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return adminInfos.get(0);
	}
	
	@RequestMapping(value="/saveAdminRole.do")
	@ResponseBody
	public Object saveAdminRole(@RequestParam("departmentCode") String departmentCode,
			@RequestParam("adminId") long adminId,
			@RequestParam("adminName") String adminName,
			@RequestParam("positionId") long positionId,
			@RequestParam("roleId") long roleId,
			@RequestParam("adminCode") String adminCode,
			HttpServletRequest request) {
		
		if(adminId == 0L) {
			return "emptyAdmin";
		}
		
		List<AdminInfo> adminInfos;
		try {
			adminInfos = adminInfoService.findByCode(adminCode);
			if(adminInfos.size() > 0) {
				long checkAdminId = adminInfos.get(0).getAdminId();
				String checkDisplayName = adminInfos.get(0).getDisplayName();
				if(checkAdminId != adminId || !checkDisplayName.equals(adminName)) {
					return "wrong";
				}
			}else {
				return "wrong";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "wrong";
		}
		
		AdminRole adminRole = new AdminRole();
		adminRole.setAdminId(adminId);
		adminRole.setDepartmentCode(departmentCode);
		adminRole.setPositionId(positionId);
		adminRole.setRoleId(roleId);
		adminRole.setDelState(AdminRole.DELSTATE_UNDELETED);
		adminRoleService.addAdminRole(adminRole);
		return "success";
	}
	
	@RequestMapping(value="/deleteAdmin.do")
	@ResponseBody
	public Object deleteAdmin(@RequestParam("userRoleIds") String userRoleIds) {
		try {
			adminRoleService.deleteAdminRoles(userRoleIds);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}
	
	@RequestMapping(value="/findAllRoots.do")
	@ResponseBody
	public Object findAllRoots() {
		Department department = new Department();
		department.setDelState(Department.DELSTATE_UNDELETED);
		List<Department> roots = departmentService.findAllRoots(department);
		return roots;
	}
	
	/**
	 * 组织机构添加操作员的条件：存在资金帐户
	 * @param departmentCode
	 * @return
	 */
	@RequestMapping(value="/addCondition.do")
	@ResponseBody
	public Object addCondition(@RequestParam("departmentCode") String departmentCode) {
		try {
			Map<Long, String> departAccounts =  departmentService.findCustomerAccountByCode(departmentCode);
			if(departAccounts.isEmpty()) {
				return "noAccount";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}
	
}
