package org.opensuse.osc.api;
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.xml.sax.*;

public class Api {
	public Api(String newhost) {
		host = newhost;
		try {
			dombuilder = domfactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	protected String username;
	protected String password;
	protected String host;

	public void setAuth(String username, String password) {
		this.username = username;
		this.password = password;
	}


	public void issue(Call call) throws
		MalformedURLException, IOException, SAXException {
		if(username != null) {
			java.net.Authenticator.setDefault(new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication () {
					if(password != null)
						return new PasswordAuthentication (username, password.toCharArray());
					else
						return new PasswordAuthentication (username, "".toCharArray());
				}
			});
		}

		call.result = new Result(call);

		URLConnection connection;

		call.result.status = Result.Status.ERROR;
		connection = call.openConnection();

		call.result.stream = connection.getInputStream();
		String content_type = connection.getContentType();
		System.out.println(content_type);
		if(content_type.matches("(application|text)/xml.*")) {
			call.result.document = dombuilder.parse(call.result.stream);
			call.result.type = Result.Type.RESPONSE;
		} else {
			call.result.type = Result.Type.STREAM;
		}
		call.result.status = Result.Status.OK;
	}
	private static DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
	private static XPathFactory xpathfactory = XPathFactory.newInstance();
	protected static XPath xpath = xpathfactory.newXPath();
	private DocumentBuilder dombuilder;
	public Call newCall(String method, String path) {
		return new Call(this, method, path);
	}
}
