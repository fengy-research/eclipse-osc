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
	private String title;
	private String description;
	private String revision;
	public String getRevision() { return revision; }
	public String getTitle() { return title; }
	public String getDescription() { return description; }

	public String getUsername() {
		return username;
	}

	public void login(String username, String password) throws Exception {
		this.username = username;
		this.password = password;

		Call call = newCall("get", "/about");
		issue(call);
		title = call.getResult().query("/about/title/text()");
		description = call.getResult().query("/about/description/text()");
		revision = call.getResult().query("/about/revision/text()");
	}

	public void issue(Call call) throws OSCException,
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

		HttpURLConnection connection;

		call.result.status = Result.Status.ERROR;
		connection = call.openConnection();

		try {
			call.result.stream = connection.getInputStream();
		} catch (IOException e) {
			InputStream error_stream = connection.getErrorStream();
			if(error_stream != null) {
				StringBuffer sb = new StringBuffer();
				BufferedReader bi = new BufferedReader(new InputStreamReader(error_stream));
				String line ;
				while ((line = bi.readLine())!= null) {
					sb.append(line);
				}
				throw new OSCException(sb.toString());
			}
		}
		String content_type = connection.getContentType();
		System.out.println(content_type);
		if(content_type.matches("(application|text)/xml.*")) {
			call.result.document = dombuilder.parse(call.result.stream);
			call.result.type = Result.Type.RESPONSE;
		} else {
			call.result.type = Result.Type.STREAM;
		}
		call.result.status = Result.Status.OK;
		System.out.println(call.getResult().toString());
	}
	private static DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
	private static XPathFactory xpathfactory = XPathFactory.newInstance();
	protected static XPath xpath = xpathfactory.newXPath();
	private DocumentBuilder dombuilder;
	public Call newCall(String method, String path) {
		return new Call(this, method, path);
	}
	public Package refPackage(String proj, String pac) {
		return new Package(this, proj, pac);
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Title: " + title);
		sb.append("Desc: " + description);
		sb.append("Revision: " + revision);
		return sb.toString();
	}
}
