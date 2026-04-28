package exercises.vt_17_architecture_speedup.base;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Die Frage in diese Aufgabe ist, welche Arten von Systemen und Architekturen 
 * sich mit Virutal Threads beschleunigen lassen.
 * 
 * In der Aufgabe sind zwei Beispiel-Methoden vorgegeben, die sich lediglich in 
 * einem Punkt unterscheiden. Die eine ist CPU intensiv, die andre ist IO-bound.
 * 
 * Beide Methoden sind bereits parallelisiert, und zwar mit Platform-Threads.
 * 
 * Die Aufbage besteht nun darin, herauszufinden, welche dieser Methoden durch den 
 * Umstieg auf Virtual Threads beschleunigen laesst, und welche sich dadurch nicht
 * beschleunigen laesst.
 * 
 * A) Zeige, dass Du eine diser Methoden Virtual Threads massiv beschleunigen kannst
 * 
 * B) Erklaere, warum sich die andere nicht durch Virtual Threads beschleunigen laesst.
 */
public class ArchitectureSpeedup_BASE {
	
	static final int SMALL_LOOP_COUNT =  100;
	static final int BIG_LOOP_COUNT	= 100_000;
	static final int POOL_COUNT	= 10_000;
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ArchitectureSpeedup_BASE instance = new ArchitectureSpeedup_BASE();
		
		
		///////////// AUFRUFE DER AUSGANGSBASIBASIS ZUR AUFGABE ///////////////////////////
		/// Welcher der beiden Aufrufe kann mit Virtual Threaes beschleunigt werden? ///
		
		// Variante 1 Platform Threads mit CPU intensiver Aufgabe 
		instance.cpuIntensiveParallelLoopPlatformThreads();	
		
		
		// Variante 2 Platform Threads mit blockierenden Aufrufen
		//instance.ioBoundParallelLoopPlatformThreads();
		
		////////////////////////////////////////////////////////////////////

	}

	
	/*
	 * Variante 1
	 * 
	 * Diese Methode ist mit Platform Threads 
	 * parallelisiert und führt einen CPU-intensiven
	 * Algortihmus aus.
	 */
	void cpuIntensiveParallelLoopPlatformThreads() throws InterruptedException {
		
		System.out.println("cpuIntensiveParallelLoopPlatfromThreads()");
		long start = System.currentTimeMillis();
		
		List<Thread> threadBuffer = new ArrayList<Thread>(SMALL_LOOP_COUNT);
		
		// Start many threads, each of them does something CPU intensive
		for(int i = 0; i < SMALL_LOOP_COUNT; ++i) {
			
			Thread thread = new Thread(()-> {
				// Diese Schleife braucht auf meinem Rechner ca. 1 Sekunde
				for (long j = 0; j < (Long.MAX_VALUE / 7); ++j) {
					// CPU Aktivität
				}
				
				return;
				
			});
			
			// Starte den neuen Thread
			thread.start();
			
			threadBuffer.add(thread);
			
		}
		System.out.println("Started " + SMALL_LOOP_COUNT + " CPU intensive Threads after " + (System.currentTimeMillis() - start) + " Milliseconds");
		
		// Warte auf die Beendigung aller Trheads
		for(int i = 0; i < SMALL_LOOP_COUNT; ++ i) {
			threadBuffer.get(i).join();
			System.out.println("Thread " + i +" returning afer " + (System.currentTimeMillis() - start));	
		}

		System.out.println(SMALL_LOOP_COUNT + " All Threads returned afer: " + (System.currentTimeMillis() - start) + " Milliseconds");

	}
	
	
	/*
	 * Variante 2
	 * 
	 * Diese Methode startet viele Tasks die in einem blockierenden 
	 * Aufruf warten. 
	 * 
	 * Die Tasks werden von einem Pool von Platform Threads ausgefuehrt, der eine
	 * beschraenkte Größe hat.
	 * 
	 * Man sieht, dass die Tasks in Chunks der Groesse des Pools abgearbeitet werden. 
	 * 
	 * Die Laufzeit erklaert sich zwingend aus: 
	 * (Anzahl Tasks) * Laufzeit einzenle Task / Poolsize 
	 * 
	 * Die Frage ist: Kann man dieses Szenario mit Virtual Threads beschleunigen?
	 * 
	 *  
	 *  
	 *  Bemerkung: 
	 *  Man kann beobachten, dass ein wesentlicher Teil der Laufzeit für den Start und Aufwecken
	 *  der Threads benötigt wird. (Dies könnte man durch das vorwaermen des Threadpools ein wenig optimieren,
	 *  das ist aber hier nicht das Ziel. Ziel ist eine massive VErbesserung zu erreichen, indem 
	 *  die Context-Swithches der Threads eingespart werden und die Poolsize kein limitierender Faktor mehr ist.)
	 *  
	 */
	void ioBoundParallelLoopPlatformThreads() throws InterruptedException, ExecutionException {
		
		System.out.println("ioBoundParallelLoopPlatfromThreads()");
		long start = System.currentTimeMillis();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(POOL_COUNT);
		
		List<Future<?>> futuresBuffer = new ArrayList<Future<?>>(BIG_LOOP_COUNT);
		
		// Start many threads, each of them does something blocking (like waiting for I/O)
		for(int i = 0; i < BIG_LOOP_COUNT; ++i) {
			
			Future<?> future = threadPool.submit(() -> {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {}
				
				return;
				 
			});
			
			futuresBuffer.add(future);
			
		}
		System.out.println("Submitted  " + BIG_LOOP_COUNT + " Tasks into Thread-Pool after " + (System.currentTimeMillis() - start) + " Milliseconds" );
		
		// Wait for all threads to complete 
		// Dies ist keine korrekte Zeitmessung, weil die Reihnefolge, in der die Tasks zurückkheren, eine andere sein kann
		// als die Reihenfolge in der Liste
		for(int i = 0; i < BIG_LOOP_COUNT; ++ i) {
			futuresBuffer.get(i).get();
			System.out.println("Task " + i +" returning afer " + (System.currentTimeMillis() - start));
		}
		
		threadPool.shutdown();
	
		System.out.println(BIG_LOOP_COUNT + " All Tasks returned afer: " + (System.currentTimeMillis() - start) + " Milliseconds");
	}
}
