package com.callor.gallery.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.callor.gallery.model.GalleryDTO;
import com.callor.gallery.model.GalleryFilesDTO;
import com.callor.gallery.service.GalleryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@RequiredArgsConstructor
@Slf4j
@Controller
@RequestMapping(value="/gallery")
public class GalleryController {
	
	protected final GalleryService gaService;
	
	/*
	 * 주소창에 직접 입력후 Enter 로 요청할 때 Request를 처리
	 * 		http://localhost:8080/rootPath/gallery/dumy
	 * 
	 * a tag 를 클릭했을때
	 * 		<a href="${rootPath}/gallery/dumy>열기</a> 
	 * 
	 * JS
	 * 		location.href="${rootPath}/gallery/dumy" 가 실행됬을때
	 */
	@RequestMapping(value="/dumy",method=RequestMethod.GET)
	public String dumy() {
		return "home";
	}

	/*
	 * <form action="${rootPath}/dumy" method="POST">
	 * 		<input name="str">
	 * 		<button type="submit">전송</button>
	 * </form>
	 */
	@RequestMapping(value="/dumy",method=RequestMethod.POST)
	public String dumy(String str) {
		return "home";
	}
	
	// localhost:8080/rootPath/gallery/ 또는
	// localhost:8080/rootPath/gallery 로 요청했을 때
	@RequestMapping(value={"/", ""},method=RequestMethod.GET)
	public String list(Model model) throws Exception {
		
		List<GalleryDTO> gaList = gaService.selectAll();
		model.addAttribute("GALLERYS",gaList);
		model.addAttribute("BODY","GA-LIST");
		return "home";
		
	}
	
	
	@RequestMapping(value="/input",method=RequestMethod.GET)
	public String input(Model model) {
		
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat st = new SimpleDateFormat("hh:mm:ss");
		
		String curDate = sd.format(date);
		String curTime = st.format(date);
		
		GalleryDTO gaDTO = GalleryDTO.builder()
							.g_date(curDate)
							.g_time(curTime)
							.g_writer("callor")
							.build();
		
		model.addAttribute("CMD",gaDTO);
		model.addAttribute("BODY","GA-INPUT");
		return "home";
	
	}
	
	@RequestMapping(value="/input",method=RequestMethod.POST)
	public String input(
			GalleryDTO gaDTO, 
			MultipartFile one_file,
			MultipartHttpServletRequest m_file,
			Model model) throws Exception {

		log.debug("겔러리 정보 {}", gaDTO.toString());
		log.debug("싱글 파일 {}", one_file.getOriginalFilename());
		log.debug("멀티파일 {}", m_file.getFileMap().toString());
		
		gaService.input(gaDTO, one_file, m_file);
		
		return "redirect:/gallery";
	}
	
	@RequestMapping(value="/detail/{seq}",method=RequestMethod.GET)
	public String detail(
			@PathVariable("seq") String seq, Model model) {

		Long g_seq = 0L;
		try {
			g_seq = Long.valueOf(seq);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return "redirect:/gallery";
		}
		
		List<GalleryFilesDTO> gfList 
			= gaService.findByIdGalleryFiles(g_seq);
		model.addAttribute("GFLIST",gfList);
		model.addAttribute("BODY","GA-DETAIL");
		return "home";
		
	}
	
	
	
	
	
}
