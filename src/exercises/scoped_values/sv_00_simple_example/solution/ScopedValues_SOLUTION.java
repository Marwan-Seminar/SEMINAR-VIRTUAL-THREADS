package exercises.scoped_values.sv_00_simple_example.solution;

import java.util.concurrent.ExecutionException;


/*
 *
 * Scoped Value
 * 
 * Zeige, dass eine einzige ScopedValue Instanz in verschiedenen Virtual Threads
 * gleichzeitig unterschiedliche Werte haben kann
 * 
 *
 */
public class ScopedValues_SOLUTION {
	
	// Scoped Value als "global" für alle Threaes deklarieren
	static ScopedValue<String> SCOPED_VALUE_INSTANCE = ScopedValue.newInstance();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		System.out.println("ScopedValues_SOLUTION");
		
		new ScopedValues_SOLUTION().scopedValueTest();

	}
	
	
	
	void scopedValueTest() throws InterruptedException {
		
		System.out.println("ScopedValues_SOLUTION.scopedValueTest()");

		// Thread starten
		Thread virtThread1 = Thread.startVirtualThread(() -> {
			
			
			// Scoped Value zuweisen, "binden", mit where() und Scope definieren mit run()
			ScopedValue.where(SCOPED_VALUE_INSTANCE, "Hallo").run(() -> {
				
				
				while(true) {
					System.out.println("Wert in Thread 1: " + SCOPED_VALUE_INSTANCE.get() + " Scoped Value Instance: " + SCOPED_VALUE_INSTANCE);
				}
			});
		
		});
		
		Thread virtThread2 = Thread.startVirtualThread(() -> {
			// Anderer Wert in anderem Scope
			ScopedValue.where(SCOPED_VALUE_INSTANCE, "GutenTag").run(() -> {
				
				while(true) {
					System.out.println("Wert in Thread 2: " + SCOPED_VALUE_INSTANCE.get() + " Scoped Value Instance: " + SCOPED_VALUE_INSTANCE);
				}
			});
		
		});
		
		virtThread1.join();
		virtThread2.join();
		
	}
}
