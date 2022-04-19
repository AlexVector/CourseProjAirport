package com.rusetskii.cp.model;


import com.rusetskii.cp.entity.Ticket;

 
public class TicketInfo {
    private String ticket_id;
    private String name;
    private double price;
 
    public TicketInfo() {}
 
    public TicketInfo(Ticket ticket) {
        this.ticket_id = ticket.getTicket_id();
        this.name = ticket.getName();
        this.price = ticket.getPrice();
    }
 
    // Using in JPA/Hibernate query
    public TicketInfo(String ticket_id, String name, double price) {
        this.ticket_id = ticket_id;
        this.name = name;
        this.price = price;
    }
    public String getTicket_id() {
        return ticket_id;
    }
    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }
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
}
