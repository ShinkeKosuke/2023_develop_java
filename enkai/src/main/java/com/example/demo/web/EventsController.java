package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.example.demo.entity.User;
import com.example.demo.service.EventService;
import com.example.demo.service.EventUserService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping(value = { "/", "/admin", "/admin/events" })
public class EventsController {
	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	@Autowired
	EventUserService eventUserService;

	/*
	 * 画面表示
	 */

	@GetMapping
	public String list(Model model) {
		// 全件取得
		model.addAttribute("events", eventService.findAllUserCount());
		return "events/list";
	}

	@GetMapping(value = "/mylist")
	public String mylist(Model model) {
		try {
			model.addAttribute("events", eventService.findMyEventAllUserCount(userService.findLoginUser().getId()));
			return "admin/events/mylist";
		} catch (DataNotFoundException e) {
			return "admin";
		}
	}

	@GetMapping(value = "/view/{id}")
	public String view(@PathVariable Integer id, Model model) {
		try {
			User user = userService.findLoginUser();
			Event event = eventService.findById(id);
			model.addAttribute("events", event);
			model.addAttribute("eventUsers", eventUserService.findByEvent(eventService.findById(id)));
			model.addAttribute("participationStatus", eventUserService.findByUserAndEvent(user, event));

		} catch (DataNotFoundException e) {
			return "redirect:/admin";
		}
		return "admin/events/view";
	}

	@GetMapping(value = "/create")
	public String form(@ModelAttribute Event event, Model model) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("login_user", email);
		return "admin/events/create";
	}

	@PostMapping(value = "/create")
	public String register(@Validated @ModelAttribute Event event, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/events/create";
			}
			eventService.save(event);
			flash = new FlashData().success("イベントの登録が完了しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin";
	}

	@GetMapping(value = "/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		try {
			model.addAttribute("event", eventService.findById(id));
		} catch (DataNotFoundException e) {
			return "redirect:/admin";
		}
		return "admin/events/edit";
	}

	@PostMapping(value = "/edit/{id}")
	public String update(@PathVariable Integer id, @Validated Event event, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/events/edit";
			}
			eventService.findById(id);
			// 更新
			eventService.save(event);
			flash = new FlashData().success("イベントの更新が完了しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin";
	}

	@GetMapping(value = "/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		FlashData flash;
		try {
			eventService.findById(id);
			eventService.deleteById(id);
			flash = new FlashData().success("イベントの削除が完了しました");
		} catch (DataNotFoundException e) {
			flash = new FlashData().danger("該当データがありません");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin";
	}
}
