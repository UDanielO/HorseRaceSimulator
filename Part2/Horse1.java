import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Horse1 extends JFrame {

    private JTextArea outputArea;

    public Horse1() {
        setTitle("Horse Betting System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel 1
        JPanel panel1 = new JPanel();
        panel1.setBackground(new Color(29, 112, 180)); // Dark Blue
        panel1.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel("Welcome to the Horse Betting Simulator!");
        welcomeLabel.setForeground(Color.WHITE); // White text
        panel1.add(welcomeLabel);

        // Panel 2
        JPanel panel2 = new JPanel();
        panel2.setBackground(new Color(243, 235, 233)); // Light Gray
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel courseLabel = new JLabel("Course:");
        JTextField courseField = new JTextField(15);
        JButton submitButton = new JButton("Submit");
        JLabel studentLabel = new JLabel("Student Name:");
        JTextField studentField = new JTextField(15);
        JButton enrollButton = new JButton("Enroll");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel2.add(courseLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel2.add(courseField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panel2.add(submitButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel2.add(studentLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel2.add(studentField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        panel2.add(enrollButton, gbc);

        // Output panel
        JPanel outputPanel = new JPanel();
        outputPanel.setBackground(new Color(243, 235, 233)); // Light Gray
        outputPanel.setLayout(new BorderLayout());
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(224, 224, 224)); // Light Gray
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        // Add panels to frame
        add(panel1, BorderLayout.NORTH);
        add(panel2, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);

        // Action listeners for buttons
		submitButton.addActionListener(_ -> showMessage("Submitted course: " + courseField.getText()));

        enrollButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = studentField.getText();
                showMessage("Enrolled student: " + student);
            }
        });

        setVisible(true);
    }

    private void showMessage(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Horse1::new);
    }
}
