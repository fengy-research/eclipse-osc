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
	protected OutputStream body_stream = null;

	protected boolean hasData;
	public Api getApi() { return api; }
	public String getMethod() { return method; }
	public String getPath() { return path; }
	public OutputStream getBodyStream () { return body_stream;}

	protected Result result = null;
	public Result getResult() {
		return result;
	}

	protected HttpURLConnection openConnection() throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(api.host);
		if(path.charAt(0) != '/') {
			sb.append('/');
		}
		sb.append(path);
		/** @TODO: Add the parameters **/
		System.out.println(sb.toString());
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(method.toUpperCase());
		if(method.toUpperCase().equals("POST")
		|| method.toUpperCase().equals("PUT")) {
			connection.setDoOutput(true);
			body_stream = connection.getOutputStream();
			body_stream.close();
		}
		return connection;
	}

}
