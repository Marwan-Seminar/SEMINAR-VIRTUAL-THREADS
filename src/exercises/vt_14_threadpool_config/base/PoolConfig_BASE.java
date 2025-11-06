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
 *  B) Konfiguriere den Pool so, dass im Falle vieler laufender Threads zu einem Spreicherplatz-Problem kommt.
 * Hinweis: Du musst dafuer sehr viele virtual Threads starten, und sie muessen auf der CPU arbeiten (warum?)
 * Nutze z.B. die Funktion cpuIntensiveCall(1000);
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
	 * B) Erzeuge Exeptions oder uebermaessigen Speicherbedarf
	 * indem du Pool durch "boesartige" Einstellung der folgenden VM-Parameter dazu dazu 
	 *  bringst sehr weit zu wachsen
	 *  
	 -Djdk.virtualThreadScheduler.parallelism=X
	 -Djdk.virtualThreadScheduler.maxPoolSize=Y
	 -Djdk.virtualThreadScheduler.minRunnable=Z
	 */
	void bigPool() {
	
		// muss unter Umstaenden noch groesser gewaehlt werden, z.B. 300.000 (Plattform / OS abhaengig)
		final int THREAD_COUNT = 100_000;
		
		for (int i = 0; i < THREAD_COUNT; i++) {
			final int cnt = i;
			Thread.ofVirtual().name(" VT " + cnt).start(() -> {
				System.out.println("VT: " + cnt + " started" + Thread.currentThread());
				
				cpuIntensiveCall(1000);
				System.out.println("VT " + cnt + " woke up " + Thread.currentThread());

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
