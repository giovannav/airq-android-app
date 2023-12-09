package com.projeto2.env_station_app.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class User implements Serializable {
    String id;
    String name;
    String surname;
    String email;
    String password;
    Date birthdate;
    Date created_at;
    Boolean confirmed_email;
    String [] device;

    public User(String name, String surname, String email, String password, String[] device) {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.password = password;
        this.device = device;
    }

    public User(String id, String name, String surname, String email, String password, String[] device) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.password = password;
        this.device = device;
    }

    public User() {
        this.name = name;
        this.email = email;
        this.surname = surname;
        this.password = password;
        this.device = device;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Boolean getConfirmed_email() {
        return confirmed_email;
    }

    public void setConfirmed_email(Boolean confirmed_email) {
        this.confirmed_email = confirmed_email;
    }

    public String[] getDevice() {
        if (device == null || device.length == 0) {
            return new String[]{"0"};
        }
        return device;
    }

    public void setDevice(List<String> device) {
        this.device = device.isEmpty() ? new String[]{"0"} : device.toArray(new String[0]);
    }
}
