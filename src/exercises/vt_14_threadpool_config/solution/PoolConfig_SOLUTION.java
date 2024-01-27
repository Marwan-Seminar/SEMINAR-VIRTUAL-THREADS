package exercises.vt_14_threadpool_config.solution;

/*
 * Zeigt, wie der Threadpool konfiguriert werden kann, und wie sich das 
 * auf das Verhalten der Virtual-Threads auswirkt
 * 
 * VM Parameter mit denen der Threadpool Konfiguriert werden kann:
 * -Djdk.virtualThreadScheduler.parallelism=1
 * -Djdk.virtualThreadScheduler.maxPoolSize=1
 * -Djdk.virtualThreadScheduler.minRunnable=1
 * 
 * 
 * A) Konfiguriere den Threadpool so, dass nur ein Virtual-Thread gleichzeitig laufen kann
 * und zeige an einem Beispiel, dass dann andere Virtual-Threads warten müssen
 * 
 * B) Konfiguriere den Pool so, dass im Falle vieler pinning Threads
 * der Pool auf 100 anwaechst. Zeige dass damit auch bei Pinning 100 
 * Virtual-Threads starten.
 */
public class PoolConfig_SOLUTION {
	public static void main(String[] args) {

		PoolConfig_SOLUTION instance = new PoolConfig_SOLUTION();

		// A) Nur ein Virtual Thread kann laufen, ein zweiter muss warten
		//instance.smallPool();
		
		// B) Viele Pool Threads, bei Pinning eventuell hilfreich (Vorsicht, manchmal irregulaeres Verhalten)
		instance.bigPool();
	}

	/*
	 * Folgende Konfiguration der VM sorgt dafuer, dass nur ein Virtual-Thread 
	 * zu einem Zeitpunkt laufen kann, indem der Threadpool nur einen Platform-Thread
	 * enthaelt
	 * 
	 -Djdk.virtualThreadScheduler.parallelism=1
	 -Djdk.virtualThreadScheduler.maxPoolSize=1
	 -Djdk.virtualThreadScheduler.minRunnable=1
	 */
	private void smallPool() {

		Thread.startVirtualThread(() -> {
			System.out.println("First VT started");
			cpuIntensiveCall(10);

		});

		Thread.startVirtualThread(() -> {
			System.out.println("Second VT started");
			cpuIntensiveCall(10);

		});

		sleep(30000);
	}
		
	/*
	 * ACHTUNG: Seltsames Verhalten
	 *  
	 * Zeigen, dass ein immer weiter wachsender Pool auch bei Pinning sehr viele 
	 * gleichzeitige Virtual-Threads ermoeglicht
	 * 
	 * ACHTUNG
	 -Djdk.virtualThreadScheduler.parallelism=100
	 -Djdk.virtualThreadScheduler.maxPoolSize=100
	 -Djdk.virtualThreadScheduler.minRunnable=100
	 *  erzeugt hier manchmal einen Deadlock, wenn die Anzahl gestartetet Virtual-Threads 
	 *  groesser ist als 100
	 *  
	 *  Die Deadlocks verschwinden, wenn die printouts im synchronized Block auskommentiert werden.
	 */
	void bigPool() {
		
		for (int i = 0; i < 200; i++) {
			final int cnt = i;
			Thread.ofVirtual().name(" VT " + cnt).start(() -> {
				System.out.println("VT: " + cnt + " started" + Thread.currentThread());
				synchronized(this) {
					// Sehr seltsam: Diese Zeile provoziert machmal einen Deadlock, wenn die maxPoolSize kleiner als die Zahl der gestarteten Threads ist. Bug?
					//System.out.println("VT " + cnt + " in synch block " + Thread.currentThread());
					sleep(1000);
					System.out.println("VT " + cnt + " woke up " + Thread.currentThread());
				}

			});
			
		}
		
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

	// runs about 1 second
	void cpuIntensiveCall() {
		cpuIntensiveCall(1);
	}

	void cpuIntensiveCall(int seconds) {
		long start = System.currentTimeMillis();
		for (long i = 0; i < (Long.MAX_VALUE / 16) * seconds; ++i) {
			/*
			 * for(long j = 0; j < Integer.MAX_VALUE/32; ++j) {
			 * 
			 * }
			 */

		}
		long duration = System.currentTimeMillis() - start;
		//System.out.println("cpuIntensiveCall : " + duration);
	}
}
