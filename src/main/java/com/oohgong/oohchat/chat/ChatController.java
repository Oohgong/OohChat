package com.oohgong.oohchat.chat;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ChatController {

	@GetMapping("/chating/{roomNum}")
	public ModelAndView chat (HttpSession session, @PathVariable String roomNum) throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.addObject("roomNum", roomNum);
		mv.setViewName("chating");
		return mv;
	}
	
}
