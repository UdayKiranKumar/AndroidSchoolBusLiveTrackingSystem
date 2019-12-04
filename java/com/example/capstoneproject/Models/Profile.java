package com.example.capstoneproject.Models;

public class Profile {
    private String fullName;
    private String email;
    private String password;
    private String type;
    private String capacity;
    private String current;
    private String phoneNumber;
    private String number_of_child;
    private String assignee;
    private String id;
    private String area;
    private String status;
    private String plateNumber;

    public Profile (){

    }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber= plateNumber;}
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getArea() { return area; }

    public void setArea(String area) { this.area = area; }

    public String getAssignee() { return assignee; }

    public void setAssignee(String assignee) { this.assignee = assignee; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNumber_of_child(){ return number_of_child; }

    public void setNumber_of_child(String number_of_child) { this.number_of_child = number_of_child; }

    public String getCapacity() { return capacity; }

    public void setCapacity(String capacity) { this.capacity = capacity; }

    public String getCurrent() { return current; }

    public void setCurrent(String current) { this.current = current; }

    public String getFullName(){
        return fullName;
    }

    public void setFullName(String firstName){
        this.fullName = firstName;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}
    public void setPassword(String password){
        this.password = password;
    }
}
