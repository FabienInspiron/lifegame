package jeux;

import java.util.concurrent.Semaphore;


public class SimulatorVoisin extends Simulator {

    private Producer[][] producers;
        
    public int calculated;

    public SimulatorVoisin(int rowNumber, int lineNumber, int stepNumber) {
        super(rowNumber, lineNumber, stepNumber);

        producers = new Producer[lineNumber][rowNumber];
        
        calculated = 0;

        for (int i = 0; i < currentField.getDepth(); i++) {
            for (int j = 0; j < currentField.getWidth(); j++) {
                producers[i][j] = new Producer(i, j);
                producers[i][j].start();
            }
        }
    }

    // Calculate the next step of the simulation
    @Override
    public void nextStep() {
        Field tmp = new Field(currentField);
        Producer p;

        for (int i = 0; i < tmp.getDepth(); i++) {
            for (int j = 0; j < tmp.getWidth(); j++) {
                p = producers[i][j];
                tmp.place(p.readValue(tmp), i, j);
            }
        }
        currentField = tmp;
    }

    @Override
    protected void endSimulation() {
        for (int i = 0; i < currentField.getDepth(); i++) {
            for (int j = 0; j < currentField.getWidth(); j++) {
                producers[i][j].stop();
            }
        }
    }
    
    /*@Override
    public void reset() {
        super.reset();
        for (int i = 0; i < currentField.getDepth(); i++) {
            for (int j = 0; j < currentField.getWidth(); j++) {
                producers[i][j].setCurrentField(currentField);
            }
        }
    }*/
    
    class Producer extends Thread {
        private Semaphore ready;
        private boolean valueN1, valueN2;
        private int i, j;

        public Producer(int i, int j) {
            ready = new Semaphore(0);
            valueN1 = valueN2 = false;
            this.i = i;
            this.j = j;
        }

        public void run() {
            int nbadj = 0;
            while (true) {
            	
                nbadj = currentField.nbAdjacentTrue(i, j);
                valueN1 = (nbadj == 3 || (nbadj == 2 && currentField.getState(i, j)));
                synchronized (this) {
                    ready.release();
                    try {
                        ready.acquire();
                    } catch (InterruptedException e) {
                        System.out.println("oups : " + e);
                        return;
                    }
                }
            }
        }

        public synchronized boolean readValue(Field nextField) {
            try {
                ready.acquire();
            } catch (InterruptedException ex) {
            }
            boolean tmp = valueN1;
            currentField = nextField;
            notify();
            return tmp;
        }
    }
}
