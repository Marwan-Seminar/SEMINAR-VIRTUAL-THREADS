package exercises.vt_14_threadpool_config.base;

/*
 * Zeige, wie der Threadpool konfiguriert werden kann, und wie sich das 
 * auf das Verhalten der Virtual-Threads auswirkt.
 * 
 * VM Parameter mit denen der Threadpool konfiguriert werden kann:
 * -Djdk.virtualThreadScheduler.parallelism=X
 * -Djdk.virtualThreadScheduler.maxPoolSize=Y
 * -Djdk.virtualThreadScheduler.minRunnable=Z
 * 
 * 
 * A) Konfiguriere den Threadpool so, dass nur ein Virtual-Thread gleichzeitig laufen kann
 * und zeige an einem Beispiel, dass dann andere Virtual-Threads warten müssen
 * 
 * B) Konfiguriere den Pool so, dass im Falle vieler pinning Threads
 * der Pool auf 100 anwaechst. Zeige dass damit auch bei Pinning 100 
 * Virtual-Threads starten‚
 * 
 */
public class PoolConfig_BASE {
	public static void main(String[] args) {

		PoolConfig_BASE instance = new PoolConfig_BASE();

		// A) Nur ein Virtual Thread kann laufen, ein zweiter muss warten
		instance.smallPool();
		
		// B) Viele Pool Threads, bei Pinning eventuell hilfreich (Vorsicht, manchmal irregulaeres Verhalten)
		//instance.bigPool();
	}

	/*
	 * 
	 * Finde Zahlen für X,Y und Z so dass folgende Situation hergestellt wird:
	 * 
	 * Nur ein Virtual-Thread kann zu einem Zeitpunkt laufen, da der Thread-Pool nur einen 
	 * Platform Thread enthaelt.
	 * 
	 * Die beiden Virtual-Threads im folgendne Beispiel-Code koennen dann nicht mehr gleichzeitig laufen.
	 * 
	 * Die folgenden Parameter muessen der Java VM beim Start uebergeben werden
	 * 
	 -Djdk.virtualThreadScheduler.parallelism=X
	 -Djdk.virtualThreadScheduler.maxPoolSize=Y
	 -Djdk.virtualThreadScheduler.minRunnable=Z
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
	 * ACHTUNG: Kann zu Deadlock fuehren (fuer mich unerklaerlich)!
	 *  
	 * Zeige, dass ein immer weiter wachsender Pool auch bei Pinning sehr viele 
	 * gleichzeitige Virtual-Threads ermoeglicht
	 * 
	 -Djdk.virtualThreadScheduler.parallelism=X
	 -Djdk.virtualThreadScheduler.maxPoolSize=Y
	 -Djdk.virtualThreadScheduler.minRunnable=Z
	 
	 * KOMMENTAR zu den Deadlocks: 
	 *  Erzeugt hier einen Deadlock, wenn die Anzahl gestartetet Virtual-Threads 
	 *  groesser ist als die Anzahl der Threads im Pool
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
