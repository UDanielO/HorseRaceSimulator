import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel{
    private JTabbedPane tabbedPane;
    private horseStatsPanel horseStatsPanel;
    private trackStatsPanel trackStatsPanel;
    private bettingPanel bettingPanel;

    public StatisticsPanel(){
        setLayout(new BorderLayout());

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create stats panel for each category
        horseStatsPanel = new horseStatsPanel();
        trackStatsPanel = new trackStatsPanel();
        bettingPanel = new bettingPanel();
        
        

        add(tabbedPane, BorderLayout.CENTER);
    }
}