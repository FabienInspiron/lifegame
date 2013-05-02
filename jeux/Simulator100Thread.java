package jeux;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Simulator100Thread extends Simulator {
	
	// Barrière annoncant le début du travail des trheads 
	final CyclicBarrier barrierStart;
	
	// Barrière attendant la fin du travail des threads
	final CyclicBarrier barrierEnd;

	int nbThreadCree = 0;
	
	// The futur state of the field.
	Field futurField;

	public Simulator100Thread(int rowNumber, int lineNumber, int stepNumber) {
		super(rowNumber, lineNumber, stepNumber);
		
		barrierStart = new CyclicBarrier(rowNumber * lineNumber + 1, null);
		barrierEnd = new CyclicBarrier(rowNumber * lineNumber + 1, new Runnable() {
			@Override
			public void run() {
				currentField = new Field(futurField);
			}
		});
		
		futurField = new Field(rowNumber, lineNumber);

		for (int i = 0; i < currentField.getDepth(); i++) {
			for (int j = 0; j < currentField.getWidth(); j++) {
				new Thread(new Worker(i, j)).start();
				nbThreadCree++;
				System.out.println(nbThreadCree);
			}
		}
	}

	// Calculate the next step of the simulation
	public void nextStep() {
		try {
			// Donne l'ordre aux worker de commencer le travail
			barrierStart.await();
			// Attend que le travail des workers soit fini
			barrierEnd.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void endSimulation() {
		barrierEnd.reset();
	}

	/**
	 * Thread en charge de calculer l'état d'une case
	 */
	class Worker implements Runnable {
		private int i, j;
		private boolean isAlive;

		public Worker(int i, int j) {
			super();
			this.i = i;
			this.j = j;
			isAlive = false;
		}

		public void run() {
			while (true) {
				try {
					// Attend l'odre de commencer
					barrierStart.await();
					doWork();		
					// attent la fin de tous le threads
					barrierEnd.await();
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
			 * Une cellule vide à l'étape n-1 et ayant exactement 3 voisins sera
			 * occupée à l'étape suivante. (naissance liée à un environnement
			 * optimal)
			 */
			if (nbadj == 3 || (nbadj == 2 && currentField.getState(i, j))) {
				isAlive = true;
			} else {
				isAlive = false;
			}

			futurField.place(isAlive, i, j);
			if (isAlive) {
				incAlive();
			}
		}
	}
}
