package exercises.vt_05_negative_performance_snc.base;

import java.util.LinkedList;
import java.util.List;



/*
 * Zeige, dass es Szenarien gibt, die durch den Einsatz von Virtual Threads langsamer werden.
 * 
 * HINWEIS: Im vorliegenden Fall kann dafür Folgendes ausgenutzt werden:
 * 
 * 1. Ein Virtual Thread in einem synchronized Block gibt bei blockierenden Aufrufen seinen Carrier-Thread nicht frei
 * 2. Der Pool der Carrier Threads ist im Defaul Fall nur so groß, wie die Anzahl der logischen CPUs des Rechners auf dem das Programm laeuft
 * 
 * 
 * Der vorliegende Rahmen startet sowohl Virtual Threads als auch Platform Threads, 
 * laesst sie einen Job ausfuehren und misst die Zeit.
 * 
 * Die Anzahl der Threads ist jeweils das doppelte der Anzahl der logischen CPUs im Rechner.
 * 
 * Aufgabe: Baue den Job in der Klasse Job so, dass die Virtual Thread Variante wesentlich langsamer ist als die Platform Thread Variante.
 *
*/
public class NegativePerformance_BASE {

	
	final static int NR_OF_PROCESSORS = Runtime.getRuntime().availableProcessors(); // z.B. 8
	final static int NR_OF_JOBS =  2 * NR_OF_PROCESSORS; //z.B. 16;
	
	
	public static void main(String[] args) {
		
		
		NegativePerformance_BASE instance = new NegativePerformance_BASE();
		
		// Platform Threads werden gestartet 
		instance.runJobsPlatformThreads();
		
		// Virtural Threads werden gestartet 
		instance.runJobsVirtualThreads();
		
		
	}
	
	
	/*
	 * Klasse für den Job der von den Threads ausgefuerht wird
	 */
	class Job{
		
		// TODO nutze Synchronisation und einen blockierenden Aufruf (z.B. synchronized und sleep())
		void calculate() {
				
			//System.out.println("Job running");
			
		}
		
	}

	
	/*
	 * Platform Threads werden gestartet
	 */
	void runJobsPlatformThreads() {
		
		System.out.println("runJobsPlatformThread: " + NR_OF_JOBS + " Jobs ");
		
		long start = System.currentTimeMillis();
		
		List<Thread> threadList = new LinkedList<Thread>();
		
		for(int jobCnt = 0; jobCnt < NR_OF_JOBS; jobCnt++) {
			
			
			Thread jobThread = new Thread(() -> {
				new Job().calculate();
			});
			
			jobThread.start();
			
			threadList.add(jobThread);
			
		}
		
		for (Thread jobThread : threadList) {
			try {
				jobThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new Error(e);
			}
		}
		
		System.out.println("Runtime Platform Threads: " +(System.currentTimeMillis() - start) + " Milliseconds");
	}
	
	/*
	 * Virtual Threads werden gestartet.
	 */
	void runJobsVirtualThreads() {
		
		System.out.println("runJobsVirtualThreads: " + NR_OF_JOBS + " Jobs ");
		
		long start = System.currentTimeMillis();
		
		List<Thread> threadList = new LinkedList<Thread>();
		
		for(int jobCnt = 0; jobCnt < NR_OF_JOBS; jobCnt++) {
			
			
			Thread jobThread = Thread.startVirtualThread(() -> {
				new Job().calculate();
			});
			
			
			threadList.add(jobThread);
			
		}
		
		for (Thread jobThread : threadList) {
			try {
				jobThread.join();
			} catch (InterruptedException e) {
				throw new Error(e);
			}
		}
		
		System.out.println("Runtime Virtual Threads: " +(System.currentTimeMillis() - start) + " Milliseconds");
	}


}



