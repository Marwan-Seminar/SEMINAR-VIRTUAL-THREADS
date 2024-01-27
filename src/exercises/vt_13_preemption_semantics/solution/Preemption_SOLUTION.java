package exercises.vt_13_preemption_semantics.solution;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * Zeigt, dass virtuelle Threads die Semantik einer Applikation veraendern 
 * koennen, weil das preemptive Scheduling zwischen virtuellen Threads nicht
 * unbedingt funktioniert (Thomasz Nurkiewiz). Wenn keine blockierenden Aufrufe stattfinden
 * laufen virtuelle Threads einfach weiter und lassen andere virtuelle Threads nicht zum 
 * Zug kommen.
 * 
 * A) preemtionDoesNotWork()
 * In diesem Beispiel werden N virtuelle Threads gestartet, die die N Plaetze eines
 * Arrays veraendern solten. 
 * 
 * N ist dabei groesser als die Anzahl P der CPUs des Rechners.
 * 
 *
 * Es werden jedoch nur die P ersten Plaetze im Array bearbeitet, weil die weiteren virtuellen
 * Threads nicht geschedult werden.
 * 
 * B) preemtionDoesWork()
 * Werden Platform Threads statt virtueler Threads verwendet, werden alle Plaetze des Arrays 
 * bearbeitet, da alle Threads geschedult werden (Preemption).
 * 
 */
public class Preemption_SOLUTION {

	
	public static void main(String[] args) {
		Preemption_SOLUTION instance = new Preemption_SOLUTION();
		
		// A) preemtionDoesNotWork(): virtuelle Threads, keine Preemption
		instance.preemtionDoesNotWork();
		
		// B) preemtionDoesWork(): Platform Threads mit Preemption
		//instance.preemtionDoesWork();
	}
	
	
	void preemtionDoesNotWork() {
		
		System.out.println("preemtionDoesNotWork()");
		
		// Numbercruncher virtual Threads
		final int P = Runtime.getRuntime().availableProcessors();
		System.out.println("Parallelism = " + P);
		final int N = P + 2;
		long[] numbers = new long[N];
		
		System.out.println("Anzahl Prozessoren: " + P + " Anzahl virtuelle Threads: " + N);
		// Virtual Threads: Nur die ersten P Array Plaetze werden bearbeitet, da nur P virtuelle Threads starten
		ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor();
					
		
		for(int i = 0 ; i < N; ++i) {
			final int j = i;
			
			pool.execute(() ->{
				while(true) {
					numbers[j]+=1;
				}
			});
		}
		
		sleep(1000);
		System.out.println(Arrays.toString(numbers));
		
		
		// Sonst laufen die Platform Threads im Pool weiter
		System.exit(0);
	}
	
	void preemtionDoesWork() {
		
		System.out.println("preemtionDoesWork()");
		
		
		final int P = Runtime.getRuntime().availableProcessors();
		final int N = P + 2;
		long[] numbers = new long[N];
		
		System.out.println("Anzahl Prozessoren: " + P + " Anzahl Platform Threads: " + N);

		
		// N Platform Threads: Alle Array Slots werden mit Zahlen gefuellt
		ExecutorService pool = Executors.newCachedThreadPool();
		
		for(int i = 0 ; i < N; ++i) {
			final int j = i;
			
			pool.execute(() ->{
				//System.out.println("Thread for slot " + j + " " + Thread.currentThread());
				while(true) {
					numbers[j]+=1;
				}
			});
		}
		
		sleep(1000);
		System.out.println(Arrays.toString(numbers));
		
		
		// Sonst laufen die Platform Threads im Pool weiter
		System.exit(0);
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
