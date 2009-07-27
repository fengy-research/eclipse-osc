package org.opensuse.osc.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.opensuse.osc.api.utils.XML;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public abstract class XMLFile extends Element {
	protected XML xml;
	
	
	public abstract String getTemplate();
	
	public XMLFile(IFile file) {
		super(file);
	}
	public IFile getFile() {
		return (IFile) getResource();
	}
	/* Fetch info from the project info file*/
	public void refresh() throws CoreException {
		try {
			if(getFile().exists()) {
				InputStream is = ((IFile) getResource()).getContents();
				xml = new XML(is);
			} else {
				xml = new XML(getTemplate());
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void save() throws CoreException {
		ByteArrayInputStream is = new ByteArrayInputStream(xml.toString().getBytes());
		if(getFile().exists()) {
			getFile().setContents(is, true, true, null);	
		} else {
			getFile().create(is, true, null);
		}
	}
	protected String get(String path) {
		return getNode(path).getNodeValue();
	}
	protected void set(String path, String value) {
		getNode(path).setNodeValue(value);
	}
	protected List<String> getList(String path) {
		try {
			NodeList nodeList = (NodeList) xml.evaluate(path, XPathConstants.NODESET);
			ArrayList<String> rt = new ArrayList<String>();
			for(int i = 0; i < nodeList.getLength(); i++) {
				rt.add(nodeList.item(i).getNodeValue());
			}
			return rt;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
	protected Node getNode(String path) {
		try {
			return (Node) xml.evaluate(path, XPathConstants.NODE);
			
		} catch (XPathExpressionException e) {
			int at = path.lastIndexOf('/');
			String childName = path.substring(at + 1);
			try {
				org.w3c.dom.Element ele = (org.w3c.dom.Element) xml.evaluate(path.substring(0, at), XPathConstants.NODE);
				if(childName.startsWith("@")) {	
					Attr attr = xml.document.createAttribute(childName.substring(1));
					ele.setAttributeNode(attr);
					return attr;
				} else if(childName.startsWith("text()")){
					Text text = xml.document.createTextNode("");
					ele.appendChild(text);
					return text;
				}
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
}
