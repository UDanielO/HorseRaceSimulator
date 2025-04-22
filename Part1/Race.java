import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McRaceface
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private Horse lane1Horse;
    private Horse lane2Horse;
    private Horse lane3Horse;
    private Horse winner;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance) {
        if (distance <= 0) {
            System.out.println("Race distance must be positive");
            raceLength = 10;
        } else {
            raceLength = distance;
        }
        // Rest of the constructor...
        lane1Horse = null;
        lane2Horse = null;
        lane3Horse = null;
        winner = null;
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (theHorse == null) {
            System.out.println("Cannot add null horse");
            return;
        }
        if (laneNumber < 1 || laneNumber > 3) {
            System.out.println("Lane number must be between 1 and 3");
            return;
        }
        // Check if the lane is already occupied
        if (laneNumber == 1)
        {
            if (lane1Horse != null) {
                System.out.println("Lane 1 already has a horse");
                return;
            }

            lane1Horse = theHorse;
        }
        else if (laneNumber == 2)
        {
            if (lane1Horse != null) {
                System.out.println("Lane 2 already has a horse");
                return;
            }
            lane2Horse = theHorse;
        }
        else if (laneNumber == 3)
        {
            if (lane1Horse != null) {
                System.out.println("Lane 3 already has a horse");
                return;
            }
            lane3Horse = theHorse;
        }
        else
        {
            System.out.println("Cannot add horse to lane " + laneNumber + " because there is no such lane");
        }
    }
    
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        
        //reset all the lanes (all horses not fallen and back to 0). 
        lane1Horse.goBackToStart();
        lane2Horse.goBackToStart();
        lane3Horse.goBackToStart();
                      
        winner = null;

        while (!finished)
        {
            //move each horse
            moveHorse(lane1Horse);
            moveHorse(lane2Horse);
            moveHorse(lane3Horse);
                        

            //check if horses are in lanes
            if (lane1Horse == null)
            {
                System.out.println("Lane 1 is empty");
            }
            else if (lane2Horse == null)
            {
                System.out.println("Lane 2 is empty");
            }
            else if (lane3Horse == null)
            {
                System.out.println("Lane 3 is empty");
            }

            //print the race positions
            printRace();
            
            //if any of the three horses has won the race is finished
            if(raceWonBy(lane1Horse)){
                winner = lane1Horse;
                finished = true;
            }
            else if(raceWonBy(lane2Horse)){
                winner = lane2Horse;
                finished = true;
            }
            else if (raceWonBy(lane3Horse)){
                winner = lane3Horse;
                finished = true;
            }
           
            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(InterruptedException e){
                System.out.println("Error: " + e);
            }
        }
        //print the winner
        printWinner();
    }

    //return winner of the race
    private void printWinner()
    {
        if(winner != null){
            System.out.println("And the winner is " + winner.getName());
        }
        else{
            System.out.println("Race ended with no winner");
        }
        return;
    }

    //reset the race
    public void resetRace() {
        if (lane1Horse != null) lane1Horse.goBackToStart();
        if (lane2Horse != null) lane2Horse.goBackToStart();
        if (lane3Horse != null) lane3Horse.goBackToStart();
        winner = null;
        System.out.println("Race has been reset");
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you stable the horse by only subtracting 1.0 from the confidence
            if (Math.random() < (0.1 * (1.0 - theHorse.getConfidence()))) {
                theHorse.fall();
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();
        
        printLane(lane1Horse);
        System.out.println();
        
        printLane(lane2Horse);
        System.out.println();
        
        printLane(lane3Horse);
        System.out.println();
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();
        
        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u2322');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
