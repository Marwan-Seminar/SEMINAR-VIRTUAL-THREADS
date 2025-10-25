package exercises.struct_conc.sc_01_structured_scope.base;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/*
 * Aufgabe S-C 01
 * 
 * Scope based Structured Concurrency
 * 
 * 1. Definiere einen Scope mithilfe der Klasse StructuredTaskScope
 * 
 * 2. Starte darin zwei Subtasks unter Verwendung der Methode StructuredTaskScope.fork() und zeige, dass der 
 * 	Scope erst dann beendet wird, wenn beide Subtasks abgeschlossen sind
 * 
 * 3. Werfe aus der ersten Subtasks eine Exception. Sorge dafuer, dass die zweite Subtask dadurch beendet wird.

 * Hinweis: 
 * - scope.join(); wartet auf alle Subtasks
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
 */
public class StructuredConcurrency_BASE {
	

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		System.out.println("StructuredConcurrency_BASE");
		
		new StructuredConcurrency_BASE().structuredConcurrencyExample();

	}
	
	void structuredConcurrencyExample() throws InterruptedException, ExecutionException{
		
		System.out.println("StructuredConcurrency_BASE.structuredConcurrencyExample()");

		
		// TODO nutze  StructuredTaskScope.open() um einen Scope zu oeffnen try(var scope =  StructuredTaskScope.open()){
		{	
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
			
		
		}
	}
	
}
