package exercises.vt_10_forkjoin.solution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Dieses Beispiel zeigt den Unterschied zwischen ForkJoinTasks und virtual Threads
 * in Bezug auf deren Umgang mit blockierenden Aufrufen. 
 * 
 * Am Beispiel einer blockierenden Queue wird gezeigt, dass ein Producer-Consumer Szenario
 * mit ForkJoinTasks schnell zu einem Deadlock fuehrt, weil ein ForkJoinTask im Falle eines
 * blokiernden Aufrufes auch seinen unterliegenden Platform Thread blockiert. Im Gegensatz dazu gibt der virutal 
 * Thread den unterliegenden Platform Thread frei. 
 * 
 * Das Queue Producer-Consumer Szenario kann daher mit virtual Threads stabil ohne Deadlock realisiert werden. 
 *  
 */
public class ForkJoinSolution {

	public static void main(String[] args) {
		ForkJoinSolution instance = new ForkJoinSolution();
		
		// A) ForkJoinTasks erzeugen Deadlock
		instance.blockingQueueForkJoinDeadlock();
		
		// B) virtual Threads: Kein Deadlock
		//instance.blockingQueueVirtualThreads();
	}
	
	/*
	 * 
	 * Die Verwendung von ForkJoinTasks kann zu einem Deadlock fuehren, wenn blockierende Aufrufe verwendet werden. 
	 * 
	 * Das Verhalten wird dann unten durch Virtural Threads gefixt.
	 * 
	 * BlockingQueue.take() ist z.B. eine blockierende Operation die
	 * den virtuellen Thread parkt. 
	 * 
	 * Die take() Aufrufe blockieren den Pool erst ab 264, bis dahin dieser wächst immer weiter an.
	 */
	void blockingQueueForkJoinDeadlock() {
		
		// Blocking Queue
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);
		
		// Fork-Join-Pool
		ForkJoinPool fjPool = ForkJoinPool.commonPool();
		
		// ab 269 bei mir Deadlock: DAS IST ABER PLATFORM ABHAENGIG!!!
		for(int i = 0; i< 300; ++i) {
			
			final int count = i;
			
			// take() Jobs starten
			fjPool.submit(() ->{
				
				
				try {
					System.out.println("calling take: " + count +  " " + Thread.currentThread());
					
					// Blockierender Aufruf
					int value = queue.take();
					
					System.out.println("take returned: " + value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		
		
		// kurz warten bevor die Writer gestartet werden
		sleep(100);
		
		System.out.println("////////////// put() JOBS STARTEN ////////////////// ");
		// Wenn nach diesem Console-Output keine weiteren Ausschrifen erfolgen, ist ein Deadlock eingetreten
		for(int i = 0; i< 3; ++i) {
			final int value = i;
			// put() Jobs starten
			fjPool.submit(() ->{
				
				try {
					System.out.println("calling put");
					
					// Blockierender Aufruf
					queue.put(value);
					
					System.out.println("put returned ");
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			});
		}
		
		sleep(300000);
	}

	
	void blockingQueueVirtualThreads() {
		
		// 10 Elemente maximal
		BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(10);
		
		
		ExecutorService virtualPool = Executors.newVirtualThreadPerTaskExecutor();
		
		// ab 264 bei mir Deadlock
		for(int i = 0; i< 1000; ++i) {
			
			final int count = i;
			
			// Lese Jobs starten
			virtualPool.submit(() ->{
				
				
				try {
					System.out.println("calling take: " + count +  " " + Thread.currentThread());
					
					// Blockierender Aufruf
					int value = queue.take();
					
					System.out.println("take returned: " + value);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		
		
		// kurz warten bevor die Writer gestartet werden
		sleep(100);
		
		System.out.println("////////////// put() JOBS STARTENN ////////////////// ");
		// Wenn nach diesem Console-Output keine weiteren Ausschrifen erfolgen, ist ein Deadlock eingetreten
		
		for(int i = 0; i< 3; ++i) {
			final int value = i;
			// Schreib Jobs starten
			virtualPool.submit(() ->{
				
				try {
					System.out.println("calling put");
					
					// Blockierender Aufruf
					queue.put(value);
					
					System.out.println("put returned ");
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
			});
		}
		
		sleep(300000);
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
