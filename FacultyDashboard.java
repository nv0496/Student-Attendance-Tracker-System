package com.attendancetracker;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FacultyDashboard extends JFrame {
    User me;
    DefaultTableModel tableModel;
    JComboBox<Subject> subjectBox;
    private Image bgImage;

    public FacultyDashboard(User me) {
        this.me = me;
        setTitle("Faculty Dashboard - " + me.name);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bgImage = new ImageIcon("C:/Users/ABCD/OneDrive/Desktop/app_project/com/attendancetracker/background1.jpg").getImage();

        JPanel bgPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null)
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        bgPanel.setLayout(new BorderLayout());
        setContentPane(bgPanel);

        JLabel header = new JLabel("Faculty Panel - Mark Attendance", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(Color.WHITE);
        bgPanel.add(header, BorderLayout.NORTH);

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.add(new JLabel("Select Subject:"));

        subjectBox = new JComboBox<>();
        loadFacultySubjects();
        top.add(subjectBox);

        JButton loadBtn = new JButton("Load Students");
        styleBtn(loadBtn);
        top.add(loadBtn);
        bgPanel.add(top, BorderLayout.NORTH);

        String[] columns = {"Student ID", "Name", "Present"};
        tableModel = new DefaultTableModel(columns, 0) {
            public Class<?> getColumnClass(int c) {
                return c == 2 ? Boolean.class : String.class;
            }

            public boolean isCellEditable(int r, int c) {
                return c == 2;
            }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollPane = new JScrollPane(table);
        bgPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);

        JButton markBtn = new JButton("Save Attendance");
        JButton viewBtn = new JButton("View Stats");
        styleBtn(markBtn);
        styleBtn(viewBtn);
        bottom.add(markBtn);
        bottom.add(viewBtn);
        bgPanel.add(bottom, BorderLayout.SOUTH);

        loadBtn.addActionListener(e -> loadStudents());
        markBtn.addActionListener(e -> saveAttendance());
        viewBtn.addActionListener(e -> viewStats());
    }

    private void loadFacultySubjects() {
        subjectBox.removeAllItems();
        List<Subject> mySubjects = DBManager.getSubjectsByFaculty(me.id);
        for (Subject s : mySubjects) subjectBox.addItem(s);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        for (User u : DBManager.listByRole(Role.STUDENT))
            tableModel.addRow(new Object[]{u.id, u.name, false});
    }
    private void saveAttendance() {
     Subject s = (Subject) subjectBox.getSelectedItem();
     if (s == null) return;

     for (int i = 0; i < tableModel.getRowCount(); i++) {
        String sid = (String) tableModel.getValueAt(i, 0);
        boolean present = (Boolean) tableModel.getValueAt(i, 2); // checked = present, unchecked = absent
        DBManager.markAttendance(sid, s.id, present);
     }
     JOptionPane.showMessageDialog(this, "Attendance saved!");
 }

    private void viewStats() {
     Subject s = (Subject) subjectBox.getSelectedItem();
     if (s == null) return;

     StringBuilder sb = new StringBuilder("Subject: " + s.name + "\n\n");
     sb.append(String.format("%-10s %-20s %-10s\n", "SID", "Name", "Attendance %"));
     for (User u : DBManager.listByRole(Role.STUDENT)) {
         double pct = DBManager.getAttendancePercentage(u.id, s.id);
         sb.append(String.format("%-10s %-20s %-10.1f\n", u.id, u.name, pct));
     }
     JOptionPane.showMessageDialog(this, sb.toString());
     }

    private void styleBtn(JButton b) {
        b.setBackground(new Color(45, 137, 239));
        b.setForeground(Color.white);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }
}

