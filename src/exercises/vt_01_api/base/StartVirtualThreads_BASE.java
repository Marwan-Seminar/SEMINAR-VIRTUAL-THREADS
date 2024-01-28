package exercises.vt_01_api.base;

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
public class StartVirtualThreads_BASE {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		StartVirtualThreads_BASE instance = new StartVirtualThreads_BASE();

		// 1. Einzelnen Virtual-Thread starten
		instance.startSingleVirtualThread();

		// 2. ExecutorService fuer Ausfuehrung in einem Virtual-Thread nutzen
		instance.executeInVirtualThread();
		
	}

	/*
	 * 1 Starte einen Virtual-Thread
	 */
	void startSingleVirtualThread() throws InterruptedException {
		System.out.println("StartVirtualThreads_BASE.startSingleVirtualThread()");
		// TODO 1: Starten: Thread.startVirtualThread();
		
		// TODO 2: Thread ID finden: Thread.currentThread().threadId()

		// TODO 3: Warten Thread.join()
	
	}

	/*
	 * 2 Nutze einen ExecutorService um eine Lambda Expression in einem Vitual-Thread auszufuehren
	 */
	void executeInVirtualThread() throws InterruptedException, ExecutionException {
		System.out.println("StartVirtualThreads_BASE.executeInVirtualThread()");
		
		// TODO 1:  Pool Interface fuer Virtual-Thread Erzeugung: Executors.newVirtualThreadPerTaskExecutor()
		

		// TODO 2: Code in neuem Virtual-Thread ausfuehren: ExecutorService.submitt(...)
		

		// TODO 3: Warten Future<?>.get
	}

}
