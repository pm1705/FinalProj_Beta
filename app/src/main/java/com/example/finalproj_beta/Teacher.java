package com.example.finalproj_beta;

public class Teacher extends Base_object{
    public static final String ACTIVE = "active";
    public static final String GMAIL = "gmail";
    public static final String LEVEL = "level";
    public static final String NAME = "name";
    public static final String SCHOOL = "school";

    private int level;

    public Teacher(String name, String school, String gmail, int level) {
        super(name, school, gmail);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
