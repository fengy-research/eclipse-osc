package org.opensuse.osc.api;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;

public class Api {
	

	public Api(String url) {
		this.url = url;
	
	}
	
	public Api() {}
	protected String username;
	protected String password;
	protected String url;
	protected Host host;
	
	{
		 host = new Host(this);

	}

	public String getUsername() {
		return username;
	}
	public void setURL(String url) {
		this.url = url;
	}
	public Host getHost() {
		return host;
	}
	public void login(String username, String password) throws OSCException {
		this.username = username;
		this.password = password;
		host.refresh();
	}

	public void issue(Call call) throws OSCException {
		if(username != null) {
			java.net.Authenticator.setDefault(new Authenticator() {
				@Override
				public PasswordAuthentication getPasswordAuthentication () {
					if(password != null)
						return new PasswordAuthentication (username, password.toCharArray());
					else
						return new PasswordAuthentication (username, "".toCharArray());
				}
			});
		}

		call.result = new Result(call);

		HttpURLConnection connection;

		call.result.status = Result.Status.ERROR;
		
		try {
			connection = call.openConnection();
		} catch (IOException e) {
			throw new OSCException(e);
		}
		
		try {
			String content_type = connection.getContentType();
			System.out.println(content_type);

			if(content_type.matches("(application|text)/xml.*")) {
				call.result.type = Result.Type.RESPONSE;
			} else {
				call.result.type = Result.Type.STREAM;
			}
			call.result.setStream(connection.getInputStream());
			call.result.status = Result.Status.OK;
			System.out.println(call.getResult().toString());
		} catch(OSCException e) {
			throw e;
		} catch (IOException e) {
			InputStream error_stream = connection.getErrorStream();
			if(error_stream != null) {
				StringBuffer sb = new StringBuffer();
				BufferedReader bi = new BufferedReader(new InputStreamReader(error_stream));
				String line ;
				try {
					while ((line = bi.readLine())!= null) {
						sb.append(line);
					}
				} catch (IOException e1) {
					throw new OSCException(e1);
				}
				throw new OSCException(sb.toString());
			} else {
				throw new OSCException(e);
			}
		} 

	}

	public Result issue(String method, String path) throws OSCException {
		Call call = new Call(this, method, path);
		issue(call);
		return call.getResult();
	}
	
}
