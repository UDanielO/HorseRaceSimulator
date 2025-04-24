import java.util.*;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class RacePanel extends JPanel {
    private ArrayList<Horse> horses;
    private Track track;
    private JPanel raceTrackPanel;
    private JPanel controlPanel;
    private JButton startRaceButton;
    private JButton resetRaceButton;
    private JComboBox<String> trackSelector;
    private JList<String> horseList;
    private DefaultListModel<String> horsesListModel;
    private HashMap<String, Point> horsePositions;
    private Timer raceTimer;
    private boolean raceInProgress = false;
    private HashMap<String, Double> horseDistances;
    private HashMap<String, Boolean> horseFallen;
    private HashMap<String, Long> horseFinishTimes;
    private long raceStartTime;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private JLabel statusLabel;
    private JTextArea raceResultsArea;

    public RacePanel(ArrayList<Horse> horses, Track track) {
        this.horses = horses;
        this.track = track;
        this.horsePositions = new HashMap<>();
        this.horseDistances = new HashMap<>();
        this.horseFallen = new HashMap<>();
        this.horseFinishTimes = new HashMap<>();

        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        resetRace();
    }

    private void initializeComponents(){
        raceTrackPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                drawTrack(g);
                drawHorses(g);
            }
        };
        raceTrackPanel.setPreferredSize(new java.awt.Dimension(600, 400));
        raceTrackPanel.setBorder(BorderFactory.createTitledBorder("Race Track"));

        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createTitledBorder("Control Panel"));

        // Create race button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        startRaceButton = new JButton("Start Race");
        resetRaceButton = new JButton("Reset Race");
        resetRaceButton.setEnabled(false);

        buttonPanel.add(startRaceButton);
        buttonPanel.add(resetRaceButton);

        //Track selector
        JPanel trackSelectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel trackLabel = new JLabel("Select Track:");
        trackSelector = new JComboBox<>(new String[]{"Current Track"}); 
        trackSelectorPanel.add(trackSelector); 

        //Horse Selector
        JPanel horseSelectorPanel = new JPanel(new BorderLayout());
        horseSelectorPanel.setBorder(BorderFactory.createTitledBorder("Horse Selector"));

        horsesListModel = new DefaultListModel<>();
        updateHorseList();
        horseList = new JList<>(horsesListModel);
        horseList.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane horseScrollPane = new JScrollPane(horseList);
        horseScrollPane.setPreferredSize(new java.awt.Dimension(200, 150));
        horseSelectorPanel.add(horseScrollPane, BorderLayout.CENTER);

        //Status label
        statusLabel = new JLabel("Select horses and Start Race.");
        statusLabel.setHorizontalAlignment(JLabel.CENTER);

        //Race results area
        raceResultsArea = new JTextArea(5, 20);
        raceResultsArea.setEditable(false);
        JScrollPane resultsScrollPane = new JScrollPane(raceResultsArea);
        resultsScrollPane.setBorder(BorderFactory.createTitledBorder("Race Results"));

        //Add components to control panel
        JPanel topControls = new JPanel(new BorderLayout());
        topControls.add(trackSelectorPanel, BorderLayout.NORTH);
        topControls.add(horseSelectorPanel, BorderLayout.CENTER);
        topControls.add(buttonPanel, BorderLayout.SOUTH);
        
        controlPanel.add(topControls, BorderLayout.NORTH);
        controlPanel.add(statusLabel, BorderLayout.CENTER);
        controlPanel.add(resultsScrollPane, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, raceTrackPanel, controlPanel);
        splitPane.setDividerLocation(600);
        add(splitPane, BorderLayout.CENTER);

        //Add action listeners
        startRaceButton.addActionListener(_ -> startRace());
        resetRaceButton.addActionListener(_ -> resetRace());
    }

    private void updateHorseList() {
        horsesListModel.clear();
        for (Horse horse : horses) {
            horsesListModel.addElement(horse.getName());
        }
    }

    private void drawTrack(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the track based on the selected shape
        int width = raceTrackPanel.getWidth() - 40;
        int height = raceTrackPanel.getHeight() - 40;
        int x = 20;
        int y = 20;

        // Draw the track shape
        if (track != null){
            String trackShape = track.getTrackShape();
            int lanes = track.getLaneCount();

            if("Oval".equals(trackShape)){
                for (int i = 0; i < lanes; i++){
                    int laneWidth = width - (i * 30);
                    int laneHeight = height - (i * 30);
                    int laneX = x + (i * 15);
                    int laneY = y + (i * 15);

                    g2d.setColor(getTrackColor(track.getWeatherCondition())); // Brown color for track
                    g2d.drawOval(laneX, laneY, laneWidth, laneHeight);
                }
            }else if ("Figure-Eight".equals(trackShape)){
                for (int i = 0; i < lanes; i++){
                    int laneWidth = (width / 2) - (i * 15);
                    int laneHeight = height - (i * 30);
                    int laneX1 = x + (i * 15);
                    int laneX2 = x + (width / 2) + (i * 15);
                    int laneY = y + (i * 15);

                    g2d.setColor(getTrackColor(track.getWeatherCondition())); // Brown color for track
                    g2d.drawOval(laneX1, laneY, laneWidth, laneHeight);
                    g2d.drawOval(laneX2, laneY, laneWidth, laneHeight);
                }

            }else {
                // Draw custom track (just a placeholder)
                g2d.setColor(getTrackColor(track.getWeatherCondition()));
                int[] xPoints = {x, x + width/2, x + width, x + width*3/4, x + width/4};
                int[] yPoints = {y + height/2, y, y + height/2, y + height, y + height};

                for (int i = 0; i < lanes; i++){
                    int[] adjustedX = new int[xPoints.length];
                    int[] adjustedY = new int[yPoints.length];

                    for (int j = 0; j < xPoints.length; j++){
                        adjustedX[j] = xPoints[j] + (i * 5);
                        adjustedY[j] = yPoints[j] + (i * 5);
                    }

                    g2d.drawPolygon(adjustedX, adjustedY, xPoints.length);
                }
            }

            //Start and finish line
            g2d.setColor(java.awt.Color.BLACK);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(width/2, y, width/2, y+20);
            g2d.drawString("Start/Finish", width/2 -30, y + 35);
        }else{
            //Default track
            g2d.setColor(new Color(139, 69, 19));
            g2d.drawOval(x, y, width, height);
            g2d.drawString("Default Track", x + width/2 - 30, y + height/2);
        }
    }

    private Color getTrackColor(String weatherCondition){
        switch (weatherCondition){
            case "Sunny":
                return new Color(255, 223, 186); // Light brown
            case "Rainy":
                return new Color(169, 169, 169); // Gray
            case "Snowy":
                return new Color(255, 250, 250); // White
            case "Windy":
                return new Color(135, 206, 235); // Light blue
            default:
                return new Color(139, 69, 19); // Default brown color
        }
    }
    
    private void drawHorses(Graphics g){
        if(horses == null || horses.isEmpty()){
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
        
        for(String horseName : horsePositions.keySet()){
            Horse horse = getHorseByName(horseName);
            if (horse == null){
                continue;
            }
            Point position = horsePositions.get(horseName);

            if(horseFallen.containsKey(horseName) && horseFallen.get(horseName)){
                g2d.setColor(horse.getColorObject());
                g2d.fillOval(position.x, position.y, 20, 20); // Draw fallen horse
                g2d.setColor(Color.RED);
                g2d.drawString("x", position.x + 8, position.y + 8);
                g2d.drawString("Fallen", position.x - 15, position.y - 5);
            }else{
                g2d.setColor(horse.getColorObject());
                g2d.fillOval(position.x, position.y, 20, 20); // Draw horse
                g2d.setColor(Color.BLACK);
                g2d.drawString(horse.getSymbol(), position.x + 5, position.y + 10);
            }
        }
    }
    private Horse getHorseByName(String name){
        for(Horse horse : horses){
            if(horse.getName().equals(name)){
                return horse;
            }
        }
        return null;
    }

    //Start Race
    private void startRace(){
        int[] selectedIndices = horseList.getSelectedIndices();

        if (selectedIndices.length < 2){
            JOptionPane.showMessageDialog(this, "Please select at least two horses to race.", "Not Enough horses", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (track == null){
            JOptionPane.showMessageDialog(this, "Please select a track.", "No Track Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        //Initialize horse race
        raceInProgress = true;
        startRaceButton.setEnabled(false);
        resetRaceButton.setEnabled(false);
        horseList.setEnabled(false);

        //Set horse positions
        horsePositions.clear();
        horseDistances.clear();
        horseFallen.clear();
        horseFinishTimes.clear();

        //Place horses at starting line
        int startX = raceTrackPanel.getWidth() / 2 - 10;
        int startY  = 50;
        int spacing = 30;

        for (int i = 0; i < selectedIndices.length; i++){
            String horseName = horsesListModel.getElementAt(selectedIndices[i]);
            horsePositions.put(horseName, new Point(startX, startY + (i * spacing)));
            horseDistances.put(horseName, 0.0);
            horseFallen.put(horseName, false);
        }

        //Start timer
        raceStartTime = System.currentTimeMillis();
        statusLabel.setText("Race in progess...");

        //Start racee animator time
        raceTimer = new Timer(50, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                updateRace();
                raceTrackPanel.repaint();
            
                //Check if race complete
                if (isRaceComplete()){
                    raceTimer.stop();
                    raceInProgress = false;
                    resetRaceButton.setEnabled(true);
                    displayRaceResults();
                    updateHorseStats();
                    statusLabel.setText("Race complete! See results.");
                }
            }
        }
        );
        raceTimer.start();
    }
    private void updateRace(){
        for (String horseName : horsePositions.keySet()){
            if(horseFallen.get(horseName) || horseFinishTimes.containsKey(horseName)){
                continue; // Skip horses that have fallen or finished
            }
            Horse horse = getHorseByName(horseName);
            if (horse == null){
                continue;
            }
            // Calculate speed and stamina
            double baseSpeed = horse.getSpeed() / 10.0;
            double staminaFactor = horse.getStamina() / 100.0;
            double trackSpeedFactor = track.getSpeedFactor();
            double confidenceFactor = horse.getConfidence() / 100.0;

            //Distance moved this step
            double distance = baseSpeed * staminaFactor * trackSpeedFactor * confidenceFactor;
            double currentDistance = horseDistances.get(horseName) + distance;
            horseDistances.put(horseName, currentDistance);

            //check if horse has finished
            if (currentDistance >= track.getTrackLength()){
                horseFinishTimes.put(horseName, System.currentTimeMillis() - raceStartTime);
                continue;
            }

            //Check if horse has fallen
            double fallRisk = track.getFallRisk() / (horse.getAgility() / 50.0);
            if (Math.random() < fallRisk){
                horseFallen.put(horseName, true);
                continue;
            }

            double progressPercent = currentDistance / track.getTrackLength();
            Point point = calculatePostionOnTrack(horseName, progressPercent);
            horsePositions.put(horseName, point);

        }
    }

    private Point calculatePostionOnTrack(String horseName, double progressPercent){
        int width = raceTrackPanel.getWidth() - 40;
        int height = raceTrackPanel.getHeight() - 40;
        int x = 20;
        int y = 20;

        int lane = new ArrayList<>(horsePositions.keySet()).indexOf(horseName);
        int laneOffset = lane * 15; 

        if ("Oval".equals(track.getTrackShape())){
            double angle = progressPercent * 2 * Math.PI; // Full circle
            int centerX = x + width / 2;
            int centerY = y + height / 2;
            int radiusX = width / 2 - laneOffset;
            int radiusY = height / 2 - laneOffset;

            int posX = (int) (centerX + radiusX * Math.cos(angle - Math.PI/2));
            int posY = (int) (centerY + radiusY * Math.sin(angle - Math.PI/2));
            return new Point(posX, posY);
        } else if ("Figure-Eight".equals(track.getTrackShape())){
            double angle = progressPercent * 4 * Math.PI; // Two loops
            int centerX1 = x + width / 4;
            int centerX2 = x + 3 * width / 4;
            int centerY = y + height / 2;
            int radiusX = width / 4 - laneOffset;
            int radiusY = height / 2 - laneOffset;

            int posX, posY;
            if (progressPercent < 0.5){
                // First loop
                double localAngle = angle;  
                posX = (int) (centerX1 + radiusX * Math.cos(angle - Math.PI/2));
                posY = (int) (centerY + radiusY * Math.sin(angle - Math.PI/2));
                return new Point(posX, posY);
            } else {
                // Second loop
                double localAngle = angle - 2 * Math.PI; // Adjust for second loop
                posX = (int) (centerX2 + radiusX * Math.cos(angle - Math.PI/2));
                posY = (int) (centerY + radiusY * Math.sin(angle - Math.PI/2));
            }
            return new Point(posX, posY);
        } else {
            // Custom track logic
            int startX = raceTrackPanel.getWidth() / 2 - 10;
            int startY = 50 + lane * 30;
            int endX = (int) (startX + (width / 2) * Math.sin(progressPercent * 2 * Math.PI));
            int endY = (int) (startY + (height / 2) * Math.cos(progressPercent * 4 * Math.PI));
            return new Point(endX, endY);
        }
    }
    private boolean isRaceComplete(){
        for (String horseName : horsePositions.keySet()){
            if (!horseFinishTimes.containsKey(horseName)){
                return false;
            }
        }
        return true;
    }

    private void displayRaceResults(){
        List<Map.Entry<String, Long>> sortedResults = new ArrayList<>();
        for (Map.Entry<String, Long> entry : horseFinishTimes.entrySet()){
            sortedResults.add(entry);
        }
        sortedResults.sort(Map.Entry.comparingByValue());

        //Display results
        StringBuilder results = new StringBuilder("Race Results:\n");
        int position = 1;
        for(Map.Entry<String, Long> entry : sortedResults){
            String horseName = entry.getKey();
            long finishTime = entry.getValue();
            double timeInSeconds = finishTime / 1000.0;
            results.append(position).append(". ").append(horseName).append(" - ").append(String.format("%.2f", timeInSeconds)).append(" s\n");
            position++;
        }

        results.append("\nFallen Horses:\n");
        for (String horseName : horseFallen.keySet()){
            if (horseFallen.get(horseName)){
                results.append("-").append(horseName).append(" (DNF)\n");
            }
        }
        raceResultsArea.setText(results.toString());
    }

    private void updateHorseStats(){
        String date = dateFormat.format(new Date());
        String trackName = track.getTrackShape() + "(" + track.getTrackLength() + " m)";
        for(String horseName : horseFinishTimes.keySet()){
            Horse horse = getHorseByName(horseName);
            if (horse == null){
                continue;
            }
            
            if(horseFallen.get(horseName)){
                RaceResults raceResult = new RaceResults(date, 0, 0, trackName, true);
                horse.getRaceHistory().add(raceResult);
            }else if(horseFinishTimes.containsKey(horseName)){
                int position = 1;
                long FinishTime = horseFinishTimes.get(horseName);

                for (long otherTime : horseFinishTimes.values()){
                    if (otherTime < FinishTime){
                        position++;
                    }
                }
                double timeInSeconds = FinishTime / 1000.0;
                RaceResults raceResult = new RaceResults(date, position, timeInSeconds, trackName, false);
                horse.getRaceHistory().add(raceResult);

                boolean won = (position == 1);
                horse.recordRace(won, timeInSeconds);
            }
        }
    }
    private void resetRace(){
        if (raceTimer != null && raceTimer.isRunning()){
            raceTimer.stop();
        }

        //reset race variables
        raceInProgress = false;
        horsePositions.clear();
        horseDistances.clear();
        horseFallen.clear();
        horseFinishTimes.clear();

        //reset UI components
        startRaceButton.setEnabled(true);
        resetRaceButton.setEnabled(false);
        horseList.setEnabled(true);
        statusLabel.setText("Ready to race. Select horses and track.");

        raceTrackPanel.repaint();
    }

    public void setTrack(Track track){
        this.track = track;
        resetRace();
    }
    public void setHorses(ArrayList<Horse> horses){
        this.horses = horses;
        updateHorseList();
        resetRace();
    }

    
}
