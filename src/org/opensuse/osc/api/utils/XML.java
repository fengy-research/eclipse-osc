package org.opensuse.osc.api.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XML {
	
	static DocumentBuilderFactory dbFactory;
	static XPathFactory pathFactory;
	static DocumentBuilder builder;
	static TransformerFactory transFactory;
	static Transformer transformer; 
	
	public Document document;
	static {
		dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true); // never forget this!
		pathFactory = XPathFactory.newInstance();
		try {
			builder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		transFactory = TransformerFactory.newInstance();
		try {
			transformer = transFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public XML (InputStream is) throws SAXException, IOException{
		document = builder.parse(is);	
	}
	
	public XML (Document node) {
		document = node;
	}
	
	public XML(String str) throws SAXException, IOException {
		document = builder.parse(new ByteArrayInputStream(str.getBytes()));
	}

	@Override
	public String toString() {
		
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		//create string from xml tree
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(document);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	public void write(OutputStream os) throws TransformerException {
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		//create string from xml tree
		StreamResult result = new StreamResult(os);
		DOMSource source = new DOMSource(document);
		transformer.transform(source, result);
	}
	
	public Object evaluate(String path, QName returnType) throws XPathExpressionException {
		XPath xpath = pathFactory.newXPath();
		return xpath.evaluate(path, document, returnType);
	}
}
