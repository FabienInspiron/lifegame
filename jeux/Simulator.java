package jeux;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Simulator extends JFrame {
	// The current state of the field.
    private Field field;
    
    private SettingPanel opPane;
    
    // The current step of the simulation.
    private int currentStep = 0;
    
    // The number of step needed
    private int numberStep;
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    public Simulator()	{
    	setTitle("Jeux de la vie");
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	opPane =  new SettingPanel(this);
    	setContentPane(opPane);
    	pack();
    	setVisible(true);
    }
    
    public void setConfiguration(int rowNumber, int lineNumber, int stepNumber) {
    	remove(opPane);
    	
    	numberStep =  stepNumber;
    	
		field = new Field(rowNumber, lineNumber);
    	view = new SimulatorView(rowNumber, lineNumber, this);
    	
    	setContentPane(view);
    	revalidate();
    	pack();
    	setVisible(true);
    	view.repaint();
    	view.revalidate();
    	populate();
    	
    	// Show the starting state in the view.
        view.showStatus(currentStep, field, 0);
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
    		view.showStatus(currentStep, field, population);
    		
    		// Pause in order to let the time to view the situation
    		try {
    			Thread.sleep(250);
    		} catch (InterruptedException e) {
    			System.err.println("Can't pause the thread.");
    		}
    		
    	}
    	
    	view.showStatus(currentStep, field, population);
	}

	public void liveOneStep()	{
		if (currentStep < numberStep) {
			currentStep++;

			int population = nextStep();
    		System.out.println("nombre : " + population);
    		
    		// Show the starting state in the view.
    		view.showStatus(currentStep, field, population);
    		
    		// Pause in order to let the time to view the situation
    		try {
    			Thread.sleep(250);
    		} catch (InterruptedException e) {
    			System.err.println("Can't pause the thread.");
    		}
    	}
	}

	public void populate(){
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		field.place(Randomizer.GenerateRandomBoolean(), i, j);
        	}
    	}
    }
    
    // Calculate the next step of the simulation
    public int nextStep(){
    	int nbcase = 0;
    	
    	Field tmp = new Field(field);
    	
    	for(int i = 0; i<tmp.getDepth(); i++){
        	for(int j = 0; j<tmp.getWidth(); j++){
        		int nbadj = tmp.nbAdjacentTrue(i, j);
        		if(tmp.getState(i, j)) nbcase++;

        		/**
        		 * Une cellule vide à l'étape n-1 et ayant exactement 3 
        		 * voisins sera occupée à l'étape suivante. 
        		 * (naissance liée à un environnement optimal)
        		 */
        		if(nbadj == 3){
        			field.place(true, i, j);
        		}
        		
        		/**
        		 * Une cellule occupée à l'étape n-1 et ayant 2 ou 3 voisins sera maintenue 
        		 * à l'étape n sinon elle est vidée. 
        		 * (destruction par désertification ou surpopulation)
        		 */
        		else if(nbadj == 2 && field.getState(i, j)){
        			field.place(true, i, j);
        		}
        		else{ 
        			field.place(false, i, j);
        		}
        	}
    	}
    	
    	return nbcase;
    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        currentStep = 0;
        populate();

        // Show the starting state in the view.
        view.showStatus(currentStep, field, 0);
    }
    
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
            	Simulator sim = new Simulator();
            }
        });
	}
}
