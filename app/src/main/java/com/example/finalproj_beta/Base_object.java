package com.example.finalproj_beta;

abstract class Base_object {
    private String name;
    private String school;
    private Boolean active;
    private String gmail;

    public Base_object(String name, String school, String gmail) {
        this.name = name;
        this.school = school;
        this.active = true;
        this.gmail = gmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
