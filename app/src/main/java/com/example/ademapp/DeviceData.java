package com.example.ademapp;


import java.util.ArrayList;

public class DeviceData {

    private static ArrayList<Device> devices = new ArrayList<>();
    public static void save(Device c){
        devices.add(c);
    }
    public static ArrayList<Device> getContacts(){
        return devices;
    }
}