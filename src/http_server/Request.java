package http_server;

public class Request {
	
	private String method;
	private String path;
	private String version;
	
	public Request(String method, String path, String version) {
		this.method = method;
		this.path = path;
		this.version = version;
	}
	
	public String getMethod() {
		return this.method;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
}
