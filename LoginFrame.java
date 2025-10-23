package com.attendancetracker;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<Role> roleBox;
    private Image bgImage; 

    public LoginFrame(){
        setTitle("Attendance Tracker - Login");
        setSize(550, 480); // Slightly increased size for better padding/look
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(false);
        
        // --- 1. Load Background Image ---
        String imagePath = "C:/Users/ABCD/OneDrive/Desktop/app_project/com/attendancetracker/background5.jpg";
        try {
            bgImage = new ImageIcon(imagePath).getImage();
            if (bgImage == null || bgImage.getWidth(null) == -1) {
                bgImage = null;
                System.err.println("Error: Could not load image from path.");
            }
        } catch (Exception e) {
            System.err.println("Exception while loading image: " + e.getMessage());
            bgImage = null;
        }

        // --- 2. Custom Background Panel ---
        JPanel bgPanel = new JPanel(new GridBagLayout()) { // Use GridBagLayout to center the form panel
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(DarkTheme.BG);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        setContentPane(bgPanel);

        // --- 3. Central Login Card Panel ---
        JPanel loginCard = new JPanel(new BorderLayout());
        loginCard.setBackground(new Color(28, 30, 34, 230)); // Less transparent than before
        loginCard.setPreferredSize(new Dimension(380, 380));
        
        // Add a subtle border/shadow effect to the card
        loginCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DarkTheme.ACCENT, 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Title
        JLabel title = new JLabel("ATTENDANCE TRACKER");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(DarkTheme.ACCENT_GLOW);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        loginCard.add(title, BorderLayout.NORTH);

        // Form Panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false); // Keep this transparent so loginCard background shows
        
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0; // Allow components to stretch

        // --- Username ---
        c.gridx=0; c.gridy=0; c.gridwidth=2;
        // 🔥 FIX: Added (JTextField) cast
        usernameField = (JTextField) createStyledTextField(false); 
        form.add(createInputPanel("Username:", usernameField), c);

        // --- Password ---
        c.gridy=1;
        // 🔥 FIX: Added (JPasswordField) cast
        passwordField = (JPasswordField) createStyledTextField(true);
        form.add(createInputPanel("Password:", passwordField), c);

        // --- Role ---
        c.gridy=2;
        roleBox = createStyledRoleBox();
        form.add(createInputPanel("Role:", roleBox), c);

        loginCard.add(form, BorderLayout.CENTER);

        // --- Button Panel ---
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        btns.setOpaque(false);
        
        JButton login = new JButton("LOGIN");
        styleButton(login);
        login.addActionListener(e-> doLogin());
        
        JButton cancel = new JButton("CANCEL"); 
        styleButton(cancel);
        cancel.addActionListener(e-> System.exit(0));
        
        btns.add(login); 
        btns.add(cancel);
        loginCard.add(btns, BorderLayout.SOUTH);

        // Add the central card to the center of the background panel
        bgPanel.add(loginCard); 
    }

    /** Helper to wrap a label and component in a clean panel **/
    private JPanel createInputPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText); 
        label.setForeground(DarkTheme.FG);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
    
    /** Helper to create a unified look for text/password fields **/
    private JComponent createStyledTextField(boolean isPassword) {
        JComponent field = isPassword ? new JPasswordField() : new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setBackground(new Color(50, 50, 55)); // Darker background for input area
        field.setForeground(DarkTheme.FG);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 65), 1));
        return field;
    }

    /** Helper to style the JComboBox **/
    private JComboBox<Role> createStyledRoleBox() {
        JComboBox<Role> box = new JComboBox<>(Role.values());
        box.setPreferredSize(new Dimension(200, 30));
        box.setBackground(new Color(50, 50, 55));
        box.setForeground(DarkTheme.FG);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        return box;
    }

    /** Helper to style buttons **/
    private void styleButton(JButton b){
        b.setFocusPainted(false);
        b.setBackground(DarkTheme.ACCENT);
        b.setForeground(Color.white);
        b.setPreferredSize(new Dimension(140, 40)); // Slightly taller buttons
        b.setBorder(BorderFactory.createLineBorder(DarkTheme.ACCENT_GLOW, 2));
        b.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private void doLogin(){
        String u = usernameField.getText().trim();
        String p = new String(passwordField.getPassword());
        Role chosen = (Role) roleBox.getSelectedItem();
        User user = DBManager.authenticate(u,p);
        
        if(user==null || user.role!=chosen){
            JOptionPane.showMessageDialog(this,"Invalid credentials or role mismatch.","Login failed",JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        dispose();
        if(user.role==Role.ADMIN) new AdminDashboard(user).setVisible(true);
        else if(user.role==Role.FACULTY) new FacultyDashboard(user).setVisible(true);
        else if(user.role==Role.STUDENT) new StudentDashboard(user).setVisible(true);
    }
}