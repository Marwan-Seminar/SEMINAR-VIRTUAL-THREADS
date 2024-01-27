package exercises.vt_09_wait_pinning.base;

import java.util.concurrent.atomic.AtomicBoolean;

/*
 * Zeige folgendes: 
 * A) Bei einem blockierenden wait()- Aufruf wird der Carrier Thread NICHT freigegeben
 * B) Wenn wait() blockiert, wird der Thread-Pool vergroessert, aber nur bis zu einem bestimmten Limit  
 *
 * Loesungshinweise:
 * Baue dafuer ein Szenario, bei dem viele virtual Threads wait() parallel aufrufen.
 * Danach wird ein weiterer virtueller Thread gestartet, der notifyAll() aufruft, 
 * um die im wait() blockierten Threads zu wecken.
 * 
 * Zeige, dass das nur bis zu einer bestimmten Anzahl paralleler wait()-Aufrufe funktioniert.
 * Bis zu dieser Anzahl waechst der Threadpool, ueber dieser Anzahl werden keine weiteren 
 * virtuellen Threads geschedult, somit kann das notifyAll() nie laufen.
 * 
 * */
public class Wait_BASE {
	public static void main(String[] args) {
		Wait_BASE instance = new Wait_BASE();
		
		instance.waitScenario();
	}
	
		
	/*
	 * Implementiere folgendes Szenario, die folgende Methode bietet den Rahmen dafuer.
	 * - Starte viele Virtual-Threads, die wait() aufrufen
	 * - Starte einen weiteren Virtual-Thread, der wait() Aufrufe mittels notifyAll() weckt
	 * - Pruefe aus dem Main-Thread ueber ein Flag, ob der zusaetzliche Virtual-Thread losgelaufen ist.
	 */	
	void waitScenario() {
		
		final Object lockObject = new Object();
		AtomicBoolean startedFlag = new AtomicBoolean(false);
		
		
		// TODO
		// Starte viele virtuelle Threads die wait() aufrufen: Fuer kleine Zahlen laeuft das Programm stabil. 
		// Fuer groessere Zahlen (z.B. 300) blockieren die wait()-Aufrufe den Thread-Pool und 
		// der zusaetzliche Virtual Thread, der unten gestartet wird, kann nie laufen.
		Thread.startVirtualThread(() -> {	
			
			synchronized(lockObject)
			{
				
				System.out.println("Thread waiting " + Thread.currentThread());
		
				// TODO: hier lockObject.wait(); einfuegen
					
				System.out.println("Thread woke up" + Thread.currentThread());
				
			}
			
		});
			
	
		// Einen Moment warten
		sleep(1000);
	
		
		// Einen weiteren virtuellen Thread starten der die wait() Aufrufe weckt
		Thread.startVirtualThread(() -> {
			
			startedFlag.set(true);
			// TODO hier lockObject.notifyAll(); einfuegen
			
			
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
