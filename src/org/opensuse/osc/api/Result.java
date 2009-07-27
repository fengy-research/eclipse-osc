package org.opensuse.osc.api;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.opensuse.osc.api.utils.XML;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	private XML xml;
	protected Status status;
	protected Type type;
	protected InputStream stream;
	public Call getCall() {return call;}
	public Api getApi() {return call.api;}
	public void setStream(InputStream stream) throws OSCException {
		this.stream = stream;
		if(type == Type.RESPONSE) 
			try {
				xml = new XML(stream);
			} catch (SAXException e) {
				throw new OSCException(e);
			} catch (IOException e) {
				throw new OSCException(e);
			}
		else xml = null;
	}
	public Status getStatus() {return status;}
	public Type getType() {return type;}

	public String query(String path) throws OSCException {

		try {
			Node r = (Node) xml.evaluate(path, XPathConstants.NODE);
			if(r != null)
			return r.getNodeValue();
			else return null;
		} catch (XPathExpressionException e) {
			throw new OSCException(e);
		}
		
	}

	/**
	 * 
	 * Returns an empty list if there is not such xpath
	 * @param path
	 * @return
	 * @throws OSCException
	 */
	public ArrayList<String> queryList(String path) throws OSCException {

		try {
			NodeList nodes;
			nodes = (NodeList) xml.evaluate(path, XPathConstants.NODESET);
			if(nodes == null) return new ArrayList<String>();
			ArrayList<String> l = new ArrayList<String>();
			for(int i = 0; i < nodes.getLength(); i++) {
				l.add(nodes.item(i).getNodeValue());
			}
			return l;
		} catch (XPathExpressionException e) {
			throw new OSCException(e);
		}
		
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("--- Result Type: " + type.toString() + "--\n");
		if(xml != null)
		sb.append(xml.toString());
		else
		sb.append("BINARY");
		sb.append("--- Result End -- ");
		return sb.toString();
	}
}

