package exercises.scoped_values.sv_02threading.solution;

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
		
		// C) Verschiedene Werte pro Thread
		//instance.differentValuesIndepententThreads();
	}
	
	
	
	
	
	/*
	 * A) Die Vererbung der Scoped-Values unter Verwendung von StructuredTaskScope 
	 */
	void scopedValueInheritance() throws InterruptedException {

		System.out.println("A) Vererbung der Scoped-Values unter Verwendung von StructuredTaskScope ");
		
		
		// Dies ist die Zuweisung des Wertes
		ScopedValue.where(SCOPED_VALUE, "\"Vererbter Wert\"").run(() -> {

			// Dies ist der "Scope" fuer die Vererbung
			try (var scope = StructuredTaskScope.open()) {

				scope.fork(() -> {
					System.out.println("Inherited Value " + SCOPED_VALUE.get() + " in Virtual Thread "
							+ Thread.currentThread());
				});

				scope.fork(() -> {
					System.out.println("Inherited Value " + SCOPED_VALUE.get() + " in Virtual Thread "
							+ Thread.currentThread());
				});

				scope.fork(() -> {
					System.out.println("Inherited Value " + SCOPED_VALUE.get() + " in Virtual Thread "
							+ Thread.currentThread());
				});

				scope.join();
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new Error(e);
			}
		});
	}
	
	/*
	 *  B) Verschiedene Werte innerhalb eines StructuredTaskScopes  
	 */
	void scopedValueBindingsInStructuredScope() throws InterruptedException {
		
		System.out.println("B) Verschiedene Werte innnerhalb eines StructuredTaskScopes");

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
	 *  B) Verschiedene Werte innerhalb eines StructuredTaskScopes  
	 */
	void scopedValueRebinding() throws InterruptedException {

		System.out.println(" B) Verschiedene Werte innerhalb eines StructuredTaskScopes ");
		
		// Umschliessender "StructuredTaskScope" 
		try (var scope = StructuredTaskScope.open()) {

			scope.fork(() -> {
				// Binding 1 in Subtask 1
				ScopedValue.where(SCOPED_VALUE, "Wert 1").run(() -> {
					System.out.println("Thread 1 Value " + SCOPED_VALUE.get() + " in " + Thread.currentThread());
				});
			});

			scope.fork(() -> {
				// Binding 2 in Subtask 2
				ScopedValue.where(SCOPED_VALUE, "Wert 2").run(() -> {
					System.out.println("Thread 2 Value " + SCOPED_VALUE.get() + " in " + Thread.currentThread());
				});
			});

			scope.fork(() -> {
				// Binding 3 in Subtask 3
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
	 * C) Verschiedene Werte in verschiedenen unabhängigen Threads 
	 */
	 void differentValuesIndepententThreads() throws InterruptedException {
		
		System.out.println("C) Verschiedene Werte in verschiedenen unabhängigen Threads");
		
		
		Thread vt_1 = Thread.startVirtualThread(() ->{
			ScopedValue.where(SCOPED_VALUE, "Wert Thread 1").run(() -> {
				// In eigenem Thread mit eigenem Wert von ScopedValue ausgefuehrt.
				System.out.println("scopedValueTest() Virtual Thread 1  running in: " + Thread.currentThread());
				for(int i = 0; i< 1000; ++i){ 
					String valueinRun_1 = SCOPED_VALUE.get();
					System.out.println("Thread 1: " + valueinRun_1 + "  " + Thread.currentThread());
				}
			});
			
		});
		
		Thread vt_2 = Thread.startVirtualThread(() ->{
			ScopedValue.where(SCOPED_VALUE, "Wert Thread 2").run(() -> {
				// In eigenem Thread mit eigenem Wert von ScopedValue ausgefuehrt.
				System.out.println("scopedValueTest() Virtual Thread 2  running in: " + Thread.currentThread());
				for(int i = 0; i< 1000; ++i) { 
					String valueinThread_2 = SCOPED_VALUE.get();
					System.out.println("Thread 2: " + valueinThread_2 + "  " + Thread.currentThread());
				}
			});
			
		});
		
		
		vt_1.join();
		vt_2.join();
	}
	

}
