package com.rusetskii.cp.controller;

import com.rusetskii.cp.dao.OrderDAO;
import com.rusetskii.cp.dao.TicketDAO;
import com.rusetskii.cp.model.TicketInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import com.rusetskii.cp.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @RequestMapping({ "/ticketList" })
    public String listTicketHandler(Model model, //
                                    @RequestParam(value = "name", defaultValue = "") String likeName,
                                    @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 100;
        final int maxNavigationPage = 10;
        PaginationResult<TicketInfo> result = ticketDAO.queryTickets(page, //
                maxResult, maxNavigationPage, likeName);
        model.addAttribute("paginationTickets", result);
        return "ticketList";
    }
}
