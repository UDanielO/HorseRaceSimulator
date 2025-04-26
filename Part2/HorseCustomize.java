import javax.swing.*;
import java.awt.*;

public class HorseCustomize extends JPanel{
    private JTextField horseNameField;
    private JComboBox<String> horseBreedComboBox;
    private JComboBox<String> horseColorComboBox;
    private JComboBox<String> horseSyComboBox;
    private JPanel horsePreviewPanel;
    private JComboBox<String> saddleComboBox;
    private JComboBox<String> horseShoeComboBox;
    private MainWindow mainWindow;
    
    public HorseCustomize(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        // Create control panel for horse settings
        JPanel configPanel = new JPanel();
        configPanel.setLayout(new GridBagLayout());
        configPanel.setBorder(BorderFactory.createTitledBorder("Horse Configuration"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Horse name
        gbc.gridx = 0;
        gbc.gridy = 0;
        configPanel.add(new JLabel("Horse Name:"), gbc);

        gbc.gridx = 1;  
        horseNameField = new JTextField(10);
        configPanel.add(horseNameField, gbc);

        // Horse Breed
        gbc.gridx = 0;
        gbc.gridy = 1;
        configPanel.add(new JLabel("Horse Breed:"), gbc);
        gbc.gridx = 1;
        String[] breeds = {"Thoroughbred", "Arabian", "Quarter Horse", "Appaloosa"};
        horseBreedComboBox = new JComboBox<>(breeds);
        configPanel.add(horseBreedComboBox, gbc);

        // Horse coat Color
        gbc.gridx = 0;
        gbc.gridy = 2;
        configPanel.add(new JLabel("Coat Color:"), gbc);
        gbc.gridx = 1;
        String[] colors = {"Brown", "White", "Black", "Gray"};
        horseColorComboBox = new JComboBox<>(colors);
        configPanel.add(horseColorComboBox, gbc);

        // Symbol
        gbc.gridx = 0;
        gbc.gridy = 3;
        configPanel.add(new JLabel("Symbol:"), gbc);
        
        gbc.gridx = 1;
        String[] symbols = {"♞", "♘", "H", "⚡", "★", "♥"};
        horseSyComboBox = new JComboBox<>(symbols);
        configPanel.add(horseSyComboBox, gbc);

        // Equipments and Accessories
        // Saddle
        gbc.gridx = 0;
        gbc.gridy = 4;
        configPanel.add(new JLabel("Saddle:"), gbc);
        gbc.gridx = 1;
        String[] saddles = {"Standard", "Racing", "None", "Western", "English"};
        saddleComboBox = new JComboBox<>(saddles);
        configPanel.add(saddleComboBox, gbc);

        // Horse Shoes
        gbc.gridx = 0;
        gbc.gridy = 5;
        configPanel.add(new JLabel("Horse Shoes:"), gbc);
        gbc.gridx = 1;
        String[] horseShoes = {"Standard", "Lightweight", "Winter", "None"};
        horseShoeComboBox = new JComboBox<>(horseShoes);
        configPanel.add(horseShoeComboBox, gbc);

        //Horse Button
        JButton createHorseButton = new JButton("Create Horse");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        configPanel.add(createHorseButton, gbc);

        createHorseButton.addActionListener(_ -> {
            String name = horseNameField.getText();
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a horse name!");
                return;
            }
            
            String breed = (String) horseBreedComboBox.getSelectedItem();
            String coatColor = (String) horseColorComboBox.getSelectedItem();
            String symbol = (String) horseSyComboBox.getSelectedItem();
            String saddle = (String) saddleComboBox.getSelectedItem();
            String horseShoe = (String) horseShoeComboBox.getSelectedItem();
            
            Horse newHorse = new Horse(name, breed, coatColor, symbol, saddle, coatColor, horseShoe);
            mainWindow.addHorse(newHorse);
            // Get the MainWindow reference
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof MainWindow) {
                ((MainWindow) window).addHorse(newHorse);
                JOptionPane.showMessageDialog(this, "Horse created successfully!");
            }
        });
        // Horse Preview Panel
        horsePreviewPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHorsePreview(g);
            }
        };
        horsePreviewPanel.setBorder(BorderFactory.createTitledBorder("Horse Preview"));
        horsePreviewPanel.setPreferredSize(new Dimension(200, 200));
    
        // List panels
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBorder(BorderFactory.createTitledBorder("Horse List"));
        JList<String> horseList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(horseList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        listPanel.add(scrollPane, BorderLayout.CENTER);

        // Attribute panel
        JPanel attributePanel = new JPanel(new GridLayout(4, 2, 5, 5));
        attributePanel.setBorder(BorderFactory.createTitledBorder("Horse Attributes"));

        attributePanel.add(new JLabel("Speed:"));
        attributePanel.add(new JProgressBar(0, 100) {{
            setValue(75);
        }});

        attributePanel.add(new JLabel("Stamina:"));
        attributePanel.add(new JProgressBar(0, 100) {{
            setValue(60);
        }});

        attributePanel.add(new JLabel("Agility:"));
        attributePanel.add(new JProgressBar(0, 100) {{
            setValue(80);
        }});

        //Layout Panels
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(attributePanel, BorderLayout.SOUTH);
        rightPanel.add(horsePreviewPanel, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());    
        centerPanel.add(rightPanel, BorderLayout.CENTER);
        centerPanel.add(listPanel, BorderLayout.EAST);

        add(configPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Action listener for the create horse button
        horseSyComboBox.addActionListener(_ -> horsePreviewPanel.repaint());
        horseColorComboBox.addActionListener(_ -> horsePreviewPanel.repaint());
    }

    private void drawHorsePreview(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = horsePreviewPanel.getWidth();
        int height = horsePreviewPanel.getHeight(); 

        // Draw horse silhouette
        g2d.setColor(getColorForCoat());

        //Body
        g2d.fillOval(width/2 - 100, height/2 - 30, 160, 60);

        //Head
        g2d.fillOval(width/2 + 45, height/2 - 60, 50, 40);

        //Legs
        g2d.fillRect(width/2 - 80, height/2 + 20, 10, 50);
        g2d.fillRect(width/2 - 40, height/2 + 20, 10, 50);
        g2d.fillRect(width/2 + 20, height/2 + 20, 10, 50);
        g2d.fillRect(width/2 + 60, height/2 + 20, 10, 50);

        // Draw saddle
        if (!"NONE".equals(saddleComboBox.getSelectedItem())) {
            g2d.setColor(Color.RED);
            g2d.fillRect(width/2 - 30, height/2 - 35, 60, 20);
        }

        // Draw symbol
        g2d.setColor(Color.BLACK);
        Font symbolFont = new Font("Dialog", Font.BOLD, 24);
        g2d.setFont(symbolFont);
        String symbol = (String) horseSyComboBox.getSelectedItem();
        g2d.drawString(symbol, width/2 - 10, height/2);
        
        // Draw horseshoes if selected
        if (!"None".equals(horseShoeComboBox.getSelectedItem())) {
            g2d.setColor(Color.GRAY);
            g2d.drawOval(width/2 - 80, height/2 + 60, 10, 10);
            g2d.drawOval(width/2 - 40, height/2 + 60, 10, 10);
            g2d.drawOval(width/2 + 20, height/2 + 60, 10, 10);
            g2d.drawOval(width/2 + 60, height/2 + 60, 10, 10);
        }
    }

    private Color getColorForCoat(){
        String colorName = (String) horseColorComboBox.getSelectedItem();
        switch (colorName) {
            case "Brown":
                return new Color(139, 69, 19); // Brown
            case "White":
                return Color.WHITE; // White
            case "Black":
                return Color.BLACK; // Black
            case "Gray":
                return Color.GRAY; // Gray
            default:
                return Color.BLACK; // Default color
        }
    }
}

