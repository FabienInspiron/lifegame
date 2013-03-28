package jeux;

import java.util.concurrent.Semaphore;

public class Simulator {
	
	// Semaphore global contenant ligne*colones elements
	Semaphore semGlob;
	
    // The current state of the field.
    private Field field;
    
    // The current step of the simulation.
    private int step;
    
    private int nbStep;
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    public Simulator(int depth, int width, int nbStep) {
    	field = new Field(depth, width);
    	view = new SimulatorView(depth, width);
    	semGlob = new Semaphore(depth*width);
    	populate();
    	this.nbStep = nbStep;
    }
    
    public void populate(){
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		field.place(Randomizer.GenerateRandomBoolean(), i, j);
        	}
    	}
    }
    
    
//    // Calculate the next step of the simulation
//    public int nextStep(){
//    	int nbcase = 0;
//    	
//    	Field tmp = new Field(field);
//    	
//    	for(int i = 0; i<tmp.getDepth(); i++){
//        	for(int j = 0; j<tmp.getWidth(); j++){
//        		int nbadj = tmp.nbAdjacentTrue(i, j);
//        		if(tmp.getState(i, j)) nbcase++;
//
//        		/**
//        		 * Une cellule vide à l'étape n-1 et ayant exactement 3 
//        		 * voisins sera occupée à l'étape suivante. 
//        		 * (naissance liée à un environnement optimal)
//        		 */
//        		if(nbadj == 3){
//        			field.place(true, i, j);
//        		}
//        		
//        		/**
//        		 * Une cellule occupée à l'étape n-1 et ayant 2 ou 3 voisins sera maintenue 
//        		 * à l'étape n sinon elle est vidée. 
//        		 * (destruction par désertification ou surpopulation)
//        		 */
//        		else if(nbadj == 2 && field.getState(i, j)){
//        			field.place(true, i, j);
//        		}
//        		else{ 
//        			field.place(false, i, j);
//        		}
//        	}
//    	}
//    	
//    	return nbcase;
//    }
    
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    public void createThread(){
    	Field tmp = new Field(field);
    	for(int i = 0; i<tmp.getDepth(); i++){
        	for(int j = 0; j<tmp.getWidth(); j++){
                SimulationThreadFull thread = new SimulationThreadFull(field,nbStep,semGlob, i, j);
                thread.run();
        	}
        }
    }
    
    public static void main(String[] args) {
        Simulator sim = new Simulator(10,10, 10);
        sim.createThread();
        
//        int day = 0;
//        while (day<50) {
//            //int nb = sim.nextStep();
//            //System.out.println("nombre : " + nb);
//        	sim.createThread();
//        	
//            // Show the starting state in the view.
//            sim.view.showStatus(day, sim.field);
//
//            // Pause in order to let the time to view the situation
//            try {
//                Thread.sleep(250);
//            } catch (InterruptedException e) {
//                System.err.println("Can't pause the thread.");
//            }
//
//            day++;
//        }
  
        //sim.view.showStatus(day, sim.field);
	}
}
