package com.hx.auth.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.bean.FunctionInfo;
import com.hx.auth.bean.FunctionUrl;
import com.hx.auth.bean.OptionLog;
import com.hx.auth.cached.OptionInfoAndNavTitleCached;
import com.hx.auth.service.AdminInfoService;
import com.hx.auth.service.FunctionInfoService;
import com.hx.auth.service.FunctionUrlService;
import com.hx.auth.service.OptionLogService;
//import com.hx.auth.bean.FeesItem;
//import com.hx.auth.bean.LendOrder;
//import com.hx.auth.bean.LendProduct;
//import com.hx.auth.bean.LoanApplication;
//import com.hx.auth.bean.LoanProduct;
//import com.hx.service.FeesItemService;
//import com.hx.service.LendOrderService;
//import com.hx.service.LendProductService;
//import com.hx.service.LoanApplicationService;
//import com.hx.service.LoanProductService;
//import com.hx.auth.customer.bean.CustomerInfo;
//import com.hx.customer.service.CustomerInfoService;

/**
 * @author anyang
 * @date 2014-5-22 下午3:15:00
 */
public class OptionLogInterceptor implements HandlerInterceptor {

	@Autowired
	private OptionLogService optionLogService;
	@Autowired
	private AdminInfoService adminInfoService;
//	@Autowired
//	private LoanApplicationService loanApplicationService;
//	@Autowired
//	private CustomerInfoService customerInfoService;
//	@Autowired
//	private LendOrderService lendOrderService;
	@Autowired
	private OptionInfoAndNavTitleCached optionInfoAndNavTitleCached;
//	@Autowired
//	private FeesItemService feesItemService;
//	@Autowired
//	private LoanProductService loanProductService;
//	@Autowired
//	private LendProductService lendProductService;
	
	public List<String> allows;	//放行指定内容,在拦截器配置文件中配置

	public void setAllows(List<String> allows) {
		this.allows = allows;
	}

	/**
	 * 调用controller中的方法之前拦截
	 * 记录操作日志
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");   //当前访问的url(不带项目路径及参数) 
		//System.out.println(requestUrl);  
		Map<String, String[]> params = request.getParameterMap();	//请求中的参数
		/*for(Entry<String, String[]> entry : params.entrySet()) {
			System.out.println(entry.getKey() + "---" + entry.getValue()[0]);
		}*/
		
	    //js、css等直接放行
        if(null != allows && allows.size()>=1)  
        for(String url : allows) {
        	//int j = requestUrl.indexOf(url);
            if(requestUrl.indexOf(url) != -1) {    
                return true;    
            }    
        }  
		
		//记录操作日志
		OptionLog optionLog = new OptionLog();
		String ip = this.getIpAddr(request);
		optionLog.setLoginIP(ip);				//登录IP
		optionLog.setOptionTime(new Date());	//操作时间
		
		String userRoleIdStr = AdminInfo.USERROLEID;
		long userRoleId = (request.getSession().getAttribute(userRoleIdStr) != null ? (Long) request.getSession().getAttribute(userRoleIdStr) : 0L);
		
		Map<String, String> optionInfoMap = optionInfoAndNavTitleCached.getOptionInfo();
		if(optionInfoMap.containsKey(requestUrl)) {			//如果当前链接已做记录
			String optInfo = optionInfoMap.get(requestUrl);	//获取当前链接对应的信息
			if(userRoleId != 0) {
				optionLog.setUserRoleId(userRoleId);		//员工角色ID
				String logInfo = this.getLogInfo(optInfo, params);	//操作内容
				optionLog.setLogInfo(logInfo);
			}else {
				if(("/login.do").equals(requestUrl)) {	//系统登录	 (path + "login.do").equals(currentUrl)
					if(params.containsKey("loginName") && params.get("loginName") != null && params.get("loginName").length > 0) {
						String loginName = params.get("loginName")[0];
						List<AdminInfo> adminInfos = adminInfoService.findByLoginName(loginName);
						if(adminInfos.size() > 0) {		//用户存在的话就记录用户信息
							AdminInfo adminInfo = new AdminInfo();
							adminInfo = adminInfos.get(0);
							String currentDepartCode = adminInfo.getDepartmentCode();
							long currentUserRoleId = adminInfo.getUserRoleId();
							for(AdminInfo adminInfoSaved : adminInfos) {
								if(adminInfoSaved.getDepartmentCode().length() < currentDepartCode.length()) {
									currentDepartCode = adminInfoSaved.getDepartmentCode();
									currentUserRoleId = adminInfoSaved.getUserRoleId();
								}
							}
							optionLog.setUserRoleId(currentUserRoleId);
						}
					}
					optionLog.setLogInfo(optInfo);
				}else {
					optionLog.setLogInfo("session timeout!");
				}
			}
			
			optionLogService.addOptionLog(optionLog);
		}
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
	/**
	 * 获得当前访问ip地址
	 * @param request
	 * @return ip
	 */
    private String getIpAddr(HttpServletRequest request) { 
    	
    	String ipAddress = null;
        //ipAddress = request.getRemoteAddr();
        ipAddress = request.getHeader("x-forwarded-for");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
         ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
        	ipAddress = request.getRemoteAddr();
        	if(ipAddress.equals("127.0.0.1") || "0:0:0:0:0:0:0:1".equals(ipAddress)){
	          	//根据网卡取本机配置的IP(获取应用服务器地址)
	          	InetAddress inet=null;
		        try {
		        	inet = InetAddress.getLocalHost();
		        } catch (UnknownHostException e) {
		        	e.printStackTrace();
		        }
		        ipAddress= inet.getHostAddress().toString();
         	}
        }

        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress; 
    }

    /**
     * 根据url判断操作内容,对操作内容进行相关记录、描述、备注
     * （注：系统新增url后，如需对该url返回描述，需在此处进行判断，并配置相应返回信息）
     * @param optInfo
     * @param params
     * @return logInfo
     */
	private String getLogInfo(String optInfo, Map<String, String[]> params) {
		
		StringBuffer sBuffer = new StringBuffer();
		String optName = "";
		String optDesc = "";
		
		if(optInfo != null && !"".equals(optInfo)) {
			String[] optInfos = optInfo.split(",");
			optName = optInfos[0];	//操作名
			//有参数通过参数判断
			if(optInfos.length > 1) {
				for(int i=1; i<optInfos.length; i+=2) {
					String paramName = optInfos[i+1]; //参数名
					String paramOptName = optInfos[i];//参数含义
					
					if(params.containsKey(paramName)) {
						String paramValue = params.get(paramName)[0];	//获取url中参数值
						
						//根据参数判断操作
						if("addFlag".equals(paramName)) {
							if("false".equals(paramValue)) {
								optName = paramOptName;
							}
						}
						
						/*//借款合同
						if("loanApplicationId".equals(paramName)) {
							LoanApplication loanApplication = loanApplicationService.findById(Long.parseLong(paramValue));
							if(loanApplication != null) {
								optDesc = optInfos[i] + " : " + loanApplication.getAgreementCode();
							}
						//客户
						}else if("customerId".equals(paramName)) {
							CustomerInfo customerInfo = customerInfoService.findById(Long.parseLong(paramValue));
							if(customerInfo != null) {
								optDesc = "客户姓名  : " + customerInfo.getTrueName() + "&nbsp&nbsp&nbsp证件  : " + customerInfo.getCertificatesCode();//optInfos[i] + ":" + 
							}
						//出借合同
						}else if("lendOrderId".equals(paramName)) {
							LendOrder lendOrder = lendOrderService.findById(Long.parseLong(paramValue));
							if(lendOrder != null) {
								optDesc = optInfos[i] + " : " + lendOrder.getOrderCode();
							}
						//费用
						}else if("feesItemId".equals(paramName)) {
							FeesItem feesItem = feesItemService.findById(Long.parseLong(paramValue));
							if(feesItem != null) {
								optDesc = optInfos[i] + " : " + feesItem.getItemName();
							}
						//借款产品
						}else if("loanProductId".equals(paramName)) {
							LoanProduct loanProduct = loanProductService.findById(Long.parseLong(paramValue));
							if(loanProduct != null) {
								optDesc = optInfos[i] + " : " + loanProduct.getProductName();
							}
						//出借产品
						}else if("lendProductId".equals(paramName)) {
							LendProduct lendProduct = lendProductService.findById(Long.parseLong(paramValue));
							if(lendProduct != null) {
								optDesc = optInfos[i] + " : " + lendProduct.getProductName();
							}
						//组织机构
						}else */
						
						if("departmentCode".equals(paramName) && params.containsKey("addFlag") && "false".equals(params.get("addFlag")[0])) {
							if(paramValue != null && !"".equals(paramValue)) {
								optDesc = optInfos[i] + " : " + paramValue;
							}
						}else if("editingId".equals(paramName)) {
							if(paramValue != null && !"".equals(paramValue)) {
								optDesc = optInfos[i] + " : " + paramValue;
							}
						//员工
						}else if("adminId".equals(paramName)) {
							List<AdminInfo> adminInfos = adminInfoService.findById(Long.parseLong(paramValue));
							if(adminInfos != null && adminInfos.size() > 0) {
								optDesc = optInfos[i] + " : " + adminInfos.get(0).getDisplayName(); 
							}
						}
					}
				}
			}
			
		}
		
		if(!"".equals(optName)) {
			sBuffer.append(optName + "/r/n");//"操作名称：" + 
			if(!"".equals(optDesc)) {
				sBuffer.append(optDesc + "/r/n");//"操作描述：" + 
			}
			return sBuffer.toString();
		}
		
		return "no reference url";
	}
	
}
