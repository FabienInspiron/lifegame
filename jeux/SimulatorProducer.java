package jeux;

import java.util.concurrent.Semaphore;


public class SimulatorProducer extends Simulator {

    private Producer[][] producers;

    public SimulatorProducer(int rowNumber, int lineNumber, int stepNumber) {
        super(rowNumber, lineNumber, stepNumber);

        producers = new Producer[lineNumber][rowNumber];

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
                tmp.place(p.readValue(), i, j);
            }
        }
        currentField = tmp;
        synchronized (this) {
			notifyAll();
		}
    }

    @Override
    protected void endSimulation() {
        for (int i = 0; i < currentField.getDepth(); i++) {
            for (int j = 0; j < currentField.getWidth(); j++) {
                producers[i][j].stop();
            }
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        for (int i = 0; i < currentField.getDepth(); i++) {
            for (int j = 0; j < currentField.getWidth(); j++) {
                producers[i][j].start();
            }
        }
    }
    
    class Producer extends Thread {
        private Semaphore ready;
        private boolean value;
        private int i, j;

        public Producer(int i, int j) {
            ready = new Semaphore(0);
            value = false;
            this.i = i;
            this.j = j;
        }

        public void run() {
            int nbadj = 0;
            while (true) {
                nbadj = currentField.nbAdjacentTrue(i, j);
                value = (nbadj == 3 || (nbadj == 2 && currentField.getState(i, j)));
                synchronized (SimulatorProducer.this) {
                    ready.release();
                    try {
                    	SimulatorProducer.this.wait();
                    } catch (InterruptedException e) {
                        System.out.println("oups : " + e);
                        return;
                    }
                }
            }
        }

        public boolean readValue() {
        	try {
                ready.acquire();
            } catch (InterruptedException ex) {
            }
            return value;
        }
    }
}
