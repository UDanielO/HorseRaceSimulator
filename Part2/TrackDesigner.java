import javax.swing.*;
import java.awt.*;


public class TrackDesigner extends JPanel {
    
    private JSpinner laneCountSpinner;
    private JTextField trackLengthField;
    private JComboBox<String> trackShapeComboBox;
    private JComboBox<String> weatherConditionComboBox;
    private JPanel trackPreviewPanel;
    
    public TrackDesigner() {
        setLayout(new BorderLayout());
        
        // Create control panel for track settings
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Track Configuration"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Lane count
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(new JLabel("Number of Lanes:"), gbc);
        
        gbc.gridx = 1;
        SpinnerNumberModel laneModel = new SpinnerNumberModel(4, 1, 10, 1);
        laneCountSpinner = new JSpinner(laneModel);
        controlPanel.add(laneCountSpinner, gbc);
        
        // Track length
        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(new JLabel("Track Length (meters):"), gbc);
        
        gbc.gridx = 1;
        trackLengthField = new JTextField("1000", 10);
        controlPanel.add(trackLengthField, gbc);
        
        // Track shape
        gbc.gridx = 0;
        gbc.gridy = 2;
        controlPanel.add(new JLabel("Track Shape:"), gbc);
        
        gbc.gridx = 1;
        String[] shapes = {"Oval", "Figure-Eight", "Custom"};
        trackShapeComboBox = new JComboBox<>(shapes);
        controlPanel.add(trackShapeComboBox, gbc);
        
        // Weather conditions
        gbc.gridx = 0;
        gbc.gridy = 3;
        controlPanel.add(new JLabel("Weather Condition:"), gbc);
        
        gbc.gridx = 1;
        String[] conditions = {"Dry", "Muddy", "Icy", "Wet"};
        weatherConditionComboBox = new JComboBox<>(conditions);
        controlPanel.add(weatherConditionComboBox, gbc);
        
        // Save button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save Track");
        controlPanel.add(saveButton, gbc);

        saveButton.addActionListener(_ -> {
            int lanes = (Integer) laneCountSpinner.getValue();
            int length = Integer.parseInt(trackLengthField.getText());
            String shape = (String) trackShapeComboBox.getSelectedItem();
            String weather = (String) weatherConditionComboBox.getSelectedItem();
            
            Track newTrack = new Track(lanes, length, shape, weather);
            
            // Get the MainWindow reference
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainWindow) {
                ((MainWindow) window).updareTrack(newTrack);
                JOptionPane.showMessageDialog(this, "Track saved successfully!");
            }
        });
        
        // Track preview panel
        trackPreviewPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTrackPreview(g);
            }
        };
        trackPreviewPanel.setBorder(BorderFactory.createTitledBorder("Track Preview"));
        trackPreviewPanel.setPreferredSize(new Dimension(500, 400));
        
        // Add components to main panel
        add(controlPanel, BorderLayout.WEST);
        add(trackPreviewPanel, BorderLayout.CENTER);
        
        // Add listeners
        trackShapeComboBox.addActionListener(_ -> trackPreviewPanel.repaint());
        laneCountSpinner.addChangeListener(_ -> trackPreviewPanel.repaint());
    }
    
    private void drawTrackPreview(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = trackPreviewPanel.getWidth() - 40;
        int height = trackPreviewPanel.getHeight() - 40;
        int x = 20;
        int y = 20;
        
        String selectedShape = (String) trackShapeComboBox.getSelectedItem();
        int lanes = (Integer) laneCountSpinner.getValue();
        
        if ("Oval".equals(selectedShape)) {
            // Draw oval track
            for (int i = 0; i < lanes; i++) {
                int laneWidth = width - (i * 30);
                int laneHeight = height - (i * 30);
                int laneX = x + (i * 15);
                int laneY = y + (i * 15);
                
                g2d.setColor(new Color(139, 69, 19)); // Brown color for track
                g2d.drawOval(laneX, laneY, laneWidth, laneHeight);
            }
        } else if ("Figure-Eight".equals(selectedShape)) {
            // Draw figure-eight track
            for (int i = 0; i < lanes; i++) {
                int laneWidth = (width / 2) - (i * 15);
                int laneHeight = height - (i * 30);
                int laneX1 = x + (i * 15);
                int laneX2 = x + (width / 2) + (i * 15);
                int laneY = y + (i * 15);
                
                g2d.setColor(new Color(139, 69, 19)); // Brown color for track
                g2d.drawOval(laneX1, laneY, laneWidth, laneHeight);
                g2d.drawOval(laneX2, laneY, laneWidth, laneHeight);
            }
        } else {
            // Draw custom track (just a placeholder)
            g2d.setColor(new Color(139, 69, 19));
            int[] xPoints = {x, x + width/2, x + width, x + width*3/4, x + width/4};
            int[] yPoints = {y + height/2, y, y + height/2, y + height, y + height};
            
            for (int i = 0; i < lanes; i++) {
                int[] adjustedX = new int[xPoints.length];
                int[] adjustedY = new int[yPoints.length];
                
                for (int j = 0; j < xPoints.length; j++) {
                    adjustedX[j] = xPoints[j] + (i * 5);
                    adjustedY[j] = yPoints[j] + (i * 5);
                }
                
                g2d.drawPolygon(adjustedX, adjustedY, xPoints.length);
            }
        }
    }
    
    // Main method for debugging
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Track Debugger");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new TrackDesigner());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}