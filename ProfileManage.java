import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ProfileManage extends JFrame {
    public ProfileManage() {
        this.setTitle("Food Align: Profile Management");
        this.setDefaultCloseOperation(2);
        this.setSize(500, 400);
        this.setLayout(new GridLayout(6, 2, 10, 10));
        this.getContentPane().setBackground(new Color(230, 210, 250));
        ImageIcon frameIcon = new ImageIcon("dish1.png");
        this.setIconImage(frameIcon.getImage());
        Color textColor = new Color(128, 0, 128);
        Color buttonColor = new Color(216, 191, 216);
        Color dropdownColor = new Color(230, 204, 255);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setForeground(textColor);
        String[] roles = new String[]{"Admin", "Student"};
        final JComboBox<String> roleComboBox = new JComboBox(roles);
        roleComboBox.setBackground(dropdownColor);
        JLabel oldEmailLabel = new JLabel("Email:");
        oldEmailLabel.setForeground(textColor);
        final JTextField oldEmailField = new JTextField();
        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordLabel.setForeground(textColor);
        final JPasswordField oldPasswordField = new JPasswordField();
        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordLabel.setForeground(textColor);
        final JPasswordField newPasswordField = new JPasswordField();
        JButton updateButton = new JButton("Update Password");
        updateButton.setBackground(buttonColor);
        updateButton.setForeground(textColor);
        this.add(roleLabel);
        this.add(roleComboBox);
        this.add(oldEmailLabel);
        this.add(oldEmailField);
        this.add(oldPasswordLabel);
        this.add(oldPasswordField);
        this.add(newPasswordLabel);
        this.add(newPasswordField);
        this.add(new JLabel());
        this.add(updateButton);
        updateButton.addActionListener(new ActionListener(this) {
            public void actionPerformed(ActionEvent e) {
                String role = (String)roleComboBox.getSelectedItem();
                String oldEmail = oldEmailField.getText();
                String oldPassword = new String(oldPasswordField.getPassword());
                String newPassword = new String(newPasswordField.getPassword());
                ProfileManage.updatePassword(role, oldEmail, oldPassword, newPassword);
            }
        });
    }

    public static void updatePassword(String role, String oldEmail, String oldPassword, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/foodalign";
        String dbUsername = "root";
        String dbPassword = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            try {
                System.out.println("Role: " + role);
                System.out.println("Old Email: " + oldEmail);
                System.out.println("Old Password: " + oldPassword);
                String query = "UPDATE users SET password = ? WHERE email = ? AND password = ? AND role = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, newPassword);
                stmt.setString(2, oldEmail);
                stmt.setString(3, oldPassword);
                stmt.setString(4, role);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog((Component)null, "Password updated successfully!");
                } else {
                    JOptionPane.showMessageDialog((Component)null, "Invalid email or old password.");
                }
            } catch (Throwable var12) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                    }
                }

                throw var12;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (ClassNotFoundException var13) {
            ClassNotFoundException e = var13;
            e.printStackTrace();
            JOptionPane.showMessageDialog((Component)null, "JDBC Driver not found");
        } catch (SQLException var14) {
            SQLException ex = var14;
            ex.printStackTrace();
            JOptionPane.showMessageDialog((Component)null, "Database error occurred");
        }

    }

    public static void main(String[] args) {
        ProfileManage profileManage = new ProfileManage();
        profileManage.setVisible(true);
    }
}
