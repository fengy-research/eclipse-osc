package org.opensuse.osc.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

	public Api getApi() {
		return api;
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	protected Result result = null;

	public Result getResult() {
		return result;
	}
	/**
	 * Opens a HTTP connection. Feed the put/post with empty data if the method is POST or PUT.
	 * @return
	 * @throws IOException
	 */
	protected HttpURLConnection openConnection() throws IOException {
		return openConnection(null);
	}
	/**
	 * Opens a HTTP connection. Feed the put/post data with stream if the method is POST or PUT.
	 * 
	 * FIXME The inputstream should not be converted to an outputstream in this function.
	 *       Because there is no way to report the progress from here. And because we
	 *       do not introducing any eclipse dependency in osc.api. 
	 * @param stream
	 * @return
	 * @throws IOException
	 */
	protected HttpURLConnection openConnection(InputStream stream) throws IOException {
		StringBuffer sb = new StringBuffer();
		sb.append(api.host);
		if (path.charAt(0) != '/') {
			sb.append('/');
		}
		sb.append(path);
		String url_string = sb.toString();
		System.out.printf("Method %s URL %s\n", method, url_string);
		URL url = new URL(url_string);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(30000);
		connection.setRequestMethod(method.toUpperCase());
		if("POST".equals(method.toUpperCase())
		|| "PUT".equals(method.toUpperCase())) {
			connection.setDoOutput(true);
			OutputStream os = connection.getOutputStream();
			if(stream != null) {
				BufferedInputStream bis = new BufferedInputStream(stream);
				int n;
				byte[] buffer = new byte[65536];
				while((n = bis.read(buffer))> 0) {
					os.write(buffer, 0, n);
				}
			}
			os.close();
		}
		return connection;
	}

}
