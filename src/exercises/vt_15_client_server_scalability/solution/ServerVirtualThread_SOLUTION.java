package exercises.vt_15_client_server_scalability.solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * Implements a trivial Server that waits for a while before responding.
 * 
 * Is not stable and only good for demo purposes!!!
 * 
 * It shows that scalability of Virtual Threads is much better than scalability of Platform Threads.
 * 
 * In Case of Platform Threads:
 * - Server crashes if there are more than 4000 Requests per Second (on my Platform, Mac, 4-Core)
 * 
 * In Case of Virtual Threads used:
 * - Runs stable for a single Client 
 * - Client can call up to 10.000 Requests per which are answered roughly within a Second.
 * - Server Threads sleep for a Second. Logs show, that 10.000 Virtual Threads are running simultaneously.
 * 
 * PROBLEMS
 * - Not stable in case of more than one client!!!
 */
public class ServerVirtualThread_SOLUTION {
	
	// Definiert ob Virtual Threads oder Platform Threads verwendet werden
	final static boolean USE_VIRTUAL_THREADS = false;
	
	// Verbindung zum Client
	ServerSocket serverSocket;
	
	// Zaehlt die aktuell laufenden Threads
	final static AtomicInteger runningThreads = new AtomicInteger();
	
	
	public static void main(String[] args) throws IOException {
		ServerVirtualThread_SOLUTION serverInstance = new ServerVirtualThread_SOLUTION();
	
		serverInstance.serverSocket = new ServerSocket();
	
		// Startet den Server in einem eigenen Thread
		new Thread(() ->{ serverInstance.startListenig();}).start();
		
		System.out.println("Server started, hit any key to exit");
		System.in.read();
		System.out.println("Server ending...");
		serverInstance.serverSocket.close();
	}
	
	
	void startListenig() {
			
		// zaehlt die gestartetetn Threads (Im Unterschied zu den aktuell laufenden)
		int threadCounter = 1;
		
		try {
			
			serverSocket.bind(new InetSocketAddress("localhost", 8080));
		
			while(true) {
				threadCounter++;
				// System.out.println(threadCounter);
	
				// Auf Verbindungsanfrage durch den Client warten
				Socket clientSocket = serverSocket.accept();
	
				// Dies ist die entscheidende Stelle um zwischen Virtual und Platform Thread zu
				// unterscheiden
				if (USE_VIRTUAL_THREADS) {
					Thread.ofVirtual().start(new ClientHandler(clientSocket, threadCounter));
				} else {
					Thread.ofPlatform().start(new ClientHandler(clientSocket, threadCounter));;
				}
			}
		}
		catch(IOException e) {
			throw new Error(e);
		}
	
	}
	
	final class ClientHandler implements Runnable{
		
		ClientHandler(Socket clientSocket, int threadCounter){
			this.clientSocket = clientSocket;
			this.threadCounter = threadCounter;
		}
		
		// Verbindung zum client
		Socket clientSocket;
		
		// Anzahl gestarteter Threads
		int threadCounter;
		
		public void run(){
		
			System.out.println("Virtual Thread start " + threadCounter + " currentlyRunningThreads " + runningThreads.incrementAndGet());
			String answer = "Hallo " + threadCounter;
			
			try {
				
				// Daten vom Cleint lesen
				
				BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
				
				String clientMessage = in.readLine();
				System.out.println("Server got Client message: " + clientMessage);
				
				System.out.println("Server Thread sending answer");
				
				// Simulate blocking IO 3 Seconds
				// WICHTIG um das Problem der Skalierbarkeit zu zeigen.
				// Ohne diese Wartezeit  beenden sich die Threads schnell und es kommt nicht zum Engpass!
				Thread.sleep(3000);
				
			
				PrintWriter out =
		                new PrintWriter(clientSocket.getOutputStream(), true);
				out.println(answer);
				
				// Verbindung zum Client schliessen
				clientSocket.close();
				
				
				System.out.println("Virtual Thread end " + threadCounter + " running threads " + runningThreads.get());
				runningThreads.decrementAndGet();
			
			} catch (IOException e) {
					e.printStackTrace();
					throw new Error(e);
					
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new Error(e);
			}
		};		
	}
}
