import java.awt.Color;

public class Horse {
    private String name;
    private String breed;
    private String color;
    private String symbol;
    private String coatColor;
    private String saddle;
    private String horseShoe;

    // Horse attributes
    private int speed;
    private int stamina;
    private int agility;
    private int confidence;

    //Race statistics
    private int races;
    private int wins;
    private double BestTime;

    public Horse(String name, String breed, String color, String symbol, String saddle, String coatColor, String horseShoe){
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.symbol = symbol;
        this.saddle = saddle;
        this.coatColor = coatColor;
        this.horseShoe = horseShoe;

        InitializeAttributes();

        this.races = 0;
        this.wins = 0;
        this.BestTime = Double.MAX_VALUE;

    }

    private void InitializeAttributes() {
        //Set default values for horse attributes
        speed = 50;
        stamina = 50;
        agility = 50;
        confidence = 50;

        // Adjust attributes based on breed
        switch (breed) {
            case "Thoroughbred":
                speed += 20;
                stamina += 10;
                break;
            case "Arabian":
                speed += 10;
                stamina += 20;
                agility += 10;
                break;
            case "Quarter Horse":
                speed += 15;
                agility += 15;
                stamina += 5;
                break;
            case "Appaloosa":
                agility += 20;
                confidence += 10;
                stamina += 5;
                break;
            default:
                break;
        }

        // Adjust attributes based on equipment
        if (saddle.equals("Racing")) {
            speed += 5;
            agility += 5;
        } else if (saddle.equals("Western")) {
            speed -= 5;
            stamina += 5;
        }
        if (horseShoe.equals("Lightweight")) {
            speed += 5;
            stamina += 5;
        } else if (horseShoe.equals("Winter")) {
            speed -= 5;
            agility += 5;
        }

        //Ensure attributes are within bounds
        speed = Math.max(0, Math.min(speed, 100));
        stamina = Math.max(0, Math.min(stamina, 100));
        agility = Math.max(0, Math.min(agility, 100));
        confidence = Math.max(0, Math.min(confidence, 100));
    }
    
    public Color getColorObject() {
        switch (coatColor) {
            case "Brown": return new Color(139, 69, 19);
            case "Black": return Color.BLACK;
            case "White": return Color.WHITE;
            case "Grey": return Color.GRAY;
            case "Chestnut": return new Color(205, 92, 92);
            case "Palomino": return new Color(238, 221, 130);
            default: return Color.BLACK;
        }
    }

    //race results
    public void recordRace(boolean won, double time){
        races++;
        if (won) {
            wins++;
        }
        if (time < BestTime) {
            BestTime = time;
        }
    }

    public double getWinRatio(){
        return races > 0 ? (double) wins / races : 0;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBreed() {
        return breed;
    }
    public void setBreed(String breed) {
        this.breed = breed;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getSaddle() {
        return saddle;
    }
    public void setSaddle(String saddle) {
        this.saddle = saddle;
    }
    public String getCoatColor() {
        return coatColor;
    }
    public void setCoatColor(String coatColor) {
        this.coatColor = coatColor;
    }
    public String getHorseShoe() {
        return horseShoe;
    }
    public void setHorseShoe(String horseShoe) {
        this.horseShoe = horseShoe;
    }

    
    public int getSpeed() {
        return speed;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public int getStamina() {
        return stamina;
    }
    public void setStamina(int stamina) {
        this.stamina = stamina;
    }
    public int getAgility() {
        return agility;
    }
    public void setAgility(int agility) {
        this.agility = agility;
    }
    public int getConfidence() {
        return confidence;
    }
    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }
    
}
