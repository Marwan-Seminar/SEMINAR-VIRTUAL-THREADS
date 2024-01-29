package exercises.vt_02_blocking.solution;

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
public class Blocking_SOLUTION {

	public static void main(String[] args) throws InterruptedException {
		Blocking_SOLUTION instance = new Blocking_SOLUTION();
		
		instance.blockingCallReleasesCarrier();
		
		instance.blockingCallReleasesCarrierPrecise();
	}
	
	

	/*
	 * Zeigt anhand der toString() Repraesentation von Thread.currentThread()
	 * dass blockierende System-Calls den Carrier Thread freigeben.
	 * 
	 * BEMERKUNG: Diese Loesung ist nicht ganz präzise, weil out.orintln() selbst ein 
	 * blockierender System-Call ist, der den Carrier freigibt.
	 */
	void blockingCallReleasesCarrier() throws InterruptedException {
		System.out.println("Blocking_SOLUTION.blockingCallReleasesCarrier()");

		for(int i = 0; i < 4; ++i) {
			Thread.startVirtualThread(() -> {
				
				System.out.println("Virtual Thread ID and underlying Carrier ID before sleep: " + Thread.currentThread());
				  
				try {
					// Blocking System Call
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				System.out.println("Virtual Thread id and underlying Carrier ID after sleep: " + Thread.currentThread());
				  
			});
		}
		
		// Wait for scenario to finish
		Thread.sleep(30000);
	}
	
	
	/*
	 * Genauere Version:
	 * 
	 * Zeigt anhand der toString() Repraesentation von Thread.currentThread()
	 * dass blockierende System-Calls den Carrier Thread freigeben.
	 * 
	 */
	void blockingCallReleasesCarrierPrecise() throws InterruptedException {

		System.out.println("Blocking_SOLUTION.blockingCallReleasesCarrierPrecise()");
		
		for(int i = 0; i < 4; ++i) {
			Thread.startVirtualThread(() -> {
				
				String threadNameBeforeSleep = Thread.currentThread().toString(); 
				try {
					// Blocking System Call
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				String threadNameafterSleep = Thread.currentThread().toString(); 
				
				System.out.println("Virtual Thread id and underlying Carrier ID before sleep: " + threadNameBeforeSleep);
				System.out.println("Virtual Thread id and underlying Carrier ID after sleep: " + threadNameafterSleep);
				  
			});
		}
		
		// Wait for scenario to finish
		Thread.sleep(30000);
	}


}
