package exercises.vt_08_queue_blocking.base;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/* 
 * Uebung Queue: Blocking-Verhalten
 * 
 * Zeige, dass ein virtueller Thread, der einen blockierenden Aufruf einer Queue
 * benutzt, im Falle des Blockierens seinen unterliegenden Carrier Thread freigibt.
 * 
 * Starte dafuer viele virtuelle Threads, die queue.take() aufrufen, so dass sie blockieren.
 * 
 * Starte dann einige virtuelle Threads, die mit queue.put() einen Wert in die Queue einfuegen.
 * 
 * Zeige, dass es zu keinem Deadlock kommt, Obwohl alle diese virtuellen Threads einen gemeinsamen
 * Threadpool beschraenkter Groesse benutzen.
 * 
 * Rahmen: Die Queue und der Code fuer put() und take() sind bereits vorhanden. 
 * 
 * TODO: Es fehlt das Starten der virtuellen Threads.
 * 
 */
public class BlockingQueue_BASE {

	public static void main(String[] args) {
		
		System.out.println("BlockingQueue_BASE");
		
		BlockingQueue_BASE instance = new BlockingQueue_BASE();
		instance.blockingQueueVirtualThreads();
	}
	
	
	void blockingQueueVirtualThreads() {
		
		System.out.println("blockingQueueVirtualThreads");
		
		// 10 Elemente maximal
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);
		
		
		// Viele Auftraege fuer das Entnehmen aus der Queue (take())
		for(int i = 0; i< 1000; ++i) {
			
			// TODO queue.take() in einem virtual Thread ausfuehren.
		
			try {
				System.out.println("calling take " + Thread.currentThread());
				
				// Blockierender Aufruf der den Carrier-Thread freigibt, entnimmt Wert aus der Queue
				int value = queue.take();
				
				System.out.println("take returned: " + value + " " + Thread.currentThread());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
		
		// kurz warten bevor die Writer gestartet werden
		sleep(100);
		System.out.println("Lese Jobs gestartet");
		
		// Einige wenige Auftraege fuer das Einfuegen in die Queue (put())
		for(int i = 0; i< 3; ++i) {
			final int value = i;
			
			// TODO queue.put() in virtuellem Thread ausfuehren.
				
			try {
				System.out.println("calling put" + value + " " + Thread.currentThread());
				
				// In die Queue einfuegen
				queue.put(value);
		
				System.out.println("put returned ");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}

		
		// Warten bis das Szenario beendet ist
		sleep(100000);
	}
	
///////////////// HELPER ///////////////////////
	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
