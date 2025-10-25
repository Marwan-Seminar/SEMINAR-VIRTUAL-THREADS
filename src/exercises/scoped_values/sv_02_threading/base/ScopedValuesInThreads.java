package exercises.scoped_values.sv_02_threading.base;

import java.util.concurrent.StructuredTaskScope;

/*
 * Der Sinn von Scoped Values ist es, diese in Threads zur Verfügung zu stellen.
 * 
 * Dafuer gibt es unterschiedliche Szenarien. Hier zwei Beispiele
 * 
 * A) Inheritance: Nutze eine ScopedValue Instanz um mehrerern Threads einen indentischen Wert zur Verfuegung zu stellen. 
 * 	Verwende dafuer einen StructuredTaskScope. 
 * 
 * B) Weise mehreren Threads innerhalb eines StructuredTaskScope jeweils unterschiedliche Werte zu
 * 
 * c) Nutze Bindings (where()) in unstrukturierten Threads um jedem einen eigenen Wert zuzuweisen
 */
public class ScopedValuesInThreads {
	
	// Die ScopedValue instanz fuer diese Beispiele
	final static ScopedValue<String>SCOPED_VALUE = ScopedValue.newInstance();
	
	
	public static void main(String[] args) throws InterruptedException {
		
		
		ScopedValuesInThreads instance = new ScopedValuesInThreads();
		
		// A) Inheritance
		instance.scopedValueInheritance();
		
		// B) Verschiedene Werte innnerhalb eines StructuredTaskScopes
		//instance.scopedValueBindingsInStructuredScope();
		
		// C) Verschiedene Werte in unabhaengigen Thread
		//instance.differentValuesIndepententThreads();
	}
	
	
	
	
	
	/*
	 * A) Die Vererbung der Scoped-Values unter Verwendung von StructuredTaskScope 
	 */
	void scopedValueInheritance() throws InterruptedException {

		System.out.println("A) Vererbung der Scoped-Values unter Verwendung von StructuredTaskScope ");
		
		
		// TODO:  Zuweisung des Wertes und die Eroeffnung eines Scopes fuer den Value //ScopedValue.where(SCOPED_VALUE, "\"Vererbter Wert\"").run(() -> { //... });

			// TODO: Den "StructuredTaskScope" fuer die Vererbung beginnen: //try (var scope = StructuredTaskScope.open()) { //...}

				// TODO: Mehrere Subtaks starten, mit scope.fork()scope.fork(() -> { // ...});
		
					// TODO innerhalb der Subtask auf den ScopedValue zugreifen und ihn auf die Shell schreiben
					/*
					System.out.println("Inherited Value " + SCOPED_VALUE.get() + " in Virtual Thread "
							+ Thread.currentThread());
					*/

				// TODO eine weitere Subtaks: scope.fork(() -> { 	});

				// TODO etc. scope.fork(() -> { 	});
			

				// TODO: auf Beendigung des StructuredTaskScopes warten: // scope.join();
				
			/*
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new Error(e);
			}
			*/
		
	}
	
	/*
	 *  B) Verschiedene Werte innerhalb eines StructuredTaskScopes  
	 */
	void scopedValueBindingsInStructuredScope() throws InterruptedException {
		
		System.out.println("B) Verschiedene Werte innnerhalb eines StructuredTaskScopes");

		
		// TODO // Umschliessender "StructuredTaskScope": // try (var scope = StructuredTaskScope.open()) {
		
			// TODO: Subtasks starten: //scope.fork(() -> {
				// TODO; Binding 1 in Subtaks // ScopedValue.where(SCOPED_VALUE, "Wert 1").run(() -> { 
				// auf die Shell SCOPED_VALUE.get() 
				//});
			
			// TODO weitere Subtasks mir anderen Werten		scope.fork(() -> {		ScopedValue.where(SCOPED_VALUE, "Wert 2").run(() -> { // ... });
			
			// Scpoe Ende abwarten
			//scope.join();

		/*
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error(e);
		}*/

	}

	/*
	 *  B) Verschiedene Werte innerhalb eines StructuredTaskScopes  
	 */
	void scopedValueRebinding() throws InterruptedException {

		System.out.println(" B) Verschiedene Werte innerhalb eines StructuredTaskScopes ");
		
		// Umschliessender "StructuredTaskScope" 
		try (var scope = StructuredTaskScope.open()) {

			scope.fork(() -> {
				// Binding 1 in Scope 1
				ScopedValue.where(SCOPED_VALUE, "Wert 1").run(() -> {
					System.out.println("Thread 1 Value " + SCOPED_VALUE.get() + " in " + Thread.currentThread());
				});
			});

			scope.fork(() -> {
				// Binding 2 in Scope 2
				ScopedValue.where(SCOPED_VALUE, "Wert 2").run(() -> {
					System.out.println("Thread 2 Value " + SCOPED_VALUE.get() + " in " + Thread.currentThread());
				});
			});

			scope.fork(() -> {
				// Binding 3 in Scope 3
				ScopedValue.where(SCOPED_VALUE, "Wert 2").run(() -> {
					System.out.println(
							"Thread 3 Value " + SCOPED_VALUE.get() + " in " + Thread.currentThread());
				});
			});

			scope.join();

		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new Error(e);
		}

	}

	
	/*
	 * C) Verschiedene Werte in  unabhängigen Threads 
	 */
	 void differentValuesIndepententThreads() throws InterruptedException {
		
		System.out.println("C) Verschiedene Werte in verschiedenen unabhängigen Threads");
		
		// TODO: einen Virtual Thread starten // Thread vt_1 = Thread.startVirtualThread(() ->{ //... });
			
			// TODO "Binding": innerhaqlb des Threads dem ScoepdVAlue einen Wert zuseisen: // ScopedValue.where(SCOPED_VALUE, "Wert Thread 1").run(() -> { //... });
				
				// TODO: Aus dem Thread heraus  Auf den Wert zugreifen: String valueinRun_1 = SCOPED_VALUE.get();
				
			
		
		// TODO einen zweiten Thread wie oben, und zeigen dass er einen eigenen Wert sieht
		
		// TODO: Auf die Beendigung der beiden Threads warten
		/*
		vt_1.join();
		vt_2.join();
		*/
	}
	

}
