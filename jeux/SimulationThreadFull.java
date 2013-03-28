package jeux;

import java.util.concurrent.Semaphore;

public class SimulationThreadFull extends Thread{
	// The current state of the field.
	private Field field;

	private int i;
	private int j;
	private int nbStep;
	private int currentStep;
	private Semaphore semGlob;

	/**
	 * Constructeur normal
	 * @param field
	 */
	public SimulationThreadFull(Field field,int step, Semaphore sem, int i, int j) {
		this.field = field;
		this.i = i;
		this.j = j;
		nbStep = step;
		currentStep = 0;
		semGlob = sem;
	}

	/**
	 * Methode run qui calcul un nextStep
	 */
	@Override
	public void run(){
		while(currentStep <= nbStep){
			currentStep++;

			/**
			 * Aquisition du semaphore
			 */
			try {
				semGlob.acquire();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			synchronized (field) {
				int nbadj = field.nbAdjacentTrue(i, j);

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

			/**
			 * Blocage du thread pour attendre la fin du calcul de toutes les lignes
			 */
			synchronized (semGlob) {
				try {
					wait();
				} catch (InterruptedException e) {
					System.out.println("Impossible de stopper le thread");
				}
			}

			/**
			 * Relacher le semaphore
			 */
			semGlob.release();
			System.out.println("boucle"+i+j+"-"+semGlob);
		}
	}
}
