package jeux;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import jeux.Simulator100Thread.Worker;

public class SimulatorNThread extends Simulator {

	//public static final int NB_THREADS = 4;

	// Barrière annoncant le début du travail des trheads
	final CyclicBarrier barrierStart;

	// Barrière attendant la fin du travail des threads
	final CyclicBarrier barrierEnd;

	// The futur state of the field.
	Field futurField;

	public SimulatorNThread(int rowNumber, int lineNumber, int stepNumber, int NbRow, int NbCol) {
		super(rowNumber, lineNumber, stepNumber);

		barrierStart = new CyclicBarrier(NbRow*NbCol + 1, null);
		barrierEnd = new CyclicBarrier(NbRow*NbCol + 1,
				new Runnable() {
					@Override
					public void run() {
						currentField = new Field(futurField);
					}
				});

		futurField = new Field(rowNumber, lineNumber);

		int casesI = rowNumber/NbRow;
		int casesJ = lineNumber/NbCol;
		
		for (int i = 0; i < NbRow; i++) {
			for (int j = 0; j < NbCol; j++) {
				int A = i*casesI;
				int B = j*casesJ;
				new Thread(new Worker(A, B, A+casesI-1, B+casesJ-1)).start();
				System.out.println("TH("+A+","+B+","+(A+casesI-1)+","+(B+casesJ-1)+")");
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

		private int hautGaucheI;
		private int hautGaucheJ;
		private int basDroitJ;
		private int basDroitI;

//		+........
//		.........
//		.........
//		........+
		public Worker(int hautGaucheI, int hautGaucheJ, 
				int basDroitJ, int basDroitI) {
			super();
			this.hautGaucheI = hautGaucheI;
			this.hautGaucheJ = hautGaucheJ;
			this.basDroitJ = basDroitJ;
			this.basDroitI = basDroitI;
			isAlive = false;
		}

		private boolean isAlive;

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
			for (int i = hautGaucheI; i <= basDroitI; i++) {
				for (int j = hautGaucheJ; j <= basDroitJ; j++) {
					int nbadj = currentField.nbAdjacentTrue(i, j);
					
					/**
					 * Une cellule vide à l'étape n-1 et ayant exactement 3
					 * voisins sera occupée à l'étape suivante. (naissance liée
					 * à un environnement optimal)
					 */
					isAlive = (nbadj == 3 || (nbadj == 2 && currentField.getState(i, j)));

					futurField.place(isAlive, i, j);
					if (isAlive) {
						incAlive();
					}
				}
			}
		}
	}
}
