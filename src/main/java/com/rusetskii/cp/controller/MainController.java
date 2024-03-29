package com.rusetskii.cp.controller;

import com.rusetskii.cp.dao.OrderDAO;
import com.rusetskii.cp.dao.PlaneDAO;
import com.rusetskii.cp.dao.TicketDAO;
import com.rusetskii.cp.entity.Ticket;
import com.rusetskii.cp.form.CustomerForm;
import com.rusetskii.cp.model.CartInfo;
import com.rusetskii.cp.model.CustomerInfo;
import com.rusetskii.cp.model.PlaneInfo;
import com.rusetskii.cp.model.TicketInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import com.rusetskii.cp.utils.Utils;
import com.rusetskii.cp.validator.CustomerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@Transactional
public class MainController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private PlaneDAO planeDAO;

    @Autowired
    private CustomerFormValidator customerFormValidator;

    @InitBinder
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null)
            return;
        System.out.println("Target=" + target);
        if (target.getClass() == CartInfo.class) {}
        else if (target.getClass() == CustomerForm.class) {
            dataBinder.setValidator(customerFormValidator);
        }

    }

    @RequestMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping("/")
    public String home() {
        return "index";
    }

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

    public PaginationResult<PlaneInfo> hardListPlaneHandler(String likeName, int page, int maxResult, int maxNavigationPage){
        PaginationResult<PlaneInfo> result = planeDAO.queryPlanes(page, maxResult, maxNavigationPage, likeName);
        return result;
    }

    @RequestMapping({ "/planeList" })
    public String listPlaneHandler(Model model, //
                                    @RequestParam(value = "name", defaultValue = "") String likeName,
                                    @RequestParam(value = "page", defaultValue = "1") int page) {
        final int maxResult = 100;
        final int maxNavigationPage = 10;
        PaginationResult<PlaneInfo> result = planeDAO.queryPlanes(page, //
                maxResult, maxNavigationPage, likeName);
        model.addAttribute("paginationPlanes", result);
        return "planeList";
    }

    public Map<String, Integer> hardOrderChartHandler(){
        Map<String, Integer> result = orderDAO.getInfoForChart();
        return result;
    }

    @RequestMapping({ "/sellChart" })
    public String orderChartHandler(Model model) {
        Map<String, Integer> graphmap = orderDAO.getInfoForChart();
        model.addAttribute("graphmap", graphmap);
        return "sellChart";
    }

    @RequestMapping({ "/buyTicket" })
    public String listTicketHandler(HttpServletRequest request, Model model, //
                                    @RequestParam(value = "ticket_id", defaultValue = "") String code) {

        Ticket ticket = null;
        if (code != null && code.length() > 0) {
            ticket = ticketDAO.findTicket(code);
        }
        if (ticket != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            TicketInfo ticketInfo = new TicketInfo(ticket);
            cartInfo.addTicket(ticketInfo, 1);
        }
        return "redirect:/shoppingCart";
    }

    @RequestMapping({ "/shoppingCartRemoveTicket" })
    public String removeTicketHandler(HttpServletRequest request, Model model, //
                                      @RequestParam(value = "ticket_id", defaultValue = "") String code) {
        Ticket ticket = null;
        if (code != null && code.length() > 0)
            ticket = ticketDAO.findTicket(code);
        if (ticket != null) {
            CartInfo cartInfo = Utils.getCartInSession(request);
            TicketInfo ticketInfo = new TicketInfo(ticket);
            cartInfo.removeTicket(ticketInfo);
        }
        return "redirect:/shoppingCart";
    }

    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
    public String shoppingCartUpdateQty(HttpServletRequest request,
                                        Model model,
                                        @ModelAttribute("cartForm") CartInfo cartForm) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        cartInfo.updateQuantity(cartForm);
        return "redirect:/shoppingCart";
    }


    @RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
    public String shoppingCartHandler(HttpServletRequest request, Model model) {
        CartInfo myCart = Utils.getCartInSession(request);
        model.addAttribute("cartForm", myCart);
        return "shoppingCart";
    }


    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
    public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo.isEmpty())
            return "redirect:/shoppingCart";
        CustomerInfo customerInfo = cartInfo.getCustomerInfo();
        CustomerForm customerForm = new CustomerForm(customerInfo);
        model.addAttribute("customerForm", customerForm);
        return "shoppingCartCustomer";
    }


    // POST: Save customer information.
    @RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
    public String shoppingCartCustomerSave(HttpServletRequest request,
                                           Model model,
                                           @ModelAttribute("customerForm") @Validated CustomerForm customerForm, //
                                           BindingResult result,
                                           final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            customerForm.setValid(false);
            return "shoppingCartCustomer";
        }
        customerForm.setValid(true);
        CartInfo cartInfo = Utils.getCartInSession(request);
        CustomerInfo customerInfo = new CustomerInfo(customerForm);
        cartInfo.setCustomerInfo(customerInfo);
        return "redirect:/shoppingCartConfirmation";
    }

    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
    public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo == null || cartInfo.isEmpty())
            return "redirect:/shoppingCart";
        else if (!cartInfo.isValidCustomer())
            return "redirect:/shoppingCartCustomer";
        model.addAttribute("myCart", cartInfo);
        return "shoppingCartConfirmation";
    }

    public void hardShoppingCartConfirmation(CartInfo cartInfo){
        orderDAO.saveOrder(cartInfo);
    }

    @RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
    public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
        CartInfo cartInfo = Utils.getCartInSession(request);
        if (cartInfo.isEmpty())
            return "redirect:/shoppingCart";
        else if (!cartInfo.isValidCustomer())
            return "redirect:/shoppingCartCustomer";
        try {
            orderDAO.saveOrder(cartInfo);
        } catch (Exception e) {
            return "shoppingCartConfirmation";
        }
        // Remove Cart from Session.
        Utils.removeCartInSession(request);
        // Store last cart.
        Utils.storeLastOrderedCartInSession(request, cartInfo);
        return "redirect:/shoppingCartFinalize";
    }

    @RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
    public String shoppingCartFinalize(HttpServletRequest request, Model model) {

        CartInfo lastOrderedCart = Utils.getLastOrderedCartInSession(request);

        if (lastOrderedCart == null) {
            return "redirect:/shoppingCart";
        }
        model.addAttribute("lastOrderedCart", lastOrderedCart);
        return "shoppingCartFinalize";
    }
}
