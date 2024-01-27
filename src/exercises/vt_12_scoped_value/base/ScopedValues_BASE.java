package exercises.vt_12_scoped_value.base;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/*
 *
 * Scoped Value
 * 
 * Zeige, dass eine einzige ScopedValue Instanz in verschiedenen Virtual Threads
 * gleichzeitig unterschiedliche Werte haben kann
 * 
 *
 */
public class ScopedValues_BASE {
	
	// TODO Scoped Value als "global" für alle Threads deklarieren 
	// Zutaten:
	// static SCOPED_VALUE_INSTANCE
	// ScopedValue<String>
	// ScopedValue.newInstance()

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("ScopedValues_BASE");
		new ScopedValues_BASE().scopedValueTest();

	}
	
	
	
	void scopedValueTest() throws InterruptedException {
		System.out.println("ScopedValues_BASE.scopedValueTest()");
		
		// Ersten Thread starten
		Thread virtThread1 = Thread.startVirtualThread(() -> {
		
		// Zutaten: 
		//	ScopedValue.where(...).run(() -> {})
			
			// TODO 1: ScopedValue zuweisen, "binden", mit ScopedValue.where(SCOPED_VALUE_INSTANCE, ...)
			// TODO 2: Auszufuerenden Code im Scope definieren mit run(), auf dem Rueckgabewert von where(), vom Typ ScopedValue.Carrier
			// TODO 3: Auf ScopedValue lesend zugreifen: SCOPED_VALUE_INSTANCE.get()
			
		
		});
	
		// Zweiten Thread starten
		Thread virtThread2 = Thread.startVirtualThread(() -> {
			// TODO: Wie oben im ersten Thread, aber anderen Wert an SCOPED_VALUE_INSTANCE zuweisen.
			// TODO: Zeigen, dass innerhalb des Threads der andere Wert gelesen wird.
		
		});
		
		virtThread1.join();
		virtThread2.join();
		
	}
}
