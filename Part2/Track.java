public class Track {
    private int laneCount;
    private int trackLength;
    private String trackShape;
    private String weatherCondition;

    public Track(int laneCount, int trackLength, String trackShape, String weatherCondition) {
        this.laneCount = laneCount;
        this.trackLength = trackLength;
        this.trackShape = trackShape;
        this.weatherCondition = weatherCondition;
    }

    public double getSpeedFactor() {
        double speedFactor = 1.0;

        // Adjust speed factor based on weather condition
        switch (weatherCondition) {
            case "Dry":
                speedFactor *= 1.0; // Normal speed
                break;
            case "Rainy":
                speedFactor *= 0.8; // 20% slower
                break;
            case "Sunny":
                speedFactor *= 1.2; // 20% faster
                break;
            case "Icy":
                speedFactor *= 0.5; // 50% slower
                break;
            case "Windy":
                speedFactor *= 0.9; // 10% slower
                break;
            case "Muddy":
                speedFactor *= 0.7; // 30% slower
                break;
            default:
                break;
        }

        return speedFactor;
    }

    //Fall risk based on track conditions
    public double getFallRisk() {
        double fallRisk = 0.0;

        // Adjust fall risk based on weather condition
        switch (weatherCondition) {
            case "Dry":
                fallRisk *= 0.01; // 1% chance of falling
                break;
            case "Wet":
                fallRisk *= 0.03; // 3% chance of falling
                break;
            case "Sunny":
                fallRisk *= 0.02; // 2% chance of falling
                break;
            case "Icy":
                fallRisk *= 0.15; // 15% chance of falling
                break;
            case "Muddy":
                fallRisk *= 0.06; // 6% chance of falling
                break;
            default:
                break;
        }

        return fallRisk;
    }

    //Confidence factor based on track conditions
    public double getConfidenceFactor() {
        double confidenceFactor = 1.0;

        // Adjust confidence factor based on weather condition
        switch (weatherCondition) {
            case "Dry":
                confidenceFactor *= 1.0; // Normal confidence
                break;
            case "Rainy":
                confidenceFactor *= 0.95; // 5% less confident
                break;
            case "Sunny":
                confidenceFactor *= 1.2; // 20% more confident
                break;
            case "Icy":
                confidenceFactor *= 0.5; // 50% less confident
                break;
            case "Windy":
                confidenceFactor *= 0.9; // 10% less confident
                break;
            case "Muddy":
                confidenceFactor *= 0.7; // 30% less confident
                break;
            default:
                break;
        }

        return confidenceFactor;
    }

    //turn difficulty based on track shape
    public double getTurnDifficulty() {
        double turnDifficulty = 0.1;

        // Adjust turn difficulty based on track shape
        switch (trackShape) {
            case "Oval":
                turnDifficulty *= 0.1; // Easy turns
                break;
            case "Figure-Eight":
                turnDifficulty *= 0.3; // Difficult turns
                break;
            case "Custom":
                turnDifficulty *= 0.2; // Moderate turns
                break;
            default:
                break;
        }

        return turnDifficulty;
    }

    //Getters and Setters
    public int getLaneCount() {
        return laneCount;
    }
    public void setLaneCount(int laneCount) {
        this.laneCount = laneCount;
    }
    public int getTrackLength() {
        return trackLength;
    }
    public void setTrackLength(int trackLength) {
        this.trackLength = trackLength;
    }
    public String getTrackShape() {
        return trackShape;
    }
    public void setTrackShape(String trackShape) {
        this.trackShape = trackShape;
    }
    public String getWeatherCondition() {
        return weatherCondition;
    }
    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    @Override
    public String toString() {
        return "Track{" +
                "laneCount=" + laneCount +
                ", trackLength=" + trackLength +
                ", trackShape='" + trackShape + '\'' +
                ", weatherCondition='" + weatherCondition + '\'' +
                '}';
    }
}

