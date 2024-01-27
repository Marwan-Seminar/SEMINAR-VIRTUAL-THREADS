package exercises.vt_01_api.solution;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
 * Starte Virtual-Threads
 * 
 * 1. Starte einen einzelnen Virtual-Thread.
 * 	- Nutze eine statische Methode der Klasse Thread, um den Virtual-Thread zu starten
 * 	- Schreibe aus dem Virtual-Thread heraus dessen ID auf die Konsole
 * 	- Warte auf das Ende des Virtual-Threads
 * 
 * 2. Nutze einen Virtual-Thread mithilfe eines ExecutorService
 * 	- Verwende Executors.newVirtualThreadPerTaskExecutor(), um einen geeigneten Executor Service zu erhalten
 * 	- Fuehre einen Virtual-Thread aus, schreibe dessen String Repraesentation auf die Konsole
 * 	- Warte auf das Ende der Ausfuehrung, nutze dafür ein Future Objekt
 * 
 * 
 */
public class StartVirtualThreads_SOLUTION {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		StartVirtualThreads_SOLUTION instance = new StartVirtualThreads_SOLUTION();
		
		// 1. Einzelnen Virtual-Thread starten
		instance.startSingleVirtualThread();
		
		// 2. ExecutService fuer Ausfuehrung in einem Virtual-Thread nutzen 
		instance.executeInVirtualThread();
	}
	
	/*
	 * Startet einen Virtual-Thread
	 */
	void startSingleVirtualThread() throws InterruptedException{
		System.out.println("StartVirtualThreads_SOLUTION.startSingleVirtualThread()");
		// Starten
		Thread  virtualThread =  Thread.startVirtualThread(() -> {
			System.out.println("Virtual Thread: " + Thread.currentThread());
		});
		
		// Warten
		virtualThread.join();
	}
	
	/*
	 * Nutzt einen ExecutorService um eine Lamda Expression in einem Vitual-Thread auszufuehren
	 */
	void executeInVirtualThread() throws InterruptedException, ExecutionException {
		System.out.println("StartVirtualThreads_SOLUTION.executeInVirtualThread()");
		// Pool Interface: ExecutorService
		ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

		// Starten
		Future<?> threadResult = virtualThreadExecutor.submit(() -> {
			System.out.println("Virtual Thread via Executor " + Thread.currentThread());
		});

		// Warten
		threadResult.get();
	}

}
