public class RaceResults {
    private String date;
    private int position;
    private double time;
    private String trackName;
    private boolean fallen;
    
    // Constructor
    public RaceResults(String date, int position, double time, String trackName, boolean fallen) {
        this.date = date;
        this.position = position;
        this.time = time;
        this.trackName = trackName;
        this.fallen = fallen;
    }
    
    // Getter methods
    public String getDate() { 
        return date; 
    }
    public int getPosition() { 
        return position; 
    }
    public double getTime() { 
        return time; 
    }
    public String getTrackName() { 
        return trackName; 
    }
    public boolean hasFallen() { 
        return fallen; 
    }
    // Setter methods
    public void setDate(String date) { 
        this.date = date; 
    }
    public void setPosition(int position) { 
        this.position = position; 
    }
    public void setTime(double time) { 
        this.time = time; 
    }
    public void setTrackName(String trackName) { 
        this.trackName = trackName; 
    }
    public void setFallen(boolean fallen) { 
        this.fallen = fallen; 
    }
    
    @Override
    public String toString() {
        return date + " - Position: " + position + 
               ", Time: " + time + "s, Track: " + trackName + 
               (fallen ? " (FELL)" : "");
    }
}

