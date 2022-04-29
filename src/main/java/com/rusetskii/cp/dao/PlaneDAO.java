package com.rusetskii.cp.dao;

import com.rusetskii.cp.entity.Plane;
import com.rusetskii.cp.form.PlaneForm;
import com.rusetskii.cp.model.PlaneInfo;
import com.rusetskii.cp.pangination.PaginationResult;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.NoResultException;

@Transactional
@Repository
public class PlaneDAO {
 
    @Autowired
    private SessionFactory sessionFactory;

    public void planeDeleter(String plane_id){
        try{
            Session session = this.sessionFactory.getCurrentSession();
            Query query = session.createQuery("delete Plane where plane_id = :plane_id");
            query.setParameter("plane_id", plane_id);
            int result = query.executeUpdate();
        }catch (Exception e){}
    }
 
    public Plane findPlane(String plane_id) {
        try {
            String sql = "Select e from " + Plane.class.getName() + " e Where e.plane_id =:plane_id ";
 
            Session session = this.sessionFactory.getCurrentSession();
            Query<Plane> query = session.createQuery(sql, Plane.class);
            query.setParameter("plane_id", plane_id);
            return (Plane) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
 
    public PlaneInfo findPlaneInfo(String plane_id) {
        Plane plane = this.findPlane(plane_id);
        if (plane == null) {
            return null;
        }
        return new PlaneInfo(plane.getPlane_id(), plane.getName(), plane.getInfo());
    }
 
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void save(PlaneForm planeForm) {
        Session session = this.sessionFactory.getCurrentSession();
        String plane_id = planeForm.getPlane_id();
        Plane plane = null;
 
        boolean isNew = false;
        if (plane_id != null) {
            plane = this.findPlane(plane_id);
        }
        if (plane == null) {
            isNew = true;
            plane = new Plane();
        }
        plane.setPlane_id(plane_id);
        plane.setName(planeForm.getName());
        plane.setInfo(planeForm.getInfo());
 
        if (planeForm.getFileData() != null) {}
        if (isNew) {
            session.persist(plane);
        }
        // If error in DB, Exceptions will be thrown out immediately
        session.flush();
    }
 
    public PaginationResult<PlaneInfo> queryPlanes(int page, int maxResult, int maxNavigationPage,
                                                     String likeName) {
        String sql = "Select new " + PlaneInfo.class.getName() //
                + "(p.plane_id, p.name, p.info) " + " from "//
                + Plane.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        // 
        Session session = this.sessionFactory.getCurrentSession();
        Query<PlaneInfo> query = session.createQuery(sql, PlaneInfo.class);
 
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        return new PaginationResult<PlaneInfo>(query, page, maxResult, maxNavigationPage);
    }
 
   
    public PaginationResult<PlaneInfo> queryPlanes(int page, int maxResult, int maxNavigationPage) {
        return queryPlanes(page, maxResult, maxNavigationPage, null);
    }
}
