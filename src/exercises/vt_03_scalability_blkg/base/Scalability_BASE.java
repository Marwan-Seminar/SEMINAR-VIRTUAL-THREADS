package exercises.vt_03_scalability_blkg.base;

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
public class Scalability_BASE {
	
	// Diese beiden Parameter sind entscheidend um das Skalierbarkeits-Problem zu demonstrieren
	// sie sind fuer jede Plattform unterschiedlich einzustellen.
	final static int NR_OF_THREADS = 100_000;
	final static int SLEEP_SECONDS = 10;
	
	public static void main(String[] args) {
		
		System.out.println("Scalability_BASE ");
		
		Scalability_BASE instance = new Scalability_BASE();
		
		instance.platformThreadsScalabiltyCrash();
	}

	/*
	 * Dieses Beispiel zeigt, dass nicht beliebig viele Platform Threads gestartet werden
	 * koennen.
	 * 
	 * Ab einer bestimmten Anzahl wird eine Exception geworfen. Diese Anzahl kann je nach 
	 * Betriebssystem und Java Heap Space variieren. 
	 */
	private void platformThreadsScalabiltyCrash() {
		System.out.println("Scalability_BASE.platformThreadsScalabiltyCrash()");
		
		// Auf meinem Rechner stuerzt das Programm ab ca. 12.000 Threads ab.
		for(int i = 0; i < NR_OF_THREADS; ++i) {
			
			// TODO: Plattform-Thread starten: new Thread(()->{...}).start();
			
			// TODO 2: Im Thread einen Blocking-Call aufrufen, z.B. sleep(SLEEP_SECONDS * 1000)
			
			// TODO 3: Zeigen, dass es abstuerzt mit java.lang.OutOfMemoryError
			
			// TODO 4: Nun alternativ Virtual-Threads verwenden und zeigen,
			// dass das Programm stabil laeuft: Thread.startVirtualThread(()->{...});			
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
