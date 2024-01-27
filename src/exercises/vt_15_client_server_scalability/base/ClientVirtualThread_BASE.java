package exercises.vt_15_client_server_scalability.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
 * Ein trivialer Client der sich zum Server verbindet und einen 
 * String abholt.
 * 
 * Fuhrt viele Server-Aufrufe schnell hintereinander aus, um die Skalierbarkeit des 
 * Servers zu pruefen.
 * 
 * Lauft auf meinem Rechner nur stabil bis ca. 10.000 Calls.
 * Darueber "too many files" Exception, weil zu viele Sockets verwendet werden.
 * 
 * Das genuegt aber, um die bessere Skalierbarkeit der Virtual Threads gegenueber den
 * Platform Threads im Server zu zeigen. 
 * 
 */
public class ClientVirtualThread_BASE {
	
	final static int SERVER_CALL_COUNT = 10_000;	

	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		ClientVirtualThread_BASE clientInstance = new ClientVirtualThread_BASE();
		
		// Server Konversation starten
		clientInstance.setServerUnderLoadReceiveBuffer();
		
		long runtime = System.currentTimeMillis() - start;
		System.out.println("Client received: " + SERVER_CALL_COUNT + " Responses in " + runtime + " Milliseconds " );
	}
	
	
	/*
	 * Startet zwei Threads, einen der sich zum Server verbindet und Daten dorthin schickt, 
	 * und einen anderen, der vom Server Daten entgegennimmt.
	 */
	void setServerUnderLoadReceiveBuffer(){
		
	
		final BlockingQueue<Socket> receiveQueue = new LinkedBlockingQueue<Socket>(SERVER_CALL_COUNT);
	
		// Start requesting Thread
		Thread requestThread = new Thread(()->{
			
			for(int count = 0; count < SERVER_CALL_COUNT; ++ count) {
				System.out.println("Client Calling Server count:"  + count);
				
				Socket socket = new Socket();
				
				try {
					
					// Socket mit dem Server verbinden
					socket.connect(new InetSocketAddress("localhost", 8080));
					
					// Auf den Socket schreiben
					PrintWriter out =
			                new PrintWriter(socket.getOutputStream(), true);
					out.println("Client-Message " + count);
					
					// Socket in interne Queue legen, damit ein anderer Thread daraus lesen kann
					receiveQueue.offer(socket);
					
				} catch (IOException e) {
					e.printStackTrace();
					throw new Error(e);
				}
				
			}
		});
		
		
		Thread receiveThread = new Thread(() -> {
			
			try {
				// eigenltich unlogisch hier eine for schleife zu nehmen, da Queue Consumer.
				for(int count = 0; count < SERVER_CALL_COUNT; ++ count) {
					
					// Socket aus der internen Queue entnehmen
					Socket socket = receiveQueue.take();
					
					// Vom Socket lesen
					BufferedReader in =
				                new BufferedReader(
				                    new InputStreamReader(socket.getInputStream()));
				      
					String answer  = in.readLine();					
					System.out.println("Client Received: " + answer);
		
					// Socket schliessen
					socket.close();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
				throw new Error (e);
			} catch (InterruptedException e) {
				e.printStackTrace();
				throw new Error (e);
			}
		
			
			
		});
		
		requestThread.start();
		receiveThread.start();
		
		try {
			receiveThread.join();
			requestThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
