package jeux;

/*
 * Field représente la grille du jeux.
 * Elle est représentée par une matrice de taille n+2 
 * car on ajoute un ligne de 0 autour de la matrice pour
 * Que le calcul des cases sur les bords soit plus simple.
 * Exemple de matrice 4x4 :
 * 0 0 0 0 0 0
 * 0 		 0
 * 0 Matrice 0
 * 0  Réelle 0
 * 0         0
 * 0 0 0 0 0 0
 */
public class Field {
	// The depth and width of the field.
    private int depth, width;
    
    // Storage for the field
    private short[][] field;
    
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new short[depth+2][width+2];

        clear();
    }
    
    public Field(Field f) {
    	this.width = f.width;
    	this.depth = f.depth;
    	this.field = f.field.clone();
    }
    
    /**
     * Empty the field.
     */
    public void clear() {
        for (int row = 0; row < depth+2; row++) {
            for (int col = 0; col < width+2; col++) {
                field[row][col] = 0;
            }
        }
    }
    
    public void place(boolean b, int i, int j){
    	field[i+1][j+1] = (short) (b?1:0);
    }
    
	public int getDepth() {
		return depth;
	}

	public int getWidth() {
		return width;
	}
	
	public boolean getState(int i, int j){
		return (field[i+1][j+1] > 0);
	}
	
	/**
	 * Retourne le nombre d'element se trouvant autour
	 * @param i
	 * @param j
	 * @return nombre d'element
	 */
	public int nbAdjacentTrue(int i, int j) {
		int I = i+1, J = j+1;
		return 	field[I-1][J-1]	+field[I-1][J]	+field[I-1][J+1]
			+	field[I][J-1]	+	0		 	+field[I][J+1]
			+ 	field[I+1][J-1]	+field[I+1][J]	+field[I+1][J+1];
	}
}
