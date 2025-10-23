package com.attendancetracker;

import javax.swing.*;
import java.awt.*;

class Utils {
    static String shortId(){ return java.util.UUID.randomUUID().toString().substring(0,6); }


    public static JButton createGoHomeButton(JFrame currentFrame) {

        JButton homeBtn = new JButton("𖠿");

        homeBtn.setFont(new Font("Dialog", Font.BOLD, 18)); 
        homeBtn.setBackground(new Color(220, 50, 50)); 
        homeBtn.setForeground(Color.WHITE);
        homeBtn.setFocusPainted(false);
        homeBtn.setPreferredSize(new Dimension(50, 50));
        homeBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 100, 100), 2));
        homeBtn.setToolTipText("Go to Login Screen");


        homeBtn.addActionListener(e -> {
            currentFrame.dispose();
            new LoginFrame().setVisible(true);
        });

        return homeBtn;
    }
}
