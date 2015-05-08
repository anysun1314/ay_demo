package com.hx.auth.controller.http;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.AdminRole;
import com.hx.auth.controller.BaseController;
import com.hx.auth.service.AdminInfoService;
import com.hx.auth.util.MD5Key;
import com.hx.auth.util.PubMethod;

/**
 * @author anyang
 * @date 2015-1-14 下午4:03:15
 */
@Controller("httpAdminInfoController")
@RequestMapping("/http/admin")
public class HttpAdminInfoController extends BaseController{
	
	@Autowired
	private AdminInfoService adminInfoService;
	
	@RequestMapping(value="/findAdminInfosByRoleCode.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosByRoleCode(@ModelAttribute("roleCode") String roleCode) {
		List<AdminInfo> adminInfos = adminInfoService.findAdminInfosByRoleCode(roleCode);
		String jsonRet = JSON.toJSONString(adminInfos);
		return jsonRet;
	}
	
	@RequestMapping(value="/findAdminInfosByAdminCode.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosByAdminCode(@ModelAttribute("adminCode") String adminCode) {
		List<AdminInfo> adminInfos = adminInfoService.findAdminInfosByAdminCode(adminCode);
		String jsonRet = JSON.toJSONString(adminInfos);
		return jsonRet;
	}
	
	@RequestMapping(value="/findAdminInfosByAdminId.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosByAdminId(@ModelAttribute("adminId") String adminId) {
		List<AdminInfo> adminInfos = adminInfoService.findById(Long.parseLong(adminId));
		String jsonRet = JSON.toJSONString(adminInfos);
		return jsonRet;
	}
	
	@RequestMapping(value="/findAdminByAdminId.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminByAdminId(@ModelAttribute("adminId") String adminId) throws Exception {
		AdminInfo adminInfo = adminInfoService.findAdminInfoById(Long.parseLong(adminId));
		adminInfo.setLoginPwd("");
		Map<String, Object> map = PubMethod.convertBean(adminInfo);
		String jsonRet = JSON.toJSONString(adminInfo);
		return jsonRet;
	}
	
	/**
	 * 根据组织机构编码和角色ID查人员
	 */
	@RequestMapping(value="/findAdminInfosByDepartmentCodeAndRoleId.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosByDepartmentCodeAndRoleId(@RequestParam("departmentCode") String departmentCode,
														  @RequestParam("roleId") String roleId) {
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("roleId", Long.parseLong(roleId));
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		List<AdminInfo> adminInfos = adminInfoService.findAdminInfosByDepartmentCodeAndRoleId(map);
		String jsonRet = JSON.toJSONString(adminInfos);
		return jsonRet;
	}
	
	/**
	 * 根据组织机构编码和角色Code查人员
	 */
	@RequestMapping(value="/findAdminInfosByDepartmentCodeAndRoleCode.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosByAdminCodeAndRoleCode(@RequestParam("departmentCode") String departmentCode,
			@RequestParam("roleCode") String roleCode) {
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("roleCode", roleCode);
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		List<AdminInfo> adminInfos = adminInfoService.findAdminInfosByDepartmentCodeAndRoleCode(map);
		String jsonRet = JSON.toJSONString(adminInfos);
		return jsonRet;
	}
	
	@RequestMapping(value="/changeAdminPwd.do", method = RequestMethod.POST)
	@ResponseBody
	public Object changeAdminPwd(@RequestParam("adminId") String adminId,
								 @RequestParam("oP") String originalPwd,
								 @RequestParam("nPTwo") String newPwd) {
		if(!PubMethod.isEmpty(adminId) && !PubMethod.isEmpty(originalPwd) && !PubMethod.isEmpty(newPwd)) {
			AdminInfo adminInfo = adminInfoService.findAdminInfoById(Long.parseLong(adminId));
			if(adminInfo != null) {
				MD5Key md5Key = new MD5Key();
				String md5OriginalPwd = md5Key.getkeyBeanofStr(originalPwd);
				if(adminInfo.getLoginPwd().equals(md5OriginalPwd)) {
					String md5NewPwd = md5Key.getkeyBeanofStr(newPwd);
					adminInfo.setLoginPwd(md5NewPwd);
					adminInfoService.changePwd(adminInfo);
					return "success";
				}else {
					return "originalPwdError";
				}
			}else {
				return "notExist";
			}
		}else {
			return "paramError";
		}
	}
	
}
