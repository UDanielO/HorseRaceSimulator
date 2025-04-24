import javax.swing.*;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;

public class StatisticsPanel extends JPanel {
    private ArrayList<Horse> horses;
    private JTable horseStatsTable;
    private DefaultTableModel tableModel;
    private JTextArea horseDetailsArea;
    private JComboBox<String> horseSelector;
    private JTextArea detailsArea;

    public StatisticsPanel(ArrayList<Horse> horses){
        this.horses = horses;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        initComponents();
    }

    private void initComponents(){
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Horse Statistics"));

        //Table for displaying horse statistics
        String[] tableColumns = {"Horse Name", "Races", "Wins", "Best Time", "Average Speed", "Win Rate", "Falls"};
        tableModel = new DefaultTableModel(tableColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        horseStatsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(horseStatsTable);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(5,5));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Horse Details"));

        //Selector dropdown for horse names
        JPanel selectorPanel = new JPanel();
        horseSelector = new JComboBox<>();  
        updateHorseSelector();
        horseSelector.addActionListener(_ -> showHorseDetails());
        selectorPanel.add(horseSelector);
        bottomPanel.add(selectorPanel, BorderLayout.NORTH);

        //Text area for displaying horse details
        horseDetailsArea = new JTextArea(10, 30);
        horseDetailsArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(horseDetailsArea);
        bottomPanel.add(detailsScrollPane, BorderLayout.CENTER);

        //Add a border to the details area
        detailsArea = new JTextArea(8, 40);
        detailsArea.setEditable(false);

        //Button to refresh statistics
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(_ -> refreshStatistics());

        JButton compareButton = new JButton("Compare Horses");
        compareButton.addActionListener(_ -> compareSelectedHorses());

        buttonPanel.add(compareButton);
        buttonPanel.add(refreshButton);

        //Add panels to main panel
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.NORTH);

        //Initial refresh of statistics
        refreshStatistics();
    }

    //Update the horse selector dropdown with horse names
    private void updateHorseSelector() {
        horseSelector.removeAllItems();
        for (Horse horse : horses) {
            horseSelector.addItem(horse.getName());
        }

        if (horses.size() > 0) {
            horseSelector.setSelectedIndex(0);
        }
    }

    //Refresh the statistics table
    private void refreshStatistics() {
        tableModel.setRowCount(0); // Clear existing rows

        for (Horse horse : horses) {
            Object[] rowData = {
                horse.getName(),
                horse.getRaces(),
                horse.getWins(),
                calculateWinPercentage(horse),
                formatSpeed(horse.getAverageSpeed()),
                formatTime(horse.getBestTime()),
                horse.getFalls()
            };
            tableModel.addRow(rowData);
        }
        updateHorseSelector();

        showHorseDetails(); // Show details of the first horse by default
    }

    //show horse details in the text area
    private void showHorseDetails() {
        int selectedIndex = horseSelector.getSelectedIndex();
        if (selectedIndex == -1 || selectedIndex >= horses.size()) {
            horseDetailsArea.setText("No horse selected.");
            return;
        }

        Horse selectedHorse = horses.get(selectedIndex);
        StringBuilder details = new StringBuilder();
        details.append("Horse Name: ").append(selectedHorse.getName()).append("\n");
        details.append("Breed: ").append(selectedHorse.getBreed()).append("\n");
        details.append("Color: ").append(selectedHorse.getColor()).append("\n");
        details.append("Symbol: ").append(selectedHorse.getSymbol()).append("\n");

        details.append("PERFORMANCE METRICS:\n");
        details.append("Total Races: ").append(selectedHorse.getRaces()).append("\n");
        details.append("Wins: ").append(selectedHorse.getWins()).append("\n");
        details.append("Win Rate: ").append(calculateWinPercentage(selectedHorse)).append("%\n");
        details.append("Best Time: ").append(formatTime(selectedHorse.getBestTime())).append("\n");
        details.append("Average Speed: ").append(formatSpeed(selectedHorse.getAverageSpeed())).append("\n");
        details.append("Current Confidence: ").append(selectedHorse.getConfidence()).append("/10\n");
        details.append("Falls: ").append(selectedHorse.getFalls()).append("\n");
        
        details.append("HORSE HISTORY:\n");
        ArrayList<RaceResults> history = selectedHorse.getRaceHistory();
        if (history.isEmpty()){
            details.append("No race history available.\n");
        }
        else{
            for (RaceResults result : history){
                details.append("-").append(result.getDate()).append(": Positon # ").append(result.getPosition()).append(", Time: ").append(formatTime(result.getTime())).append(", Track: ").append(result.getTrackName()).append("\n");
                if (result.hasFallen()){
                    details.append("(FELL)");
                }
                details.append("\n");
            }
        }

        detailsArea.setText(details.toString());
        detailsArea.setCaretPosition(0); // Scroll to top
    }
    
    // Compare selected horses
    private void compareSelectedHorses() {
        int[] selectedRows = horseStatsTable.getSelectedRows();
        
        if (selectedRows.length < 2) {
            JOptionPane.showMessageDialog(this, 
                "Please select at least two horses to compare (use CTRL+click to select multiple rows)",
                "Selection Required", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create a simple comparison dialog
        StringBuilder comparison = new StringBuilder("HORSE COMPARISON:\n\n");
        comparison.append(String.format("%-15s", "Metric"));
        
        // Add horse names as column headers
        for (int rowIndex : selectedRows) {
            String horseName = (String) tableModel.getValueAt(rowIndex, 0);
            comparison.append(String.format("%-15s", horseName));
        }
        comparison.append("\n");
        comparison.append("-".repeat(15 * (selectedRows.length + 1))).append("\n");
        
        // Add metrics for each selected horse
        String[] metrics = {"Races", "Wins", "Win %", "Avg Speed", "Best Time", "Falls"};
        for (int i = 0; i < metrics.length; i++) {
            comparison.append(String.format("%-15s", metrics[i]));
            
            for (int rowIndex : selectedRows) {
                comparison.append(String.format("%-15s", tableModel.getValueAt(rowIndex, i + 1)));
            }
            comparison.append("\n");
        }
        
        // Show comparison in a dialog
        JTextArea textArea = new JTextArea(comparison.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Horse Comparison", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Helper methods
    private String calculateWinPercentage(Horse horse) {
        if (horse.getRaces() == 0) return "0.0";
        double percentage = (double) horse.getWins() / horse.getRaces() * 100;
        return String.format("%.1f", percentage);
    }
    
    private String formatSpeed(double speed) {
        return String.format("%.2f", speed);
    }
    
    private String formatTime(double time) {
        // Format time as mm:ss.ms
        int minutes = (int) (time / 60);
        double seconds = time % 60;
        return String.format("%d:%05.2f", minutes, seconds);
    }
}


        