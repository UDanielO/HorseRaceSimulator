import java.text.SimpleDateFormat;
import java.util.Date;

public class BetRecord {
    private String horseName;
    private double betAmount;
    private double odds;
    private boolean resolved;
    private boolean won;
    private String timestamp;
    
    public BetRecord(String horse, double betAmount, double odds) {
        this.horseName = horse;
        this.betAmount = betAmount;
        this.odds = odds;
        this.resolved = false;
        this.won = false;
        
        // Generate timestamp
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    public void resolveBet(boolean won) {
        this.resolved = true;
        this.won = won;
    }
    public double getPotentialPayout() {
        return betAmount * odds;
    }
    public double getProfit(){
        if (!won || !resolved){
            return 0;
        }
        return (betAmount * odds) - betAmount;
    }

    // Getters
    public String getHorseName() { 
        return horseName; 
    }
    public double getBetAmount() { 
        return betAmount; 
    }
    public double getOdds() { 
        return odds; 
    }
    public boolean isResolved() { 
        return resolved; 
    }
    public boolean isWon() { 
        return won; 
    }
    public String getTimestamp() { 
        return timestamp; 
    }

    // Setters
    public void setResolved(boolean resolved) { 
        this.resolved = resolved; 
    }
    public void setWon(boolean won) { 
        this.won = won; 
    }
    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }
    public void setBetAmount(double betAmount) {
        this.betAmount = betAmount;
    }
    public void setOdds(double odds) {
        this.odds = odds;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString(){
        String status = resolved ? (won ? "WON" : "LOST") : "PENDING";
        return String.format("%s: $%.2f on %s @ %.2f odds [%s]", timestamp, betAmount, horseName, odds, status);
    }

}
