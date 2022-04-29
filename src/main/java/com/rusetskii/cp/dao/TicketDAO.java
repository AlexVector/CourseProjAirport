package com.rusetskii.cp.dao;

import com.rusetskii.cp.entity.Plane;
import com.rusetskii.cp.entity.Ticket;
import com.rusetskii.cp.form.TicketForm;
import com.rusetskii.cp.model.TicketInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.Date;

@Transactional
@Repository
public class TicketDAO {
 
    @Autowired
    private SessionFactory sessionFactory;


    public void ticketDeleter(String ticket_id){
        try{
            Session session = this.sessionFactory.getCurrentSession();
            Query query = session.createQuery("delete Ticket where ticket_id = :ticket_id");
            query.setParameter("ticket_id", ticket_id);
            int result = query.executeUpdate();
        }catch (Exception e){}
    }


    public Ticket findTicket(String ticket_id) {
        try {
            String sql = "Select e from " + Ticket.class.getName() + " e Where e.ticket_id =:ticket_id ";
 
            Session session = this.sessionFactory.getCurrentSession();
            Query<Ticket> query = session.createQuery(sql, Ticket.class);
            query.setParameter("ticket_id", ticket_id);
            return (Ticket) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
 
    public TicketInfo findTicketInfo(String ticket_id) {
        Ticket ticket = this.findTicket(ticket_id);
        if (ticket == null) {
            return null;
        }
        return new TicketInfo(ticket.getTicket_id(), ticket.getPlane().getPlane_id(), ticket.getName(), ticket.getPrice());
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(TicketForm ticketForm) {
 
        Session session = this.sessionFactory.getCurrentSession();
        String ticket_id = ticketForm.getTicket_id();
        Ticket ticket = null;
        boolean isNew = false;
        if (ticket_id != null) {
            ticket = this.findTicket(ticket_id);
        }
        if (ticket == null) {
            isNew = true;
            ticket = new Ticket();
            ticket.setCreateDate(new Date());
        }
        Plane plane = new Plane();
        plane.setPlane_id(ticketForm.getPlane_id());
        ticket.setTicket_id(ticket_id);
        ticket.setPlane(plane);
        ticket.setName(ticketForm.getName());
        ticket.setPrice(ticketForm.getPrice());
 
        if (ticketForm.getFileData() != null) {}

        if (isNew) {session.persist(ticket);}
        session.flush();
    }
 
    public PaginationResult<TicketInfo> queryTickets(int page, int maxResult, int maxNavigationPage,
                                                     String likeName) {
        String sql = "Select new " + TicketInfo.class.getName()
                + "(p.ticket_id, p.plane.plane_id, p.name, p.price) " + " from "
                + Ticket.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";

        Session session = this.sessionFactory.getCurrentSession();
        Query<TicketInfo> query = session.createQuery(sql, TicketInfo.class);
 
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<TicketInfo>(query, page, maxResult, maxNavigationPage);
    }
 
   
    public PaginationResult<TicketInfo> queryTickets(int page, int maxResult, int maxNavigationPage) {
        return queryTickets(page, maxResult, maxNavigationPage, null);
    }
}
