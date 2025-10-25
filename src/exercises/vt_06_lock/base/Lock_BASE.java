package exercises.vt_06_lock.base;

import java.util.concurrent.locks.ReentrantLock;

/*
 * Zeige, dass ein virtueler Thread, der die lock() Methode einer Lock Instanz aus util.concurrent
 * aufruft seinen Carrier-Thread freigibt.
 * 
 * Locks sind eine Alternative zu synchronized.
 * 
 * Der unten stehende Code zeigt, dass nicht alle virtuellen Threads gleichzeitig starten, 
 * wenn keine blockierenden Aufrufe erfolgen. 
 * 
 * Baue ReentrantLock.lock() und unlock() so ein, dass alle Virtual-Threads nahezu gleichzeitig 
 * starten. 
 * 
 * Die Zeitmessung ist bereits vorhanden und wird auf die Konsole ausgegeben.
 * 
 */
public class Lock_BASE {
	

	final static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	final static int TASK_COUNT = CPU_COUNT +2;

	public static void main(String[] args) {
		Lock_BASE instance = new Lock_BASE();
		instance.lockScenario();
	}
	
	/* 
	 * Es werden mehr virtual Threads gestartet als der Rechner Cores hat (CPU_COUNT).
	 * Der Zeitpunkt des jewiligen Starts wird gemessen,
	 * und am Ende des Threads ausgegeben. 
	 *  
	 * Ohne Lock laufen zunaechst nur CPU_COUNT viele virtual Threads, diese belegen die CPUs,
	 * alle weiteren virtual Threads bekommen erst dann einen Carrier, wenn die ersten 
	 * Virtual-Threads beendet sind.
	 *  
	 * Bemerkung: Es ist wichtig, dass die println() Aufrufe erst am Ende des jeweiligen Threads
	 * stehen, denn sie sind blockiernde Aufrufe die anderenfalls den Carrier freigeben und so 
	 * das Verhalten des Programms verfaelschen.
	 * 
	 */
	void lockScenario() {
		
		System.out.println("CPU_COUNT " + CPU_COUNT + " TASK_COUNT " + TASK_COUNT);
		
		// TODO 1 ReentrantLock instanziieren
		
		final long baseTime = System.currentTimeMillis();
		
		// start 10 virtual Threads
		for(int i = 0; i < TASK_COUNT; ++i) {
			final int cnt = i;
			
			Thread.startVirtualThread(() -> {
				long startThreadTime = System.currentTimeMillis() - baseTime;
			
				// TODO 2 lock() hier einfuegen
			
				// do some CPU work
				cpuIntensiveCall(1);
					
				// Es ist entscheidend, den println() Aufruf erst hier zu machen,
				// da dieser ein blockierender Aufruf ist, der den Programmablauf veraendert
				System.out.println("virtual thread running " + cnt + " at " + startThreadTime + " Milliseconds " + Thread.currentThread());
					
				// TODO 3 unlock lock() hier einfuegen
			});
			
		}
		
		sleep(10000);
	}
	
	
	////////// HELPER ///////////////////
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// runs about 1 second
	void  cpuIntensiveCall() {
		cpuIntensiveCall(1);
	}
	void  cpuIntensiveCall(int seconds) {
		long start = System.currentTimeMillis();
		for(long i = 0; i < (Long.MAX_VALUE/16)*seconds; ++i) {
			/*for(long j = 0; j < Integer.MAX_VALUE/32; ++j) {
			
			}*/
				
		}
		long duration = System.currentTimeMillis()-start;
		//System.out.println("cpuIntensiveCall : " + duration);
	}
}
