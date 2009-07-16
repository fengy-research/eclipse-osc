package org.opensuse.osc.api;
import java.io.*;
import java.net.*;

class Call {
	protected Call(Api api, String method, String path) {
		this.api = api;
		this.method = method;
		this.path = path;
	}
	protected Api api;
	protected String path;
	protected String method;
	protected InputStream body_stream;

	protected boolean hasData;
	public Api getApi() { return api; }
	public String getMethod() { return method; }
	public String getPath() { return path; }
	public InputStream getBodyStream () { return body_stream;}
	public void setBodyStream (InputStream stream) {
		body_stream = stream;
	}

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
		URL url = new URL(sb.toString());
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setRequestMethod(method.toUpperCase());
		if(body_stream != null) {
			connection.setDoOutput(true);
			OutputStream stream = connection.getOutputStream();
			byte[] buffer = new byte[1000];
			int n;
			while( (n = body_stream.read(buffer)) != -1) {
				stream.write(buffer, 0, n);
			}
		}
		return connection;
	}

}
