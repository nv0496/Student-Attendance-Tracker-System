package com.attendancetracker;

import java.io.Serializable; 
public class Subject implements Serializable { 
    public String id;
    public String name;
    public String facultyId; 

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
        this.facultyId = null; 
    }


    public Subject(String id, String name, String facultyId) {
        this.id = id;
        this.name = name;
        this.facultyId = facultyId;
    }

    @Override
    public String toString() {
        if (facultyId != null && DBManager.users.containsKey(facultyId)) {
            return name + " (Taught by " + DBManager.users.get(facultyId).name + ")";
        }
        return name;
    }
}

