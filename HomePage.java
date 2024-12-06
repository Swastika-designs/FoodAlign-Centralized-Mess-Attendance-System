import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class HomePage {
    public HomePage() {
    }

    public static void main(String[] args) {
        final JFrame homeFrame = new JFrame("Food Align: Home Page");
        homeFrame.setSize(500, 400);
        homeFrame.setDefaultCloseOperation(3);
        homeFrame.setLayout((LayoutManager)null);
        homeFrame.setResizable(false);
        ImageIcon fm = new ImageIcon("dish1.png");
        homeFrame.setIconImage(fm.getImage());
        homeFrame.getContentPane().setBackground(new Color(230, 210, 250));
        JLabel goalLabel = new JLabel("<html><h1>Welcome to Food Align</h1><br>Food Align helps to minimize food wastage by tracking meal attendance and optimizing food preparation based on prior meal count.We're grateful for your participation in Food Align.Your involvement is crucial in helping us achieve our goal of reducing food waste and creating a more sustainable future. <br><br>Click the button below to log in.</html>", 0);
        goalLabel.setBounds(30, 50, 440, 200);
        goalLabel.setForeground(new Color(128, 0, 128));
        homeFrame.add(goalLabel);
        JButton loginButton = new JButton("Go to Login");
        loginButton.setBounds(180, 280, 120, 40);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(128, 0, 128));
        homeFrame.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HomePage.showLoginPage();
                homeFrame.dispose();
            }
        });
        homeFrame.setVisible(true);
    }

    public static void showLoginPage() {
        final JFrame frame = new JFrame("Food Align: Login Page");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(3);
        frame.setLayout((LayoutManager)null);
        frame.setResizable(false);
        ImageIcon frameIcon = new ImageIcon("dish1.png");
        frame.setIconImage(frameIcon.getImage());
        frame.getContentPane().setBackground(new Color(230, 210, 250));
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 50, 100, 30);
        emailLabel.setForeground(new Color(128, 0, 128));
        frame.add(emailLabel);
        final JTextField emailField = new JTextField();
        emailField.setBounds(150, 50, 200, 30);
        frame.add(emailField);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 100, 30);
        passwordLabel.setForeground(new Color(128, 0, 128));
        frame.add(passwordLabel);
        final JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 200, 30);
        frame.add(passwordField);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 150, 100, 30);
        roleLabel.setForeground(new Color(128, 0, 128));
        frame.add(roleLabel);
        String[] roles = new String[]{"Admin", "Student"};
        final JComboBox<String> roleComboBox = new JComboBox(roles);
        roleComboBox.setBounds(150, 150, 200, 30);
        roleComboBox.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? new Color(128, 0, 128) : Color.WHITE);
                c.setForeground(isSelected ? Color.WHITE : Color.BLACK);
                return c;
            }
        });
        frame.add(roleComboBox);
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 200, 100, 30);
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(128, 0, 128));
        frame.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String)roleComboBox.getSelectedItem();
                HomePage.authenticateUser(email, password, role, frame);
            }
        });
        frame.setVisible(true);
    }

    public static void authenticateUser(String email, String password, String role, JFrame frame) {
        String url = "jdbc:mysql://localhost:3306/foodalign";
        String dbUsername = "root";
        String dbPassword = "password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, dbUsername, dbPassword);

            try {
                String query = "SELECT id  FROM users WHERE email = ? AND password = ? AND role = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, email);
                stmt.setString(2, password);
                stmt.setString(3, role);
                System.out.println("Executing query: " + query);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
                System.out.println("Role: " + role);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    System.out.println(role + " found: " + email);
                    if (role.equals("Admin")) {
                        System.out.println("Admin found");
                        AdminDash.main((String[])null);
                    } else {
                        int studentId = rs.getInt("id");
                        System.out.println("Student found with ID: " + studentId);
                        StudentDash sd = new StudentDash(studentId);
                        sd.frame.setVisible(true);
                    }

                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid email or password");
                }
            } catch (Throwable var14) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var13) {
                        var14.addSuppressed(var13);
                    }
                }

                throw var14;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (ClassNotFoundException var15) {
            ClassNotFoundException e = var15;
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "JDBC Driver not found");
        } catch (SQLException var16) {
            SQLException ex = var16;
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database error occurred");
        }

    }
}
