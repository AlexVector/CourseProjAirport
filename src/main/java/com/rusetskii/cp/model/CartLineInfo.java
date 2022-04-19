package com.rusetskii.cp.model;

public class CartLineInfo {
  
    private TicketInfo ticketInfo;
    private int quantity;
  
    public CartLineInfo() {
        this.quantity = 0;
    }
  
    public TicketInfo getTicketInfo() {
        return ticketInfo;
    }
  
    public void setTicketInfo(TicketInfo ticketInfo) {
        this.ticketInfo = ticketInfo;
    }
  
    public int getQuantity() {
        return quantity;
    }
  
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
  
    public double getAmount() {
        return this.ticketInfo.getPrice() * this.quantity;
    }
     
}
