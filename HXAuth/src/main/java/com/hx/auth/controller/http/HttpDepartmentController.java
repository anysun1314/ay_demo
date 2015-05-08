package com.hx.auth.controller.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.AdminRole;
import com.hx.auth.bean.Department;
import com.hx.auth.controller.BaseController;
import com.hx.auth.service.AdminInfoService;
import com.hx.auth.service.DepartmentService;
import com.hx.auth.util.JSONUtil;

/**
 * @author anyang
 * @date 2015-1-15 下午1:07:36
 */
@Controller("httpDepartmentController")
@RequestMapping("/http/department")
public class HttpDepartmentController extends BaseController {
	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private AdminInfoService adminInfoService;
	
	@RequestMapping(value="/findAdminInfosByDepartmentCode.do", method = RequestMethod.POST)
	@ResponseBody
	//机构下关联的人员
	public Object findAdminInfosByDepartmentCode(@RequestParam("departmentCode") String departmentCode) {
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		List<AdminInfo> adminInfos = adminInfoService.findByDepartmentCodeConcat(map);
		String jsonRet = JSON.toJSONString(adminInfos);
		return jsonRet;
	}
	
	/**
	 * For bpm
	 * 通过组织机构Code,查询组织机构树上相应的人员信息
	 * @param departmentCode
	 * @return json格式数据：
	 * [
		    {
		        "text": "信贷部客服组",
		        "state":{"opened":"true"},
		        "classes": "important",
		        "children":
		            [
		                {
		                    "text": "客服组",
		                    "state":{"opened":"true"},
		                    "children":[
		                        {
		                            "text":"刘莉莉 [客户经理]",
		                            "id":"01"
		                        },
		                        {
		                            "text":"陈艳辉 [客户经理]",
		                            "id":"02"
		                        },
		                        {
		                            "text":"于伟山 [客户经理]",
		                            "id":"03"
		                        },
		                        {
		                            "text":"马文娜 [客户经理]",
		                            "id":"04"
		                        },
		                        {
		                            "text":"黄红霞 [客户经理]",
		                            "id":"05"
		                        },
		                        {
		                            "text":"王婧 [客户经理]",
		                            "id":"06"
		                        },
		                        {
		                            "text":"Jack [客户经理]",
		                            "id":"07"
		                        },
		                        {
		                            "text":"TOM [客户经理]",
		                            "id":"08"
		                        }
		                    ]
		                }
		
		            ]
		    }
		]
	 */
	@RequestMapping(value="/findAdminInfosFromDeptTreeByDeptCode.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosFromDeptTreeByDeptCode(@RequestParam("departmentCode") String departmentCode) {
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		String jsonRet = departmentService.findAdminInfosFromDeptTreeByDeptCode(map);
		return jsonRet;
	}
	
	/**
	 * For bpm
	 * 通过组织机构Code,查询组织机构树上相应的人员信息
	 * @param departmentCode[部门号],isScreen[是否筛选],condition[筛选条件]
	 * @return json格式数据
	 * @throws Exception 
	 */
	@RequestMapping(value="/findAdminInfosFromDeptTreeByDeptCodeAndScreenCondition.do", method = RequestMethod.POST)
	@ResponseBody
	public Object findAdminInfosFromDeptTreeByDeptCodeAndScreenCondition(@RequestParam("departmentCode") String departmentCode,
																		 @RequestParam("isScreen") String isScreenStr,
																		 @RequestParam("condition") String conditionListStr) throws Exception {
		Boolean isScreen = Boolean.parseBoolean(isScreenStr);
		List<String> conditionList = JSONUtil.getListFormJSON(conditionListStr);
		
		Map map = new HashMap();
		map.put("departmentCode", departmentCode);
		map.put("delState", AdminRole.DELSTATE_UNDELETED);
		map.put("isScreen", isScreen);
		map.put("condition", conditionList);
		String jsonRet = departmentService.findAdminInfosFromDeptTreeByDeptCodeAndScreenCondition(map);
		return jsonRet;
	}
	
	@RequestMapping(value="/findBelongsDepartByCrtDepartmentCode.do", method = RequestMethod.POST)
	@ResponseBody
	//查询门店组人员所在门店
	public Object findBelongsDepartByCrtDepartmentCode(@RequestParam("departmentCode") String departmentCode) {
		String rstJson;
		try {
			rstJson = departmentService.findBelongsDepartByCrtDepartmentCode(departmentCode);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return rstJson;
	}
	
	@RequestMapping(value="/findAllOutletDepartments.do", method = RequestMethod.POST)
	@ResponseBody
	//根据节点类型查询所有部门
	public Object findAllOutletDepartments() {
		List<Department> departments = departmentService.findDepartmentByNodeType(Department.NODETYPE_BPM_OUTLET_CHILD);
		String rstJson = JSON.toJSONString(departments);
		return rstJson;
	}

	/**
	 * 根据父级节点code查询，返回下属子节点、孙节点集合
	 * @param departmentCode
	 * @return
	 */
    @RequestMapping(value="/findChildrenByDepartment.do", method = RequestMethod.POST)
    @ResponseBody
    public Object findChildrenByDepartment(@RequestParam("departmentCode") String departmentCode) {
        List<Department> allChildByCode = departmentService.findChildDepartments(departmentCode, Department.DELSTATE_UNDELETED);
        if (allChildByCode != null) {
            return allChildByCode;
        } else {
            return new ArrayList<Department>();
        }
    }
	
    /**
     * 判断当前人员属于[门店]还是[信审中心]
     * @param adminId
     * @return	0[门店]	1[信审中心]
     * @throws IOException 
     * @throws NumberFormatException 
     */
    @RequestMapping(value="/belongToStoreOrCredit.do", method = RequestMethod.POST)
    @ResponseBody
    public Object belongToStoreOrCredit(@RequestParam("adminId") String adminId) throws NumberFormatException, IOException {
    	int rst = departmentService.belongToStoreOrCredit(Long.parseLong(adminId));
    	return rst;
    }
	
}
