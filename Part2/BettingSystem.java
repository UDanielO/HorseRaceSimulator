import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingSystem{
    private double userBalance;
    private List<BetRecord> betHistory;
    private Map<String, Double> currentOdds;
    private Map<String, Double> totalBetsPerHorse;

    public BettingSystem(){
        this.userBalance = 1000.0; // Initial balance
        this.betHistory = new ArrayList<>();
        this.currentOdds = new HashMap<>();
        this.totalBetsPerHorse = new HashMap<>();
    }

    public void calculateOdds(ArrayList<Horse> horses, Track track){
        currentOdds.clear();
        totalBetsPerHorse.clear();

        //multipliers for different attributes
        double speedWeight = 0.4;
        double staminaWeight = 0.2;
        double winRatioWeight = 0.3;
        double trackConditionWeight = 0.1;

        double totalOddsInverse = 0.0;

        //First pass
        for (Horse horse : horses){
            double speedFactor = horse.getSpeed() / 100.0;
            double staminaFactor = horse.getStamina() / 100.0;
            double winRatioFactor = horse.getWins();

            double trackEffect = calculateTrackEffect(horse, track);

            double performanceScore = (speedFactor * speedWeight) +
                    (staminaFactor * staminaWeight) +
                    (winRatioFactor * winRatioWeight) +
                    (trackEffect * trackConditionWeight);

            double odds = (1.0 / performanceScore) * 2.0 + 1.2;
            currentOdds.put(horse.getName(), odds);
            totalOddsInverse += 1.0 / odds;

        }

        double houseEdge = 0.05; // 5% house edge
        for (String horseName : currentOdds.keySet()){
            double currentOdd = currentOdds.get(horseName);
            double adjustedOdd = currentOdd * (1.0 - houseEdge) * (1.0 / totalOddsInverse);

            adjustedOdd = Math.round(adjustedOdd * 100.0) / 100.0; // Round to 2 decimal places

            adjustedOdd = Math.max( 1.1, adjustedOdd); // Ensure odds are at least 1.1
            currentOdds.put(horseName, adjustedOdd);
        }
    }

    private double calculateTrackEffect(Horse horse, Track track){
        double effect = 1.0;
        // Adjust effect based on horse's attributes and track conditions
        String weather = track.getWeatherCondition();
        switch (weather){
            case "Dry":
                effect *= 1.0;
                break;
            case "Muddy":
                if (horse.getBreed().equals("Appaloosa")) {
                    effect *= 1.1;
                } else {
                    effect *= 0.9;
                }
                break;
            case "Icy":
                if (horse.getHorseShoe().equals("Winter")) {
                    effect *= 0.95;
                } else {
                    effect *= 0.7;
                }
                break;
            case "Wet":
                if (horse.getBreed().equals("Arabian")) {
                    effect *= 0.85;
                }
                break;
            default:
                break;
        }
        String trackShape = track.getTrackShape();
        switch (trackShape){
            case "Figure-Eight":
                if (horse.getAgility() > 70) {
                    effect *= 1.1;
                } else {
                    effect *= 0.9;
                }
                break;
            default:
                break;
        }
        return effect;
    }

    public void updateOddsAfterBet(String horseName, double betAmount){
        double currentTotal = totalBetsPerHorse.getOrDefault(horseName, 0.0);
        totalBetsPerHorse.put(horseName, currentTotal + betAmount);

        double totalBets = totalBetsPerHorse.values().stream().mapToDouble(Double::doubleValue).sum();
        
        if (totalBets > 100) {
            for (String horse : currentOdds.keySet()) {
                double currentOdds = this.currentOdds.get(horse);
                double horseTotalBets = totalBetsPerHorse.getOrDefault(horse, 0.0);
                
                // If this horse is receiving a lot of bets, slightly increase its odds
                if (horseTotalBets > 0) {
                    double betRatio = horseTotalBets / totalBets;
                    if (betRatio > 0.3) { 
                        // Increase odds slightly (lower chance of winning from bookmaker's perspective)
                        double adjustment = 1.0 + (betRatio - 0.3) * 0.5;
                        this.currentOdds.put(horse, currentOdds * adjustment);
                    }
                }
            }
        }
    }

    public boolean placeBet(String horseName, double betAmount){
        if (betAmount > userBalance || betAmount <= 0){
            return false;
        }

        if (!currentOdds.containsKey(horseName)){
            return false;
        }

        //Place bet
        userBalance -= betAmount;
        double odds = currentOdds.get(horseName);

        //New bet record
        BetRecord bet = new BetRecord(horseName, betAmount, odds);
        betHistory.add(bet);
        updateOddsAfterBet(horseName, betAmount);

        return true;
    }

    public void resolveBets(String winningHorse){
        for (BetRecord bet : betHistory){
            if(!bet.isResolved()){
                boolean won = bet.getHorseName().equals(winningHorse);
                bet.resolveBet(won);

                if (won){
                    double winnings = bet.getBetAmount() * bet.getOdds();
                    userBalance += winnings;
                }
            }
        }
    }

    public String getBettingInsights() {
        if (betHistory.size() < 3) {
            return "Place more bets to receive personalized betting insights.";
        }
        
        int totalBets = 0;
        int winningBets = 0;
        double totalWagered = 0;
        double totalWon = 0;
        boolean alwaysBetsHighOdds = true;
        boolean alwaysBetsLowOdds = true;
        double avgOddsBet = 0;
        
        for (BetRecord bet : betHistory) {
            if (bet.isResolved()) {
                totalBets++;
                totalWagered += bet.getBetAmount();
                avgOddsBet += bet.getOdds();
                
                if (bet.isWon()) {
                    winningBets++;
                    totalWon += bet.getBetAmount() * bet.getOdds();
                }
                
                // Check betting patterns
                if (bet.getOdds() < 2.0) alwaysBetsHighOdds = false;
                if (bet.getOdds() > 3.0) alwaysBetsLowOdds = false;
            }
        }
        
        if (totalBets == 0) return "No resolved bets yet.";
        
        avgOddsBet /= totalBets;
        double winRate = (double) winningBets / totalBets;
        double roi = totalWagered > 0 ? (totalWon - totalWagered) / totalWagered : 0;
        
        StringBuilder insights = new StringBuilder();
        insights.append(String.format("Win rate: %.1f%% (%d/%d bets)\n", winRate * 100, winningBets, totalBets));
        insights.append(String.format("Return on investment: %.1f%%\n", roi * 100));
        
        // Add betting advice
        if (winRate < 0.3) {
            insights.append("Consider betting on favorites with lower odds for more consistent wins.\n");
        } else if (roi < 0) {
            insights.append("You're winning bets but losing money overall. Try more selective betting.\n");
        }
        
        if (alwaysBetsLowOdds) {
            insights.append("You tend to bet only on favorites. Consider occasional bets on underdogs for bigger payouts.\n");
        } else if (alwaysBetsHighOdds) {
            insights.append("You prefer betting on underdogs. Mix in some safer bets for more consistent returns.\n");
        }
        
        if (avgOddsBet > 4.0) {
            insights.append("Your average bet has very high odds. These are less likely to win but pay more when they do.\n");
        }
        
        return insights.toString();
    }

    // Getters and Setters
    public double getUserBalance() {
        return userBalance;
    }
    public void setUserBalance(double userBalance) {
        this.userBalance = userBalance;
    }
    public List<BetRecord> getBetHistory() {
        return betHistory;
    }
    public void setBetHistory(List<BetRecord> betHistory) {
        this.betHistory = betHistory;
    }
    public Map<String, Double> getCurrentOdds() {
        return currentOdds;
    }
    public void setCurrentOdds(Map<String, Double> currentOdds) {
        this.currentOdds = currentOdds;
    }
    public void addFunds(double amount){
        if (amount > 0){
            userBalance += amount;
        }
    }

}
