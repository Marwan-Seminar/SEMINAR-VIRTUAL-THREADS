package exercises.vt_05_negative_performance_snc.solution;

import java.util.LinkedList;
import java.util.List;


/*
 * VERALTET!!! PINNING IN SYNCHRONIZED NICHT MHER AKTUELL
 * 
 * Zeige, dass es Szenarien gibt, die durch den Einsatz von Virtual Threads langsamer werden.
 * 
 * Im vorliegenden Fall wird dafür folgendes ausgenutzt.
 * 
 * 1. Ein Virtual Thread in einem synchronized Block gibt bei blockierenden Aufrufen seinen Carrier-Thread nicht frei.
 * 2. Der Pool der Carrier Threads ist im Defaul Fall nur so groß wie die Anzahl der logischen CPUs des Rechners auf dem das Programm laeuft.
 * 
 * Konkret passiert hier folgendes (C logische CPUs):
 * 
 * 16 Jobs die je 1 Sekunde schlafen. Die Jobs sind (sinnlos) auf jeweils unterschiedliche Objekte snychronisiert.
 * 
 * Mit 16 Platform-Threads: 1 Sekunde Laufzeit, da die blockierten Threads zwar existieren und Memory kosten, aber die CPU freigeben.
 * Mit 16 Virtual-Threds: 2 Sekunden Laufzeit, weil aufgrund der Synchronisation nur 8 V-T gleichzeitig die synchronized Blöcke ausführen
 * 
 * Bei 8 Jobs sind beide Ansaetze gleichschnell.
 *
*/
public class NegativePerformance_SOLUTION {

	
	final static int NR_OF_PROCESSORS = Runtime.getRuntime().availableProcessors(); // 8
	final static int NR_OF_JOBS =  2 * NR_OF_PROCESSORS; // 16;
	
	
	public static void main(String[] args) {
		
		
		NegativePerformance_SOLUTION instance = new NegativePerformance_SOLUTION();
		
		//Platform Threads werden gestartet und laufen effizient
		instance.runJobsPlatformThreads();
		
		//iturelle Threads werden gestartet und behindern sich gegenseitig
		instance.runJobsVirtualThreads();
		
		
	}
	
	/*
	 * Platform Threads werden gestartet und laufen effizient
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
	 * Viturelle Threads werden gestartet und behindern sich gegenseitig.
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


/*
 * Klasse für den Job der von den Threads ausgefuerht wird
 */
class Job{
	
	// TODO sinnlose Synchronisation, die Virtual Threads benachteiligt
	synchronized void calculate() {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}
	
}


