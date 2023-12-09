package com.projeto2.env_station_app.Model;

import java.util.Date;

public class Station {
    String id;
    String timestamp;
    double degree_c;
    double degree_f;
    double humidity;
    double ppm_mq135;
    double voltage_ldr;
    int presence;
    String device_id;

    public Station(String id, String timestamp, double degree_c, double degree_f, double humidity, double ppm_mq135, double voltage_ldr, int presence) {
        this.id = id;
        this.timestamp = timestamp;
        this.degree_c = degree_c;
        this.degree_f = degree_f;
        this.humidity = humidity;
        this.ppm_mq135 = ppm_mq135;
        this.voltage_ldr = voltage_ldr;
        this.presence = presence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getDegree_c() {
        return degree_c;
    }

    public void setDegree_c(double degree_c) {
        this.degree_c = degree_c;
    }

    public double getDegree_f() {
        return degree_f;
    }

    public void setDegree_f(double degree_f) {
        this.degree_f = degree_f;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPpm_mq135() {
        return ppm_mq135;
    }

    public void setPpm_mq135(double ppm_mq135) {
        this.ppm_mq135 = ppm_mq135;
    }

    public double getVoltage_ldr() {
        return voltage_ldr;
    }

    public void setVoltage_ldr(double voltage_ldr) {
        this.voltage_ldr = voltage_ldr;
    }

    public int getPresence() {
        return presence;
    }

    public void setPresence(int presence) {
        this.presence = presence;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
}
