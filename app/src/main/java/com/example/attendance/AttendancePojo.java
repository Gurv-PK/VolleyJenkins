package com.example.attendance;

public class AttendancePojo {
    String Date;
    String Status;
    String Name;
    String id;
    String CheckInTime;
    String CheckOutTime;

    public String getCheckInTime() {
        return CheckInTime;
    }

    public void setCheckInTime(String checkInTime) {
        CheckInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return CheckOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        CheckOutTime = checkOutTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AttendancePojo() {
    }

    public AttendancePojo(String name) {
        Name = name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public String getDate() {
        return Date;
    }

    public String getStatus() {
        return Status;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
