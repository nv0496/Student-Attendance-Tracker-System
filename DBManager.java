package com.attendancetracker;

import java.sql.*;
import java.util.*;

public class DBManager {

    private static final String URL = "jdbc:mysql://localhost:3306/attendance_tracker_db";
    private static final String USER = "root";
    private static final String PASSWORD = "2747389Jn@";

    public static Map<String, User> users = new LinkedHashMap<>();
    public static Map<String, Subject> subjects = new LinkedHashMap<>();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            loadUsers();
            loadSubjects();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================== CONNECTION ==================
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ================= USERS =================
    public static void loadUsers() {
        users.clear();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM user")) {
            while (rs.next()) {
                String id = rs.getString("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String name = rs.getString("name");
                Role role = Role.valueOf(rs.getString("role").toUpperCase());
                String assignedSub = rs.getString("assigned_subject_id");
                User u = new User(id, username, password, name, role);
                u.assignedSubjectId = assignedSub;
                users.put(id, u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User u) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO user (id, username, password, name, role, assigned_subject_id) VALUES (?, ?, ?, ?, ?, ?)")) {
            pst.setString(1, u.id);
            pst.setString(2, u.username);
            pst.setString(3, u.password);
            pst.setString(4, u.name);
            pst.setString(5, u.role.name());
            pst.setString(6, u.assignedSubjectId);
            pst.executeUpdate();
            users.put(u.id, u);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static User authenticate(String username, String password) {
        for (User u : users.values()) {
            if (u.username.equals(username) && u.password.equals(password)) {
                return u;
            }
        }
        return null;
    }

    public static List<User> listByRole(Role role) {
        List<User> out = new ArrayList<>();
        for (User u : users.values()) {
            if (u.role == role) out.add(u);
        }
        return out;
    }

    public static void removeUser(String userId) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM user WHERE id=?")) {
            pst.setString(1, userId);
            pst.executeUpdate();
            users.remove(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SUBJECTS =================
    public static void loadSubjects() {
        subjects.clear();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM subject")) {
            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String facultyId = rs.getString("faculty_id");
                subjects.put(id, new Subject(id, name, facultyId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addSubject(Subject s) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "INSERT INTO subject (id, name, faculty_id) VALUES (?, ?, ?)")) {
            pst.setString(1, s.id);
            pst.setString(2, s.name);
            pst.setString(3, s.facultyId);
            pst.executeUpdate();
            subjects.put(s.id, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateSubject(Subject s) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "UPDATE subject SET name=?, faculty_id=? WHERE id=?")) {
            pst.setString(1, s.name);
            pst.setString(2, s.facultyId);
            pst.setString(3, s.id);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeSubject(String subjectId) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement("DELETE FROM subject WHERE id=?")) {
            pst.setString(1, subjectId);
            pst.executeUpdate();
            subjects.remove(subjectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Subject> getSubjectsByFaculty(String facultyId) {
        List<Subject> list = new ArrayList<>();
        for (Subject s : subjects.values()) {
            if (facultyId.equals(s.facultyId)) list.add(s);
        }
        return list;
    }

    // ================= ATTENDANCE =================
    public static void markAttendance(String studentId, String subjectId, boolean present) {
        try (Connection con = getConnection()) {
            // Check if record exists
            String selectSql = "SELECT attendance_percentage FROM attendance_records WHERE student_id=? AND subject_id=?";
            PreparedStatement pstSelect = con.prepareStatement(selectSql);
            pstSelect.setString(1, studentId);
            pstSelect.setString(2, subjectId);
            ResultSet rs = pstSelect.executeQuery();

            double pct = 0;
            boolean exists = false;
            if (rs.next()) {
                pct = rs.getDouble("attendance_percentage");
                exists = true;
            }

            // Update percentage
            if (present) pct += 2;
            else pct -= 5;
            if (pct > 100) pct = 100;
            if (pct < 0) pct = 0;

            if (exists) {
                String updateSql = "UPDATE attendance_records SET attendance_percentage=? WHERE student_id=? AND subject_id=?";
                PreparedStatement pstUpdate = con.prepareStatement(updateSql);
                pstUpdate.setDouble(1, pct);
                pstUpdate.setString(2, studentId);
                pstUpdate.setString(3, subjectId);
                pstUpdate.executeUpdate();
            } else {
                String insertSql = "INSERT INTO attendance_records (student_id, subject_id, attendance_percentage) VALUES (?, ?, ?)";
                PreparedStatement pstInsert = con.prepareStatement(insertSql);
                pstInsert.setString(1, studentId);
                pstInsert.setString(2, subjectId);
                pstInsert.setDouble(3, pct);
                pstInsert.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getAttendancePercentage(String studentId, String subjectId) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT attendance_percentage FROM attendance_records WHERE student_id=? AND subject_id=?")) {
            pst.setString(1, studentId);
            pst.setString(2, subjectId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getDouble("attendance_percentage");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static List<AttendanceRecord> getRecordsForStudent(String studentId) {
        List<AttendanceRecord> records = new ArrayList<>();
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM attendance_records WHERE student_id=?")) {
            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                records.add(new AttendanceRecord(
                        rs.getString("student_id"),
                        rs.getString("subject_id"),
                        rs.getDouble("attendance_percentage")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    public static Map<String, Double> getAttendanceForStudentAllSubjects(String studentId) {
        Map<String, Double> map = new LinkedHashMap<>();
        for (Subject s : subjects.values()) {
            map.put(s.name, getAttendancePercentage(studentId, s.id));
        }
        return map;
    }
}
