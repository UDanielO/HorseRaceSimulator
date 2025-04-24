import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use the Event Dispatch Thread for Swing applications
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Create and display the main window
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}