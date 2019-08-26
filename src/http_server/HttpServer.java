package http_server;

import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	
	public static void main(String[] args) throws Exception {
		
		/* Create server a local port 8080. (http://localhost:8080/) */
		final ServerSocket server = new ServerSocket(8080);
		System.out.println("Listening for connection on port 8080...");
		
		while (true) {
				
			/* Listen for client connection. */
			final Socket socket = server.accept();
						
			/* Create thread to handle response. */
			Thread t1 = new Thread(new Response(socket));
			t1.start();
			
		}
	}
}
