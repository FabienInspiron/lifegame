package jeux;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Field {
	// The depth and width of the field.
    private int depth, width;
    
    // Storage for the field
    private boolean[][] field;
    
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new boolean[depth][width];

        clear();
    }
    
    /**
     * Empty the field.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = false;
            }
        }
    }
    
    public void place(boolean b, int i, int j){
    	field[i][j] = b;
    }
    
	public int getDepth() {
		return depth;
	}

	public int getWidth() {
		return width;
	}
	
	public boolean getState(int i, int j){
		return field[i][j];
	}
	
	/**
	 * Retourne le nombre d'element se trouvant autour
	 * @param i
	 * @param j
	 * @return nombre d'element
	 */
	public int nbAdjacentTrue(int i, int j) {
		int row = i;
		int col = j;
		int nb = 0;
		
		for (int roffset = -1; roffset <= 1; roffset++) {
			int nextRow = row + roffset;
			if (nextRow >= 0 && nextRow < depth) {
				for (int coffset = -1; coffset <= 1; coffset++) {
					int nextCol = col + coffset;
					// Exclude invalid locations and the original location.
					if (nextCol >= 0 && nextCol < width
							&& (roffset != 0 || coffset != 0)) {
						if(field[i][j]) nb++;
					}
				}
			}
		}
		return nb;
	}
}
