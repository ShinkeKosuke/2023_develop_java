package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.common.FlashData;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventUser;
import com.example.demo.service.EventUserService;

@Controller
@RequestMapping(value = "/admin/pay")
public class PayController {
	@Autowired
	EventUserService eventUserService;

	@GetMapping(value = "/{id}")
	public String form(@PathVariable Integer id, Model model) {
		try {
			model.addAttribute("eventUser", eventUserService.findById(id));
		} catch (DataNotFoundException e) {
			return "admin/events/edit";
		}
		return "admin/pay/pay";
	}


	@PostMapping(value = "/edit/{id}")
	public String update(@PathVariable Integer id, @ModelAttribute EventUser eventUser, BindingResult result, Model model, RedirectAttributes ra) {
		try {
			eventUserService.findById(id);
			// 更新
			eventUserService.save(eventUser);
			ra.addFlashAttribute("flash", new FlashData().success("支払いが完了しました"));
			return "redirect:/admin/events/view/%s".replace("%s", eventUser.getEvent().getId().toString());
		} catch (Exception e) {
			ra.addFlashAttribute("flash", new FlashData().success("処理中にエラーが発生しました"));
			return "redirect:/admin";
		}
	}
}

