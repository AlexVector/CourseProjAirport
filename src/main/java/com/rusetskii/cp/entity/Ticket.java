package com.rusetskii.cp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

 
@Entity
@Table(name = "Tickets")
public class Ticket implements Serializable {
 
    private static final long serialVersionUID = -1000119078147252957L;
 
    @Id
    @Column(name = "Ticket_ID", length = 20, nullable = false)
    private String ticket_id;
 
    @Column(name = "Name", length = 255, nullable = false)
    private String name;
 
    @Column(name = "Price", nullable = false)
    private double price;
 
     
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "Create_Date", nullable = false)
    private Date createDate;
 
    public Ticket() {}
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
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
