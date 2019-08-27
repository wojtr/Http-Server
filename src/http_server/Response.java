package http_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Response implements Runnable {
	
	private Socket socket;
	
	public Response(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		
		try {
			
			/* Parse request into request object. */
			Request request = parseRequest(socket);
		
			/* Create and Send Http Response with Home Page as Body. */
			String httpResponse = createResponse(request);
			socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
		
			/* Close Socket */
			socket.close();
		
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println(e.getMessage());
			
		}
	}
	
	/* Parse the input request from the socket.
	 * Input: socket - open socket with input stream to parse.
	 * Output: request - Request object with data from the socket's input stream.
	 *         null - Returns null if the request can not be parsed. */	
	private Request parseRequest(Socket socket) {
		
		try {
			
			/* Read the HTTP Request Line. (This is always the first line in the request.) */
			BufferedReader is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String requestLine = is.readLine();
			String[] requestLineComponents = requestLine.split(" ");
			
			/* Create request object. ([0] = Method, [1] = path, [2] = version) */
			Request request = new Request(requestLineComponents[0], requestLineComponents[1], requestLineComponents[2]);
			return request;
			
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println(e.getMessage());
			
		}
		
		return null;
		
	}
	
	private String createResponse(Request request) {
		
		try {
			
			if (request == null) {
				
				/* If the input stream was unable to be read then return a 400 error response.  */
				String httpResponse = "HTTP/1.1 404 Bad Request\\r\\n\\r\\n";
				return httpResponse;
				
			} else if (request.getPath().equals("/")) {
			
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
				
				/* Create and return Http Response with Home Page as Body. */
				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + web_page;
				return httpResponse;
			
			} else {
				
				/* Create and return Http Response redirecting to home page for every path. */
				String httpResponse = "HTTP/1.1 303 See Other\r\nLocation: http://localhost:8080/";
				return httpResponse;
			
			}
			
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println(e.getMessage());
			
		}
		
		/* If all else fails return a 400 error message.  */
		String httpResponse = "HTTP/1.1 404 Bad Request\\r\\n\\r\\n";
		return httpResponse;
		
	}
}
