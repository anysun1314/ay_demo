package com.hx.auth.controller.http;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hx.auth.bean.RoleInfo;
import com.hx.auth.controller.BaseController;
import com.hx.auth.service.RoleInfoService;

/**
 * @author anyang
 * @date 2015-1-14 下午3:56:08
 */
@Controller("httpRoleInfoController")
@RequestMapping("/http/role")
public class HttpRoleInfoController extends BaseController{
	
	@Autowired
	private RoleInfoService roleInfoService;
	
	@RequestMapping(value="/roleList.do", method = RequestMethod.POST)
	@ResponseBody
	public Object rolesList() {
		List<RoleInfo> roleInfos = roleInfoService.findAll();
		String jsonRet = JSON.toJSONString(roleInfos);
		return jsonRet;
	}
	
}
