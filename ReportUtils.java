package com.attendancetracker;
public class ReportUtils {
    public static String summarizeStudent(String studentId){
        int total=0,present=0;
        for(AttendanceRecord r: DBManager.getRecordsForStudent(studentId)){
         total++;
         present += r.attendancePercentage / 100;  

        }
        if(total==0) return "No records";
        return "Present: "+present+" / "+total;
    }
}
