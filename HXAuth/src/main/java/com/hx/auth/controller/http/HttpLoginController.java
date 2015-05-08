package com.hx.auth.controller.http;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.Department;
import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.bean.Position;
import com.hx.auth.bean.RoleInfo;
import com.hx.auth.cached.AdminFunctionUrlCached;
import com.hx.auth.cached.FunctionUrlCached;
import com.hx.auth.cached.OptionInfoAndNavTitleCached;
import com.hx.auth.controller.BaseController;
import com.hx.auth.service.AdminInfoService;
import com.hx.auth.service.DepartmentService;
import com.hx.auth.service.FunctionInfoService;
import com.hx.auth.service.FunctionUrlService;
import com.hx.auth.service.PositionService;
import com.hx.auth.service.RoleInfoService;
import com.hx.auth.util.JSONUtil;
import com.hx.auth.util.MD5Key;
import com.hx.auth.util.PubMethod;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * @author anyang
 * @date 2015-1-8 下午5:02:13
 */
@Controller("httpLoginController")
@RequestMapping("/http/login")
public class HttpLoginController extends BaseController {
	
	@Autowired
	private AdminInfoService adminInfoService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private FunctionInfoService functionInfoService;
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private AdminFunctionUrlCached adminFunctionUrlCached;
	@Autowired
	private FunctionUrlCached functionUrlCached;
	@Autowired
	private FunctionUrlService functionUrlService;
	@Autowired
	private OptionInfoAndNavTitleCached optionInfoAndNavTitleCached;
	
	@RequestMapping(value="/login.do", method = RequestMethod.POST)
	@ResponseBody
	public Object login(
						@ModelAttribute("name") String loginName,
						@ModelAttribute("pwd") String loginPwd,
						@ModelAttribute("userInfoSessionStr") String sessionAdminInfoStr) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		//List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		//获取登录用户信息
		AdminInfo adminInfo = new AdminInfo();
		List<AdminInfo> adminInfos = adminInfoService.findByLoginName(loginName);
		if(!PubMethod.isEmpty(sessionAdminInfoStr)) {
			AdminInfo sessionAdminInfo = new AdminInfo();
			try {
				sessionAdminInfo = JSONObject.parseObject(sessionAdminInfoStr, AdminInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("result", "sysError");
			}
			if(sessionAdminInfo.getAdminCode() != null) {
				List<AdminInfo> adminInfosSaved = adminInfoService.findByLoginName(sessionAdminInfo.getLoginName());
				String savedPwd = adminInfosSaved.get(0).getLoginPwd();
				sessionAdminInfo.setLoginPwd(savedPwd);
				adminInfo = sessionAdminInfo;
			}
		}else {
			if(adminInfos.size() > 0)
				adminInfo = adminInfos.get(0);
		}
		
		List<String> permissionStr = new ArrayList<String>();	//默认角色具有的权限集
		if(adminInfo.getAdminCode() != null) {
			if(adminInfo.getAdminState() == adminInfo.ENBALEADMIN_DISABLE) {
				map.put("result", "error");//该用户状态为无效，不可以登录系统！
				//list.add(map);
			}else {
				if(adminInfo.getAdminState() == adminInfo.ENBALEADMIN_GONE) {
					map.put("result", "error");//该用户已离职，不可以登录系统！
					//list.add(map);
				}else {
					MD5Key md5Key = new MD5Key();
					String md5Pwd = md5Key.getkeyBeanofStr(loginPwd);
					if(md5Pwd.equals(adminInfo.getLoginPwd())) { 
						//登录成功
						adminInfo.setLoginPwd("");
						Map<String, Object> mapUserInfo = PubMethod.convertBean(adminInfo);
						//list.add(mapUserInfo);	
						map.put("userInfo", mapUserInfo);	//记录当前登录用户信息
						
						/*
						 * 获取当前用户的默认部门，职位及角色，记录到session
						 * 以该员工最顶级的组织机构对应的用户权限，作为用户权限的缺省值（多个？）
						 */
						String currentDepartCode = adminInfo.getDepartmentCode();
						long currentRoleId = adminInfo.getRoleId();
						long currentPositionId = adminInfo.getPositionId();
						long currentUserRoleId = adminInfo.getUserRoleId();
						for(AdminInfo adminInfoSaved : adminInfos) {
							if(adminInfoSaved.getDepartmentCode().length() < currentDepartCode.length()) {	//用部门code长度判断
								currentDepartCode = adminInfoSaved.getDepartmentCode();
								currentRoleId = adminInfoSaved.getRoleId();
								currentPositionId = adminInfoSaved.getPositionId();
								currentUserRoleId = adminInfoSaved.getUserRoleId();
							}
							adminInfoSaved.setLoginPwd("");
						}
						//获取默认角色对应的权限Code,记录默认角色具有的权限集
						List<FunctionInfo> functionInfos = functionInfoService.findByRoleId(currentRoleId);
						for(FunctionInfo functionInfo : functionInfos) {
							permissionStr.add(functionInfo.getFunctionCode());
						}
						//记录用户信息
						this.userInfoToMap(map, adminInfos,currentDepartCode,currentRoleId,currentPositionId,currentUserRoleId,permissionStr);
						
						//初始化当前角色对应权限的url--changed[改用session储存]
                        //adminFunctionUrlCached.reset(currentRoleId);
						
					}else {
						map.put("result", "error");//密码错误！
					}
				}
			}
		}else {
			map.put("result", "error");//用户名错误！
		}
		
		String jsonString = JSON.toJSONString(map);
		return jsonString;
	}
	
	@RequestMapping(value="/logout.do", method = RequestMethod.POST)
	@ResponseBody
	public Object logout(HttpServletRequest request, HttpSession session) {
		
		session.invalidate();
		
		return "success";
	}
	
	/**
	 * 登录用户选择当前使用的角色（默认为顶层组织机构对应角色，如果多个取第一个）
	 */
	@RequestMapping(value="/changeDepart.do", method = RequestMethod.POST)
	@ResponseBody
	public Object changeDepart(HttpServletRequest request, HttpSession session,
						@ModelAttribute("userRoleId") long userRoleId) {
		//获取当前登录用户，不存在跳出
		AdminInfo sessionAdminInfo = (AdminInfo) session.getAttribute(AdminInfo.LOGINUSER);
		ModelAndView modelAndView = new ModelAndView();
		if(sessionAdminInfo != null) {
			String loginName = sessionAdminInfo.getLoginName();
			List<AdminInfo> adminInfos = adminInfoService.findByLoginName(loginName);
			
			String currentDepartCode = "";
			long currentRoleId = 0L;
			long currentPositionId = 0L;
			long currentUserRoleId = 0L;
			for(AdminInfo adminInfo : adminInfos) {
				if(adminInfo.getUserRoleId() == userRoleId) {
					currentDepartCode = adminInfo.getDepartmentCode();
					currentRoleId = adminInfo.getRoleId();
					currentPositionId = adminInfo.getPositionId();
					currentUserRoleId = adminInfo.getUserRoleId();
				}
			}
			//获取默认角色对应的权限Code,记录默认角色具有的权限集
			List<FunctionInfo> functionInfos = functionInfoService.findByRoleId(currentRoleId);
			List<String> permissionStr = new ArrayList<String>();
			for(FunctionInfo functionInfo : functionInfos) {
				permissionStr.add(functionInfo.getFunctionCode());
			}
			//记录用户信息
			//TODO this.userInfoToSession(session, adminInfos,currentDepartCode,currentRoleId,currentPositionId,currentUserRoleId,permissionStr);
			
			//重置当前角色对应权限的url--changed[改用session储存]
			//adminFunctionUrlCached.reset(currentRoleId);
			//重置所有链接的缓存
			functionUrlCached.reset();
			
		}else {
			return "false";
		}
		return "success";
	}
	
	@RequestMapping(value="/refresh.do")
	public ModelAndView refresh() {
		ModelAndView modelAndView = new ModelAndView();
		List<String> permissionStr = this.getCurrentPermission();
		this.jumpToDefaultPage(modelAndView, permissionStr);
		modelAndView.setViewName("index");
		return modelAndView;
	}

	/**
	 * 将除了loginUser以外的其他信息,封装到map
	 */
	private void userInfoToMap(Map<String, Object> map,
			List<AdminInfo> adminInfos, String currentDepartCode,
			long currentRoleId, long currentPositionId,long currentUserRoleId,List<String> permissionStr) {
		//默认部门对象
		Department currentDepart = departmentService.findByCode(currentDepartCode);
		map.put(AdminInfo.CURRENTDEPARTMENT, currentDepart);
		
		//默认部门的父级部门对象
		String parentDepartmentCode = currentDepart.getpDeptCode();
		if(parentDepartmentCode != null) {
			Department parentDepartment = departmentService.findByCode(parentDepartmentCode);
			map.put(AdminInfo.CURRENTPARENTDEPARTMENT, parentDepartment);
		}else {
			map.put(AdminInfo.CURRENTPARENTDEPARTMENT, new Department());
		}
		
		//默认部门的子部门对象集
		/*List<Department> childDepartments = departmentService.findChildDepartments(currentDepartCode, Department.DELSTATE_UNDELETED);
		if(childDepartments.isEmpty()) {
			childDepartments = new ArrayList<Department>();
		}
		map.put(AdminInfo.CURRENTCHILDDEPARTMENTS, childDepartments);*/
		
		//默认角色对象
		RoleInfo currentRoleInfo = roleInfoService.findById(currentRoleId);
		map.put(AdminInfo.CURRENTROLE, currentRoleInfo);
		
		//默认职位对象
		Position currentPosition = positionService.findById(currentPositionId);
		map.put(AdminInfo.CURRENTPOSITION, currentPosition);
		
		//默认角色对应的权限Code,记录默认角色具有的权限集
		map.put(AdminInfo.CURRENTPERMISSION, permissionStr);
		
		//记录带有部门权限信息的用户集
		map.put(AdminInfo.USERINFOS, adminInfos);
		
		//记录当前员工角色关联id
		map.put(AdminInfo.USERROLEID, currentUserRoleId);
		
		//用户权限url
		this.functionUrlMap(map, currentRoleId);
	}
	
	/**
	 * 跳转到默认首页
	 * @param modelAndView
	 * @param permissionStr
	 */
	private void jumpToDefaultPage(ModelAndView modelAndView,
			List<String> permissionStr) {
		//获取拥有权限的编码最大的一级导航
		String firstNav = "";
		if(!permissionStr.isEmpty()) {
			long firstNavInt = 0;
			for(String pString : permissionStr) {
				if(pString.length() == 2 && Long.parseLong(pString) > firstNavInt) {
					firstNavInt = Long.parseLong(pString);
					firstNav = pString;
				}
			}
		}
		//获取一级导航下拥有权限的第一个二级导航作为默认页
		List<String> secondNavs = new ArrayList<String>();
		if(!"".equals(firstNav)) {
			for(String subPString : permissionStr) {
				if(subPString.length() == 4 && subPString.startsWith(firstNav)) {
					secondNavs.add(subPString);
				}
			}
		}
		String secondNav = "";
		if(!secondNavs.isEmpty()) {
			secondNav = secondNavs.get(0);
			for(String nav : secondNavs) {
				if(Long.parseLong(secondNav) > Long.parseLong(nav)) {
					secondNav = nav;
				}
			}
		}
		
		String defaultUrl = "";
		String defaultNavName = "";
		if(!"".equals(secondNav)) {
			FunctionInfo functionInfo = functionInfoService.findByCode(secondNav);
			if(functionInfo != null) {
				List<FunctionUrl> functionUrls = functionUrlService.findByFunctionId(functionInfo.getFunctionId());
				if(!functionUrls.isEmpty()) {
					defaultUrl = functionUrls.get(0).getUrlInfo();
					Map<String, String> navTitleMap = optionInfoAndNavTitleCached.getNavTitle();
					if(navTitleMap.containsKey(defaultUrl)) {
						defaultNavName = navTitleMap.get(defaultUrl);
					}
				}
			}
		}
		
		modelAndView.addObject("defaultUrl", defaultUrl);
		modelAndView.addObject("defaultNavName", defaultNavName);
	}
	
	@RequestMapping(value="/sessionTimeout.do")
	public ModelAndView sessionTimeout(@ModelAttribute("timeout") String timeout) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("timeout", timeout);
		modelAndView.setViewName("login");
		return modelAndView;
	}
	
	@RequestMapping(value="/noAccess.do")
	public ModelAndView noAccess(@ModelAttribute("view") String view) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("view", view);
		modelAndView.setViewName("index");
		return modelAndView;
	}
	
	/**
	 * 2014-11-7 Changed: Static to Session 改用session存储当前用户functionUrl
	 * 缓存当前用户的权限所关联的url
	 */
	private void functionUrlMap(Map<String, Object> map, long currentRoleId) {
		//当前用户当前角色对应的权限集
		List<FunctionInfo> functionInfos = functionInfoService.findByRoleId(currentRoleId);
		List<String> adminUrls = new ArrayList<String>();
		if(!functionInfos.isEmpty()) {
			for(FunctionInfo functionInfo : functionInfos) {
				long functionId = functionInfo.getFunctionId();
				List<FunctionUrl> functionUrls = functionUrlService.findByFunctionId(functionId);
				if(!functionUrls.isEmpty()) {
					for(FunctionUrl functionUrl : functionUrls) {
						String urlInfo = functionUrl.getUrlInfo();
						if(!adminUrls.contains(urlInfo) && !"".equals(urlInfo)) {
							adminUrls.add(urlInfo);
						}
					}
				}
			}
		}
		map.put(AdminInfo.USERFUNCTIONURL, adminUrls);
	}
	
}
