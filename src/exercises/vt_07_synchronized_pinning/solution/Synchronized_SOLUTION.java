package exercises.vt_07_synchronized_pinning.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;



/*
 * Synchronized Besonderheit: In einem synchronized Block wird bei blockierendem
 * OS-Aufruf der virtuelle Thread nicht vom OS-Thread entkoppelt. Das wird als "Pinning"
 * bezeichnet.
 * 
 * A) Zeige, dass ein virtueller Thread seinen Carrier-Thread nicht freigibt, wenn 
 * der virtuelle Thread auf das Betreten eines synchronized Blockes wartet oder wenn 
 * er innerhalb des synchronized Blockes einen blockierenden Betriebssystem-Aufruf taetigt.
 * 
 * B) Repariere das Verhalten durch Nutzung von ReentrantLock.
 * 
 */
public class Synchronized_SOLUTION {
 
	public static void main(String[] args) {
		
		Synchronized_SOLUTION instance = new Synchronized_SOLUTION();
		
		// A) synchronized: Keine Freigabe der Carrier, einige Threads starten spaeter
		instance.synchronizedSzenario();
		
		// B) Reentrant Lock: Freigabe der Carrier, Threads starten fast gleichzeitig
		//instance.reentrantLockSzenario();
	}
 
	/* 
	 * A)  
	 * 
	 * Das folgende Beispiel zeigt, dass im synchronisierten Block bei sleep()
	 * die Carrier nicht freigegeben werden.
	 * Am Output sieht man, dass die virtuellen Threads von einem Threadpool begrenzter Groesse 
	 * ausgefuerht werden.
	 * Es laufen zunaechst nur 8 Threads, und mit jeweils 1 Sekunde Abstand dann die beiden anderen.
	 * 
	 */
	void synchronizedSzenario(){
		
		System.out.println("synchronizedSzenario()");
		
		final long base = System.currentTimeMillis();
		final Object lockObject = new Object();
		final List<Thread> threads = new ArrayList<>();
		
		for(int i = 0; i< 10; i++) {
			final int j = i;
			
			
			Thread thread = Thread.startVirtualThread(() -> {
				long threadStart = System.currentTimeMillis()-base;
				
				synchronized(lockObject)
				{
					
					// gibt den Carrier Thread nicht frei, da in synchronized section
					sleep(1000);
				}
				// muss hier unten stehen, veraendert sonst das Verhalten da blockierend.
				System.out.println("Virtual Thread Job Started " + j + " id: " + Thread.currentThread().threadId() + " at Millisecond: " + threadStart);
				
			});
			
			threads.add(thread);
		}
		
		
		// warten bis das Szenario beendet ist
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
		
		
	}
	
	/*
	 * B)
	 * 
	 * Das folgende Beispiel behebt das Problem, indem synchronized durch 
	 * ReentrantLock ersetzt wird.
	 * 
	 * Man sieht an der Zeitmessung im Output, dass alle virtuellen Threads
	 * nahezu gleichzeitg starten. Das ist trotz des begrenzten unterliegenen Pools 
	 * von Platform-Threads nur dadurch möglich, dass sleep() nun den jeweiligen Carrier Thread
	 * freigibt.
	 * 
	 */
	void reentrantLockSzenario(){
		
		System.out.println("reentrantLockSzenario()");
		
		final long base = System.currentTimeMillis();
		final Object lockObject = new Object();
		
		// Das ist der korrekte Fix:
		final ReentrantLock reentrantLock = new ReentrantLock();
		
		for(int i = 0; i< 10; i++) {
			final int j = i;
			
			Thread.startVirtualThread(() -> {
				long threadStart = System.currentTimeMillis()-base;
				
				reentrantLock.lock();
				try{
					
					// gibt den Carrier Thread nun frei, da nicht in synchronized section
					sleep(1000);
				
				} finally {
					reentrantLock.unlock();
				}
				// muss hier unten stehen, veraendert sonst das Verhalten da blockierend.
				System.out.println("Virtual Thread Job Started " + j + " id: " + Thread.currentThread().threadId() + " at Millisecond: " + threadStart );
				
			});
		}
		
		// warten bis das Szenario beendet ist.
		sleep(40000);
		
		
	}
	
	/////////////// HELPER ///////////////////////////
	
	private static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
