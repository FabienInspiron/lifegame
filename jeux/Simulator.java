package jeux;

public class Simulator {
    // The current state of the field.
    private Field field;
    
    // The current step of the simulation.
    private int step;
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    public Simulator(int depth, int width) {
    	field = new Field(depth, width);
    	view = new SimulatorView(depth, width);
    	populate();
    }
    
    public void populate(){
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		field.place(Randomizer.GenerateRandomBoolean(), i, j);
        	}
    	}
    }
    
    // Calculate the next step of the simulation
    public void nextStep(){
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		if(field.getState(i, j)){
        			
        		}
        	}
    	} 	
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    public static void main(String[] args) {
    	  // Game initialization
        Simulator sim = new Simulator(50,50);
        //sim.init();
        //System.out.println("Game starts with " + sim.nbHumansAlive() + " humans!");
        // Iterate until no alive human remains
        int day = 0;
        while (day<100) {

            sim.nextStep();

            // Show the starting state in the view.
            sim.view.showStatus(day, sim.field);

            // Pause in order to let the time to view the situation
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                System.err.println("Can't pause the thread.");
            }

            day++;
        }
        
        System.out.println("All humans have been eaten by day " + day + "th");
        sim.view.showStatus(day, sim.field);
	}
}
