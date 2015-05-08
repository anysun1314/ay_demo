package com.hx.auth.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.bean.RoleFunction;
import com.hx.auth.cached.FunctionUrlCached;
import com.hx.auth.service.FunctionInfoService;
import com.hx.auth.service.FunctionUrlService;
import com.hx.auth.service.RoleFunctionService;
import com.hx.auth.util.JSONUtil;

/**
 * @author anyang
 * @date 2014-5-13 上午11:54:21
 */
@Controller
@RequestMapping("/jsp/function")
public class FuntionController {
	
	@Autowired
	private FunctionInfoService functionInfoService;
	
	@Autowired
	private FunctionUrlService functionUrlService;
	
	@Autowired
	private RoleFunctionService roleFunctionService;
	
	@Autowired
	private FunctionUrlCached functionUrlCached;
	
	@RequestMapping("/functionsList.do")
	public ModelAndView functionsList() {
		return new ModelAndView();
	}
	
	@RequestMapping(value="/addFunction.do")
	public ModelAndView addFunction(@RequestParam("editingId") long editingId,
		@RequestParam("editingPid") long editingPid,
		@RequestParam("flag") String flag) {
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("add", true);
		modelAndView.addObject("flag", flag);
		modelAndView.addObject("functionId", editingId);
		modelAndView.addObject("pFunctionId", editingPid);
		modelAndView.addObject("functionCode", functionInfoService.findById(editingId).getFunctionCode());
		
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(editingId != 0) {
			childName = functionInfoService.findById(editingId).getFunctionName();
			if(editingPid != 0) {
				parentName = functionInfoService.findById(editingPid).getFunctionName();
			}
		}
		modelAndView.addObject("childName", childName);
		modelAndView.addObject("parentName", parentName);
		
		return modelAndView;
	}
	
	@RequestMapping(value="/saveFunction.do", method=RequestMethod.POST)
	@ResponseBody
	public Object saveFunction(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("flag") String flag,
			@ModelAttribute("Url") String Url,
			@ModelAttribute("functionInfo") FunctionInfo functionInfo) {
		
		Map map = new HashMap();
		
		if("".equals(functionInfo.getFunctionName())) {
			map.put("name", "null");
			return map;
		}
		
		//权限Url
		List<JSONObject> urlJsonObjects = JSONUtil.getListFromPageJSON(Url);
		if(!urlJsonObjects.isEmpty()) {
			//Url是否重复
			List urlInfos = new ArrayList();
			for (JSONObject jsonObject : urlJsonObjects) {
				urlInfos.add(jsonObject.getString("urlInfo"));
			}
			
			for (int i = 0; i < urlInfos.size() - 1; i++) {
		         for (int j = urlInfos.size() - 1; j > i; j--) {
		              if (urlInfos.get(j).equals(urlInfos.get(i))) {
		            	  map.put("equal", true);
		            	  return map;
		              }
		        }
		    }
		}
		
		String functionCode = functionInfo.getFunctionCode();
		long functionId = functionInfo.getFunctionId();
		long pFunctionId = functionInfo.getpFunctionId();
		
		if("".equals(pFunctionId)) {	//添加第一个节点时
			pFunctionId = 0L;
		}
		
		long pId = 0;
		if("1".equals(flag)) {
			pId = pFunctionId;
		}else if("2".equals(flag)) {
			pId = functionId;
		}
		
		if(addFlag) {
			List<FunctionInfo> functionInfos = new ArrayList();
			//根据当前id，判断是否有子节点，并构造新的子节点code
			//if(pId != 0) {
			functionInfos = functionInfoService.findByPid(pId);
			//}
			String newCode;
			if(functionInfos.size() > 0) {
				List<Long> childCodes = new ArrayList();
				for(int i=0; i<=functionInfos.size()-1; i++) {
					childCodes.add(Long.parseLong(functionInfos.get(i).getFunctionCode()));
				}
				Collections.sort(childCodes);
				
				long lastChildCode = childCodes.get(childCodes.size()-1);	//得到子节点code最后一位的code
				long newCodeLong = lastChildCode+1;
				newCode = "0" + newCodeLong;
			}else {
				newCode = functionCode + "01";
			}
			
			functionInfo.setFunctionCode(newCode);
			functionInfo.setpFunctionId(pId);
			try {
				functionInfoService.addFunctionInfo(functionInfo,urlJsonObjects);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", true);
				return map;
			}
			functionUrlCached.reset();
			map.put("success", true);
			map.put("newId", functionInfoService.findByCode(functionInfo.getFunctionCode()).getFunctionId());
		}else {
			//更新权限信息
			try {
				functionInfoService.updateFunctionInfo(functionInfo,urlJsonObjects);
			} catch (Exception e) {
				e.printStackTrace();
				map.put("error", true);
				return map;
			}
			functionUrlCached.reset();
			map.put("success", true);
		}
		return map;
	}
	
	@RequestMapping(value="/showFunction.do")
	public ModelAndView showFunction(@RequestParam("functionId") long functionId) {
		ModelAndView modelAndView = new ModelAndView();
		FunctionInfo functionInfo = functionInfoService.findById(functionId);
		
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(functionId != 0) {
			childName = functionInfo.getFunctionName();
			if(functionInfo.getpFunctionId() != 0) {
				parentName = functionInfoService.findById(functionInfo.getpFunctionId()).getFunctionName();
			}
		}
		modelAndView.addObject("childName", childName);
		modelAndView.addObject("parentName", parentName);
		
		modelAndView.addObject(functionInfo);
		modelAndView.setViewName("jsp/function/showFunction");
		return modelAndView;
	}
	
	@RequestMapping(value="/editFunction.do")
	public Object editfunction(@RequestParam("editingId") long editingId) {
		ModelAndView modelAndView = new ModelAndView();
		FunctionInfo functionInfo = functionInfoService.findById(editingId);
		//显示节点信息用
		String childName = "";
		String parentName = "";
		if(editingId != 0) {
			childName = functionInfo.getFunctionName();
			if(functionInfo.getpFunctionId() != 0) {
				parentName = functionInfoService.findById(functionInfo.getpFunctionId()).getFunctionName();
			}
		}
		modelAndView.addObject("childName", childName);
		modelAndView.addObject("parentName", parentName);
		
		modelAndView.addObject(functionInfo);
		modelAndView.addObject("add", false);
		modelAndView.addObject("flag", "");
		modelAndView.addObject("functionCode", functionInfo.getFunctionCode());
		modelAndView.addObject("functionId", functionInfo.getFunctionId());
		modelAndView.addObject("pFunctionId", functionInfo.getpFunctionId());
		modelAndView.setViewName("jsp/function/addFunction");
		return modelAndView;
	}
	
	@RequestMapping("/list.do")
	@ResponseBody
	public Object functionsTree() {
		List<FunctionInfo> functionInfos = functionInfoService.findAll();
		
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(FunctionInfo functionInfo : functionInfos) {
			String id = functionInfo.getFunctionId() + "";
			String text = functionInfo.getFunctionName();
			String parentId = functionInfo.getpFunctionId() + "";
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(functionInfo.getpFunctionId() != 0) {
					map.put("_parentId", parentId);
				}
				list.add(map);
		}
		pMap.put("rows",list);
		return pMap;
	}
	
	@RequestMapping(value="/showUrls.do")
	@ResponseBody
	public Object showUrls(@ModelAttribute("functionId") long functionId) {
		List<FunctionUrl> functionUrls = functionUrlService.findByFunctionId(functionId);
		return functionUrls;
	}
	
	@RequestMapping("/deleteFunction.do")
	@ResponseBody
	public Object deleteFunction(@ModelAttribute("editingId") long editingId) {
		FunctionInfo functionInfo = new FunctionInfo();
		functionInfo.setFunctionId(editingId);
		
		return functionInfoService.deleteFunction(functionInfo);
	}
	
	/**
	 * 用于转到显示，并选择权限节点页面
	 */
	@RequestMapping("/showFunctionTree.do")
	@ResponseBody
	public Object showFunctionTree(@ModelAttribute("roleId") long roleId,
			@ModelAttribute("roleName") String roleName){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("roleId", roleId);
		modelAndView.addObject("roleName", roleName);
		
		//将角色已有的权限传到页面选中
		List<RoleFunction> roleFunctions = roleFunctionService.findByRoleId(roleId);
		StringBuffer sBuffer = new StringBuffer();
		for(int i=0; i<roleFunctions.size(); i++) {
			sBuffer.append(roleFunctions.get(i).getFunctionId());
			if(i != roleFunctions.size() - 1) {
				sBuffer.append("-");
			}
		}
		String functionIds = sBuffer.toString();
		modelAndView.addObject("functionIds", functionIds);
		return modelAndView;
	}
	
	/**
	 * 显示权限树，添加默认选中
	 */
	@RequestMapping("/functionTree.do")
	@ResponseBody
	public Object functionTree(@ModelAttribute("roleId") long roleId) {
		List<FunctionInfo> functionInfos = functionInfoService.findAll();
		
		//封装该角色对应的functionId
		List<RoleFunction> roleFunctions = roleFunctionService.findByRoleId(roleId);
		List<Long> functionIds = new ArrayList<Long>();
		for(RoleFunction roleFunction : roleFunctions) {
			functionIds.add(roleFunction.getFunctionId());
		}
		
		Map pMap = new HashMap();
		List list = new ArrayList();
		for(FunctionInfo functionInfo : functionInfos) {
			String id = functionInfo.getFunctionId() + "";
			String text = functionInfo.getFunctionName();
			String parentId = functionInfo.getpFunctionId() + "";
				Map map = new HashMap();
				map.put("id", id);
				map.put("name", text);
				if(functionInfo.getpFunctionId() != 0) {
					map.put("_parentId", parentId);
				}
				/*if(functionIds.contains(id)) {	//默认情况下需要关闭的节点
					map.put("state", "closed");
				}*/
				list.add(map);
		}
		pMap.put("rows",list);
		return pMap;
	}
}
