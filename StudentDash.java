import java.awt.*;
import java.sql.*;
import java.time.*;
import java.util.Locale;
import javax.swing.*;

public class StudentDash {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/foodalign";
    private static final String USER = "root";
    private static final String PASS = "password";
    private int studentId;
    public JFrame frame;

    public StudentDash(int studentId) {
        this.studentId = studentId;
        this.createAndShowGUI();
    }

    private void createAndShowGUI() {
        this.frame = new JFrame("Food Align: Student Dashboard");
        this.frame.setDefaultCloseOperation(3);
        this.frame.setSize(600, 300);
        this.frame.setLayout(new GridBagLayout());
        this.frame.setResizable(false);
        ImageIcon frameIcon = new ImageIcon("dish1.png");
        this.frame.setIconImage(frameIcon.getImage());
        this.frame.getContentPane().setBackground(new Color(230, 210, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = 1;
        Color purple = new Color(128, 0, 128);
        Color mediumPurple = new Color(221, 160, 221);
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints menuGbc = new GridBagConstraints();
        menuGbc.insets = new Insets(5, 5, 5, 5);
        menuGbc.fill = 2;
        menuGbc.weightx = 1.0;
        JLabel menuLabel = new JLabel("Today's Menu:");
        menuLabel.setFont(new Font("Arial", 0, 14));
        menuLabel.setForeground(purple);
        menuGbc.gridx = 0;
        menuGbc.gridy = 0;
        menuPanel.add(menuLabel, menuGbc);
        JTextArea menuTextArea = new JTextArea(10, 25);
        menuTextArea.setEditable(false);
        menuTextArea.setBackground(mediumPurple);
        menuTextArea.setForeground(Color.WHITE);
        menuGbc.gridy = 1;
        menuPanel.add(new JScrollPane(menuTextArea), menuGbc);
        this.loadMenuForToday(menuTextArea);
        JPanel attendancePanel = new JPanel();
        attendancePanel.setLayout(new GridBagLayout());
        GridBagConstraints attendanceGbc = new GridBagConstraints();
        attendanceGbc.insets = new Insets(5, 5, 5, 5);
        attendanceGbc.anchor = 17;
        attendanceGbc.fill = 2;
        attendanceGbc.weightx = 1.0;
        JLabel attendanceLabel = new JLabel("Attendance:");
        attendanceLabel.setFont(new Font("Arial", 1, 16));
        attendanceLabel.setForeground(purple);
        attendanceGbc.gridwidth = 2;
        attendanceGbc.gridx = 0;
        attendanceGbc.gridy = 0;
        attendancePanel.add(attendanceLabel, attendanceGbc);
        JLabel breakfastLabel = new JLabel("Breakfast:");
        breakfastLabel.setForeground(purple);
        attendanceGbc.gridwidth = 1;
        attendanceGbc.gridx = 0;
        attendanceGbc.gridy = 1;
        attendancePanel.add(breakfastLabel, attendanceGbc);
        final JRadioButton breakfastYes = new JRadioButton("Yes");
        JRadioButton breakfastNo = new JRadioButton("No");
        breakfastYes.setForeground(purple);
        breakfastNo.setForeground(purple);
        ButtonGroup breakfastGroup = new ButtonGroup();
        breakfastGroup.add(breakfastYes);
        breakfastGroup.add(breakfastNo);
        attendanceGbc.gridx = 1;
        attendancePanel.add(breakfastYes, attendanceGbc);
        attendanceGbc.gridx = 2;
        attendancePanel.add(breakfastNo, attendanceGbc);
        JLabel lunchLabel = new JLabel("Lunch:");
        lunchLabel.setForeground(purple);
        attendanceGbc.gridx = 0;
        attendanceGbc.gridy = 2;
        attendancePanel.add(lunchLabel, attendanceGbc);
        final JRadioButton lunchYes = new JRadioButton("Yes");
        JRadioButton lunchNo = new JRadioButton("No");
        lunchYes.setForeground(purple);
        lunchNo.setForeground(purple);
        ButtonGroup lunchGroup = new ButtonGroup();
        lunchGroup.add(lunchYes);
        lunchGroup.add(lunchNo);
        attendanceGbc.gridx = 1;
        attendancePanel.add(lunchYes, attendanceGbc);
        attendanceGbc.gridx = 2;
        attendancePanel.add(lunchNo, attendanceGbc);
        JLabel dinnerLabel = new JLabel("Dinner:");
        dinnerLabel.setForeground(purple);
        attendanceGbc.gridx = 0;
        attendanceGbc.gridy = 3;
        attendancePanel.add(dinnerLabel, attendanceGbc);
        final JRadioButton dinnerYes = new JRadioButton("Yes");
        JRadioButton dinnerNo = new JRadioButton("No");
        dinnerYes.setForeground(purple);
        dinnerNo.setForeground(purple);
        ButtonGroup dinnerGroup = new ButtonGroup();
        dinnerGroup.add(dinnerYes);
        dinnerGroup.add(dinnerNo);
        attendanceGbc.gridx = 1;
        attendancePanel.add(dinnerYes, attendanceGbc);
        attendanceGbc.gridx = 2;
        attendancePanel.add(dinnerNo, attendanceGbc);
        JButton markAttendanceButton = new JButton("Mark Attendance");
        markAttendanceButton.setForeground(purple);
        markAttendanceButton.setBackground(new Color(216, 191, 216));
        attendanceGbc.gridwidth = 2;
        attendanceGbc.gridx = 0;
        attendanceGbc.gridy = 4;
        attendanceGbc.anchor = 10;
        attendancePanel.add(markAttendanceButton, attendanceGbc);
        JButton profileManageButton = new JButton("Profile Management");
        profileManageButton.setForeground(purple);
        profileManageButton.setBackground(new Color(216, 191, 216));
        attendanceGbc.gridy = 5;
        attendancePanel.add(profileManageButton, attendanceGbc);
        profileManageButton.addActionListener(new ActionListener(this) {
            public void actionPerformed(ActionEvent e) {
                ProfileManage profileManage = new ProfileManage();
                profileManage.setVisible(true);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        this.frame.add(menuPanel, gbc);
        gbc.gridx = 1;
        this.frame.add(attendancePanel, gbc);
        markAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String breakfastAttendance = breakfastYes.isSelected() ? "Yes" : "No";
                String lunchAttendance = lunchYes.isSelected() ? "Yes" : "No";
                String dinnerAttendance = dinnerYes.isSelected() ? "Yes" : "No";
                StudentDash.this.saveAttendance(StudentDash.this.studentId, "Breakfast", breakfastAttendance);
                StudentDash.this.saveAttendance(StudentDash.this.studentId, "Lunch", lunchAttendance);
                StudentDash.this.saveAttendance(StudentDash.this.studentId, "Dinner", dinnerAttendance);
                JOptionPane.showMessageDialog(StudentDash.this.frame, "Attendance marked:\nBreakfast: " + breakfastAttendance + "\nLunch: " + lunchAttendance + "\nDinner: " + dinnerAttendance);
            }
        });
        this.frame.setVisible(true);
    }

    private void loadMenuForToday(JTextArea menuTextArea) {
        LocalDate today = LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodalign", "root", "password");

            try {
                Statement stmt = conn.createStatement();

                try {
                    String sql = "SELECT meal_type, items FROM menu WHERE Day = '" + dayOfWeek + "'";
                    ResultSet rs = stmt.executeQuery(sql);
                    StringBuilder menu = new StringBuilder();
                    menu.append("Today's Menu (").append(dayOfWeek).append("):\n\n");

                    while(rs.next()) {
                        menu.append(rs.getString("meal_type")).append(": ").append(rs.getString("items")).append("\n");
                    }

                    menuTextArea.setText(menu.toString());
                    rs.close();
                } catch (Throwable var11) {
                    if (stmt != null) {
                        try {
                            stmt.close();
                        } catch (Throwable var10) {
                            var11.addSuppressed(var10);
                        }
                    }

                    throw var11;
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Throwable var12) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var9) {
                        var12.addSuppressed(var9);
                    }
                }

                throw var12;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var13) {
            SQLException e = var13;
            menuTextArea.setText("Error loading menu: " + e.getMessage());
        }

    }

    private void saveAttendance(int studentId, String mealType, String attendance) {
        LocalDate today = LocalDate.now();
        String currentDate = today.toString();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodalign", "root", "password");

            try {
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO attendance (student_id, attendance_date, meal_type, attendance_status) VALUES (?, ?, ?, ?)");

                try {
                    pstmt.setInt(1, studentId);
                    pstmt.setString(2, currentDate);
                    pstmt.setString(3, mealType);
                    pstmt.setString(4, attendance);
                    pstmt.executeUpdate();
                } catch (Throwable var12) {
                    if (pstmt != null) {
                        try {
                            pstmt.close();
                        } catch (Throwable var11) {
                            var12.addSuppressed(var11);
                        }
                    }

                    throw var12;
                }

                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (Throwable var13) {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Throwable var10) {
                        var13.addSuppressed(var10);
                    }
                }

                throw var13;
            }

            if (conn != null) {
                conn.close();
            }
        } catch (SQLException var14) {
            SQLException e = var14;
            JOptionPane.showMessageDialog(this.frame, "Error marking attendance: " + e.getMessage(), "Database Error", 0);
        }

    }
}
