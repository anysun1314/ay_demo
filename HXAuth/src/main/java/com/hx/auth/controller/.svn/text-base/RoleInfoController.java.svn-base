package com.hx.auth.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jdt.internal.compiler.ast.TrueLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.bean.RoleFunction;
import com.hx.auth.bean.RoleInfo;
import com.hx.auth.cached.AdminFunctionUrlCached;
import com.hx.auth.service.FunctionInfoService;
import com.hx.auth.service.FunctionUrlService;
import com.hx.auth.service.RoleFunctionService;
import com.hx.auth.service.RoleInfoService;
import com.hx.auth.service.RoleInfoServiceImpl;

/**
 * @author anyang
 * @date 2014-3-27 下午2:57:16
 */
@Controller
@RequestMapping("/jsp/role")
public class RoleInfoController extends BaseController{
	@Autowired
	private RoleInfoService roleInfoService;
	@Autowired
	private RoleFunctionService roleFunctionService;
	@Autowired
	private AdminFunctionUrlCached adminFunctionUrlCached;
	@Autowired
	private FunctionInfoService functionInfoService;
	@Autowired
	private FunctionUrlService functionUrlService;
	
	@RequestMapping(value = "/rolesList.do")
	public ModelAndView rolesList() {
		return new ModelAndView();
	}
	
	@RequestMapping(value="/list.do", method = RequestMethod.POST)
	@ResponseBody
	public Object rolesList(HttpServletRequest request, HttpSession session) {
		List<RoleInfo> roleInfos = roleInfoService.findAll();
		return roleInfos;
	}
	
	@RequestMapping(value="/addRole.do")
	public ModelAndView addRole() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("add", true);
		modelAndView.addObject("roleId", -1);
		modelAndView.addObject("roleCode", -1);
		return modelAndView;
	}
	
	@RequestMapping(value="/saveRole.do", method=RequestMethod.POST)
	@ResponseBody
	public Object saveRole(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("roleInfo") RoleInfo roleInfo,
			HttpServletRequest request) {
		if(addFlag) {
			List<RoleInfo> roleInfos = roleInfoService.findAll();
			List list = new ArrayList();
			for(RoleInfo roleInfoFind : roleInfos) {
				list.add(roleInfoFind.getRoleName());
			}
			if(list.contains(roleInfo.getRoleName())) {
				return "exist";
			}
			roleInfoService.addRoleInfo(roleInfo);
		}else {
			roleInfoService.updateRoleInfo(roleInfo);
		}
		
		return "success";
	}
	
	@RequestMapping(value="/editRole.do")
	public ModelAndView editRole(@ModelAttribute("roleId") long roleId) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(roleInfoService.findById(roleId));
		modelAndView.addObject("add", false);
		modelAndView.setViewName("jsp/role/addRole");
		return modelAndView;
	}
	
	@RequestMapping(value="/findRoleNames.do")
	@ResponseBody
	public Object findRolesName() {
		List<RoleInfo> roleInfos = roleInfoService.findAll();
		List roleNames = new ArrayList();
		for(int i=0; i<roleInfos.size(); i++) {
			Map map = new HashMap();
			map.put("id", roleInfos.get(i).getRoleId());
			map.put("text", roleInfos.get(i).getRoleName());
			roleNames.add(map);
		}
		return roleNames;
	}
	
	@RequestMapping(value="/saveRoleFunction.do")
	@ResponseBody
	public Object saveRoleFunction(@ModelAttribute("functionIdStr") String functionIdStr,HttpSession session,
			@ModelAttribute("roleId") long roleId) {
		//if(!"".equals(functionIdStr)) {
			try {
				RoleFunction roleFunction = new RoleFunction();
				roleFunction.setRoleId(roleId);
				roleFunctionService.addRoleFunction(roleFunction,functionIdStr);
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
			if(roleId == super.getCurrentRole().getRoleId()) {
				//adminFunctionUrlCached.reset((Long)super.getSession().getAttribute(AdminInfo.USERROLEID));
				//当前用户当前角色对应的权限集
				RoleInfo roleInfo = (RoleInfo)super.getSession().getAttribute(AdminInfo.CURRENTROLE);
				List<FunctionInfo> functionInfos = functionInfoService.findByRoleId(roleInfo.getRoleId());
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
				session.setAttribute(AdminInfo.USERFUNCTIONURL, adminUrls);
			}
			return "success";
		//}else {
		//	return "empty";
		//}
	}
}
