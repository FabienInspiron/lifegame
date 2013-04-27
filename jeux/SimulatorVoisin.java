package jeux;

/**
 * Classe de gestion des processus par les voisins
 * Principe : Une case attend que ses voisins soient calculées pour 
 * calculer sa propre valeur a son tour.
 * 
 * @author belli
 *
 */
public class SimulatorVoisin extends Simulator {

	public SimulatorVoisin(int rowNumber, int lineNumber, int stepNumber) {
		super(rowNumber, lineNumber, stepNumber);
	}

	@Override
	public void nextStep() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * L'algorithme de calcul est le suivant :
	 * Pour une case donnée il faut verifier que ses voisins sont pret.
	 * 	1-2-3
	 * 	4-5-6
	 * 	7-8-9
	 * 
	 * Il faut donner des prioritées de calcul à chacun, sinon tout le monde attendrai tout
	 * le monde => Interblocage.
	 * 
	 * Solution : Pour la case 1, on calcul les cases 4, 5 et 2, dans cet ordre.
	 * Pour la case 2 il faut calcule les cases 1, 4, 5, 6, 3. 
	 * Mais les cases 4 et 5 ont déja été calculées, donc pas de nouveau calcul.
	 * Et ainsi de suite...
	 * 
	 * Arrivé aux derniere cases, plus de calcul nécessaire car les données sont à jour.
	 * 
	 * Le but etant de savoir si un case est a jour ou pas.
	 */

}
