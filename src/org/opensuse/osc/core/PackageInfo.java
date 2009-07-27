package org.opensuse.osc.core;


import org.eclipse.core.resources.IProject;

public class PackageInfo extends XMLFile {
	/**
	 * NOTE: This constructor takes a IProject handle,
	 * not the underlying IFile handle for `package.xml'!.
	 * 
	 * @param project
	 */
	public PackageInfo(IProject project) {
		super(project.getFile("package.xml"));
	}
	public org.opensuse.osc.api.Package getApiPackage() throws org.opensuse.osc.api.OSCException {
		org.opensuse.osc.api.Api api = new org.opensuse.osc.api.Api();
		api.setURL(getHost());
		api.login(getUsername(), getPassword());
		org.opensuse.osc.api.Host host = api.getHost();
		org.opensuse.osc.api.Project project = host.getProject(getProjectName());
		return project.getPackage(getPackageName());
	}
	
	public void setProjectName(String value) {
		set("/osc/package/@project", value);
	}
	public String getProjectName() {
		return get("/osc/package/@project");
	}
	public String getPackageName() {
		return get("/osc/package/@package");
	}
	public void setPackageName(String value) {
		set("/osc/package/@package" , value);
	}
	public String getHost() {
		return get("/osc/host/@url");
	}
	public void setHost(String value) {
		set("/osc/host/@url", value);
	}
	public String getUsername() {
		return get("/osc/host/@username");
	}
	public void setUsername(String value) {
		set("/osc/host/@username", value);
	}
	public String getPassword() {
		return get("/osc/host/@password");
	} 
	public void setPassword(String value) {
		set("/osc/host/@password", value);
	}
	
	@Override
	public String getTemplate() {
		return  
	
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
	"<osc>"+
	   "<host url=\"\" username=\"\" password=\"\"/>"+
	   "<package project=\"\" package=\"\"/>" +
	 "</osc>";
	}
			
}
