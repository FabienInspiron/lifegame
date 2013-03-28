package jeux;

public class SimulationThreadFull extends Thread{
	// The current state of the field.
	private Field field;

	private int i;
	private int j;

	/**
	 * Constructeur normal
	 * @param field
	 */
	public SimulationThreadFull(Field field, int i, int j) {
		this.field = field;
		this.i = i;
		this.j = j;
	}

	/**
	 * Methode run qui calcul un nextStep
	 */
	public void run(){
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
}
