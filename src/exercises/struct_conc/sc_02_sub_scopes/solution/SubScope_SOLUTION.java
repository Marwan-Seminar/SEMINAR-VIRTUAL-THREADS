package exercises.struct_conc.sc_02_sub_scopes.solution;



import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/*
 * Aufgabe S-C 02 zu Sub-Scopes.
 * 
 * Es gibt mehrere Varianten von Sub-Scopes in einem StructduredTaskScope.
 * 
 *  Variante 1. innerhalb eines umschließenden Scopes
 *  Variante 2. innerhalb eines Tasks
 *  
 *  Aufgabe
 *  
 *  
 *  A) Baue einen aeusseren Scope unter Verwendung von StructuredTaskScope.open.
 *  	Erweitere den aeusseren Scope um einen Subscope (Variante 1). Starte einen Task-1 in diesem Subscope.
 *  
 *  B) Erweitere das Ergebnis von A) indem Du in dem aeusseren Scope einen Task-2 forkst, und erzeuge innerhalb dieses Task-2 einen 
 *  	Subscope. Darin startest Du dann wiederum einen Task-3. Das ist dann die Variante 2.
 *  
 *  C) Wie lange muesste das Programm laufen, wenn Task-1 4 Sekunden, Task-2 3 Sekunden und Task-3 2 Sekunden lang laufen?
 *  
 */
public class SubScope_SOLUTION {
	
	private static long starttime; 
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		starttime = System.currentTimeMillis();
		
		SubScope_SOLUTION instance = new SubScope_SOLUTION();
		//instance.structuredConcurrencyNestedScope_A();
		instance.structuredConcurrencyNestedScope_B();
		
		
	}
	
	

	/*
	 * A) Baue einen aeusseren Scope unter Verwendung von StructuredTaskScope.open.
	 * 	Erweitere den aeusseren Scope um einen Subscope (Variante 1). Starte einen Task-1 in diesem Subscope.
	 *  
	 */
	void structuredConcurrencyNestedScope_A() throws InterruptedException, ExecutionException {

		// Scope oeffnen, dies ist der aussere, der umschliessende Scope.
		try (var outerScope = StructuredTaskScope.open()) {
			System.out.println("Outer-Scope opened");
			
			// Subscope Variante 1: innerhalb eines Scopes
			try (var subScopeTopLevel = StructuredTaskScope.open()) {
				System.out.println("Sub-Scope within Top-Level Scope opened (Variant 1)");
				
				// Subtask starten, im Nested-Scope Variante 1
				subScopeTopLevel.fork(() -> {
					System.out.println("Task 1 started in Sub-Scope (Variant 1)");
					Thread.sleep(4000);
					System.out.println("Task 1 returning");
					return "Task 1 Return-Value";
				});
				// Subscope abwarten
				subScopeTopLevel.join();
				System.out.println("Sub-Scope Variant 1 join returned at Second " + ((System.currentTimeMillis() - starttime) / 1000));

			}

			// Auf Beendigung aller Subscopes warten
			outerScope.join();
			System.out.println("Enclosing join returned at Second " + ((System.currentTimeMillis() - starttime) / 1000));
			
		
		}
	}


	/*
	 * B) Erweitere das Ergebnis von A) indem Du in dem aeusseren Scope einen Task-2 forkst, und erzeuge innerhalb dieses Task-2 einen 
 *  	Subscope. Darin startest Du dann wiederum einen Task-3. Das ist dann die Variante 2.
	 */
	void structuredConcurrencyNestedScope_B() throws InterruptedException, ExecutionException {

		// Scope oeffnen, dies ist der aeussere, der umschliessende Scope.
		try (var outerScope = StructuredTaskScope.open()) {
			System.out.println("Outer-Scope opened");
			
			// Subscope Variante 1: innerhalb eines Scopes
			try (var subScopeTopLevel = StructuredTaskScope.open()) {
				System.out.println("Sub-Scope within Top-Level Scope opened (Variant 1)");
				
				// Subtask starten, im Nested-Scope Variante 1
				subScopeTopLevel.fork(() -> {
					System.out.println("Task 1 started in Sub-Scope (Variant 1)");
					Thread.sleep(4000);
					System.out.println("Task 1 returning");
					return "Task 1 Return-Value";
				});
				// Subscope abwarten
				subScopeTopLevel.join();
				System.out.println("Sub-Scope Variant 1 join returned at Second " + ((System.currentTimeMillis() - starttime) / 1000));

			}

			// Subtask im aeusseren Scope starten
			outerScope.fork(() -> {

				System.out.println("Task 2 started: In Top-Level Scope");

				// Sub-Scope Variante 2: innerhalb eines Tasks
				try (var subScopeWithinSubtask = StructuredTaskScope.open()) {
					System.out.println("Sub-Scope within Task opened (Variant 2)");
					// Subtask 3 starten
					subScopeWithinSubtask.fork(() -> {
						System.out.println("Task 3 started in Sub-Scope (Variant 2)");
						Thread.sleep(3000);
						System.out.println("Task 3 returning");
						return "Task 3 Subscope from Subtask Return-Value";
					});

					// Task 2 working for 2 Seconds 
					Thread.sleep(2000);
					
					subScopeWithinSubtask.join();
					System.out.println("Subscope Variant 2 join returned at Second " + ((System.currentTimeMillis() - starttime) / 1000));
					
				}
				
				
				
				System.out.println("Task 2 returning");
				return "Task 2 return value";
			});

		

			// Auf Beendigung aller Subscopes warten
			outerScope.join();
			System.out.println("Enclosing join returned at Second " + ((System.currentTimeMillis() - starttime) / 1000));
			
		
		}
	}

}
