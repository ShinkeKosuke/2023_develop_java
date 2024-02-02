package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.dao.ChatJsonDao;
import com.example.demo.entity.Chat;
import com.example.demo.entity.Event;
import com.example.demo.service.ChatService;
import com.example.demo.service.EventService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping(value = "/admin/chats")
public class ChatsController {
	@Autowired
	EventService eventService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ChatService chatService;

	@GetMapping(value = "/talk/{id}")
	public String talk(@PathVariable Integer id, Model model, @ModelAttribute Chat chat) {
		try {
			Event event = eventService.findById(id);
			model.addAttribute("event", eventService.findById(id));
			model.addAttribute("chats", chatService.findByEvent(event));
			return "admin/chats/talk";
		} catch (DataNotFoundException e) {
			return "admin";
		}
	}
	

	@PostMapping(value = "/create")
	public String register(@RequestBody ChatJsonDao request) {
		try {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			Integer eventId = request.getEventId();
			chatService.save(new Chat(null, userService.findByEmail(email), eventService.findById(eventId), request.getMessage()));
			return "redirect:/admin/chats/talk/%s".replace("%s", eventId.toString());
		} catch (Exception e) {
		}
		return "redirect:/admin";
	}
}

