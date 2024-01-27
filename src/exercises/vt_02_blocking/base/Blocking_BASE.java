package exercises.vt_02_blocking.base;


/*
 * Blocking Calls geben den Carrier Thread frei
 * 
 * - Zeige, dass ein virtueller Thread, der einen blockierenden Aufruf ausführt, 
 *   den unterliegenden Platform Thread („Carrier“) freigibt.
 *   
 * - Nutze dafuer z.B. den blockierenden Aufruf: Thread.sleep()
 * 
 * - Nutze: Thread.currentThread().toString() um den Carrier-Thread zu identifizieren
 * 	 VirtualThread[#22]/runnable@ForkJoinPool-1-worker-2
 *   Bedeutet: Virtual Thread "22" wird ausgefuerht von Platform Thread "ForkJoinPool-1-worker-2"
 * 
 * - Zeige, dass ein Virtual-Thread verschiedene Carrier haben kann, indem Du den Carrier vor und nach sleep() vergleichst
*/
public class Blocking_BASE {

	public static void main(String[] args) throws InterruptedException {
		Blocking_BASE instance = new Blocking_BASE();
		
		instance.blockingCallReleasesCarrier();
	}
	
	

	/*
	 * Zeigte anhand der toString() Repraesentation von Thread.currentThread()
	 * dass blockierende System-Calls den Carrier Thread freigeben.
	 */
	void blockingCallReleasesCarrier() throws InterruptedException {
		System.out.println("Blocking_BASE.blockingCallReleasesCarrier()");
		for(int i = 0; i < 4; ++i) {
			Thread.startVirtualThread(() -> {
				
				// TODO 1: Thread toString auf die Konsole schreiben: Thread.currentThread().toString()
				  
				// TODO 2: Blockierenden Aufruf, z.B: Thread.sleep(1000);
				
				// TODO 3: Zeige, dass Thread.currentThread().toString() nun einen anderen Carrier identifiziert
				  
			});
		}
		
		// Wait for scenario to finish
		Thread.sleep(30000);
	}

}

