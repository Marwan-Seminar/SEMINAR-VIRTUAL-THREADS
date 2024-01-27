package exercises.vt_03_scalability_blkg.solution;

/*
 * Uebung V-T 3, Skalierbarkeit bei Blocking
 * 
 * Zeige, dass eine Anwendung mit virtual Threads besser skalieren kann als mit Platform-Threads
 * 
 * 1 Starte so viele Platform Threads, dass das Programm abstürzt
 *   - Nutze dafür z.B. Thread.sleep() um die Threads blockierend am Leben zu erhalten
 *   
 * 2 Zeige, dass wesentlich mehr virtuelle Threads gleichzeitig existieren können
 * 
 * 3 Erkläre den Unterschied
 */
public class Scalability_SOLUTION {
	
	public static void main(String[] args) {
		
		System.out.println("Scalability_SOLUTION ");
		
		Scalability_SOLUTION instance = new Scalability_SOLUTION();
		
		// 1: Viele gleichzeitige Platform Threads fuehren zum Crash
		//instance.platformThreadsScalabiltyCrash();
		
		// 2: Viele gleichzeitige Virtual-Threads laufen stabil
		instance.virtualThreadsScalabiltyStable();
	}

	/*
	 * Dieses Beispiel zeigt, dass nicht beliebig viele Platform Threads gestartet werden
	 * koennen.
	 * 
	 * Ab einer bestimmten Anzahl wird eine Exception geworfen. Diese Anzahl kann je nach 
	 * Betriebssystem und Java Heap Space variieren. 
	 */
	private void platformThreadsScalabiltyCrash() {
		System.out.println("Scalability_SOLUTION.platformThreadsScalabiltyCrash()");
		// Auf meinem Rechner stuerzt das Programm ab ca. 4100 Threads ab.
		for(int i = 0; i < 10_000; ++i) {
			
			final int cnt = i;
			
			// Platform Thread
			Thread thread = new Thread(()->{
				
				System.out.println("New OS Thread " + cnt);
				
				sleep(1000);
				
				
			});
			
			thread.start();
		}
		
		// warten, bis das Szenario beendet ist
		sleep(3000);	
	}
	
	/*
	 * Dieses Beispiel zeigt, dass sich z.B. 10.000 virtuelle Threads starten lassen, 
	 * die gleichzeitig einen blockierenden OS-Aufruf ausfuehren.
	 */
	private void virtualThreadsScalabiltyStable() {
		System.out.println("Scalability_SOLUTION.virtualThreadsScalabiltyStable()");

		// Schleife zum Starten vieler virtueller Threads
		for(int i = 0; i < 10_000; ++i) {
			
			final int cnt = i;
			
			// Starte virtuellen Thread
			Thread.startVirtualThread(()->{
				
				System.out.println("New virtual Thread " + cnt);
				
				sleep(1000);
				
				//System.out.println("Virtual Thread woke up " + cnt);
				
				
			});
			
		}
		
		
		// warten, bis das Szenario beendet ist
		sleep(3000);
		
		
		
	}
	
	/////////////// HELPER ///////////////////
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
