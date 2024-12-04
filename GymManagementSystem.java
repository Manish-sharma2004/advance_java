import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class GymManagementSystem {
    private JFrame frame;
    private JTextField memberIdField;
    private JTextField memberNameField;
    private JTextField contactField;
    private JButton insertButton, updateButton, deleteButton, viewButton;
    private JTextArea resultArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GymManagementSystem window = new GymManagementSystem();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public GymManagementSystem() {
        initialize();
    }

    private void initialize() {
        // Create frame
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        // Member ID Field
        JLabel memberIdLabel = new JLabel("Member ID");
        memberIdLabel.setBounds(20, 20, 100, 25);
        frame.getContentPane().add(memberIdLabel);

        memberIdField = new JTextField();
        memberIdField.setBounds(130, 20, 200, 25);
        frame.getContentPane().add(memberIdField);

        // Member Name Field
        JLabel memberNameLabel = new JLabel("Member Name");
        memberNameLabel.setBounds(20, 60, 100, 25);
        frame.getContentPane().add(memberNameLabel);

        memberNameField = new JTextField();
        memberNameField.setBounds(130, 60, 200, 25);
        frame.getContentPane().add(memberNameField);

        // Contact Number Field
        JLabel contactLabel = new JLabel("Contact Number");
        contactLabel.setBounds(20, 100, 120, 25);
        frame.getContentPane().add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(130, 100, 200, 25);
        frame.getContentPane().add(contactField);

        // Buttons
        insertButton = new JButton("Insert Member");
        insertButton.setBounds(20, 140, 150, 30);
        frame.getContentPane().add(insertButton);

        updateButton = new JButton("Update Member");
        updateButton.setBounds(180, 140, 150, 30);
        frame.getContentPane().add(updateButton);

        deleteButton = new JButton("Delete Member");
        deleteButton.setBounds(20, 180, 150, 30);
        frame.getContentPane().add(deleteButton);

        viewButton = new JButton("View Members");
        viewButton.setBounds(180, 180, 150, 30);
        frame.getContentPane().add(viewButton);

        // Text Area to display results
        resultArea = new JTextArea();
        resultArea.setBounds(20, 220, 380, 120);
        resultArea.setEditable(false);
        frame.getContentPane().add(resultArea);

        // Action Listeners for Buttons
        insertButton.addActionListener(this::insertMember);
        updateButton.addActionListener(this::updateMember);
        deleteButton.addActionListener(this::deleteMember);
        viewButton.addActionListener(this::viewMembers);
    }

    // Method to get the database connection

    private Connection getDatabaseConnection() throws SQLException {
	//Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/gym_management", "root", "root");
    }

    // Insert Member
    private void insertMember(ActionEvent e) {
        String memberId = memberIdField.getText();
        String memberName = memberNameField.getText();
        String contact = contactField.getText();

        if (memberId.isEmpty() || memberName.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.");
            return;
        }

        try (Connection connection = getDatabaseConnection()) {
            String query = "INSERT INTO members (member_id, member_name, contact_number) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, memberId);
            statement.setString(2, memberName);
            statement.setString(3, contact);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Member Inserted Successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Error Inserting Member.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    // Update Member
    private void updateMember(ActionEvent e) {
        String memberId = memberIdField.getText();
        String contact = contactField.getText();

        if (memberId.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill all fields.");
            return;
        }

        try (Connection connection = getDatabaseConnection()) {
            String query = "UPDATE members SET contact_number = ? WHERE member_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, contact);
            statement.setString(2, memberId);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Member Updated Successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Member ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    // Delete Member
    private void deleteMember(ActionEvent e) {
        String memberId = memberIdField.getText();

        if (memberId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter the Member ID.");
            return;
        }

        try (Connection connection = getDatabaseConnection()) {
            String query = "DELETE FROM members WHERE member_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, memberId);

            int rows = statement.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(frame, "Member Deleted Successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Member ID not found.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }

    // View Members
    private void viewMembers(ActionEvent e) {
        StringBuilder result = new StringBuilder();

        try (Connection connection = getDatabaseConnection()) {
            String query = "SELECT * FROM members";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                result.append("ID: ").append(rs.getString("member_id"))
                        .append(", Name: ").append(rs.getString("member_name"))
                        .append(", Contact: ").append(rs.getString("contact_number"))
                        .append("\n");
            }

            resultArea.setText(result.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Database Error: " + ex.getMessage());
        }
    }
}
