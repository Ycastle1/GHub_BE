package com.ce.back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToLogin() {

        // static 디렉토리의 index.html로 리디렉션
        return "redirect:/index.html";
    }
}