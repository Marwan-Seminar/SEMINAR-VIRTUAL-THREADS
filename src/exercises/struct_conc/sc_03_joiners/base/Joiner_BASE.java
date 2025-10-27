package exercises.struct_conc.sc_03_joiners.base;

import java.util.OptionalInt;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;


/*
 * Joiner definieren das Verhalten eines StructuredTaskScope.
 * 
 * Es gibt merhere vordefinierte Joiner, die z.B. das erste verfuegbare 
 * Ergebnis zurueckliefern und die verbleibenden Tasks im Scope abbrechen.
 * 
 * Man kann aber auch selbst defienerte Joiner schreiben.
 * 
 * Aufgabe
 * 
 * A) 	Baue einen Scope, der drei unterschiedlich lang laufende Subtasks umfasst.
 * 	
 * 	1) 	Nutze einen Joiner, der dazu fuehrt, dass das Ergebnis derjenigen Subtask, die 
 * 		als erste fertig ist, zurueckgegeben wird. 
 * 
 * 	2) 	Stelle sicher, dass sich die anderen Tasks beenden.
 * 
 * 
 * 
 * B) 	
 * 	
 * 	1)	Baue einen eigenen Joiner, indem du das Interface StructuredTaskScope.Joiner<T, R> implementierst.
 * 		Dieser Joiner soll das Maximum der Ergebnisse der Subtasks berechnen.
 * 		(Der Einfachheit halber beschraenke Dich auf Subtasks, die Integer zurueckgeben).
 *  
 *  2)	Baue einen Scope, der mehrere Tasks hat, die Integer Werte zurueckgeben.
 *  	Benutze darin den Joiner aus 1) und zeige, dass er das Maximum der Rueckgabwerte findet.
 *  
 */
public class Joiner_BASE {
	
	public static void main(String[] args) throws InterruptedException {
		
		
		System.out.println("Joiner_SOLUTION");
		
		Joiner_BASE instance = new Joiner_BASE();
		
		// A 1) und 2) 
		instance.scopeAwaitAny();
		
		// B)
		//instance.scopeMax();
	}
	
	/*
	 * Diese Methode definiert einen Scope, der das Ergebnis der ersten erfolgreichen Subtsaks
	 * zurueckgibt und die anderen Subtasks abbricht
	 * 
	 * Der Joinder dafür ist ueber folgenden Aufruf erhaeltlich:
	 * 
	 * Joiner.<String>anySuccessfulResultOrThrow()
	 * 
	 */
	void scopeAwaitAny() throws InterruptedException{
		
		
		// TODO den passenden Joiner auswaehlen. Z.B.: anySuccessfulResultOrThrow
		// Die Dokumentation befindet sich hier:
		// https://docs.oracle.com/en/java/javase/25/core/structured-concurrency.html#GUID-97F95BF4-A1E2-40A3-8439-6BB4E3D5C422
		// Abschnitt: Table 14-2 Static Methods for Policies
		
		// TODO den Scope eroeffnen: // try(var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulResultOrThrow())){ //... }
		
			// TODO: Task 1 starten, und eine Zeit lang arbeiten lassen. Benutze z.B. cpuIntensiveCall(1); 
	
			// TODO scope.fork(() -> {
				// TODO for(int i = 0; i < 3; ++i) {
					
					// TODO work some time
					//cpuIntensiveCall(1);
					
					// TODO: von Zeit zu Zeit schauen, ob der Subtasks unterbrochen wurde!!!
					//check interrupted status
					/*
					if(Thread.currentThread().isInterrupted()) {
						System.out.println("Interruped Task 1, returning...");
						return "Interruped Task 1";
					}
					*/
					
				//}
				//System.out.println("Task 1 returning successful");
				//return "Success Task 1";
			//});

			
			// TODO WEitere Subtasks starten, anlaog zu obiger: Workflow jeweils: Im Wechsel arbeiten und Interrupted Status checken
		
			// Task 2: Working 5 Seconds
			//TODO : //scope.fork(() -> { });
			
			
			// Task 3: Working  6 Seconds
			//scope.fork(() -> {});
			
			// TODO: Auf Scope Ende warten, mit join()
			//String resultOfScope = scope.join();
			
			//System.out.println("Scope result is: " + resultOfScope);
		
		
	}
	
	/*
	 * B) 
	 * 
	 * Diese Methode definiert einen Scope, der drei Subtasks forkt,
	 * die ein jeweils ein int zurueckgeben.
	 * 
	 * Er benutzt einen selbst geschriebenen Joiner. Dieser findet das
	 * Maximum der Rueckgabewerte und gibt es ueber join() zurueck.
	 * 
	 */
	void scopeMax() throws InterruptedException {
		
		try(var scope = StructuredTaskScope.open(new MaxJoiner())){
			
			scope.fork(()->{
					return 7;
			});
			
			scope.fork(()->{
				return 9;
			});
			

			scope.fork(()->{
				sleep(3000);
				return 12;
			});
			
			Integer result =scope.join();
			
			System.out.println("MaxJoiner yields result: " + result);
		}
	}
	
	/*
	 * B) 
	 * 
	 * Diese Klasse impelementiert einen Joiner, der das Maximum der Ergebnisse
	 * aller Subtasks zurückliefert.
	 * 
	 * (Dieses Beispiel funktioniert der Einfachheit halber nur fuer Integer Werte)
	 */
	static class MaxJoiner implements Joiner<Integer, Integer> {

		private final Queue<Integer> subtaskResults = new ConcurrentLinkedQueue<>();

		// TODO
		@Override
		public boolean onComplete(Subtask<? extends Integer> subtask) {
			// TODO 
			// Diese Methode wird fuer jede Subtask aufgerufen, sobald sie beendet ist
			// Hierin auf die Ergebnisse der Subrtasks zugreifen, und diese einsammeln
			// z.B. folgendermassen:
			/*	
			if (subtask.state() == Subtask.State.SUCCESS) {
				System.out.println("Joiner received Success Result: " + subtask.get());
				subtaskResults.add(subtask.get());
			}
			*/
			
			// return false bedeutet, dass der Scope nicht abgebrochen wird.
			return false;
		}

		/*
		 * TODO
		 */
		@Override
		public Integer result() {
			
			// Hier die Ergebnisse der Subtasks zusammenfassen: z.B. 
			OptionalInt resultInt = 
			subtaskResults.stream().
				mapToInt(i->i).
				max();
			return resultInt.getAsInt();
		}

		
		/*
		 * Diese Methode ist fuer die Aufgabe nicht relevant,
		 * aber zu Debug-Zwecken eventuell interessanT
		 */
		@Override
		public boolean onFork(Subtask<? extends Integer> subtask) {
			System.out.println("onFork called for Subtask: " + subtask);
			return Joiner.super.onFork(subtask);
		}
		
	}
	
	
	///////////// HELPER //////////
	
	/*
	 * Diese Methode arbeitet ca. seconds Skunden lang auf der CPU
	 */
	static void cpuIntensiveCall(int seconds) {
		long start = System.currentTimeMillis();
		while(true){
			long sq = 0;
			for(long i = 0; i < Integer.MAX_VALUE; ++ i) {
				sq = i*i;
			}
			if((System.currentTimeMillis() - start) >= (seconds*1000)) {
				return;
			}

		}
	}
	
	/*
	 * Diese Methode legt den aufrufenden Thread 
	 * ca. millis Millisekunden lang schlafen.
	 */
	static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Error(e);
		}
	}

}
