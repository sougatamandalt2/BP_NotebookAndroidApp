package com.example.mybpnotebook;

public class modelRecord {
    String sys,dia,date,time,status,user;

    public modelRecord(String sys, String dia, String date, String time, String status,String user) {
        this.sys = sys;
        this.dia = dia;
        this.date = date;
        this.time = time;
        this.status = status;
        this.user = user;
    }

    public modelRecord() {
    }

    public String getSys() {
        return sys;
    }
    public String getUser() {
        return user;
    }

    public String getDia() {
        return dia;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }


    public void setSys(String sys) {
        this.sys = sys;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
