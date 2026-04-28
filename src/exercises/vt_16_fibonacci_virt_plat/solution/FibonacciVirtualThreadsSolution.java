// Copyright Marwan Abu-Khalil 2012

package exercises.vt_16_fibonacci_virt_plat.solution;

import java.util.concurrent.atomic.AtomicInteger;


/*
 * Virtual Thread implemantation of Fibonacci
 * 1. Sequential
 * 2. Naively with virt Threads: No Crasch
 * 3. Efficiently with Virt Thrads: Fast
 */

// These classes show how to recursively calculate the Fibonacci-Sequence
// 1. Sequentially
// 2. Naively parallelized
// 3. Parallelized with threshold
// Expected values on my machine, Mac Pro M4, 14 Cores
// Fibo 50: Sequential: 20 Sec, 
// Fibo Platform Threads Parallel with Threshiold: 2 Sec.
// Fibo Virtual Threads with Threshold 2 Sed
// Naively parallelized: Crash
public class FibonacciVirtualThreadsSolution {

	public static void main(String[] args) throws InterruptedException{
		FibonacciVirtualThreadsSolution instance = new FibonacciVirtualThreadsSolution();
		
		
		int arg = 50;// 50; // 48 50 are options
		
		//instance.testFiboSequential(arg);
		
		//instance.testFiboPlatformNaive(arg);
		
		//instance.testFiboVirtualNaive(arg);	
		
		instance.testFiboVirtualThreshold(arg);
		
		
		
	}
	

	/* 
	 * Szenario 1:
	 * Calls Sequential Fibonacci implementation.
	 */
	void testFiboSequential(int arg ){
		
		System.out.println("SequentialFibonacci stared with Argument: " + arg);
		long start = System.currentTimeMillis();
		long result = new SequentialFibonacci().fibo(arg);
		long end = System.currentTimeMillis();
		System.out.println("SequentialFibonacci:  Argument: " + arg + " Result: " + result +  " time(ms): " + (end - start));	
	}

	
	/*
	 * Szenario 2:
	 * Calls Platform-Threads based naive parallelization of Fibonacci: 
	 * Results in CRASH due to OutOfMemoryError (or runs endlessly due to Memory issues)
	 */
	void testFiboPlatformNaive(int arg) throws InterruptedException {
		System.out.println("FiboNaive started with argument: " + arg);
		FiboNaivePlatformThreads fiboNaive = new FiboNaivePlatformThreads(arg);
		fiboNaive.start();
		
		fiboNaive.join();
		System.out.println("FiboNaive: " + arg + " = " + fiboNaive.result );
		
	}


	/*
	 * Szenario 3:
	 * Naive parallelization of Fibonacci based on Virtual Threads
	 * Result: Stable but slow: That is already a significant gain over
	 * the naive platform threead parallelization!
	 */
	void testFiboVirtualNaive(int arg ) throws InterruptedException{
		
		System.out.println("FiboNaive started with argument: " + arg);
		FiboNaiveVirtual fiboNaive = new FiboNaiveVirtual(arg);
		Thread fiboNaiveThgread = Thread.startVirtualThread(fiboNaive);
		fiboNaiveThgread.join();
		System.out.println("FiboNaive: " + arg + " = " + fiboNaive.result );
	}
	// FiboThreshold: 46 = 535828592 time: 4797
	void testFiboVirtualThreshold(int arg) throws InterruptedException{
		
		System.out.println("FiboThreshold started with: Argument: " + arg); 
		FiboThresholdVirtual fiboThreshold = new FiboThresholdVirtual(arg);
		long start = System.currentTimeMillis();
		Thread fiboThread = Thread.startVirtualThread(fiboThreshold);
		
		fiboThread.join();
		long end = System.currentTimeMillis();
		System.out.println("FiboThreshold returned: Argument: " + arg + " Result: " + fiboThreshold.result +  " time(ms): "  + (end-start) );
		System.out.println("Threshold: " + FiboThresholdVirtual.threshold + " NrOFThreads: " + FiboThresholdVirtual.threadCount  );
	}
	
	
	
	
}

/*
 * Scenario 1: Sequential
 */
class SequentialFibonacci{
	
	long fibo(long n){
		if(n<=1){
			return 1;
		}else{
			return fibo(n-1) + fibo(n-2);
		}
	}
}

/*
 * Scenario 2
 * This class realizes a naive parallelization of Fibonacci, based on Platfrom-Threads.
 * It can cause out of memory errors.
 * this shows, that platform threads are expensive.
 */
class FiboNaivePlatformThreads extends Thread{
	long arg;
	long result;
	
	FiboNaivePlatformThreads(long arg){
		this.arg = arg;
	}
	
	public void run(){
		if(arg <= 1){
			result = 1;
		}else{
			// Start new Threads for recursion
			FiboNaivePlatformThreads fibo_n_1 = new FiboNaivePlatformThreads(arg - 1);
			FiboNaivePlatformThreads fibo_n_2 = new FiboNaivePlatformThreads(arg - 2);
			// Start the threads
			fibo_n_1.start();
			fibo_n_2.start();
			// Wait for the threads run() method to complete
			try {
				fibo_n_1.join();
				fibo_n_2.join();
			} catch (InterruptedException e) {
				throw new Error();
			}
			
			// use threads results
			result = fibo_n_1.result + fibo_n_2.result;
		}
	}
}

/*
 * Scenario 3:
 * This class realizes a naive parallelization of Fibonacci with Virtual Threads
 * It runs stable, but is is VERY slow (can run for Hours...)
 */
class FiboNaiveVirtual implements Runnable{
	long arg;
	long result;
	
	FiboNaiveVirtual(long arg){
		this.arg = arg;
	}
	
	public void run(){
		if(arg <= 1){
			result = 1;
		}else{
			// Start new Threads for recursion
			FiboNaiveVirtual fibo_n_1 = new FiboNaiveVirtual(arg - 1);
			FiboNaiveVirtual fibo_n_2 = new FiboNaiveVirtual(arg - 2);
			// Start the threads
			Thread thread_1 = Thread.startVirtualThread(fibo_n_1);
			Thread thread_2 =Thread.startVirtualThread(fibo_n_2);
			
			// Wait for the threads run() method to complete
			try {
				thread_1.join();
				thread_2.join();
			} catch (InterruptedException e) {
				throw new Error();
			}
			
			// use threads results
			result = fibo_n_1.result + fibo_n_2.result;
		}
	}
}

 
/*
 * Scenario 4:
 * Solution: This class realizes an efficient and stable parallelization.
 * It is based on Virtual Threads.
 * But the same success can also be achieved with platform threads! 
 * It limits the number of created threads with a threshold.
 */
class FiboThresholdVirtual implements Runnable{
	int arg;
	long result;
	
	static volatile int threshold;
	static volatile boolean thresholdInitialized = false;
	static AtomicInteger threadCount = new AtomicInteger();
	
	FiboThresholdVirtual(int arg){
		this.arg = arg;
		
		if(!thresholdInitialized){
			threshold = arg - Runtime.getRuntime().availableProcessors();
			System.out.println("theshold " + threshold + " CPU count: " + Runtime.getRuntime().availableProcessors());
			thresholdInitialized = true;
		}
	}
	
	long fiboSequential(int seqArg){
		if(seqArg <= 1){
			return 1;
		}else{
			return fiboSequential(seqArg - 1) + fiboSequential(seqArg -2);
		}
	}
	public void run(){
		threadCount.incrementAndGet();
		if(arg <= 1){
			result = 1;
		}else if( arg < threshold ){
			// Sequential calculation
			result = fiboSequential(arg);
		}
		else{
			// Parallel calculation: Start new Threads for recursion
			FiboThresholdVirtual fibo_n_1 = new FiboThresholdVirtual(arg - 1);
			FiboThresholdVirtual fibo_n_2 = new FiboThresholdVirtual(arg - 2);
			
			Thread thread_1 = Thread.startVirtualThread( fibo_n_1);
			Thread thread_2 =  Thread.startVirtualThread(fibo_n_2);
		
			
			// Wait for the threads run() method to complete
			try {
				thread_1.join();
				thread_2.join();
			} catch (InterruptedException e) {
				throw new Error();
			}
			
			// use threads results
			result = fibo_n_1.result + fibo_n_2.result;
		}
	}
}

