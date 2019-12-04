package com.example.capstoneproject.Models;


public class Child {
    private String name;
    private String parent;
    private String driver;
    private String gender;
    private String age;
    private String status;
    private String timeIn;
    private String timeOut;

    public Child(){

    }

    public String getTimeIn() { return timeIn; }

    public void setTimeIn(String timeIn) { this.timeIn = timeIn; }

    public String getTimeOut() { return timeOut; }

    public void setTimeOut(String timeOut) { this.timeOut = timeOut; }

    public String getGender() { return gender;}

    public void setGender(String gender) { this.gender = gender; }

    public String getAge() { return age; }

    public void setAge(String age) { this.age = age; }

    public String getStatus() {return status;}

    public void setStatus(String status){this.status =status;}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getParent(){
        return parent;
    }

    public void setParent(String parent){
        this.parent = parent;
    }

    public String getDriver() { return driver; }

    public void setDriver(String driver) { this.driver = driver; }

}
