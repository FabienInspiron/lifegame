package jeux;

import java.util.concurrent.Semaphore;

import jeux.SimulatorQuart.Worker;


public class SimulatorProducer4Th extends Simulator {

    private Producer4[] producers;
    
    // The futur state of the field.
 	Field futurField;

    public SimulatorProducer4Th(int rowNumber, int colNumber, int stepNumber) {
        super(rowNumber, colNumber, stepNumber);

        producers = new Producer4[4];
        
        producers[0] = new Producer4(0, 0, rowNumber/2, colNumber/2);
        producers[1] = new Producer4(0, colNumber/2, rowNumber/2, colNumber);
        producers[2] = new Producer4(rowNumber/2, 0, rowNumber, colNumber/2);
        producers[3] = new Producer4(rowNumber/2, colNumber/2, rowNumber, colNumber);

        futurField = new Field(rowNumber, colNumber);
        
        for (int i = 0; i < 4; i++) {
	            producers[i].start();
	    }
    }

    // Calculate the next step of the simulation
    @Override
    public void nextStep() {
        for (int i = 0; i < 4; i++) {
            producers[i].await();
        }
        currentField = futurField;
    }

    @Override
    protected void endSimulation() {
        for (int i = 0; i < 4; i++) {
                producers[i].stop();
        }
    }
    
    /*@Override
    public void reset() {
        super.reset();
        for (int i = 0; i < currentField.getDepth(); i++) {
            for (int j = 0; j < currentField.getWidth(); j++) {
                producers[i][j].setCurrentField(currentField);
            }
        }
    }*/
    
    class Producer4 extends Thread {
        private Semaphore ready;
        
        private int hautGaucheI;
		private int hautGaucheJ;
		private int hautDroiteJ;
		private int basGaucheI;

		public Producer4(int hautGaucheI, int hautGaucheJ, int basGaucheI , 
				int hautDroiteJ) {
			super();
			this.hautGaucheI = hautGaucheI;
			this.hautGaucheJ = hautGaucheJ;
			this.hautDroiteJ = hautDroiteJ;
			this.basGaucheI = basGaucheI;
            ready = new Semaphore(0);
		}

        public void run() {
        	boolean isAlive = false;
            int nbadj = 0;
            while (true) {
            	for (int i = hautGaucheI; i < basGaucheI; i++)
    				for (int j = hautGaucheJ; j < hautDroiteJ; j++) {
    					nbadj = currentField.nbAdjacentTrue(i, j);
    					
    					/**
    					 * Une cellule vide à l'étape n-1 et ayant exactement 3
    					 * voisins sera occupée à l'étape suivante. (naissance liée
    					 * à un environnement optimal)
    					 */
    					if (nbadj == 3
    							|| (nbadj == 2 && currentField.getState(i, j))) {
    						isAlive = true;
    					} else {
    						isAlive = false;
    					}

    					futurField.place(isAlive, i, j);
    					if (isAlive) {
    						incAlive();
    					}
    				}
                synchronized (this) {
                    ready.release();
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.out.println("oups : " + e);
                        return;
                    }
                }
            }
        }

        public synchronized void await() {
            try {
                ready.acquire();
            } catch (InterruptedException ex) {
            }
            notify();
        }
    }
}
