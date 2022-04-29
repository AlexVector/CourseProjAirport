package com.rusetskii.cp.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Planes",
        uniqueConstraints = {@UniqueConstraint(columnNames = "Name")})
public class Plane implements Serializable {
    private static final long serialVersionUID = -1000119078147252957L;

    @Id
    @Column(name = "Plane_ID", length = 20, nullable = false)
    private String plane_id;

    @Column(name = "Name", length = 20, nullable = false)
    private String name;

    @Column(name = "Info", length = 255, nullable = false)
    private String info;

    public Plane() {}

    public static class Builder{
        private Plane newplane;

        public Builder(){
            newplane = new Plane();
        }

        public Builder id(String plane_id){
            newplane.plane_id = plane_id;
            return this;
        }

        public Builder name(String name){
            newplane.name = name;
            return this;
        }

        public Builder info(String info){
            newplane.info = info;
            return this;
        }
    }

    public String getPlane_id() {
        return plane_id;
    }
    public void setPlane_id(String plane_id) {
        this.plane_id = plane_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
}
