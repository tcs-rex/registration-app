package com.srsapi.model;

public class Registration {
    private Offering theOffering;
    private String theGrade;
    private String theStatus;


    public Registration(Offering theOffering, String theGrade, String theStatus) {
        this.theOffering = theOffering;
        this.theGrade = theGrade;
        this.theStatus = theStatus;
    
    }


    public void setTheGrade(String theGrade) {
        this.theGrade = theGrade;
    }

    public String getTheGrade() {
        return theGrade;
    }

    public Offering getTheOffering() {
        return theOffering;
    }

    
    public Course getTheCourse() {
        return theOffering.getTheCourse();
    }

    public String getTheStatus() {
        return theStatus;
    }

    public void setTheStatus(String theStatus) {
        this.theStatus = theStatus;
    }

   



}
