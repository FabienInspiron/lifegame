package jeux;

import java.util.concurrent.Semaphore;

public class ThreadManageSem extends Thread{
	int sem;

	public ThreadManageSem(int s) {
		sem = s;
	}

	@Override
	public void run() {
		while(true){
			if(sem <= 0){ notifyAll(); System.out.println("Notification"); }
		}
	}
}
