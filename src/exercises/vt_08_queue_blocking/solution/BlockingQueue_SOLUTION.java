package exercises.vt_08_queue_blocking.solution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/* 
 * Uebung Queue: Blocking-Verhalten MUSTERLOESUNG
 * 
 * Zeige, dass ein virtueller Thread der einen blockierenden Aufruf einer Queue
 * benutzt, im Falle des Blockierens seinen unterliegenden Carrier Thread freigibt.
 * 
 * Starte dafuer viele virtuelle Threads, die queue.take() aufrufen, so dass sie blockieren.
 * 
 * Starte dann einige virtuelle Threads, die mit queue.put() einen Wert in die Queue einfuegen.
 * 
 * Zeige, dass es zu keinem Deadlock kommt, Obwohl alle diese virtuellen Threads einen gemeinsamen
 * Threadpool beschraenkter Groesse benutzen.
 * 
 * Die Queue und der Code fuer put() und take() sind bereits vorhanden. 
 * Es fehlt das Starten der virtuellen Threads.
 */
public class BlockingQueue_SOLUTION {

	public static void main(String[] args) {
		
		System.out.println("BlockingQueue_SOLUTION");
		
		BlockingQueue_SOLUTION instance = new BlockingQueue_SOLUTION();
		instance.blockingQueueVirtualThreads();
	}
	
	/* 
	 * 
	 * 1000 virtuelle Threads lesen aus einer blockierende Queue mittels take().
	 * 
	 * Nur wenige virtuelle Threads schreiben in diese Queue mittels put().
	 * 
	 * Obwohl alle diese virteullen Threads einen gemeinsamen Threadpool beschraenkter Groesse 
	 * benutzen, laufen alle Schreib- und Leseoperationen. Am Output sieht man, dass alle virtuellen
	 * Threads auf wenige (z.B. 8) Platform-Threads geschedult werden.
	 * 
	 * Daran sieht man, dass Queue.take() den jeweiligen Platform-Thread 
	 * freigibt. 
	 * 
	 */
	void blockingQueueVirtualThreads() {
		
		System.out.println("blockingQueueVirtualThreads");
		
		// 10 Elemente maximal
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);
		
		// Virtual Threads ExecutorService: startet fuer jeden Auftrag einen neuen virtual Thread
		ExecutorService virtExecutor = Executors.newVirtualThreadPerTaskExecutor();
		
		// Viele Auftraege
		for(int i = 0; i< 1000; ++i) {
			
			// Lese Jobs starten
			virtExecutor.execute(() ->{
				
				try {
					System.out.println("calling take " + Thread.currentThread());
					
					// Blockierender Aufruf der den Carrier-Thread freigibt
					int value = queue.take();
					
					System.out.println("take returned: " + value + " " + Thread.currentThread());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		
		
		// kurz warten bevor die Writer gestartet werden
		sleep(100);
		System.out.println("Lese Jobs gestartet");

		
		for(int i = 0; i< 3; ++i) {
			final int value = i;
			// Schreib Jobs starten
			virtExecutor.execute(() ->{
				
				try {
					System.out.println("calling put" + value + " " + Thread.currentThread());
					queue.put(value);
					System.out.println("put returned ");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
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
