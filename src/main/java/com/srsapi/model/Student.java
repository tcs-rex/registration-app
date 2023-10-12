package com.srsapi.model;

import java.util.ArrayList;

public class Student {

    private String firstName;
    private String lastName;
    private String email;
    private String uuid;
    private String password;

    private ArrayList<Registration> courseList;

    public Student() {
        courseList = new ArrayList<>();
    }

    public Student(String uuid, String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.uuid = uuid;
        this.password = password;
        courseList = new ArrayList<>();
    }

    public void addCourse(Registration r) {
        courseList.add(r);
    }

    public void removeCourse(Registration r) {
        courseList.remove(r);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String id) {
        this.uuid = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getId() {
        return uuid;

    }

    public void setId(String id) {
        this.uuid = id;
    }

    public ArrayList<Registration> getCourseList() {
        return courseList;
    }

    public void setCourseList(ArrayList<Registration> courseList) {
        this.courseList = courseList;
    }

    
  
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
