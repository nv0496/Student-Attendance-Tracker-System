package com.attendancetracker;

public class AttendanceRecord {
    public String studentId;
    public String subjectId;
    public double attendancePercentage;

    public AttendanceRecord(String studentId, String subjectId, double attendancePercentage) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.attendancePercentage = attendancePercentage;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "studentId='" + studentId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", attendancePercentage=" + attendancePercentage +
                '}';
    }
}
