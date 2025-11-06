package exercises.vt_06_lock.solution;

import java.util.concurrent.locks.ReentrantLock;

/*
 * Zeigt, dass ein virtueller Thread, der die lock() Methode einer Lock Instanz aus util.concurrent aufruft
 * seinen Carrier Thread freigibt.
 * 
 * Locks sind eine Alternative zu synchronized.
 */
public class Lock_SOLUTION {
	
	
	final static int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	final static int TASK_COUNT = CPU_COUNT +2;

	public static void main(String[] args) {
		Lock_SOLUTION instance = new Lock_SOLUTION();
		instance.lockScenario();
	}
	
	/* 
	 * Der blockierende lock() Aufruf gibt den Carrier Thread frei.
	 * 
	 * Man sieht das an folgendem Vergleich:
	 * 
	 * Es werden 10 virtual Threads gestartet. Der Zeitpunkt des jewiligen Starts wird gemessen,
	 * und am Ende des Threads ausgegeben. 
	 *  
	 * Ohne Lock laufen zunaechst nur 8 virtual Threads, diese ersten 8 belegen die CPUs,
	 * alle weiteren virtual Threads bekommen erst dann einen Carrier, wenn die ersten 
	 * virtual Threads beendet sind.
	 * 
	 * Mit Lock laufen alle nahezu gleichzeitig los!
	 *  
	 * Bemerkung: Es ist wichtig, dass die println() Aufrufe erst am Ende des jeweiligen Threads
	 * stehen, denn sie sind blockiernde Aufrufe die anderenfalls den Carrier freigeben und so 
	 * das Verhalten des Programms verfaelschen.
	 * 
	 */
	void lockScenario() {
		
		System.out.println("CPU_COUNT " + CPU_COUNT + " TASK_COUNT " + TASK_COUNT);
		
		final ReentrantLock reentrantLock = new ReentrantLock();
		final long baseTime = System.currentTimeMillis();
		
		// start competing Threads
		for(int i = 0; i < TASK_COUNT; ++i) {
			final int cnt = i;
			
			Thread.startVirtualThread(() -> {
				long startThreadTime = System.currentTimeMillis() - baseTime;
				
				// Critical Section
				reentrantLock.lock();
				{
					// Dieser Aufruf ferfaelscht das Ergebnis!! Er gibt den Carrier-Thread frei.
					//System.out.println("virtual thread in critical section " + Thread.currentThread());

					// do some work
					cpuIntensiveCall(5);
					
					// Es ist entscheidend, den println() Aufruf erst hier zu machen 
					System.out.println("virtual thread running " + cnt + " at " + startThreadTime + " Milliseconds " + Thread.currentThread());
					
				}
				reentrantLock.unlock();
			});
			
		}
		
		sleep(60000);
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
