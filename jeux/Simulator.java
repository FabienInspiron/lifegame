package jeux;
public class Simulator {
    // The current state of the field.
    private Field field;
    
    // The current step of the simulation.
    private int step;
    
    // A graphical view of the simulation.
    private SimulatorView view;
    
    public Simulator(int depth, int width) {
    	field = new Field(depth, width);
    	view = new SimulatorView(depth, width);
    	populate();
    }
    
    public void populate(){
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		field.place(Randomizer.GenerateRandomBoolean(), i, j);
        	}
    	}
    }
    
    // Calculate the next step of the simulation
    public void nextStep(){
    	for(int i = 0; i<field.getDepth(); i++){
        	for(int j = 0; j<field.getWidth(); j++){
        		if(field.getState(i, j)){
        			
        		}
        	}
    	} 	
    }
    
    public static void main(String[] args) {
		Simulator s = new Simulator(50, 50);
	}
}
