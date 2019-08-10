package http_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
	
	public static void main(String[] args) throws Exception {
		
		/* Create server a local port 8080. (http://localhost:8080/) */
		final ServerSocket server = new ServerSocket(8080);
		System.out.println("Listening for connection on port 8080...");
		
		while (true) {
			
			/* Listen for client connection. */
			try (final Socket socket = server.accept()) {
				
				/*	Retrieve Home Page. */
				File index = new File("index.html");
				
				/* Read Home Page into a String. */
				BufferedReader reader = new BufferedReader(new FileReader(index));
				String line = reader.readLine();
				String web_page = "";
				while (line != null) {
					web_page += line;
					line = reader.readLine();
				}
				reader.close();
				
				/* Create and Send  Http Response with Home Page as Body. */
				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + web_page;
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
				
			}
		}
	}

}
