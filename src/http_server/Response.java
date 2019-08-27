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
			System.out.println("Bad Request Recieved.");
			return null;
			
		}		
	}
	
	/* Create a Response to return to socket connection.
	 * Input: request - Request object with all the necessary information to create a response. 
	 * Output: String - Contains the correct HTTP response based upon the data in the Request object. */
	private String createResponse(Request request) {
			
			if (request == null) {
				
				/* If the input stream was unable to be read then return a 400 error response.  */
				String httpResponse = "HTTP/1.1 400 Bad Request\r\n\r\n";
				return httpResponse;
				
			} else if (request.getPath().equals("/")) {
				
				/* Get the requested web page. */
				String webPage = getWebPage("index.html");
				
				/* Return system error if web page cannot be found. */
				if (webPage == null) {
					
					/* If the server fails to get the web page return a 500 error message.  */
					String httpResponse = "HTTP/1.1 500 Internal Server Error\r\n\r\n";
					return httpResponse;
					
				}
				
				/* Create and return Http Response with Home Page as Body. */
				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + webPage;
				return httpResponse;
			
			} else {
				
				/* Create and return Http Response redirecting to home page for every path. */
				String httpResponse = "HTTP/1.1 404 Not Found\r\n";
				return httpResponse;
			
			}		
	}
	
	/* Get the web page requested.
	 * Input: file - String containing file name. (All files are stored on the base level "/".) 
	 * Output: String - Full web page ready to be sent in the body of a HTTP message. */
	private String getWebPage(String file) {
		
		try {
			
			/*	Retrieve Home Page. */
			File index = new File(file);	
	
			/* Read Home Page into a String. */
			BufferedReader reader = new BufferedReader(new FileReader(index));
			String line = reader.readLine();
			String webPage = "";
			while (line != null) {
				webPage += line;
				line = reader.readLine();
			}
			reader.close();
		
			return webPage;
			
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println("Unable to get requested web page.");
			return null;
			
		}
	}
}
