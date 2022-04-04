package com.rusetskii.cp.controller;

import com.rusetskii.cp.dao.OrderDAO;
import com.rusetskii.cp.dao.TicketDAO;
import com.rusetskii.cp.entity.Ticket;
import com.rusetskii.cp.form.TicketForm;
import com.rusetskii.cp.model.OrderDetailInfo;
import com.rusetskii.cp.model.OrderInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import com.rusetskii.cp.validator.TicketFormValidator;
import org.apache.tomcat.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private TicketFormValidator ticketFormValidator;

    @RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
    public String login(Model model) {return "login";}

    @RequestMapping(value = { "/admin/accountInfo" }, method = RequestMethod.GET)
    public String accountInfo(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userDetails.getPassword());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());

        model.addAttribute("userDetails", userDetails);
        return "accountInfo";
    }

    @RequestMapping(value = { "/admin/orderList" }, method = RequestMethod.GET)
    public String orderList(Model model, //
                            @RequestParam(value = "page", defaultValue = "1") String pageStr) {
        int page = 1;
        try {
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        final int MAX_RESULT = 5;
        final int MAX_NAVIGATION_PAGE = 10;

        PaginationResult<OrderInfo> paginationResult //
                = orderDAO.listOrderInfo(page, MAX_RESULT, MAX_NAVIGATION_PAGE);

        model.addAttribute("paginationResult", paginationResult);
        return "orderList";
    }

    // GET: Show ticket.
    @RequestMapping(value = { "/admin/ticket" }, method = RequestMethod.GET)
    public String ticket(Model model, @RequestParam(value = "code", defaultValue = "") String code) {
        TicketForm ticketForm = null;

        if (code != null && code.length() > 0) {
            Ticket ticket = ticketDAO.findTicket(code);
            if (ticket != null) {
                ticketForm = new TicketForm(ticket);
            }
        }
        if (ticketForm == null) {
            ticketForm = new TicketForm();
            ticketForm.setNewTicket(true);
        }
        model.addAttribute("ticketForm", ticketForm);
        return "ticket";
    }

    // POST: Save ticket
    @RequestMapping(value = { "/admin/ticket" }, method = RequestMethod.POST)
    public String ticketSave(Model model, //
                             @ModelAttribute("ticketForm") @Validated TicketForm ticketForm, //
                             BindingResult result, //
                             final RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "ticket";
        }
        try {
            ticketDAO.save(ticketForm);
        } catch (Exception e) {
            //TODO: проблема с методом getRootCause класса ExceptionUtils
            //Throwable rootCause = ExceptionUtils.getRootCause(e);
            //String message = rootCause.getMessage();
            //model.addAttribute("errorMessage", message);
            // Show ticket form.
            return "ticket";
        }
        return "redirect:/ticketList";
    }

    @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null) {
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        }
        if (orderInfo == null) {
            return "redirect:/admin/orderList";
        }
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
        model.addAttribute("orderInfo", orderInfo);
        return "order";
    }
}
