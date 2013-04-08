package jeux;


public class SimulatorLinear extends Simulator {    
    
    public SimulatorLinear(int rowNumber, int lineNumber, int stepNumber) {
		super(rowNumber, lineNumber, stepNumber);
		// TODO Auto-generated constructor stub
	}

	// Calculate the next step of the simulation
    public void nextStep(){
    	Field tmp = new Field(currentField);
    	
    	for(int i = 0; i<tmp.getDepth(); i++){
        	for(int j = 0; j<tmp.getWidth(); j++){
        		int nbadj = tmp.nbAdjacentTrue(i, j);
        		boolean isAlive;
        		
        		/**
    			 * Une cellule vide à l'étape n-1 et ayant exactement 3 voisins sera
    			 * occupée à l'étape suivante. (naissance liée à un environnement
    			 * optimal)
    			 */
    			if (nbadj == 3 || (nbadj == 2 && tmp.getState(i, j))) {
    				isAlive = true;
    			} else {
    				isAlive = false;
    			}

    			currentField.place(isAlive, i, j);
    			if (isAlive) {
    				incAlive();
    			}
        	}
    	}
    }
    
//    public static void main(String[] args) {
//    	SwingUtilities.invokeLater(new Runnable()
//        {
//            public void run()
//            {
//            	SimulatorLinear sim = new SimulatorLinear();
//            }
//        });
//	}
}
