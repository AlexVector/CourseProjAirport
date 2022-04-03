package com.rusetskii.cp.controller;

import com.rusetskii.cp.dao.OrderDAO;
import com.rusetskii.cp.dao.TicketDAO;
import com.rusetskii.cp.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private OrderDAO orderDAO;
    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private CustomerFormValidator customerFormValidator;

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

    // Ticket List
    @RequestMapping({ "/showTickets" })
    public String listTicketHandler() {return "showTickets";}
}
