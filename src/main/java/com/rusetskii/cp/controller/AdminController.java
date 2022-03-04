package com.rusetskii.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminController {

    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model) {return "login";}
}
