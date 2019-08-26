package http_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.Socket;

public class Response implements Runnable {
	
	private Socket socket;
	
	public Response(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		try {
			
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
		
			/* Close Socket */
			socket.close();
		
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println(e.getMessage());
			
		}
	}
}
