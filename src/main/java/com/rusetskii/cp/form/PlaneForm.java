package com.rusetskii.cp.form;

import com.rusetskii.cp.entity.Plane;
import org.springframework.web.multipart.MultipartFile;

public class PlaneForm {
    private String plane_id;
    private String name;
    private String info;

    private boolean newPlane = false;

    private MultipartFile fileData;

    public PlaneForm() {
        this.newPlane = true;
    }

    public PlaneForm(Plane plane) {
        this.plane_id = plane.getPlane_id();
        this.name = plane.getName();
        this.info = plane.getInfo();
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

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    public boolean isNewPlane() {
        return newPlane;
    }

    public void setNewPlane(boolean newPlane) {
        this.newPlane = newPlane;
    }

}
