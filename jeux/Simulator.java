package jeux;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Simulator {
	final CyclicBarrier barrier;

	// The current and futur state of the field.
	Field current_field, futur_field;

	// The current step of the simulation.
	private int step;

	// A graphical view of the simulation.
	private SimulatorView view;
	
	static boolean JobDone = false;

	public Simulator(int depth, int width) {
		barrier = new CyclicBarrier(depth*width+1, new Runnable() {

			@Override
			public void run() {
				
				current_field = new Field(futur_field);
			}
		});

		current_field = new Field(depth, width);
		futur_field = new Field(depth, width);
		view = new SimulatorView(depth, width);

		populate();
		

		for(int i = 0; i<futur_field.getDepth(); i++){
			for(int j = 0; j<futur_field.getWidth(); j++){
				new Thread(new Worker(i, j)).start();
			}
		}
	}

	/*
	 * Rempli de manière aléatoire la grille
	 */
	public void populate(){
		for(int i = 0; i<current_field.getDepth(); i++){
			for(int j = 0; j<current_field.getWidth(); j++){
				current_field.place(Randomizer.GenerateRandomBoolean(), i, j);
			}
		}
	}

	// Calculate the next step of the simulation
	public void nextStep() throws InterruptedException, BrokenBarrierException{
		barrier.await();
	}

	/**
	 * Reset the simulation to a starting position.
	 */
	public void reset() {
		step = 0;
		populate();

		// Show the starting state in the view.
		view.showStatus(step, futur_field);
	}

	public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
		Simulator sim = new Simulator(50,50);
		int day = 0;
		sim.view.showStatus(day, sim.current_field);
		while (day<50) {
			sim.nextStep();

			// Show the starting state in the view.
			sim.view.showStatus(day, sim.current_field);

			// Pause in order to let the time to view the situation
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				System.err.println("Can't pause the thread.");
			}

			day++;
		}
		
		sim.endSimulation();

		sim.view.showStatus(day, sim.futur_field);
	}

	private void endSimulation() {
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
			int nbadj = current_field.nbAdjacentTrue(i, j);

			/**
			 * Une cellule vide à l'étape n-1 et ayant exactement 3 
			 * voisins sera occupée à l'étape suivante. 
			 * (naissance liée à un environnement optimal)
			 */
			if(nbadj == 3){
				futur_field.place(true, i, j);
			}

			/**
			 * Une cellule occupée à l'étape n-1 et ayant 2 ou 3 voisins sera maintenue 
			 * à l'étape n sinon elle est vidée. 
			 * (destruction par désertification ou surpopulation)
			 */
			else if(nbadj == 2 && futur_field.getState(i, j)){
				futur_field.place(true, i, j);
			}
			else{ 
				futur_field.place(false, i, j);
			}
		}
	}
}
