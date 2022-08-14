package com.ss_technology.dims.Adapter;

import java.io.Serializable;

public class Appoiment_Container implements Serializable {
    int patient_id;
    int appoiment_id;
    String date;
    String status;
    String name;
    String prev_app_id;

    public String getPrev_app_id() {
        return prev_app_id;
    }

    public void setPrev_app_id(String prev_app_id) {
        this.prev_app_id = prev_app_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public int getAppoiment_id() {
        return appoiment_id;
    }

    public void setAppoiment_id(int appoiment_id) {
        this.appoiment_id = appoiment_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
