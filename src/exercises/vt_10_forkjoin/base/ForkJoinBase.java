package exercises.vt_10_forkjoin.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Zeige, dass ForkJoinTasks anders mit blockierenden Aufrufen umgehen als 
 * Virtual Threads.
 * 
 * Baue ein Szenario, bei dem ForkJoinTaks in eine blockiernde Queue Elemente mit put() einfuegen
 * und mit take() Elemente entnehmen. Dies sind beides blockierende Aufrufe.
 * 
 * A) Zeige, dass mit ForkJoinTasks ein Deadlock entstehen kann (Hinweis: Viele ForkJoinTasks starten)
 * 
 * B) Zeige, dass sich dieses Problem mit virutal Threads loesen laesst.
 */
public class ForkJoinBase {

	public static void main(String[] args) {
		ForkJoinBase instance = new ForkJoinBase();
		
		instance.blockingQueueBase();
	}
	
	
	/*
	 * Dieser Rahmen stellt die Benutzung der Queue zur Verfuegeung.
	 * 
	 * Es fehlen die ForkJoinTasks, die die Aufrufe take() und put() ausfuehren.
	 * 
	 * Hinweis: Starte viele ForkJoinTasks, damit es zum Deadlock kommt (z.B. 300).
	 * Die Anzahl ist aber plattformabhaengig!
	 * 
	 */
	void blockingQueueBase() {
		
		System.out.println("blockingQueueBase()");
		
		// Queue mit maximal 10 Elementen 
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);
		
		
		// TODO einen ForkJoinPool benutzen, z.B. mit ForkJoinPool.commonPool();
					
		
		// TODO: Viele Entnehmen-Aufgaben (take()) in den ForkJoinPool einstellen
		// z.B. mit ForkJoinPool.submit(() ->{ ...});
		
		try {
			System.out.println("calling take: " + Thread.currentThread());
			
			// Blockierender Entnehmen-Aufruf
			int value = queue.take();
			
			System.out.println("take returned: " + value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
		
		
		// kurz warten bevor die einfuegenden Threads gestartet werden
		sleep(100);
		
		System.out.println("////////////// EINFUEGE JOBS STARTEN ////////////////// ");
		// Wenn nach diesem Console-Output keine weiteren Ausschrifen erfolgen, ist ein Deadlock eingetreten
		
		
		for(int i = 0; i< 3; ++i) {
			final int value = i;
			
			
			// TODO Einfuege-Aufgaben (put()) in den ForkJoinPool einstellen	
			try {
				System.out.println("calling put");
				
				// Blockierender Einfuegen-Aufruf
				queue.put(value);
				
				System.out.println("put returned ");
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			
		}
		
		sleep(300000);
	}


/////////////// HELPER ///////////////////////

	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
}



}
