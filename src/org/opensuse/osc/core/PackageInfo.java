package org.opensuse.osc.core;

import org.eclipse.core.resources.IFile;

public class PackageInfo extends XMLFile {

	public PackageInfo(IFile file) {
		super(file);
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
	
	public String getTemplate() {
		return  
	
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
	"<osc>"+
	   "<host url=\"\" username=\"\" password=\"\"/>"+
	   "<package project=\"\" package=\"\"/>" +
	 "</osc>";
	}
			
}
