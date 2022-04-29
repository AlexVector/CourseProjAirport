package com.rusetskii.cp.controller;

import com.rusetskii.cp.dao.OrderDAO;
import com.rusetskii.cp.dao.PlaneDAO;
import com.rusetskii.cp.dao.TicketDAO;
import com.rusetskii.cp.entity.Plane;
import com.rusetskii.cp.entity.Ticket;
import com.rusetskii.cp.form.PlaneForm;
import com.rusetskii.cp.form.TicketForm;
import com.rusetskii.cp.model.OrderDetailInfo;
import com.rusetskii.cp.model.OrderInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import com.rusetskii.cp.validator.TicketFormValidator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Transactional
public class AdminController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private PlaneDAO planeDAO;

    @Autowired
    private TicketFormValidator ticketFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == TicketForm.class) {
            dataBinder.setValidator(ticketFormValidator);
        }
    }

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
    public String orderList(Model model,
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
    public String ticket(Model model, @RequestParam(value = "ticket_id", defaultValue = "") String code) {
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

    @RequestMapping(value = { "/admin/delTicket" }, method = RequestMethod.GET)
    public String deleteTicket(Model model, @RequestParam(value = "ticket_id", defaultValue = "") String code) {
        if (code != null && code.length() > 0)
            ticketDAO.ticketDeleter(code);
        return "redirect:/ticketList";
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
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            return "ticket";
        }
        return "redirect:/ticketList";
    }

    public void hardplaneSave(PlaneForm planeForm){
        planeDAO.save(planeForm);
    }

    public void hardticketSave(TicketForm ticketForm){
        ticketDAO.save(ticketForm);
    }

    // POST: Save ticket
    @RequestMapping(value = { "/admin/plane" }, method = RequestMethod.POST)
    public String planeSave(Model model, //
                             @ModelAttribute("planeForm") @Validated PlaneForm planeForm, //
                             BindingResult result, //
                             final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "plane";
        }
        try {
            planeDAO.save(planeForm);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            String message = rootCause.getMessage();
            model.addAttribute("errorMessage", message);
            return "plane";
        }
        return "redirect:/planeList";
    }

    @RequestMapping(value = { "/admin/plane" }, method = RequestMethod.GET)
    public String plane(Model model, @RequestParam(value = "plane_id", defaultValue = "") String code) {
        PlaneForm planeForm = null;

        if (code != null && code.length() > 0) {
            Plane plane = planeDAO.findPlane(code);
            if (plane != null) {planeForm = new PlaneForm(plane);}
        }
        if (planeForm == null) {
            planeForm = new PlaneForm();
            planeForm.setNewPlane(true);
        }
        model.addAttribute("planeForm", planeForm);
        return "plane";
    }

    @RequestMapping(value = { "/admin/delPlane" }, method = RequestMethod.GET)
    public String deletePlane(Model model, @RequestParam(value = "plane_id", defaultValue = "") String code) {
        if (code != null && code.length() > 0)
            planeDAO.planeDeleter(code);
        return "redirect:/planeList";
    }

    public List<OrderDetailInfo> hardOrderView(String orderId){
        OrderInfo orderInfo = null;
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        return details;
    }

    @RequestMapping(value = { "/admin/order" }, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("orderId") String orderId) {
        OrderInfo orderInfo = null;
        if (orderId != null)
            orderInfo = this.orderDAO.getOrderInfo(orderId);
        if (orderInfo == null)
            return "redirect:/admin/orderList";
        List<OrderDetailInfo> details = this.orderDAO.listOrderDetailInfos(orderId);
        orderInfo.setDetails(details);
        model.addAttribute("orderInfo", orderInfo);
        return "order";
    }
}
