package com.example.crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.crud.model.User;
import com.example.crud.repository.UserRepo;

@Controller
public class UserController {

	@Autowired
	private UserRepo myUserRepo;

	@RequestMapping("/hello")
	public String HelloPage() {
		return "hello";
	}

	// register page,get
	@RequestMapping("/register")
	public String UserRegister(Model model) {

		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("error", true);
		return "UserRegister";
	}

	/* post */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String UserRegister(Model model, @ModelAttribute User user) {

		User db_user = myUserRepo.checkEmail(user.getEmail());
		if (db_user == null) {
			myUserRepo.save(user);
//			model.addAttribute("error",true);
//			model.addAttribute("success",true);
			model.addAttribute("user", new User());
			return "login";

			// go to login page
		} else {
			model.addAttribute("error", false);

		}
		model.addAttribute("user", user);

		return "UserRegister";
	}

	@RequestMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("loginError", true);
		return "login";
	}

	/* login submit button */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String submitLoginPage(Model model, @ModelAttribute User user) {

		User db_user = myUserRepo.checkLogin(user.getEmail(), user.getPassword());

		if (db_user == null) {
			model.addAttribute("loginError", false);
		} else {
			List<User> users = myUserRepo.findAll();
			model.addAttribute("users", users);
			return "main";
		}
		model.addAttribute("user", user);
		return "login";
	}

	/* Edit page */
	@RequestMapping("/edit/{id}")
	public String editPage(Model model, @PathVariable("id") Long id) {

		User user = myUserRepo.findById(id).orElseThrow();
		model.addAttribute("user", user);
		return "edit";
	}

	@RequestMapping("/edit")
	public String editSubmit(Model model, @ModelAttribute User user) {
		myUserRepo.save(user);
		List<User> users = myUserRepo.findAll();
		model.addAttribute("users", users);
		return "main";

	}

	/* Delete */
	@RequestMapping("/delete/{id}")
	public String deletePage(Model model, @PathVariable("id") Long id) {

		myUserRepo.deleteById(id);
		List<User> users = myUserRepo.findAll();
		model.addAttribute("users", users);
		return "main";
	}
}
