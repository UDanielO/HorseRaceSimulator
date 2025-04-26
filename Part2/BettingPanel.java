import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

public class BettingPanel extends JPanel{
    private BettingSystem bettingSystem;
    private ArrayList<Horse> horses;
    private Track currentTrack;
    private JTable oddsTable;
    private DefaultTableModel oddsTableModel;
    private JTable betHistoryTable;
    private DefaultTableModel betHistoryTableModel;
    private JLabel balanceLabel;
    private JComboBox<String> horseSelector;
    private JTextField betAmountField;
    private JLabel potentialWinningsLabel;
    private JTextArea insightsArea;

    public BettingPanel(ArrayList<Horse> horses, Track track){
        this.horses = horses;
        this.currentTrack = track;  
        this.bettingSystem = new BettingSystem();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        initComponents();
        updateOdds();
    }

    private void initComponents(){
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("Your Account"));

        // Balance label
        balanceLabel = new JLabel("Balance: $1000.00");
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        topPanel.add(balanceLabel, BorderLayout.WEST);

        JButton addFundsButton = new JButton("Add Funds");
        addFundsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = JOptionPane.showInputDialog(BettingPanel.this, 
                        "Enter amount to add:", "Add Funds", JOptionPane.PLAIN_MESSAGE);
                try {
                    double amount = Double.parseDouble(input);
                    if (amount > 0) {
                        bettingSystem.addFunds(amount);
                        updateBalanceDisplay();
                    }
                } catch (NumberFormatException | NullPointerException ex) {
                    // User canceled or invalid input
                }
            }
        });
        topPanel.add(addFundsButton, BorderLayout.EAST);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Current Odds"));

        // Odds table
        String[] oddsColumns = {"Horse", "Odds", "Implied Chance"};
        oddsTableModel = new DefaultTableModel(oddsColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        oddsTable = new JTable(oddsTableModel);
        JScrollPane oddsScrollPane = new JScrollPane(oddsTable);
        leftPanel.add(oddsScrollPane, BorderLayout.CENTER);

        //Refresh odds button
        JButton refreshOddsButton = new JButton("Refresh Odds");
        refreshOddsButton.addActionListener(_ -> updateOdds());
        leftPanel.add(refreshOddsButton, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Place Bet"));
        
        JPanel betFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Horse selector
        gbc.gridx = 0;
        gbc.gridy = 0;
        betFormPanel.add(new JLabel("Select Horse:"), gbc);

        gbc.gridx = 1;
        horseSelector = new JComboBox<>();
        updateHorseSelector();
        horseSelector.addActionListener(_ -> updatePotentialWinnings());
        betFormPanel.add(horseSelector, gbc);

        // Bet amount
        gbc.gridx = 0;
        gbc.gridy = 1;
        betFormPanel.add(new JLabel("Bet Amount:"), gbc);

        gbc.gridx = 1;
        betAmountField = new JTextField(10);
        betAmountField.addActionListener(_ -> updatePotentialWinnings());
        betFormPanel.add(betAmountField, gbc);

        // Potential winnings
        gbc.gridx = 0;
        gbc.gridy = 2;
        betFormPanel.add(new JLabel("Potential Winnings:"), gbc);

        gbc.gridx = 1;
        potentialWinningsLabel = new JLabel("$0.00");
        betFormPanel.add(potentialWinningsLabel, gbc);

        // Place bet button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton placeBetButton = new JButton("Place Bet");
        placeBetButton.addActionListener(_ -> placeBet());  
        betFormPanel.add(placeBetButton, gbc);

        centerPanel.add(betFormPanel, BorderLayout.NORTH);

        //Betting insights
        insightsArea = new JTextArea(5, 30);
        insightsArea.setEditable(false);
        insightsArea.setText("Place bets to receive insights.");
        JScrollPane insightsScrollPane = new JScrollPane(insightsArea);
        insightsScrollPane.setBorder(BorderFactory.createTitledBorder("Betting Insights"));
        centerPanel.add(insightsScrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Betting History"));

        // Bet history table
        String[] historyColumns = {"Horse", "Amount", "Odds", "Status", "P/L"};
        betHistoryTableModel = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        betHistoryTable = new JTable(betHistoryTableModel);
        JScrollPane historyScrollPane = new JScrollPane(betHistoryTable);
        rightPanel.add(historyScrollPane, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

        updatePotentialWinnings();
    }

    public void updateOdds(){
        bettingSystem.calculateOdds(horses, currentTrack);
        oddsTableModel.setRowCount(0);

        DecimalFormat df = new DecimalFormat("#.##");
        Map<String, Double> odds = bettingSystem.getCurrentOdds();
        
        for (Horse horse : horses) {
            String horseName = horse.getName();
            double oddsValue = odds.getOrDefault(horseName, 0.0);
            String impliedChance = df.format((1.0 / oddsValue) * 100) + "%";
            
            Object[] rowData = {horseName, df.format(oddsValue), impliedChance};
            oddsTableModel.addRow(rowData);
        }

        updateHorseSelector();
    }

    private void updateHorseSelector(){
        horseSelector.removeAllItems();
        for (Horse horse : horses) {
            horseSelector.addItem(horse.getName());
        }
    }

    private void updatePotentialWinnings(){
        try {
            String selectedHorse = (String) horseSelector.getSelectedItem();
            if (selectedHorse == null) return;
            
            double betAmount = Double.parseDouble(betAmountField.getText());
            Map<String, Double> odds = bettingSystem.getCurrentOdds();
            
            if (odds.containsKey(selectedHorse)) {
                double oddsValue = odds.get(selectedHorse);
                double potentialWin = betAmount * oddsValue;
                DecimalFormat df = new DecimalFormat("$#,##0.00");
                potentialWinningsLabel.setText(df.format(potentialWin));
            }
        } catch (NumberFormatException e) {
            potentialWinningsLabel.setText("$0.00");
        }
    }

    private void placeBet(){
        try {
            String selectedHorse = (String) horseSelector.getSelectedItem();
            if (selectedHorse == null) {
                JOptionPane.showMessageDialog(this, "Please select a horse.");
                return;
            }
            
            double betAmount = Double.parseDouble(betAmountField.getText());
            
            if (betAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Bet amount must be greater than zero.");
                return;
            }
            
            boolean success = bettingSystem.placeBet(selectedHorse, betAmount);
            
            if (success) {
                updateBalanceDisplay();
                updateBetHistory();
                JOptionPane.showMessageDialog(this, "Bet placed successfully!");
                
                // Update odds after bet is placed
                updateOdds();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to place bet. Check your balance.");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid bet amount.");
        }
    }

    private void updateBalanceDisplay() {
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        balanceLabel.setText("Balance: " + df.format(bettingSystem.getUserBalance()));
    }

    private void updateBetHistory() {
        betHistoryTableModel.setRowCount(0);
        DecimalFormat df = new DecimalFormat("$#,##0.00");
        
        for (BetRecord bet : bettingSystem.getBetHistory()) {
            String status = bet.isResolved() ? (bet.isWon() ? "WON" : "LOST") : "PENDING";
            String profitLoss = bet.isResolved() ? 
                (bet.isWon() ? df.format(bet.getProfit()) : "-" + df.format(bet.getBetAmount())) : 
                "--";
            
            Object[] rowData = {
                bet.getHorseName(),
                df.format(bet.getBetAmount()),
                String.format("%.2f", bet.getOdds()),
                status,
                profitLoss
            };
            betHistoryTableModel.addRow(rowData);
        }
        // Update insights as well
        insightsArea.setText(bettingSystem.getBettingInsights());
    }

    public void processRaceResults(String winningHorse) {
        bettingSystem.resolveBets(winningHorse);
        updateBalanceDisplay();
        updateBetHistory();
        
        JOptionPane.showMessageDialog(this, "Race results processed!\nWinning horse: " + winningHorse,"Race Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setTrack(Track track) {
        this.currentTrack = track;
        updateOdds();
    }

    public void setHorses(ArrayList<Horse> horses) {
        this.horses = horses;
        updateOdds();
        updateHorseSelector();
    }
    
}