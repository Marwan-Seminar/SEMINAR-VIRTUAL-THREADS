package exercises.vt_13_preemption_semantics.base;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/*
 * Zeige, dass virtuelle Threads die Semantik einer Applikation veraendern koennen,
 * da das Scheduling keine Preemption zwischen virtuellen Threads erzwingt.
 * 
 * Sei P die Anzahl der Threads im Platform-Threadpool.
 * 
 * Wenn z.B. mehr als P virtuelle "numbercruncher" Threads gestartet werden, die also 
 * keine blockiernden Aufrufe taetigen, sondern nur auf der CPU arbeiten, dann 
 * werden einige dieser Virtual-Threads nie laufen.
 * 
 * Konstruiere ein Beispiel, bei dem das sichtbar wird. Z.B. ein Array mit N = P+2 Plaetzen
 * und N Threads, die jeweils einen dieser Plaetze bearbeiten. 
 * 
 * Zeige, dass sich virtuelle Threads anders verhalten als Platform Threads
 * 
 * RAHMEN: Der hier vorliegende Rahmen implemntiert ein Beispiel mit Platform-Threads,
 * die Aufgabe ist, zu zeigen, dass sich Virtual-Threads anders verhalten.
 */
public class Preemption_BASE {

	
	public static void main(String[] args) {
		Preemption_BASE instance = new Preemption_BASE();
		
		
		// preemtionDoesWork(): Platform Threads mit Preemption
		instance.preemtionDoesWork();
	}
	
	
	/*
	 * Hier sieht man, wie sich Platform Threads verhalten. 
	 * Alle Threads erhalten Laufzeit.
	 * 
	 * Das sieht man daran, dass das am Schuss ausgegebene Array keine O-Werte mehr enthaelt.
	 * 
	 * Zeige, dass virtuelle Threads sich evtl. anders verhalten. 
	 * 
	 */
	void preemtionDoesWork() {
		
		System.out.println("preemtionDoesWork()");
		
		
		final int P = Runtime.getRuntime().availableProcessors();
		final int N = P + 2;
		long[] numbers = new long[N];
		
		System.out.println("Anzahl Prozessoren: " + P + " Anzahl Platform Threads: " + N);

		
		// N Platform Threads: Alle Array Slots werden mit Zahlen gefuellt
		ExecutorService pool = Executors.newCachedThreadPool();
		// TODO starte stattdessen virtuelle Threads
		
		
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

