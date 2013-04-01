package jeux;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Simulator100Thread extends Simulator {
	final CyclicBarrier barrier;

	// The futur state of the field.
	Field futurField;

	public Simulator100Thread(int rowNumber, int lineNumber, int stepNumber) {
		super(rowNumber, lineNumber, stepNumber);
		barrier = new CyclicBarrier(rowNumber*lineNumber+1, new Runnable() {
			@Override
			public void run() {
				currentField = new Field(futurField);
			}
		});

		futurField = new Field(rowNumber, lineNumber);		

		for(int i = 0; i<currentField.getDepth(); i++){
			for(int j = 0; j<currentField.getWidth(); j++){
				new Thread(new Worker(i, j)).start();
			}
		}
	}


	// Calculate the next step of the simulation
	public int nextStep(){
		try {
			barrier.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	protected void endSimulation() {
		barrier.reset();
	}

	class Worker implements Runnable {
		private int i,j;
		public Worker(int i, int j) {
			super();
			this.i = i;
			this.j = j;
		}

		public void run() {
			while (true) {
				doWork();

				try {
					barrier.await();
				} catch (InterruptedException ex) {
					break;
				} catch (BrokenBarrierException ex) {
					break;
				}
			}

			System.out.println("Job Done");
		}

		void doWork() {
			int nbadj = currentField.nbAdjacentTrue(i, j);

			/**
			 * Une cellule vide à l'étape n-1 et ayant exactement 3 
			 * voisins sera occupée à l'étape suivante. 
			 * (naissance liée à un environnement optimal)
			 */
			if(nbadj == 3){
				futurField.place(true, i, j);
			}

			/**
			 * Une cellule occupée à l'étape n-1 et ayant 2 ou 3 voisins sera maintenue 
			 * à l'étape n sinon elle est vidée. 
			 * (destruction par désertification ou surpopulation)
			 */
			else if(nbadj == 2 && futurField.getState(i, j)){
				futurField.place(true, i, j);
			}
			else{ 
				futurField.place(false, i, j);
			}
		}
	}
}
