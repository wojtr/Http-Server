package http_server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Response implements Runnable {
	
	private Socket socket;
	private List<String> contents;
	
	public Response(Socket socket) {
		this.socket = socket;
		this.contents = Arrays.asList(new String[] {
				"index.html",
				"images/website_logo.jpg",
				"images/home_background.jpg",
				"images/facebook_logo.jpg",
				"images/twitter_logo.jpg",
				"images/linkedin_logo.png",
				"images/instagram_logo.png"
		});
	}
	
	public void run() {
		
		try {	
			
			/* Parse request into request object. */
			Request request = parseRequest(socket);
			
			/* Revert web site paths to files if needed. */
			updatePath(request);
			
			/* Write the response to the socket's output stream. */
			writeResponse(request);
		
			/* Close Socket */
			socket.close();
			
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println("Unable to close socket.");
			
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
	
	/* Create a Response and write it to socket connection.
	 * Input: request - Request object with all the necessary information to create a response. 
	 * Output: boolean - True if response was , false otherwise. */
	private void writeResponse(Request request) {
			
		try {
			
			if (request == null) {
				
				/* If the input stream was unable to be read then return a 400 error response.  */
				String httpResponse = "HTTP/1.1 400 Bad Request\r\n\r\n";
				OutputStream os = socket.getOutputStream();
				os.write(httpResponse.getBytes("UTF-8"));
				return;
				
			} else {
				
				/*	Retrieve Page by file path. */
				File file = getFile(request.getPath());
				
				/* if file could not be found, return a 404 message. */
				if (file == null) {
					
					String httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
					OutputStream os = socket.getOutputStream();
					os.write(httpResponse.getBytes("UTF-8"));
					return;
					
				}
				
				OutputStream os = socket.getOutputStream();
				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
				os.write(httpResponse.getBytes("UTF-8"));
				Files.copy(file.toPath(), os);
				return;
				
			}		
		} catch (Exception e) {
			
			/* If function fails print message to console. */
			System.out.println("Error writing response.");
			return;
			
		}
	}
	
	/* Get the file requested.
	 * Input: path - String containing file name. (All files are stored on the base level "/".) 
	 * Output: String - file ready to be copied into the body of a HTTP message. 
	 * 		   null - Returns null if there is an error retrieving the file. */
	private File getFile(String path) {
			
		/*	Retrieve Page by file name. */
		File file = new File(path);
		
		/* if the file exists and users are authorized to access said file then return it. */
		if (file.exists() && contents.contains(path)) {
			return file;
		} else {
			System.out.println("File Not found.");
			return null;
		}
		
	}
	
	/* Reverts web site paths to files if needed.
	 * Input: request - Request object with path to requested file. */
	private void updatePath(Request request) {
		
		/* Path to index.html is "/" */
		if (request.getPath().equals("/")) {
			
			request.setPath("index.html");
			return;
			
		} else {
			
			request.setPath(request.getPath().substring(1));
			return;
			
		}
	}
}
