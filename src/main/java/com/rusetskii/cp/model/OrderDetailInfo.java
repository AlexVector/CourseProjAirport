package com.rusetskii.cp.model;


public class OrderDetailInfo {
    private String id;

    //private String user_name;
 
    private String ticketCode;
    private String ticketName;
 
    private int quanity;
    private double price;
    private double amount;
 
    public OrderDetailInfo() {}

    public OrderDetailInfo(String id, String ticketCode, //
            String ticketName, int quanity, double price, double amount) {
        this.id = id;
        //this.user_name = user_name;
        this.ticketCode = ticketCode;
        this.ticketName = ticketName;
        this.quanity = quanity;
        this.price = price;
        this.amount = amount;
    }
 
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    //public String getUser_name() {return user_name;}
    //public void setUser_name(String user_name) {this.user_name = user_name;}
    public String getTicketCode() {
        return ticketCode;
    }
    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }
    public String getTicketName() {
        return ticketName;
    }
    public void setTicketName(String ticketName) {this.ticketName = ticketName;}
    public int getQuanity() {
        return quanity;
    }
    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
