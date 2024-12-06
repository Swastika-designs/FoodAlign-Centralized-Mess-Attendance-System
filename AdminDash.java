import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminDash {
    public AdminDash() {
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Food Align: Admin Dashboard");
        frame.setDefaultCloseOperation(3);
        frame.setSize(500, 400);
        frame.setResizable(false);
        ImageIcon frameIcon = new ImageIcon("dish1.png");
        frame.setIconImage(frameIcon.getImage());
        frame.getContentPane().setBackground(new Color(230, 210, 250));
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = 1;
        Color purple = new Color(128, 0, 128);
        Color lightPurple = new Color(216, 191, 216);
        JLabel titleLabel = new JLabel("Admin Dashboard - Overview", 0);
        titleLabel.setFont(new Font("Arial", 1, 20));
        titleLabel.setForeground(purple);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        frame.add(titleLabel, gbc);
        JPanel mealPanel = new JPanel();
        mealPanel.setLayout(new GridLayout(3, 2, 10, 10));
        JLabel breakfastLabel = new JLabel("Breakfast Attendance:");
        breakfastLabel.setForeground(purple);
        final JTextField breakfastCount = new JTextField("0", 5);
        breakfastCount.setEditable(false);
        breakfastCount.setForeground(purple);
        JLabel lunchLabel = new JLabel("Lunch Attendance:");
        lunchLabel.setForeground(purple);
        final JTextField lunchCount = new JTextField("0", 5);
        lunchCount.setEditable(false);
        lunchCount.setForeground(purple);
        JLabel dinnerLabel = new JLabel("Dinner Attendance:");
        dinnerLabel.setForeground(purple);
        final JTextField dinnerCount = new JTextField("0", 5);
        dinnerCount.setEditable(false);
        dinnerCount.setForeground(purple);
        mealPanel.add(breakfastLabel);
        mealPanel.add(breakfastCount);
        mealPanel.add(lunchLabel);
        mealPanel.add(lunchCount);
        mealPanel.add(dinnerLabel);
        mealPanel.add(dinnerCount);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        frame.add(mealPanel, gbc);
        JPanel attendancePanel = new JPanel();
        attendancePanel.setLayout(new GridLayout(2, 1, 10, 10));
        JLabel attendanceLabel = new JLabel("Overall Attendance Rate:");
        attendanceLabel.setForeground(purple);
        final JTextField attendanceRate = new JTextField("0%", 5);
        attendanceRate.setEditable(false);
        attendanceRate.setForeground(purple);
        attendancePanel.add(attendanceLabel);
        attendancePanel.add(attendanceRate);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.5;
        frame.add(attendancePanel, gbc);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.setPreferredSize(new Dimension(100, 30));
        refreshButton.setBackground(lightPurple);
        refreshButton.setForeground(purple);
        refreshButton.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = 10;
        frame.add(refreshButton, gbc);
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodalign", "root", "password");
                    Statement statement = connection.createStatement();
                    ResultSet totalStudentsResult = statement.executeQuery("SELECT COUNT(DISTINCT student_id) AS total_students FROM attendance");
                    totalStudentsResult.next();
                    int totalStudents = totalStudentsResult.getInt("total_students");
                    ResultSet breakfastResult = statement.executeQuery("SELECT COUNT(*) AS breakfast_attendance FROM attendance WHERE meal_type = 'Breakfast' AND attendance_status = 'Yes'");
                    breakfastResult.next();
                    int breakfastAttendance = breakfastResult.getInt("breakfast_attendance");
                    ResultSet lunchResult = statement.executeQuery("SELECT COUNT(*) AS lunch_attendance FROM attendance WHERE meal_type = 'Lunch' AND attendance_status = 'Yes'");
                    lunchResult.next();
                    int lunchAttendance = lunchResult.getInt("lunch_attendance");
                    ResultSet dinnerResult = statement.executeQuery("SELECT COUNT(*) AS dinner_attendance FROM attendance WHERE meal_type = 'Dinner' AND attendance_status = 'Yes'");
                    dinnerResult.next();
                    int dinnerAttendance = dinnerResult.getInt("dinner_attendance");
                    int breakfastPercentage = totalStudents > 0 ? breakfastAttendance * 100 / totalStudents : 0;
                    int lunchPercentage = totalStudents > 0 ? lunchAttendance * 100 / totalStudents : 0;
                    int dinnerPercentage = totalStudents > 0 ? dinnerAttendance * 100 / totalStudents : 0;
                    int totalAttendance = breakfastAttendance + lunchAttendance + dinnerAttendance;
                    int averageAttendanceRate = totalStudents > 0 ? totalAttendance * 100 / (totalStudents * 3) : 0;
                    breakfastCount.setText(String.valueOf(breakfastPercentage));
                    lunchCount.setText(String.valueOf(lunchPercentage));
                    dinnerCount.setText(String.valueOf(dinnerPercentage));
                    attendanceRate.setText("" + averageAttendanceRate + "%");
                    totalStudentsResult.close();
                    breakfastResult.close();
                    lunchResult.close();
                    dinnerResult.close();
                    statement.close();
                    connection.close();
                } catch (SQLException var17) {
                    SQLException ex = var17;
                    JOptionPane.showMessageDialog(frame, "Error refreshing data: " + ex.getMessage(), "Error", 0);
                }

            }
        });
        JButton mealCountManagementButton = new JButton("Meal Count Management");
        mealCountManagementButton.setPreferredSize(new Dimension(150, 30));
        mealCountManagementButton.setBackground(lightPurple);
        mealCountManagementButton.setForeground(purple);
        mealCountManagementButton.setFocusPainted(false);
        gbc.gridy = 3;
        frame.add(mealCountManagementButton, gbc);
        mealCountManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new MealCount();
            }
        });
        JButton profileManagementButton = new JButton("Profile Management");
        profileManagementButton.setPreferredSize(new Dimension(150, 30));
        profileManagementButton.setBackground(lightPurple);
        profileManagementButton.setForeground(purple);
        profileManagementButton.setFocusPainted(false);
        gbc.gridy = 4;
        frame.add(profileManagementButton, gbc);
        profileManagementButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ProfileManage pm = new ProfileManage();
                pm.setVisible(true);
            }
        });
        frame.setVisible(true);
    }
}
