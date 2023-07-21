package com.ezen.spring.board.teampro.admin;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/admin")
@SessionAttributes("userid")
public class AdmainController {
	
	@Autowired
	@Qualifier("admindao")
	AdminDAO admindao;
	
	@Autowired
	@Qualifier("adminvo")
	AdminVO vo;

	@GetMapping("/list")
	public String getlist(Model model) {
		List<AdminVO> alist = new ArrayList<>();
		alist = admindao.getBylist();
		model.addAttribute("alist",alist);
		return "admin/Adminlist";
	}
	
	@GetMapping("/list/data")
	public String getday(@RequestParam("category") String category,
            @RequestParam("key") String key,Model model) {
		
		List<AdminVO> daylist = new ArrayList<>();
		if (category.equals("day")) {
	        // 일별 데이터 조회를 위한 SQL 쿼리 실행
			daylist = admindao.getByDay(key);
	    } else if (category.equals("month")) {
	        // 월별 데이터 조회를 위한 SQL 쿼리 실행
	    	 String year = key.substring(0, 4);
	    	 String month = key.substring(5, 7);
	    	daylist = admindao.getByMonth(year,month);
	    } else if (category.equals("year")) {
	        // 연도별 데이터 조회를 위한 SQL 쿼리 실행
	    	daylist = admindao.getByYear(key);
	    }else if (category.equals("uid")) {
	        // 연도별 데이터 조회를 위한 SQL 쿼리 실행
	    	daylist = admindao.getById(key);
	    }
		model.addAttribute("daylist",daylist);
		return "admin/Adminlist";
	}
	
}
