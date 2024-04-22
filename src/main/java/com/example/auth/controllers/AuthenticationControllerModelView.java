package com.example.auth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("")
public class AuthenticationControllerModelView {

    @GetMapping("/")
    public ModelAndView indexPage() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("message", "Welcome to my APP!");
        return mav;
    }

    @GetMapping("/login")
    public ModelAndView loginPage(@RequestParam(required = false) String message) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("message", message);
        return mav;
    }

    @GetMapping("/register")
    public ModelAndView registerPage() {
        return new ModelAndView("register");
    }
}