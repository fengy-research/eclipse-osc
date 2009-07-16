package org.opensuse.osc.api;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import java.util.*;
import java.io.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class Result {
	public enum Status {
		OK,
		ERROR
	}
	public enum Type {
		/* An api response */
		RESPONSE,
		/* A downloadable file */
		STREAM
	}
	protected Result(Call call) {
		this.call = call;
	}
	protected Call call;
	protected Document document;
	protected Status status;
	protected Type type;
	protected InputStream stream;
	public Call getCall() {return call;}
	public Api getApi() {return call.api;}
	public Document getDocument() {return document;}
	public Status getStatus() {return status;}
	public Type getType() {return type;}

	public String query(String path) throws XPathExpressionException {
		XPathExpression expr = Api.xpath.compile(path);
		Node r = (Node) expr.evaluate(document, XPathConstants.NODE);
		return r.getNodeValue();
	}
	public ArrayList<String> queryList(String path) throws XPathExpressionException {
		XPathExpression expr = Api.xpath.compile(path);
		NodeList nodes = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
		ArrayList<String> l = new ArrayList<String>();
		for(int i = 0; i < nodes.getLength(); i++) {
			l.add(nodes.item(i).getNodeValue());
		}
		return l;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Type: " + type.toString() + "\n");
		try {
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");

			//create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(document);
			trans.transform(source, result);
			sb.append(sw.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		sb.append("end");
		return sb.toString();
	}
}

