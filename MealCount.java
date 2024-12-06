
import java.awt.*;
import java.sql.*;
import java.time.*;
import java.util.*;
import javax.swing.*;

public class MealCount extends JFrame {
    private JTextField breakfastCount;
    private JTextField lunchCount;
    private JTextField dinnerCount;
    private JTextArea currentMenuArea;
    private JTextArea updatedMenuArea;

    public MealCount() {
        this.setTitle("Food Align: Meal Count Management");
        this.setDefaultCloseOperation(3);
        this.setSize(500, 600);
        this.setResizable(true);
        ImageIcon frameIcon = new ImageIcon("dish1.png");
        this.setIconImage(frameIcon.getImage());
        this.getContentPane().setBackground(new Color(230, 210, 250));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = 1;
        Color purple = new Color(128, 0, 128);
        Color lightPurple = new Color(216, 191, 216);
        JLabel titleLabel = new JLabel("Meal Management", 0);
        titleLabel.setFont(new Font("Arial", 1, 20));
        titleLabel.setForeground(purple);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        this.add(titleLabel, gbc);
        JPanel mealPanel = new JPanel();
        mealPanel.setLayout(new GridLayout(3, 2, 10, 10));
        JLabel breakfastLabel = new JLabel("Breakfast Count:");
        breakfastLabel.setForeground(purple);
        this.breakfastCount = new JTextField(10);
        JLabel lunchLabel = new JLabel("Lunch Count:");
        lunchLabel.setForeground(purple);
        this.lunchCount = new JTextField(10);
        JLabel dinnerLabel = new JLabel("Dinner Count:");
        dinnerLabel.setForeground(purple);
        this.dinnerCount = new JTextField(10);
        mealPanel.add(breakfastLabel);
        mealPanel.add(this.breakfastCount);
        mealPanel.add(lunchLabel);
        mealPanel.add(this.lunchCount);
        mealPanel.add(dinnerLabel);
        mealPanel.add(this.dinnerCount);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        this.add(mealPanel, gbc);
        JPanel mealButtonPanel = new JPanel();
        mealButtonPanel.setLayout(new FlowLayout(1, 10, 10));
        JButton saveMealButton = new JButton("Save Meal Count");
        saveMealButton.setBackground(lightPurple);
        saveMealButton.setForeground(purple);
        JButton resetMealButton = new JButton("Reset Meal Count");
        resetMealButton.setBackground(lightPurple);
        resetMealButton.setForeground(purple);
        mealButtonPanel.add(saveMealButton);
        mealButtonPanel.add(resetMealButton);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        this.add(mealButtonPanel, gbc);
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 2, 10, 10));
        JLabel currentMenuLabel = new JLabel("Current Menu:");
        currentMenuLabel.setForeground(purple);
        this.currentMenuArea = new JTextArea(5, 20);
        this.currentMenuArea.setEditable(false);
        JLabel updatedMenuLabel = new JLabel("Update Menu:");
        updatedMenuLabel.setForeground(purple);
        this.updatedMenuArea = new JTextArea(5, 20);
        menuPanel.add(currentMenuLabel);
        menuPanel.add(this.currentMenuArea);
        menuPanel.add(updatedMenuLabel);
        menuPanel.add(this.updatedMenuArea);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        this.add(menuPanel, gbc);
        JPanel menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new FlowLayout(1, 10, 10));
        JButton saveMenuButton = new JButton("Save Menu Changes");
        saveMenuButton.setBackground(lightPurple);
        saveMenuButton.setForeground(purple);
        JButton resetMenuButton = new JButton("Reset Menu Changes");
        resetMenuButton.setBackground(lightPurple);
        resetMenuButton.setForeground(purple);
        menuButtonPanel.add(saveMenuButton);
        menuButtonPanel.add(resetMenuButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        this.add(menuButtonPanel, gbc);
        saveMealButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int breakfast = Integer.parseInt(MealCount.this.breakfastCount.getText());
                int lunch = Integer.parseInt(MealCount.this.lunchCount.getText());
                int dinner = Integer.parseInt(MealCount.this.dinnerCount.getText());

                try {
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodalign", "root", "password");
                    String updateSQL = "UPDATE meal_counts SET breakfast_count = ?, lunch_count = ?, dinner_count = ? WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
                    preparedStatement.setInt(1, breakfast);
                    preparedStatement.setInt(2, lunch);
                    preparedStatement.setInt(3, dinner);
                    preparedStatement.setInt(4, 1);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(MealCount.this, "Meal counts updated successfully!", "Update Status", 1);
                    } else {
                        JOptionPane.showMessageDialog(MealCount.this, "No records updated. Please check the meal counts.", "Update Status", 2);
                    }

                    preparedStatement.close();
                    connection.close();
                } catch (SQLException var9) {
                    SQLException ex = var9;
                    JOptionPane.showMessageDialog(MealCount.this, "Error updating meal counts: " + ex.getMessage(), "Error", 0);
                }

            }
        });
        resetMealButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MealCount.this.fetchMealCounts();
            }
        });
        saveMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String updatedMenu = MealCount.this.updatedMenuArea.getText();
                if (!updatedMenu.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(MealCount.this, "Menu changes saved successfully!", "Save Status", 1);
                    MealCount.this.currentMenuArea.setText(updatedMenu);
                    MealCount.this.updatedMenuArea.setText("");
                } else {
                    JOptionPane.showMessageDialog(MealCount.this, "Please enter menu changes before saving.", "Error", 0);
                }

            }
        });
        resetMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MealCount.this.updatedMenuArea.setText("");
            }
        });
        this.fetchMealCounts();
        String today = LocalDate.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        this.fetchMenuForDay(today);
        this.setVisible(true);
    }

    private void fetchMealCounts() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodalign", "root", "password");
            String querySQL = "SELECT breakfast_count, lunch_count, dinner_count FROM meal_counts WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                this.breakfastCount.setText(String.valueOf(resultSet.getInt("breakfast_count")));
                this.lunchCount.setText(String.valueOf(resultSet.getInt("lunch_count")));
                this.dinnerCount.setText(String.valueOf(resultSet.getInt("dinner_count")));
            } else {
                JOptionPane.showMessageDialog(this, "No meal counts found for the specified ID.", "Error", 0);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException var5) {
            SQLException ex = var5;
            JOptionPane.showMessageDialog(this, "Error fetching meal counts: " + ex.getMessage(), "Error", 0);
        }

    }

    private void fetchMenuForDay(String day) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/foodalign", "root", "password");
            String querySQL = "SELECT meal_type, items FROM menu WHERE day = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(querySQL);
            preparedStatement.setString(1, day);
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder breakfastItems = new StringBuilder();
            StringBuilder lunchItems = new StringBuilder();
            StringBuilder dinnerItems = new StringBuilder();

            while(resultSet.next()) {
                String mealType = resultSet.getString("meal_type");
                String items = resultSet.getString("items");
                switch (mealType.toLowerCase()) {
                    case "breakfast":
                        breakfastItems.append(items).append(", ");
                        break;
                    case "lunch":
                        lunchItems.append(items).append(", ");
                        break;
                    case "dinner":
                        dinnerItems.append(items).append(", ");
                }
            }

            if (breakfastItems.length() > 0) {
                breakfastItems.setLength(breakfastItems.length() - 2);
            }

            if (lunchItems.length() > 0) {
                lunchItems.setLength(lunchItems.length() - 2);
            }

            if (dinnerItems.length() > 0) {
                dinnerItems.setLength(dinnerItems.length() - 2);
            }

            JTextArea var10000 = this.currentMenuArea;
            String var10001 = String.valueOf(breakfastItems);
            var10000.setText("Breakfast:\n" + var10001 + "\n\nLunch:\n" + String.valueOf(lunchItems) + "\n\nDinner:\n" + String.valueOf(dinnerItems));
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException var13) {
            SQLException ex = var13;
            JOptionPane.showMessageDialog(this, "Error fetching menu: " + ex.getMessage(), "Error", 0);
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MealCount();
        });
    }
}
