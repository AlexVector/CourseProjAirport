package com.rusetskii.cp.form;

import com.rusetskii.cp.entity.Ticket;
import org.springframework.web.multipart.MultipartFile;

 
public class TicketForm {
    private String ticket_id;
    private String plane_id;
    private String name;
    private double price;
 
    private boolean newTicket = false;

    private MultipartFile fileData;
 
    public TicketForm() {
        this.newTicket= true;
    }
 
    public TicketForm(Ticket ticket) {
        this.ticket_id = ticket.getTicket_id();
        this.plane_id = ticket.getPlane().getPlane_id();
        this.name = ticket.getName();
        this.price = ticket.getPrice();
    }
 
    public String getTicket_id() {
        return ticket_id;
    }
    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }
    public String getPlane_id() {return plane_id;}
    public void setPlane_id(String plane_id) {this.plane_id = plane_id;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public MultipartFile getFileData() {
        return fileData;
    }
    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }
    public boolean isNewTicket() {
        return newTicket;
    }
    public void setNewTicket(boolean newTicket) {
        this.newTicket = newTicket;
    }
}
