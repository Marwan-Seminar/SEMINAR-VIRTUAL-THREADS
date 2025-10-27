package exercises.struct_conc.sc_01_structured_scope.solution;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/*
 * 
 * Aufgabe S-C 01
 * 
 * Structured Concurrency: Structured Scope
 * 
 * 1. Definiere einen Scope mithilfe der Klasse StructuredTaskScope
 * 
 * 2. Starte darin zwei Subtasks unter Verwendung der Methode StructuredTaskScope.fork() und zeige, dass der 
 * 	Scope erst dann beendet wird, wenn beide Subtasks abgeschlossen sind
 * 
 * 3. Werfe aus einer der Subtasks eine Exception. Sorge dafuer, dass die zweite Task dadurch beendet wird.
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
public class StructuredConcurrency_SOLUTION {
	

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		new StructuredConcurrency_SOLUTION().structuredConcurrencyExample();
	}
	
	void structuredConcurrencyExample() throws InterruptedException, ExecutionException{
		
		
		/////////////// Scope Begin //////////////////
		
		try(var scope =  StructuredTaskScope.open()){
			
			// Subtask starten
			Subtask<String> subTask1 =  scope.fork(() -> {
				
				System.out.println("Task 1 called in " + Thread.currentThread());
				throw new Error();
				
				//return "Hallo";
			});
			
			// Jede Subtask ist selbst dafuer verantwortlich, ihren Interrupted Status zu pruefen
			// und sich ggf. selbst zu beenden.
			Subtask<String> subTask2 =  scope.fork(() -> {
				for(int i = 0; i < 1000; ++i) {
					System.out.println("Task 2 loop: " + i  +" called in "  + Thread.currentThread());
					
					// Interrupted Status muss geprüft werden
					if (Thread.currentThread().isInterrupted()) {
		                System.out.println(" Interruption ");
		                // Subtask kehrt "freiwillig" zurueck, weil ihr Interruptd Status gesetzt wurde
		                return null;
		            }
				}
				return "World";
			});
		
			/////////////// Scope End //////////////////
			
			// Wartet auf alle Subtasks
			scope.join();
			System.out.println("Join returned");
		
			
	
		}
	}
	
}
