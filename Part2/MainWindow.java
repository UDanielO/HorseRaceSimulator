import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    
    private JTabbedPane tabbedPane;
    private ArrayList<Horse> horses;
    private Track currentTrack;
    private RacePanel racePanel;
    private StatisticsPanel statisticsPanel;
    
    public MainWindow() {

        // Initialize the horse list and track
        horses = new ArrayList<>();

        // Create default track
        currentTrack = new Track(4, 1000, "Oval", "Dry");

        // Set up the main window
        setTitle("Horse Racing Simulator");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create the tabbed pane
        tabbedPane = new JTabbedPane();

        // Track Designer tab
        TrackDesigner trackDesigner = new TrackDesigner();

        // Horse Customization tab
        HorseCustomize horseCustomize = new HorseCustomize();

        // Statistics tab
        statisticsPanel = new StatisticsPanel(horses);

        //Race panel
        racePanel = new RacePanel(horses, currentTrack);

        
        // Create and add panels for each tab
        tabbedPane.addTab("Track Design", trackDesigner);
        tabbedPane.addTab("Horse Customization", horseCustomize);
        tabbedPane.addTab("Race", racePanel);
        tabbedPane.addTab("Statistics", statisticsPanel);
        
        // Add action listeners to buttons
        tabbedPane.addChangeListener(_ ->{
            int selectedIndex = tabbedPane.getSelectedIndex();

            if (selectedIndex == 2){
                racePanel.setHorses(horses);
                racePanel.setTrack(currentTrack);
            }

            if(selectedIndex == 3){
                statisticsPanel = new StatisticsPanel(horses);
                tabbedPane.setComponentAt(3, statisticsPanel);
            }
        });
        
        // Add tabbed pane to frame
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void addSamples(){
        //Add sample horses
        horses.add(new Horse("Thunder", "Thoroughbred", "Black", "♞", "Racing", "Black", "Lightweight"));
        horses.add(new Horse("Blaze", "Arabian", "Brown", "♘", "Standard", "Brown", "Standard"));
        horses.add(new Horse("Spirit", "Quarter Horse", "White", "★", "Western", "White", "Standard"));
        horses.add(new Horse("Shadow", "Appaloosa", "Gray", "⚡", "None", "Grey", "Winter"));
    }

    public void updareTrack(Track track){
        this.currentTrack = track;
        if(racePanel != null){
            racePanel.setTrack(track);
        }
    }

    public void addHorse(Horse horse){
        horses.add(horse);
        if(racePanel != null){
            racePanel.setHorses(horses);
        }
        if(statisticsPanel != null){
            statisticsPanel = new StatisticsPanel(horses);
            tabbedPane.setComponentAt(3, statisticsPanel);
        }
    }
}