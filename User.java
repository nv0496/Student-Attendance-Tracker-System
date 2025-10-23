package com.attendancetracker;

public class User {
    public String id;
    public String username;
    public String password;
    public String name;
    public Role role;
    public String assignedSubjectId;

    public User(String id, String username, String password, String name, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    @Override
    public String toString() {
        if (this.role == Role.FACULTY) {
            String subjectName = (assignedSubjectId != null && DBManager.subjects.containsKey(assignedSubjectId)) 
                               ? DBManager.subjects.get(assignedSubjectId).name 
                               : "Unassigned";
            return name + " (" + username + ") - Teaches: " + subjectName;
        }
        return name + " (" + username + ")";
    }
}
