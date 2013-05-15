package jeux;

import javax.swing.JFrame;


public abstract class Simulator extends JFrame {
	// The current state of the field.
    protected Field currentField;
    private SettingPanel opPane;
    
    // The current step of the simulation.
    protected int currentStep = 0;
    
    // The number of step needed
    private int numberStep;
    
    protected int alive;
    synchronized protected void incAlive() { alive++; }
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    // Temps de la simulation
    double tempsDepart;
    

    public Simulator(int rowNumber, int lineNumber, int stepNumber)	{
    	setTitle("Jeux de la vie");
    	
    	numberStep =  stepNumber;
    	
    	// init field
    	currentField = new Field(rowNumber, lineNumber);
    	populate();
    	
    	// Add the simulator view to the frame
    	view = new SimulatorView(rowNumber, lineNumber, this);
    	add(view);
    	pack();
    	System.out.println(alive);
    	view.showStatus(currentStep, currentField, alive);
    	setVisible(true);
    	
    	tempsDepart = System.currentTimeMillis();
    }
    
	public void live() {
		live(numberStep);	
	}

	public void live(int stepNumber) {
		tempsDepart = System.currentTimeMillis();
		
    	while (currentStep < stepNumber) {
    		liveOneStep();    		
    	}
    	
    	System.out.println("Executé en " + (System.currentTimeMillis() - tempsDepart) + " millisecondes");
    	
    	view.showStatus(currentStep, currentField, alive);
	}

	public void liveOneStep()	{
		if (currentStep < numberStep) {
			currentStep++;
			
			alive = 0;
			nextStep();
    		
    		// Show the starting state in the view.
    		view.showStatus(currentStep, currentField, alive);
    		
    		// Pause in order to let the time to view the situation
    		try {
    			Thread.sleep(250);
    		} catch (InterruptedException e) {
    			System.err.println("Can't pause the thread.");
    		}
    	}
	}

	/**
	 * Populate the field randomly
	 */
	public void populate(){
    	for(int i = 0; i<currentField.getDepth(); i++){
        	for(int j = 0; j<currentField.getWidth(); j++){
        		boolean b = Randomizer.GenerateRandomBoolean();
        		currentField.place(b, i, j);
        		if(b) incAlive();
        	}
    	}
    }
    
	/**
	 * Calculate the next step of the simulation
	 * @return The number of people alive
	 */
    abstract public void nextStep();
    
    /**
     * Finalize
     */
    protected void endSimulation() {
    	
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
    	alive = 0;
        currentStep = 0;
        populate();
        tempsDepart = System.currentTimeMillis();
        
        // Show the starting state in the view.
        view.showStatus(currentStep, currentField, alive);
    }
    
    public int getNbStep(){
    	return numberStep;
    }
    
    public double getTime(){
    	return System.currentTimeMillis() - tempsDepart;
    }
}
