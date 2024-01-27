package exercises.vt_09_wait_pinning.solution;

import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Hier wird folgendes gezeigt: 
 * - bei wait() wird der Carrier Thread NICHT freigegeben
 * - wenn wait() blockiert, wird der Thread-Pool vergroessert, aber nur bis zu einem bestimmten Limit  
 */
public class Wait_SOLUTION {
	public static void main(String[] args) {
		Wait_SOLUTION instance = new Wait_SOLUTION();
		
		instance.waitScenario();
	}
	
	/*
	 *  Im folgenden Besipiel sieht man am Output, dass der Thread-Pool bis auf ca 256 Threads
	 *  wächst. Bei mehr blockierenden wait() Aufrufen gibt es ein Deadlock, weil wait() den Carrier
	 *  nicht frei gibt. 
	 */	
	void waitScenario() {
		
		final Object lockObject = new Object();
		final AtomicBoolean startedFlag = new AtomicBoolean(false);
		
		
		// Starte viele Threads die wait() aufrufen: Fuer kleine Zahlen laeuft das Programm stabil. 
		// Fuer groessere Zahlen (z.B. 300) blockieren die Writer den Thread-Pool und 
		// der notify() Aufruf (siehe unten) wird nie laufen.
		for(int i = 0; i < 300; i++) {
			final int wr = i;
			Thread.startVirtualThread(() -> {	
				
				synchronized(lockObject)
				{
					try {
						System.out.println("Thread " + wr + " waiting " + Thread.currentThread());
			
						// wait 
						lockObject.wait();
						
						System.out.println("Thread " + wr + " woke up" + Thread.currentThread());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			});
			
			
		}
	
	
		// Einen weiteren virtuellen Thread starten
		Thread.startVirtualThread(() -> {
			
			startedFlag.set(true);
			synchronized(lockObject) {
				lockObject.notifyAll();
			}
			
		});

		// Einen Moment warten
		sleep(1000);
		// Pruefen, ob der zusaetzliche Thread gestartet ist
		if(startedFlag.get()) {
			System.out.println("Additional Thread did start: POROGRAM OK");
		}else {
			System.out.println("Additional Thread did not start: DEADLOCK");
		}
		
		// Warten bis das Szenario beendet ist
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
}
