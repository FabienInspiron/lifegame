package jeux;

import javax.swing.JFrame;

public abstract class Simulator extends JFrame {
	// The current state of the field.
    protected Field currentField;
    private SettingPanel opPane;
    
    // The current step of the simulation.
    private int currentStep = 0;
    
    // The number of step needed
    private int numberStep;
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    public Simulator(int rowNumber, int lineNumber, int stepNumber)	{
    	setTitle("Jeux de la vie");
    	//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	//setContentPane(opPane);
    	
    	
    	numberStep =  stepNumber;
    	
    	// init field
    	currentField = new Field(rowNumber, lineNumber);
    	populate();
    	
    	// Add the simulator view to the frame
    	view = new SimulatorView(rowNumber, lineNumber, this);
    	add(view);
    	
    	setVisible(true);
    }
    
    

	public void live() {
		live(numberStep);	
	}

	public void live(int stepNumber) {
		int population = 0;
    	while (currentStep < stepNumber) {
    		currentStep++;

    		population = nextStep();
    		System.out.println("nombre : " + population);
    		
    		// Show the starting state in the view.
    		view.showStatus(currentStep, currentField, population);
    		
    		// Pause in order to let the time to view the situation
    		try {
    			Thread.sleep(250);
    		} catch (InterruptedException e) {
    			System.err.println("Can't pause the thread.");
    		}
    		
    	}
    	
    	view.showStatus(currentStep, currentField, population);
	}

	public void liveOneStep()	{
		if (currentStep < numberStep) {
			currentStep++;

			int population = nextStep();
    		System.out.println("nombre : " + population);
    		
    		// Show the starting state in the view.
    		view.showStatus(currentStep, currentField, population);
    		
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
        		currentField.place(Randomizer.GenerateRandomBoolean(), i, j);
        	}
    	}
    }
    
	/**
	 * Calculate the next step of the simulation
	 * @return The number of people alive
	 */
    abstract public int nextStep();
    
    /**
     * Finalize
     */
    protected void endSimulation() {
    	
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        currentStep = 0;
        populate();

        // Show the starting state in the view.
        view.showStatus(currentStep, currentField, 0);
    }
    
}
