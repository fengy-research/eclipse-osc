package org.opensuse.osc.api;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.opensuse.osc.api.utils.XML;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Package extends Object{
	
	protected String packageName;
	
	private Package linkTarget;
	private File _link;
	private ArrayList<LinkAction> linkActionList;
	private ArrayList<String> fileList;
	
	public Package (Api api, Project project, String packageName) {
		super(api, project, "/source/" + encodeURI(project.getName()) + "/" + encodeURI(packageName));
		this.packageName = packageName;
	}
	
	public Project getProject() {
		return (Project)owner;
	}
	public Host getHost() {
		return getProject().getHost();
	}
	public String getProjectName() { return getProject().getName(); }
	public String getName() { return packageName; }
	
	public boolean getIsLink() { return linkTarget != null; }
	public Package getLinkTarget() { return linkTarget; }
	
	public Package branch() throws OSCException {
		api.issue("post", uri + "?cmd=branch");
		Project project = new 
			Project(api, this.getHost(), "home:" + api.getUsername() + ":branches:" + getProjectName());
		return new Package(api, project, packageName);
	}
	
	public File getFile(String filename) throws OSCException {
		File f = new File(api, this, filename);
		f.refresh();
		return f;
	}
	
	public List<String> getFiles() throws OSCException {
		/** 
		 * doesn't really throw the exception, as the information
		 * is obtained in refresh();
		 */
		return fileList;
	}
	public List<LinkAction> getLinkActions() {
		return linkActionList;
	}
	
	@Override
	public void refresh(int rev) throws OSCException {
		Result r = api.issue("get", uri);
		
		fileList = r.queryList("//entry/@name");
		String link_project = r.query("//linkinfo/@project");
		String link_package = r.query("//linkinfo/@package");
		if(link_project != null && link_package != null) {
			linkTarget = getHost().getProject(link_project).getPackage(link_package);
			
		} else {
			linkTarget = null;
		}
		linkActionList = new ArrayList<LinkAction>();
		if(linkTarget != null) {
			linkTarget.refresh();
			_link = getFile("_link");
			_link.refresh();
			InputStream ins = _link.checkout(rev);
			XML xml;
			try {
				xml = new XML(ins);			
				NodeList nodeList = (NodeList) xml.evaluate("//patches/*", XPathConstants.NODESET);
				for(int i = 0; i < nodeList.getLength(); i++) {
					Element ele = (Element) nodeList.item(i);
					linkActionList.add(new LinkAction(ele));
				}
			} catch (SAXException e) {
				throw new OSCException(e);
			} catch (IOException e) {
				throw new OSCException(e);
			} catch (XPathExpressionException e) {
				throw new OSCException(e);
			}
			
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getProject().toString());
		sb.append(':');
		sb.append(packageName);
		if(getIsLink()) {
			sb.append("->");
			sb.append(linkTarget.toString());
		}
		
		return sb.toString();
	}
}

