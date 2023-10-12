package com.srsapi.model;

import java.util.ArrayList;


public class Course {

    private String uuid;
    private String courseName;
    private String courseNumber;
    private String courseDept;
    private String deptUuid;
    private ArrayList<Course> preReqs;
    private ArrayList<Offering> offeringList;

    public Course( String uuid,String courseName, String courseNumber, String courseDept, String deptUuid) {
        this.uuid = uuid;
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.courseDept = courseDept;
        this.deptUuid = deptUuid;
        preReqs = new ArrayList<>();
        offeringList = new ArrayList<>();
    }

    public Course() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

   
    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseDept() {
        return courseDept;
    }

    public void setCourseDept(String courseDept) {
        this.courseDept = courseDept;
    }

    public void setPreReqs(ArrayList<Course> preReqs) {
        this.preReqs = preReqs;
    }

    public Offering getOffering(int i) {

        for (Offering o : offeringList) {
            if (o.getSection() == i)
                return o;
        }
        return null;
    }

    public void setOfferingList(ArrayList<Offering> offeringList) {
        for (Offering offering : offeringList) {
            offering.setTheCourse(this);
            this.offeringList.add(offering);
        }

    }

    public ArrayList<Course> getPreReqs() {
        return preReqs;
    }

    public ArrayList<Offering> getOfferingList() {
        return offeringList;
    }

    public String getDeptUuid() {
        return deptUuid;
    }

    public void setDeptUuid(String deptUuid) {
        this.deptUuid = deptUuid;
    }
    

    
}
