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

import com.hx.auth.bean.Position;
import com.hx.auth.bean.RoleInfo;
import com.hx.auth.service.PositionService;

/**
 * @author anyang
 * @date 2014-4-21 下午7:55:16
 */
@Controller
@RequestMapping("/jsp/position")
public class PositionController extends BaseController {
	
	@Autowired
	private PositionService positionService;
	
	@RequestMapping(value = "/positionsList.do")
	public ModelAndView positionsList() {
		return new ModelAndView();
	}
	
	@RequestMapping(value="/list.do", method = RequestMethod.POST)
	@ResponseBody
	public Object positionsList(HttpServletRequest request, HttpSession session) {
		List<Position> positions = positionService.findAll();
		return positions;
	}
	
	@RequestMapping(value="/addPosition.do")
	public ModelAndView addPosition() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("add", true);
		modelAndView.addObject("positionId", -1);
		return modelAndView;
	}
	
	@RequestMapping(value="/savePosition.do", method=RequestMethod.POST)
	@ResponseBody
	public Object savePosition(@ModelAttribute("addFlag") Boolean addFlag,
			@ModelAttribute("position") Position position,
			HttpServletRequest request) {
		if(addFlag) {
			List<Position> positionInfos = positionService.findAll();
			List list = new ArrayList();
			for(Position positionInfoFind : positionInfos) {
				list.add(positionInfoFind.getPositionName());
			}
			if(list.contains(position.getPositionName())) {
				return "exist";
			}
			positionService.addPosition(position);
		}else {
			positionService.updatePosition(position);
		}
		
		return "success";
	}
	
	@RequestMapping(value="/editPosition.do")
	public ModelAndView editPosition(@ModelAttribute("positionId") long positionId) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject(positionService.findById(positionId));
		modelAndView.addObject("add", false);
		modelAndView.setViewName("jsp/position/addPosition");
		return modelAndView;
	}
	
	@RequestMapping(value="/findPositionNames.do")
	@ResponseBody
	public Object findPositionNames() {
		List<Position> positions = positionService.findAll();
		List positionNames = new ArrayList();
		for(int i=0; i<positions.size(); i++) {
			Map map = new HashMap();
			map.put("id", positions.get(i).getPositionId());
			map.put("text", positions.get(i).getPositionName());
			positionNames.add(map);
		}
		return positionNames;
	}
	
}
