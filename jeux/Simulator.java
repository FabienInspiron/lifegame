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
    public int nextStep(){
    	int nbcase = 0;
    	
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		int nbadj = field.nbAdjacentTrue(i, j);
        		if(field.getState(i, j)) nbcase++;

        		/**
        		 * Une cellule vide à l'étape n-1 et ayant exactement 3 
        		 * voisins sera occupée à l'étape suivante. 
        		 * (naissance liée à un environnement optimal)
        		 */
        		if(nbadj == 3){
        			field.place(true, i, j);
        			System.out.print("+"+nbadj);
        		}
        		
        		/**
        		 * Une cellule occupée à l'étape n-1 et ayant 2 ou 3 voisins sera maintenue 
        		 * à l'étape n sinon elle est vidée. 
        		 * (destruction par désertification ou surpopulation)
        		 */
        		else if(nbadj == 2 && field.getState(i, j)){
        			field.place(true, i, j);
        			System.out.print("+"+nbadj);
        		}
        		else{ 
        			field.place(false, i, j);
        			System.out.print("-"+nbadj);
        		}
        	}
    	}
    	
    	return nbcase;
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
        Simulator sim = new Simulator(50,50);
        int day = 0;
        while (day<2) {
            int nb = sim.nextStep();
            System.out.println("nombre : " + nb);
            
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
  
        sim.view.showStatus(day, sim.field);
	}
}
