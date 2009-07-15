package org.opensuse.osc;
import java.net.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.xml.sax.*;

class Api {
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

	public String makeURL(Call call) {
		StringBuffer sb = new StringBuffer();
		sb.append(host);
		if(call.command.charAt(0) != '/') {
			sb.append('/');
		}
		sb.append(call.command);
		/** @TODO: Add the parameters **/
		return sb.toString();
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

		String urlstring = makeURL(call);
		System.out.println(urlstring);
		call.result = new Result(call);

		URL url;
		URLConnection connection;

		call.result.status = Result.Status.ERROR;
		url = new URL(urlstring);
		connection = url.openConnection();
		call.result.document = dombuilder.parse(connection.getInputStream());
		call.result.status = Result.Status.OK;
	}
	private static DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
	private static XPathFactory xpathfactory = XPathFactory.newInstance();
	protected static XPath xpath = xpathfactory.newXPath();
	private DocumentBuilder dombuilder;
	public Call newCall(String command) {
		return new Call(this, command);
	}
}
