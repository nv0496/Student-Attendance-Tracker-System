package com.attendancetracker;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    User me;
    DefaultListModel<User> facultyListModel = new DefaultListModel<>();
    DefaultListModel<User> studentListModel = new DefaultListModel<>();
    DefaultListModel<Subject> subjectListModel = new DefaultListModel<>();
    private DefaultComboBoxModel<Subject> subjectComboBoxModel = new DefaultComboBoxModel<>();
    private Image bgImage;

    public AdminDashboard(User me) {
        this.me = me;
        setTitle("Admin Dashboard - " + me.name);
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load background
        String imagePath = "C:/Users/ABCD/OneDrive/Desktop/app_project/com/attendancetracker/background4.jpg";
        try {
            bgImage = new ImageIcon(imagePath).getImage();
        } catch (Exception e) {
            System.err.println("Error loading background: " + e.getMessage());
            bgImage = null;
        }

        JPanel bgPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        bgPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        // Header
        JLabel header = new JLabel("Administrator Panel");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        bgPanel.add(header, BorderLayout.NORTH);

        // Main center layout
        JPanel center = new JPanel(new GridLayout(1, 3, 12, 12));
        center.setOpaque(false);

        // -----------------------------
        // FACULTY PANEL
        // -----------------------------
        JPanel facultyPanel = new JPanel(new BorderLayout());
        facultyPanel.setBackground(new Color(210, 255, 210));
        facultyPanel.setBorder(BorderFactory.createTitledBorder("Faculty (Assign One Subject)"));

        JList<User> facultyList = new JList<>(facultyListModel);
        facultyList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane facultyScroll = new JScrollPane(facultyList);
        facultyPanel.add(facultyScroll, BorderLayout.CENTER);

        JPanel fcontrols = new JPanel(new GridLayout(4, 2, 5, 5));
        fcontrols.setOpaque(false);
        fcontrols.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JTextField fuser = new JTextField(10), fpass = new JTextField(10), fname = new JTextField(10);
        JComboBox<Subject> fSubject = new JComboBox<>(subjectComboBoxModel);
        fSubject.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Subject) setText(((Subject) value).name);
                return this;
            }
        });

        fcontrols.add(new JLabel("Username:")); fcontrols.add(fuser);
        fcontrols.add(new JLabel("Password:")); fcontrols.add(fpass);
        fcontrols.add(new JLabel("Name:")); fcontrols.add(fname);
        fcontrols.add(new JLabel("Subject:")); fcontrols.add(fSubject);

        JPanel fbtnPanel = new JPanel();
        fbtnPanel.setOpaque(false);
        JButton addFaculty = new JButton("Add Faculty");
        JButton removeFaculty = new JButton("Remove Selected");
        styleBtn(addFaculty);
        styleBtn(removeFaculty);
        fbtnPanel.add(addFaculty);
        fbtnPanel.add(removeFaculty);

        JPanel facultyBottom = new JPanel(new BorderLayout());
        facultyBottom.setOpaque(false);
        facultyBottom.add(fcontrols, BorderLayout.CENTER);
        facultyBottom.add(fbtnPanel, BorderLayout.SOUTH);
        facultyPanel.add(facultyBottom, BorderLayout.SOUTH);

        JScrollPane facultyWrapper = new JScrollPane(facultyPanel);
        facultyWrapper.setOpaque(false);
        facultyWrapper.getViewport().setOpaque(false);

        // -----------------------------
        // STUDENT PANEL
        // -----------------------------
        JPanel studentPanel = new JPanel(new BorderLayout());
        studentPanel.setBackground(new Color(255, 255, 210));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Students"));

        JList<User> studentList = new JList<>(studentListModel);
        studentList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane studentScroll = new JScrollPane(studentList);
        studentPanel.add(studentScroll, BorderLayout.CENTER);

        JPanel scontrols = new JPanel(new GridLayout(3, 2, 5, 5));
        scontrols.setOpaque(false);
        scontrols.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JTextField suser = new JTextField(10), spass = new JTextField(10), sname = new JTextField(10);
        scontrols.add(new JLabel("Username:")); scontrols.add(suser);
        scontrols.add(new JLabel("Password:")); scontrols.add(spass);
        scontrols.add(new JLabel("Name:")); scontrols.add(sname);

        JPanel sbtnPanel = new JPanel();
        sbtnPanel.setOpaque(false);
        JButton addStudent = new JButton("Add Student");
        JButton removeStudent = new JButton("Remove Selected");
        styleBtn(addStudent);
        styleBtn(removeStudent);
        sbtnPanel.add(addStudent);
        sbtnPanel.add(removeStudent);

        JPanel studentBottom = new JPanel(new BorderLayout());
        studentBottom.setOpaque(false);
        studentBottom.add(scontrols, BorderLayout.CENTER);
        studentBottom.add(sbtnPanel, BorderLayout.SOUTH);
        studentPanel.add(studentBottom, BorderLayout.SOUTH);

        JScrollPane studentWrapper = new JScrollPane(studentPanel);
        studentWrapper.setOpaque(false);
        studentWrapper.getViewport().setOpaque(false);

        // -----------------------------
        // SUBJECT PANEL
        // -----------------------------
        JPanel subjPanel = new JPanel(new BorderLayout());
        subjPanel.setBackground(new Color(255, 230, 200));
        subjPanel.setBorder(BorderFactory.createTitledBorder("Subjects"));

        JList<Subject> subjList = new JList<>(subjectListModel);
        subjList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subjPanel.add(new JScrollPane(subjList), BorderLayout.CENTER);

        JPanel subControls = new JPanel();
        subControls.setOpaque(false);
        JTextField subName = new JTextField(12);
        subControls.add(new JLabel("Subject Name:"));
        subControls.add(subName);
        JButton addSub = new JButton("Add Subject");
        JButton removeSub = new JButton("Remove Selected");
        styleBtn(addSub);
        styleBtn(removeSub);
        subControls.add(addSub);
        subControls.add(removeSub);
        subjPanel.add(subControls, BorderLayout.SOUTH);

        center.add(facultyWrapper);
        center.add(studentWrapper);
        center.add(subjPanel);
        bgPanel.add(center, BorderLayout.CENTER);

        // Bottom View Attendance button
        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        JButton viewAll = new JButton("View All Student Attendance");
        styleBtn(viewAll);
        bottom.add(viewAll);
        bgPanel.add(bottom, BorderLayout.SOUTH);

        // -----------------------------
        // Actions
        // -----------------------------
        reloadLists();

        // Add Faculty
        addFaculty.addActionListener(e -> {
            String un = fuser.getText().trim(), pw = fpass.getText().trim(), nm = fname.getText().trim();
            Subject selectedSubject = (Subject) fSubject.getSelectedItem();
            if (un.isEmpty() || pw.isEmpty() || nm.isEmpty() || selectedSubject == null) return;

            String id = "F" + Utils.shortId();
            User newFaculty = new User(id, un, pw, nm, Role.FACULTY);
            newFaculty.assignedSubjectId = selectedSubject.id;
            DBManager.addUser(newFaculty);
            reloadLists();
            fuser.setText(""); fpass.setText(""); fname.setText("");
        });

        // Remove Faculty
        removeFaculty.addActionListener(e -> {
            User sel = facultyList.getSelectedValue();
            if (sel == null || sel.username.equals("admin")) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Delete " + sel.name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DBManager.removeUser(sel.id);
                reloadLists();
            }
        });

        // Add Student
        addStudent.addActionListener(e -> {
            String un = suser.getText().trim(), pw = spass.getText().trim(), nm = sname.getText().trim();
            if (un.isEmpty() || pw.isEmpty() || nm.isEmpty()) return;
            String id = "S" + Utils.shortId();
            DBManager.addUser(new User(id, un, pw, nm, Role.STUDENT));
            reloadLists();
            suser.setText(""); spass.setText(""); sname.setText("");
        });

        // Remove Student
        removeStudent.addActionListener(e -> {
            User sel = studentList.getSelectedValue();
            if (sel == null || sel.username.equals("admin")) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Delete " + sel.name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DBManager.removeUser(sel.id);
                reloadLists();
            }
        });

        // Add Subject
        addSub.addActionListener(e -> {
            String snameStr = subName.getText().trim();
            if (snameStr.isEmpty()) return;
            String id = "SUB" + Utils.shortId();
            DBManager.addSubject(new Subject(id, snameStr));
            reloadLists();
            subName.setText("");
        });

        // Remove Subject
        removeSub.addActionListener(e -> {
            Subject s = subjList.getSelectedValue();
            if (s == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Delete subject " + s.name + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DBManager.removeSubject(s.id);
                reloadLists();
            }
        });

        // View Attendance
        viewAll.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (User u : DBManager.listByRole(Role.STUDENT)) {
                sb.append(u.name).append(" (").append(u.username).append(")\n");
                for (Subject s : DBManager.subjects.values()) {
                    double pct = DBManager.getAttendancePercentage(u.id, s.id);
                    sb.append("  ").append(s.name).append(": ").append(String.format("%.1f%%", pct)).append("\n");
                }
                sb.append("\n");
            }
            JTextArea ta = new JTextArea(sb.toString());
            ta.setEditable(false);
            JScrollPane sp = new JScrollPane(ta);
            sp.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, sp, "All Student Attendance", JOptionPane.INFORMATION_MESSAGE);
        });

        setContentPane(bgPanel);
    }

    private void styleBtn(JButton b) {
        b.setBackground(new Color(45, 137, 239));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
    }

    private void reloadLists() {
        facultyListModel.clear();
        studentListModel.clear();
        subjectListModel.clear();
        subjectComboBoxModel.removeAllElements();

        for (User u : DBManager.users.values()) {
            if (u.role == Role.FACULTY) facultyListModel.addElement(u);
            else if (u.role == Role.STUDENT) studentListModel.addElement(u);
        }
        for (Subject s : DBManager.subjects.values()) {
            subjectListModel.addElement(s);
            subjectComboBoxModel.addElement(s);
        }
    }
}
