public class horseracetest {
    public static void main(String[] args){
        Horse horse1 = new Horse('H', "Horse1", 0.5);

        horse1.fall();
        //Print out the values of the horse
        System.out.println(horse1.hasFallen());
        System.out.println("Horse name: " +horse1.getName());
        System.out.println("Symbol: " +horse1.getSymbol());
        System.out.println("Confidence level" + horse1.getConfidence());
        System.out.println("Distance Travelled: " +horse1.getDistanceTravelled());
    
        //Change the values of the horse
        horse1.goBackToStart();
        horse1.hasFallen();
        horse1.setConfidence(0.9);
        horse1.setSymbol('D');

        //Print out the new values of the horse
        System.out.println(horse1.getName() + " has a confidence level of " +horse1.getConfidence());
        System.out.println("Symbol: " +horse1.getSymbol());
        System.out.println(horse1.hasFallen());
        System.out.println("The new distance travelled is " + horse1.getDistanceTravelled());
    }


}
