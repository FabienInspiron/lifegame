package jeux;


public class SimulatorLinear extends Simulator {    
    
    public SimulatorLinear(int rowNumber, int lineNumber, int stepNumber) {
		super(rowNumber, lineNumber, stepNumber);
		// TODO Auto-generated constructor stub
	}

	// Calculate the next step of the simulation
    public int nextStep(){
    	int nbcase = 0;
    	
    	Field tmp = new Field(currentField);
    	
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
        			currentField.place(true, i, j);
        		}
        		
        		/**
        		 * Une cellule occupée à l'étape n-1 et ayant 2 ou 3 voisins sera maintenue 
        		 * à l'étape n sinon elle est vidée. 
        		 * (destruction par désertification ou surpopulation)
        		 */
        		else if(nbadj == 2 && currentField.getState(i, j)){
        			currentField.place(true, i, j);
        		}
        		else{ 
        			currentField.place(false, i, j);
        		}
        	}
    	}
    	
    	return nbcase;
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
