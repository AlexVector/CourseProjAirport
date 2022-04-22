package com.rusetskii.cp.model;

import com.rusetskii.cp.entity.Plane;

public class PlaneInfo {
    private String plane_id;
    private String name;
    private String info;

    public PlaneInfo() {}

    public PlaneInfo(Plane plane) {
        this.plane_id = plane.getPlane_id();
        this.name = plane.getName();
        this.info = plane.getInfo();
    }

    // Using in JPA/Hibernate query
    public PlaneInfo(String plane_id, String name, String info) {
        this.plane_id = plane_id;
        this.name = name;
        this.info = info;
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
