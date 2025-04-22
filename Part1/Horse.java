
/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    public char horseSymbol;
    public String horseName;
    public double horseConfidence;
    public int distanceTravelled;
    public boolean hasFallen;
    
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.horseSymbol = horseSymbol; 
        this.horseName = horseName; 
        this.horseConfidence = horseConfidence; 
        this.distanceTravelled = 0;
       
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        this.hasFallen = true;
        
    }
    
    public double getConfidence()
    {
        return this.horseConfidence;
        
    }
    
    public int getDistanceTravelled()
    {
        return this.distanceTravelled;

    }
    
    public String getName()
    {
        return this.horseName;
    }
    
    public char getSymbol()
    {
        return this.horseSymbol;
    }
    
    public void goBackToStart()
    {
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }
    
    public boolean hasFallen()
    {
        return this.hasFallen;
    }

    public void moveForward()
    {
        this.distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        this.horseConfidence = newConfidence;
        return;
    }
    
    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
        return;
    }

    public void setName(String newName)
    {
        this.horseName = newName;
        return;
    }
    
}
