package exercises.vt_07_synchronized_pinning.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/*
 * Synchronized Besonderheit: In einem synchronized Block wird bei einem blockierenden
 * OS-Aufruf der virtuelle Thread nicht vom OS-Thread entkoppelt. Das wird als "Pinning"
 * bezeichnet.
 * 
 * A) Zeige, dass ein virtueller Thread seinen Carrier-Thread nicht freigibt, wenn 
 * der virtuelle Thread auf das Betreten eines synchronized Blockes wartet oder wenn 
 * er innerhalb des synchronized Blockes einen blockierenden Betriebssystem-Aufruf taetigt.
 * 
 * B) Repariere das Verhalten durch Nutzung von ReentrantLock.
 * 
 * 
 */
public class Synchronized_BASE {

 
	public static void main(String[] args) {
		
		Synchronized_BASE instance = new Synchronized_BASE();
		
		
		instance.synchronizedSzenarioBase();
		
		
	}
 
	/*
	 * 
	 * 
	 * Der folgende Code zeigt anhand der Zeitmessung im Output, dass sleep() den
	 * Carrier Thread freigibt, wenn kein synchronized verwendet wird:
	 * 
	 * Alle virtual Threads starten fast gleichzeitig, obwohl mehr virtual Threads
	 * gestartet werden, als Carrier Threads im Pool sind.
	 * 
	 * A) Zeige, dass sleep() den Carrier nicht mehr freigibt, wenn der sleep() Aufruf in
	 * einen synchronized Block eingeschlossen ist.
	 * 
	 * B) Nutze ein ReentrantLock anstelle von synchronized, und zeige, dass dann
	 * alle Threads wieder nahezu gleichzeitig loslaufen, weil sleep() den Carrier
	 * freigibt.
	 * 
	 * Idee zur Loesung fuer A):
	 * - Starte mehr Threads als der Grad der Parallelität im Threadpool
	 * - Lasse alle einen synchronized BLock ausfuehren, in dem ein blockierender Aufruf ist
	 * - Zeige, dass nicht alle Threads starten 
	 * 
	 */
	void synchronizedSzenarioBase(){
		
		System.out.println("synchronizedSzenarioBase()");
		
		// Anzahl der Prozessoren im System
		int coreCount = Runtime.getRuntime().availableProcessors();
		
		final long base = System.currentTimeMillis();
		
		final List<Thread> threads = new ArrayList<>();
		
		for(int i = 0; i< coreCount + 2; i++) {
			final int j = i;
			
			
			Thread thread = Thread.startVirtualThread(() -> {
				// Zeitmessung: Misst wann dieser Virtual-Thread startet
				long threadStart = System.currentTimeMillis()-base;
				
				// gibt den Carrier Thread  frei, wenn kein synchronized verwendet wird
				sleep(1000);
				
				
				// Ausgbe der Zeitmessung: muss hier unten stehen, veraendert sonst das Verhalten da blockierend.
				System.out.println("Virtual Thread Job Started " + j + " id: " + Thread.currentThread().threadId() + " at Millisecond:" + threadStart);
				
			});
			
			threads.add(thread);
		}
		
		
		// warten bis das Szenario beendet ist
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		
		
	}
	
	/////////////// HELPER ///////////////////////////
	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

