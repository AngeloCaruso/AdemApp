package com.example.ademapp;


import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class Device {
    private String name, type, code;
    private ArrayList<Entry> data;
    private boolean active;

    public Device(String name, String type, String code) {
        this.name = name;
        this.type = type;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<Entry> getData() {
        return data;
    }

    public void setData(ArrayList<Entry> data) {
        this.data = data;
    }

    public void save(){
        DeviceData.save(this);
    }
}
