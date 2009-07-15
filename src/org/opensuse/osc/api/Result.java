package org.opensuse.osc;
import org.w3c.dom.*;
import javax.xml.xpath.*;
import java.util.*;

class Result {
	public enum Status {
		OK,
		ERROR
	}
	protected Result(Call call) {
		this.call = call;
	}
	protected Call call;
	protected Document document;
	protected Status status;
	public Call getCall() {return call;}
	public Api getApi() {return call.api;}
	public Document getDocument() {return document;}
	public Status getStatus() {return status;}
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
}

