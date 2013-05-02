package jeux;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SimulatorQuart extends Simulator {

	public static final int NB_THREADS = 4;

	// Barrière annoncant le début du travail des trheads
	//final CyclicBarrier barrierStart;

	// Barrière attendant la fin du travail des threads
	final CyclicBarrier barrierEnd;

	// The futur state of the field.
	Field futurField;

	public SimulatorQuart(int rowNumber, int lineNumber, int stepNumber) {
		super(rowNumber, lineNumber, stepNumber);

		//barrierStart = new CyclicBarrier(NB_THREADS + 1,
				//null);
		barrierEnd = new CyclicBarrier(NB_THREADS + 1,
				new Runnable() {
					@Override
					public void run() {
						currentField = new Field(futurField);
					}
				});

		futurField = new Field(rowNumber, lineNumber);
		
		new Thread(new Worker(0,0,currentField.getWidth()/2, currentField.getDepth()/2)).start();
		new Thread(new Worker(0, currentField.getWidth()/2, currentField.getWidth(), currentField.getDepth()/2)).start();
		new Thread(new Worker(currentField.getDepth()/2, 0, currentField.getWidth()/2, currentField.getDepth())).start();
		new Thread(new Worker(currentField.getDepth()/2,currentField.getWidth()/2,currentField.getWidth(), currentField.getDepth())).start();
	}

	// Calculate the next step of the simulation
	public void nextStep() {
		try {
			// Donne l'ordre aux worker de commencer le travail
			//barrierStart.await();
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

		private int hautGaucheI;
		private int hautGaucheJ;
		private int hautDroiteJ;
		private int basGaucheI;

		public Worker(int hautGaucheI, int hautGaucheJ, 
				int hautDroiteJ, int basGaucheI) {
			super();
			this.hautGaucheI = hautGaucheI;
			this.hautGaucheJ = hautGaucheJ;
			this.hautDroiteJ = hautDroiteJ;
			this.basGaucheI = basGaucheI;
			isAlive = false;
		}

		private boolean isAlive;

		public void run() {
			while (true) {
				try {
					// Attend l'odre de commencer
					//barrierStart.await();
					
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
			for (int i = hautGaucheI; i < basGaucheI; i++)
				for (int j = hautGaucheJ; j < hautDroiteJ; j++) {
					int nbadj = currentField.nbAdjacentTrue(i, j);
					
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
		}
	}
}
