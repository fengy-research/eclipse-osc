package org.opensuse.osc.api;
import java.io.*;
import java.net.*;

public class Call {
	protected Call(Api api, String method, String path) {
		this.api = api;
		this.method = method;
		this.path = path;
	}
	protected Api api;
	protected String path;
	protected String method;

	protected boolean hasData;
	public Api getApi() { return api; }
	public String getMethod() { return method; }
	public String getPath() { return path; }

	protected Result result = null;
	public Result getResult() {
		return result;
	}

	protected void prepareOutputStream(OutputStream stream) {

	}

	protected HttpURLConnection openConnection() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(api.host);
		if(path.charAt(0) != '/') {
			sb.append('/');
		}
		sb.append(path);
		String url_string = sb.toString();
		System.out.printf("Method %s URL %s\n", method, url_string);
		URL url = new URL(url_string);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(method.toUpperCase());
		if(method.toUpperCase().equals("POST")
		|| method.toUpperCase().equals("PUT")) {
			connection.setDoOutput(true);
			prepareOutputStream(connection.getOutputStream());
			connection.getOutputStream().close();
		}
		return connection;
	}

}
