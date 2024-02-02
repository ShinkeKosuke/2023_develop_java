package com.example.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.common.FlashData;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventUser;
import com.example.demo.entity.User;
import com.example.demo.service.EventService;
import com.example.demo.service.EventUserService;
import com.example.demo.service.UserService;

@Controller
@RequestMapping(value = "/admin/eventusers")
public class EventUsersController {
	@Autowired
	EventService eventService;

	@Autowired
	UserService userService;

	@Autowired
	EventUserService eventUserService;

	@GetMapping(value = "/create/{id}")
	public String register(@PathVariable Integer id, RedirectAttributes ra) {
		FlashData flash;

		try {
			// ユーザとイベントを取得
			User user = userService.findLoginUser();
			Event event = eventService.findById(id);
			
			if(eventUserService.findByUserAndEvent(user, event) == null) {
				// 対象のイベントにユーザが不参加か確認し不参加の場合登録
				eventUserService.save(new EventUser(null, event, user));
			}
			
			flash = new FlashData().success("イベントの登録が完了しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/events/view/%s".replace("%s", id.toString());
	}


	@GetMapping(value = "/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		FlashData flash;
		
		try {
			// イベントを取得
			Event event = eventService.findById(id);
			User user = userService.findLoginUser();
			
			EventUser eventUser = eventUserService.findByUserAndEvent(user, event);

			// 対象のイベントにユーザが参加済みか確認し参加の場合削除
			if(eventUser != null) {
				// 対象のイベントにユーザが不参加か確認し不参加の場合登録
				eventUserService.deleteById(eventUser.getId());
			}
			flash = new FlashData().success("イベントの削除が完了しました");
		} catch (DataNotFoundException e) {
			flash = new FlashData().danger("該当データがありません");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/events/view/%s".replace("%s", id.toString());
	}
}

