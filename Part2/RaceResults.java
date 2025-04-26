import java.text.SimpleDateFormat;
import java.util.Date;

public class RaceResults {
    private String horseName;
    private String date;
    private int position;
    private double time;
    private String trackName;
    private boolean fallen;
    
    // Constructor
    public RaceResults(String horseName, int position, double time, boolean fallen, String trackName) {
        this.horseName = horseName;
        this.position = position;
        this.time = time;
        this.trackName = trackName;
        this.fallen = fallen;
        this.date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
        String result = date + ": " + horseName + " - Position: " + position + ", Time: " + formatTime(time);
        if (fallen) {
            result += " (FELL)";
        }
        return result;
    }

    private String formatTime(double time) {
        int minutes = (int) (time / 60);
        double seconds = time % 60;
        return String.format("%d:%05.2f", minutes, seconds);
    }
}

