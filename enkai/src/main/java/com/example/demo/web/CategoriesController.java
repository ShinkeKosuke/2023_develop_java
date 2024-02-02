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
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin/categories")
public class CategoriesController {
	@Autowired
	CategoryService categoryService;

	@GetMapping
	public String list(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		return "admin/categories/list";
	}

	@GetMapping("/create")
	public String form(@ModelAttribute Category category, Model model) {
		return "admin/categories/create";
	}

	@PostMapping("/create")
	public String register(@Validated @ModelAttribute Category category, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/categories/create";
			}
			categoryService.save(category);
			flash = new FlashData().success("カテゴリの登録が完了しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/categories";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		try {
			model.addAttribute("category", categoryService.findById(id));
		} catch (DataNotFoundException e) {
			return "redirect:/admin/categories";
		}
		return "admin/categories/edit";
	}

	@PostMapping("/edit/{id}")
	public String update(@PathVariable Integer id, @Validated Category category, BindingResult result, Model model, RedirectAttributes ra) {
		FlashData flash;
		try {
			if (result.hasErrors()) {
				return "admin/categories/edit";
			}
			categoryService.findById(id);
			// 更新
			categoryService.save(category);
			flash = new FlashData().success("カテゴリの更新が完了しました");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/categories";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		FlashData flash;
		try {
			categoryService.findById(id);
			categoryService.deleteById(id);
			flash = new FlashData().success("カテゴリの削除が完了しました");
		} catch (DataNotFoundException e) {
			flash = new FlashData().danger("該当データがありません");
		} catch (Exception e) {
			flash = new FlashData().danger("処理中にエラーが発生しました");
		}
		ra.addFlashAttribute("flash", flash);
		return "redirect:/admin/categories";
	}
}

