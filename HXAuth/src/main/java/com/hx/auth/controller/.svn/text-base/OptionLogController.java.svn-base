package com.hx.auth.controller;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hx.auth.bean.OptionLog;
import com.hx.auth.service.OptionLogService;
import com.hx.auth.util.DateUtil;

/**
 * @author anyang
 * @date 2014-5-29 下午8:51:28
 */
@Controller
@RequestMapping("/jsp/optionLog")
public class OptionLogController extends BaseController {
	
	@Autowired
	private OptionLogService optionLogService;
	
	@RequestMapping(value = "/optionLogsList.do")
	public ModelAndView optionLogsList() {
		return new ModelAndView();
	}
	
	@RequestMapping(value = "/listByPage.do", method = RequestMethod.POST)
    @ResponseBody
    public Object adminInfosByPage(HttpServletRequest request, HttpSession session,
    		@ModelAttribute("optionLog") OptionLog optionLog) throws Exception {
        int pageSize = Integer.parseInt(request.getParameter("rows"));
        int pageNo = Integer.parseInt(request.getParameter("page"));
        return optionLogService.findAllByPage(pageNo, pageSize, optionLog);
    }
}
