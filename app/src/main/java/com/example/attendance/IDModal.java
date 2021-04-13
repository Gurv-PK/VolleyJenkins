package com.example.attendance;

import android.app.Application;

import java.util.ArrayList;

public class IDModal extends Application {
    static String ID,Name,Status,Date,Time;

    public static String getID() {
        return ID;
    }

    public static String getName() {
        return Name;
    }

    public static String getDate() {
        return Date;
    }

    public static void setDate(String date) {
        Date = date;
    }

    public static String getTime() {
        return Time;
    }

    public static void setTime(String time) {
        Time = time;
    }

    public static String getStatus() {
        return Status;
    }

    public static void setStatus(String status) {
        Status = status;
    }

    public static void setName(String name) {
        Name = name;
    }

    public static void setID(String ID) {
        IDModal.ID = ID;
    }
}
