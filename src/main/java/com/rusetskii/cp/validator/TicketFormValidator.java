package com.rusetskii.cp.validator;

import com.rusetskii.cp.dao.TicketDAO;
import com.rusetskii.cp.entity.Ticket;
import com.rusetskii.cp.form.TicketForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TicketFormValidator implements Validator {
 
   @Autowired
   private TicketDAO ticketDAO;

   @Override
   public boolean supports(Class<?> clazz) {
      return clazz == TicketForm.class;
   }
 
   @Override
   public void validate(Object target, Errors errors) {
      TicketForm ticketForm = (TicketForm) target;
 
      // Check the fields of TicketForm.
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "ticket_id", "NotEmpty.ticketForm.ticket_id");
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "plane_id", "NotEmpty.ticketForm.plane_id");
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.ticketForm.name");
      ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.ticketForm.price");
 
      String ticket_id = ticketForm.getTicket_id();
      if (ticket_id != null && ticket_id.length() > 0) {
         if (ticket_id.matches("\\s+")) {
            errors.rejectValue("ticket_id", "Pattern.ticketForm.ticket_id");
         } else if (ticketForm.isNewTicket()) {
            Ticket ticket = ticketDAO.findTicket(ticket_id);
            if (ticket != null) {
               errors.rejectValue("ticket_id", "Duplicate.ticketForm.ticket_id");
            }
         }
      }
   }
}
