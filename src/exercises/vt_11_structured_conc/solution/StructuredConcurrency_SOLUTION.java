package exercises.vt_11_structured_conc.solution;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/*
 * Structured Concurrency
 * 
 * Zwei Subtasks werden gestartet.
 * 
 * Wenn eine von beiden eine Exception wirft, so kann das im Scope festgestellt werden
 * und der Scope verlassen werden Dafuer muss aber jede Subtask ihren Interrupted Status
 * selbst regelmaessig pruefen und sich ggf. beenden.
 * 
 */
public class StructuredConcurrency_SOLUTION {
	

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		new StructuredConcurrency_SOLUTION().structuredConcurrencyExample();
	}
	
	void structuredConcurrencyExample() throws InterruptedException, ExecutionException{
		
		
		/////////////// Scope Begin //////////////////
		
		try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
			
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
					
					// Interruptede Status muss geprüft werden
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
		
			// Wirft die Exeption weiter, die in irgendeiner der Tasks geworfen wurde
			scope.throwIfFailed();	
			
			// Spaetestens hier werden Exceptions weiter geworfen
			System.out.println(subTask1.get());
			System.out.println(subTask2.get());
		}
	}
	
}
