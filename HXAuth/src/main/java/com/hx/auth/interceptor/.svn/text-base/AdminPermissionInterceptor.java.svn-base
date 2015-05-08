package com.hx.auth.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hx.auth.bean.AdminInfo;
import com.hx.auth.cached.AdminFunctionUrlCached;
import com.hx.auth.cached.FunctionUrlCached;

/**
 * @author anyang
 * @date 2014-5-26 上午9:36:36
 */
public class AdminPermissionInterceptor implements HandlerInterceptor {

	@Autowired
	private FunctionUrlCached functionUrlCached;
	@Autowired
	private AdminFunctionUrlCached adminFunctionUrlCached;
	
	public List<String> allows;
	
	public void setAllows(List<String> allows) {
		this.allows = allows;
	}
	
	/**
	 * 权限判断，是否有权限执行拦截到的url
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		//js、css直接放行
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");    
		if(null != allows && allows.size()>=1) { 
			for(String url : allows) {    
				if(requestUrl.contains(url)) {    
					return true;    
				}    
			}
		}
		
		//判断用户，即session是否存在
		long currentRoleId = (request.getSession().getAttribute(AdminInfo.USERROLEID) != null ? (Long) request.getSession().getAttribute(AdminInfo.USERROLEID) : 0L);
		AdminInfo adminInfo = (AdminInfo) request.getSession().getAttribute(AdminInfo.LOGINUSER);
		if(adminInfo != null && currentRoleId != 0) {
			List<String> allUrls = functionUrlCached.getAllUrl();
			//判断当前url是否需要拦截
			if(!allUrls.isEmpty() && allUrls.contains(requestUrl)) {
					List<String> adminUrls = (List<String>) request.getSession().getAttribute(AdminInfo.USERFUNCTIONURL);	//adminFunctionUrlCached.getAdminUrls(currentRoleId)
					//判断当前用户是否有权限访问
					if(!adminUrls.contains(requestUrl)) {
						if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){  
				            response.setStatus(912);//912：自定义码，表示noAccess
				    	}else {
				    		String path = request.getContextPath() + "/noAccess.do?view=ture";
				    		//request.getRequestDispatcher(path).forward(request, response);
				    		response.sendRedirect(path);
				    	}
						return false;
					}
			}
			return true;
		}else {	
			request.getSession().invalidate();
			//针对ajax方式
			if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")){  
	            response.setStatus(911);//911：自定义码，表示session timeout
	    	}else {
	    		String path = request.getContextPath() + "/sessionTimeout.do?timeout=true";
	    		response.sendRedirect(path);
	    	}
			
			return false;
		}
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
	
}
