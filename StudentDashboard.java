package com.attendancetracker;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StudentDashboard extends JFrame {
    User me;
    private Image bgImage;
    private double overall = 0;

    public StudentDashboard(User me) {
        this.me = me;
        setTitle("Student Dashboard - " + me.name);
        setSize(800, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        bgImage = new ImageIcon("C:/Users/ABCD/OneDrive/Desktop/app_project/com/attendancetracker/background3.jpg").getImage();

        JPanel bgPanel = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null)
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JLabel header = new JLabel("My Attendance");
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(Color.WHITE);
        bgPanel.add(header, BorderLayout.NORTH);

        Map<String, Double> all = DBManager.getAttendanceForStudentAllSubjects(me.id);

        if (!all.isEmpty()) {
            double total = 0;
            for (double val : all.values()) total += val;
            overall = total / all.size();
        }

        JPanel left = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth(), h = getHeight();
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = Math.min(w, h) - 40;
                int x = (w - size) / 2, y = 20;

                g2.setColor(new Color(40, 40, 45, 180));
                g2.fillOval(x, y, size, size);

                int arc = (int) Math.round(overall * 360 / 100.0);
                g2.setStroke(new BasicStroke(12.0f));
                g2.setColor(Color.CYAN);
                g2.drawArc(x + 12, y + 12, size - 24, size - 24, 90, -arc);

                g2.setFont(new Font("Segoe UI", Font.BOLD, 32));
                g2.setColor(Color.WHITE);
                String txt = String.format("%.0f%%", overall);
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(txt);
                g2.drawString(txt, x + (size - tw) / 2, y + size / 2 + fm.getAscent() / 2);
            }
        };
        left.setPreferredSize(new Dimension(300, 300));
        left.setOpaque(false);

        DefaultListModel<String> lm = new DefaultListModel<>();
        for (Subject s : DBManager.subjects.values()) {
            double pct = DBManager.getAttendancePercentage(me.id, s.id);
            lm.addElement(s.name + " — " + String.format("%.1f", pct) + "%");
        }

        JList<String> subjList = new JList<>(lm);
        subjList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjList.setBackground(new Color(255, 255, 200));
        JScrollPane scrollPane = new JScrollPane(subjList);

        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        JLabel lbl = new JLabel("Subjects & Attendance");
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        right.add(lbl, BorderLayout.NORTH);
        right.add(scrollPane, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout());
        center.setOpaque(false);
        center.add(left, BorderLayout.WEST);
        center.add(right, BorderLayout.CENTER);

        bgPanel.add(center, BorderLayout.CENTER);
        setContentPane(bgPanel);
    }
}
