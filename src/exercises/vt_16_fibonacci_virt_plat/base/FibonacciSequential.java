// Copyright Marwan Abu-Khalil 2012

package exercises.vt_16_fibonacci_virt_plat.base;


/*
 * Base for exercise Fibonacci Parallelization with Threads 
 * 
 * Dieser Algorithmus implementiert eine rekursive Berechnung einer Fibonacci Zahl.
 * 
 * Er lasst sich auf sehr einfache Weise parallelisieren, indem jeder Rekursionszwig in 
 * einem eignen Thread ausgefuert wird.
 * 
 * Ohne weitere Massnahmen ist dier Ansatz nicht erfolgreich, weil er zu viele Threads 
 * erzeugt.
 * 
 * Begrenzt man jedoch die Anzahl der Threads, indem man ab einer gewissen Tiefe im Baum sequentiell weiter macht, 
 * so ist der Ansatz erfolgreich. 
 * 
 * Man kann hier z.B. folgende Beobachtungen zu Threads und deren Kosten machen:
 * 
 * Scenario 1: Eine Naive Parallelisierung mit Platform Threads fürht zu einem Crash durch zu viel MEmory Bedarf
 * 
 * Scenario 2: Eine naive Parallelisierung durch Virutal Threads ist stabil aber langsam
 * 
 * Scenario 3: Eine PArallelisierung mit Threshold, wie oben beschrieben, ist effizient.
 * 
 * Implemntieren Sie diese drei Scenarien!
 * 
 * Expected values on my machine, macbooc M4, 14 Cores
 * Fibo 50 sequential: 20365011074 time(ms): 18958
 *
 */
public class FibonacciSequential {

	public static void main(String[] args) throws InterruptedException{
		FibonacciSequential instance = new FibonacciSequential();
		
		
		int arg = 50; //48 49 // 50; 
		
		instance.testFiboSequential(arg);
		
	}
	
	 
	
	void testFiboSequential(int arg ){
		
		System.out.println("SequentialFibonacci stared with Argument: " + arg);
		long start = System.currentTimeMillis();
		long result = new SequentialFibonacci().fibo(arg);
		long end = System.currentTimeMillis();
		System.out.println("SequentialFibonacci:  Argument: " + arg + " Result: " + result +  " time(ms): " + (end - start));
			
	}
	
}

/*
 * The sequential recursive Fibonacci Algorithm.
 */
class SequentialFibonacci{
	
	long fibo(long n){
		if(n<=1){
			return 1;
		}else{
			// TODO: Introduce Threads here, to execute the recursive calls in parallel. 
			return fibo(n-1) + fibo(n-2);
			
			// TODO: Wait for Threads to return
			
			// TODO: Be aware: This can create so many threads, that your program crashes. Find a solution for that!
		}
	}
}