package com.hx.auth.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.AdminRole;
import com.hx.auth.service.AdminInfoService;
import com.hx.auth.service.AdminRoleService;
import com.hx.auth.util.JSONUtil;
import com.hx.auth.util.MD5Key;

/**
 * 
 * @author anyang
 * @date 2014-4-17 下午4:30:00
 *
 */
@Controller
@RequestMapping("/jsp/admin")
public class AdminInfoController extends BaseController{
	@Autowired
	private AdminInfoService adminInfoService;
	
	@Autowired
	private AdminRoleService adminRoleService;
	
	@RequestMapping(value = "/adminsList.do")
	public ModelAndView adminsList() {
		return new ModelAndView();
	}
	
	@RequestMapping(value="/list.do", method = RequestMethod.POST)
	@ResponseBody
	public Object adminsList(HttpServletRequest request, HttpSession session) {
		List<AdminInfo> adminInfos = adminInfoService.findAll();
		return adminInfos;
	}
	/**
	 * 为分页显示员工，提供数据
	 * @param request
	 * @param session
	 * @param adminInfo 如果需要根据条件查询，将查询条件封装于此
	 * @return Pagination<AdminInfo>
	 * @throws Exception
	 */
	@RequestMapping(value = "/listByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public Object adminInfosByPage(HttpServletRequest request, HttpSession session,
    		@ModelAttribute("adminInfo") AdminInfo adminInfo) throws Exception {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        return adminInfoService.findAllByPage(pageNo, pageSize, adminInfo);
    }
	
	@RequestMapping(value = "/findAllAdminWithAccount.do", method = RequestMethod.POST)
    @ResponseBody
    public List<AdminInfo> findAllAdminWithAccount() throws Exception {
        List<AdminInfo> results = adminInfoService.findAllAdminWithAccount();
		return results;
    }
	
	@RequestMapping(value="/addAdmin.do")
	public ModelAndView addAdmin() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("add", true);
		modelAndView.addObject("adminId", -1);
		return modelAndView;
	}
	
	@RequestMapping(value="/editAdmin.do")
	public ModelAndView editAdmin(@ModelAttribute("adminId") long adminId) {
		ModelAndView modelAndView = new ModelAndView();
		List<AdminInfo> adminInfo = adminInfoService.findById(adminId);
		//adminInfo.setEmail(adminInfo.getEmail().replace("@hexindai.com", ""));
		//formatter(modelAndView, adminInfo);	//状态替换为不可选时，打开
		if(adminInfo != null) {
			modelAndView.addObject(adminInfo.get(0));
		}else {
			modelAndView.addObject(new AdminInfo());
		}
		modelAndView.addObject("add", false);
		modelAndView.addObject("adminId", adminId);
		//odelAndView.addObject("adminDepartInfo",adminInfo);
		modelAndView.setViewName("jsp/admin/addAdmin");
		return modelAndView;
	}
	
	@RequestMapping(value="/showAdmin.do")
	public ModelAndView showAdmin(@ModelAttribute("adminId") long adminId) {
		ModelAndView modelAndView = new ModelAndView();
		AdminInfo adminInfo = adminInfoService.findById(adminId).get(0);
		formatter(modelAndView, adminInfo);
		modelAndView.addObject(adminInfo);
		return modelAndView;
	}
	
	/**
	 * 转向用户修改密码展示页
	 * @param adminId
	 * @return
	 */
	@RequestMapping(value="/showAdminInfo.do")
	public ModelAndView showAdminInfo(@ModelAttribute("adminId") long adminId) {
		ModelAndView modelAndView = new ModelAndView();
		AdminInfo adminInfo = adminInfoService.findById(adminId).get(0);
		formatter(modelAndView, adminInfo);
		modelAndView.addObject(adminInfo);
		return modelAndView;
	}
	
	@RequestMapping(value="/showAdminDepart.do")
	@ResponseBody
	public Object showAdminDepart(@ModelAttribute("adminId") long adminId) {
		List<AdminInfo> adminInfos = adminInfoService.findById(adminId);
		return adminInfos;
	}
	
	@RequestMapping(value="/saveAdmin.do", method=RequestMethod.POST)
	@ResponseBody
	public Object saveAdmin(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("adminInfo") AdminInfo adminInfo,
			@ModelAttribute("departmentInfo") String departmentInfo) {
		
		List<JSONObject> departmentJsonObjects = JSONUtil.getListFromPageJSON(departmentInfo);
		if(!departmentJsonObjects.isEmpty()) {
			//判断组织权限是否重复
			List departments = new ArrayList();
			List positions = new ArrayList();
			List roles = new ArrayList();
			for (JSONObject jsonObject : departmentJsonObjects) {
				departments.add(jsonObject.getString("departmentCode"));
				positions.add(jsonObject.getLong("positionId"));
				roles.add(jsonObject.getLong("roleId"));
			}
			
			for (int i = 0; i < departments.size() - 1; i++) {
		         for (int j = departments.size() - 1; j > i; j--) {
		              if (departments.get(j).equals(departments.get(i))) {
		            	  if(positions.get(j) == positions.get(i)) {
		            		  if(roles.get(j) == roles.get(i)) {
		            			  return "equal";
		            		  }
		            	  }
		              }
		        }
		    }
		}
		
		if(addFlag) {
			//判断身份证
			String adminIdCode = adminInfo.getAdminIDCode();
			String email = adminInfo.getEmail();
			String telPhone = adminInfo.getTelPhone();
			List<AdminInfo> adminInfos = adminInfoService.findByAdminIdCode(adminIdCode);
			AdminInfo adminInfoEmail = adminInfoService.findByEmail(email);
			AdminInfo adminInfoTelPhone = adminInfoService.findByTelPhone(telPhone);
			if(adminInfos.size() != 0) {
				return "idCodeExist";
			}
			if(adminInfoEmail != null) {
				return "emailExist";
			}
			if(adminInfoTelPhone != null) {
				return "telPhoneExist";
			}
			
			try {
				if(adminInfoService.findByCode(adminInfo.getAdminCode()).size() != 0) {
					return "exist";
				}
				adminInfo.setLoginPwd(AdminInfo.DEFAULT_PASSWORD);
		        if(!departmentJsonObjects.isEmpty()) {
		        	for (JSONObject jsonObject : departmentJsonObjects) {
		        		adminInfo.setDepartmentCode(jsonObject.getString("departmentCode"));
		        		adminInfo.setPositionId(jsonObject.getLong("positionId"));
		        		adminInfo.setRoleId(jsonObject.getLong("roleId"));
		        		adminInfoService.addAdminInfo(adminInfo);
		        	}
		        }else {
		        	adminInfoService.addAdminInfo(adminInfo);
		        }
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}else {
			//判断身份证
			String adminIdCode = adminInfo.getAdminIDCode();
			String email = adminInfo.getEmail();
			String telPhone = adminInfo.getTelPhone();
			long adminId = adminInfo.getAdminId();
			List<AdminInfo> adminInfos = adminInfoService.findByAdminIdCode(adminIdCode);
			AdminInfo adminInfoEmail = adminInfoService.findByEmail(email);
			AdminInfo adminInfoTelPhone = adminInfoService.findByTelPhone(telPhone);
			if(adminInfos.size() != 0) {
				for(AdminInfo editAdminInfo : adminInfos) {
					long editAdminId = editAdminInfo.getAdminId();
					if(editAdminId != adminId) {
						return "idCodeExist";
					}
				}
			}
			if(adminInfoEmail != null && adminId != adminInfoEmail.getAdminId()) {
				return "emailExist";
			}
			if(adminInfoTelPhone != null && adminId != adminInfoTelPhone.getAdminId()) {
				return "telPhoneExist";
			}
			
			try {
				adminInfoService.updateAdminInfo(adminInfo);
				List<AdminRole> adminRoles = adminRoleService.findByAdminId(adminInfo.getAdminId());
				
				if(!departmentJsonObjects.isEmpty()) {
					if(adminRoles.size() != 0) {
						//判断departmentJsonObjects中的数据只是修改，还是有增删操作
						List<Long> formUserRoleIds = new ArrayList();	//表单提交过来数据的userRoleId
						for(JSONObject jsonObject : departmentJsonObjects) {
							formUserRoleIds.add(jsonObject.getLong("userRoleId"));
						}
						//List<Long> savedUserRoleIds = new ArrayList();	//原来保存的数据的userRoleId
						for(AdminRole adminRole : adminRoles) {
							long saveUserRoleId = adminRole.getUserRoleId();
							//savedUserRoleIds.add(saveUserRoleId);
							if(!formUserRoleIds.contains(saveUserRoleId)) {
								adminRoleService.deleteAdminRole(adminRole);
							}
						}
					}
					
		        	for (JSONObject jsonObject : departmentJsonObjects) {
			            adminInfo.setDepartmentCode(jsonObject.getString("departmentCode"));
			            adminInfo.setPositionId(jsonObject.getLong("positionId"));
			            adminInfo.setRoleId(jsonObject.getLong("roleId"));
			            
			            Long userRoleId = jsonObject.getLong("userRoleId");
			            if(userRoleId == null) {	//新加的数据
			            	adminInfo.setAdminId(adminId);
			            	adminInfoService.addAdminInfo(adminInfo);
			            }else {
			            	adminInfoService.updateRoleInfo(adminInfo,userRoleId);
			            }
			        }
		        }else {	//表单中没有选择数据
		        	if(adminRoles.size() != 0) {	//之前保存了数据，所以执行删除
		        		for(AdminRole adminRole : adminRoles) {
		        			adminRoleService.deleteAdminRole(adminRole);
		        		}
		        	}
		        	//之前无数据不做任何操作
		        }
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}
		return "success";
	}
	
	private void formatter(ModelAndView modelAndView, AdminInfo adminInfo) {
		if(adminInfo.getAdminState() == adminInfo.ENBALEADMIN_DISABLE) {
			modelAndView.addObject("adminStateName", "无效");
		}
		if(adminInfo.getAdminState() == adminInfo.ENBALEADMIN_ENABLE) {
			modelAndView.addObject("adminStateName", "有效");
		}
		if(adminInfo.getAdminState() == adminInfo.ENBALEADMIN_GONE) {
			modelAndView.addObject("adminStateName", "离职");
		}
	}
	
	/**
	 * 弹出设置密码页面
	 */
	@RequestMapping(value="/changePwd.do")
	public ModelAndView changePwd(@ModelAttribute("adminId") long adminId,
			@ModelAttribute("loginPwd") String loginPwd) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("adminId", adminId);
		modelAndView.addObject("loginPwd", loginPwd);
		return modelAndView;
	}
	
	/**
	 * 弹出修改密码页面（点击当前用户）
	 */
	@RequestMapping(value="/changeAdminPwd.do")
	public ModelAndView changeAdminPwd(@ModelAttribute("adminId") long adminId,
			@ModelAttribute("loginPwd") String loginPwd) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("adminId", adminId);
		modelAndView.addObject("loginPwd", loginPwd);
		return modelAndView;
	}
	
	/**
	 * 保存修改后的密码
	 * @param adminInfo
	 * @param loginPwdInput
	 * @return
	 */
	@RequestMapping(value="/savePwd.do", method=RequestMethod.POST)
	@ResponseBody
	public Object savePwd(@ModelAttribute("adminInfo") AdminInfo adminInfo,
			@ModelAttribute("loginPwdInput") String loginPwdInput) {
		
		if(loginPwdInput.equals("")) {
			return "empty";
		}
		
		//对密码进行加密
		MD5Key md5Key = new MD5Key();
		String md5Pwd = md5Key.getkeyBeanofStr(loginPwdInput);
		//未修改情况
		AdminInfo storeAdminInfo = adminInfoService.findById(adminInfo.getAdminId()).get(0);
		if(md5Pwd.equals(storeAdminInfo.getLoginPwd())) {
			return "none";
		}
		
		try {
			adminInfo.setLoginPwd(md5Pwd);
			adminInfoService.changePwd(adminInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
		return "success";
	}
	
	/**
	 * 保存修改后的密码(点击当前登录用户)
	 * @param adminInfo
	 * @param loginPwdInput
	 * @return
	 */
	@RequestMapping(value="/saveAdminPwd.do", method=RequestMethod.POST)
	@ResponseBody
	public Object saveAdminPwd(@ModelAttribute("adminInfo") AdminInfo adminInfo,
			@ModelAttribute("loginPwdInput") String loginPwdInput,
			@ModelAttribute("passwordAgain") String passwordAgain,
			@ModelAttribute("newPassword") String newPassword,
			HttpSession session) {
		
		if(loginPwdInput.equals("") || passwordAgain.equals("") || newPassword.equals("")) {
			return "empty";
		}
		
		//对密码进行加密
		MD5Key md5Key = new MD5Key();
		String md5Pwd = md5Key.getkeyBeanofStr(loginPwdInput);
		//判断原始密码是否正确
		AdminInfo storeAdminInfo = adminInfoService.findById(adminInfo.getAdminId()).get(0);
		if(!md5Pwd.equals(storeAdminInfo.getLoginPwd())) {
			return "wrongPwd";
		}
		
		if(!newPassword.equals(passwordAgain)) {
			return "notEqual";
		}
		
		try {
			String newPwd = md5Key.getkeyBeanofStr(newPassword);
			adminInfo.setLoginPwd(newPwd);
			adminInfoService.changePwd(adminInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return "false";
		}
		session.invalidate();
		return "success";
	}
	
	
}
