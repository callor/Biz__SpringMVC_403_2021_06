package com.callor.book.notuse;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@RequestMapping(value="movie")
//@Controller
public class NaverMovieController {

	@RequestMapping(value= {"/",""} , method=RequestMethod.GET)
	public String home(Model model) {
//		model.addAttribute("pHolder","영화 검색어");
		model.addAttribute("CAT","MOVIE");
		return "home";
	}
}
