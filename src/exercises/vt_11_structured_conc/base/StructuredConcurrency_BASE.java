package exercises.vt_11_structured_conc.base;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/*
 * Structured Concurrency 
 * 
 * 1. Definiere einen Scope mithilfe der Klasse StructuredTaskScope.ShutdownOnFailure
 * 
 * 2. Starte darin zwei Subtasks unter Verwendung der Methode StructuredTaskScope.fork()
 * 
 * 3. Werfe aus einer der Subtasks eine Exception. Sorge dafuer, dass die zweite Task dadurch beendet wird.

 * Hinweis: 
 * - scope.join(); wartet auf alle Subtasks
 * - scope.throwIfFailed(); Sorgt dafuer, dass alle Subtasks benachrichtigt werden, dass eine Exception geworfen wurde
 * - WICHTIG: Jede Subtask muss ihren Interrupted Zustand regelmaessig pruefen: Thread.currentThread().isInterrupted()
 * 
 * Zwei Subtasks werden gestartet, wenn eine von beiden eine Exception wirft, so kann das im Scope festgestellt werden
 * und der Scope verlassen werden.
 * 
 * Dafuer ist es allerdings Voraussetzung, dass die Subtasks ihren Interrupted Status pruefen und sich 
 * ggf. selbst beenden. 
 * 
 * Die Leistung des Framworks ist es, den Interrupted Status an den Subtaks-Thread zu kommunizieren.
 * 
 *
 */
public class StructuredConcurrency_BASE {
	

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		System.out.println("StructuredConcurrency_BASE");
		
		new StructuredConcurrency_BASE().structuredConcurrencyExample();

	}
	
	void structuredConcurrencyExample() throws InterruptedException, ExecutionException{
		
		System.out.println("StructuredConcurrency_BASE.structuredConcurrencyExample()");

		
		// ShutdownOnFailure ist ein Scope mit der Policy im Fehlerfall alle Subtasks zu beenden
		try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
			
			// Subtask starten
			Subtask<String> subTask1;  // TODO scope.fork(() -> { });
			
			Subtask<String> subTask2; // TODO
			 	/*
				=  scope.fork(() -> {
					return null;
				
					// TODO Interrupted Status muss geprueft werden, wenn der Thread sich im Fehlerfall beenden soll
					//if (Thread.currentThread().isInterrupted()) {
		            //   return null;
		            //}
				
				});
				*/
				
				
				
			
			// TODO: warten auf alle Subtasks
			//	scope.join();
			
			// TODO Scope abbrechen und Exception werfen
			//scope.throwIfFailed();
			
		    // TODO Ergebnisse der Subtasks abholen
			//System.out.println(subTask1.get());
			//System.out.println(subTask2.get());
		}
	}
	
}
