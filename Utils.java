package com.attendancetracker;

import javax.swing.*;
import java.awt.*;

class Utils {
    static String shortId(){ return java.util.UUID.randomUUID().toString().substring(0,6); }

    /**
     * Creates a styled button (red, '𖠿') that closes the current frame and opens the LoginFrame.
     * @param currentFrame The JFrame instance to be closed (e.g., 'this').
     * @return The styled JButton.
     */
    public static JButton createGoHomeButton(JFrame currentFrame) {
        // Requested symbol: 𖠿
        JButton homeBtn = new JButton("𖠿");
        // Using a standard font to avoid system-specific font errors.
        homeBtn.setFont(new Font("Dialog", Font.BOLD, 18)); 
        homeBtn.setBackground(new Color(220, 50, 50)); // Red color
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.setPreferredSize(new Dimension(50, 50));
        homeBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        homeBtn.setToolTipText("Go to Login Screen");

        // Action: Close current window and open a new LoginFrame
        homeBtn.addActionListener(e -> {
            currentFrame.dispose();
            new LoginFrame().setVisible(true);
        });

        return homeBtn;
    }
}