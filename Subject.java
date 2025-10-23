package com.attendancetracker;

import java.io.Serializable; // ✅ Added import

// ✅ Implement Serializable to match other models
public class Subject implements Serializable { 
    public String id;
    public String name;
    public String facultyId; // Field to track who teaches the subject

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
        this.facultyId = null; // by default, no faculty assigned
    }

    // ✅ Overloaded constructor called by DBManager
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
