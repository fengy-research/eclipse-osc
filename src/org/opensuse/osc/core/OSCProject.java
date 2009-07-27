package org.opensuse.osc.core;

import org.eclipse.core.resources.IProject;

public class OSCProject extends Element {
	protected PackageInfo packageInfo;
	
	
	public IProject getProject() {
		return (IProject) resource;
	}
	public OSCProject(IProject project) {
		super(project);
	}
	
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo pi) {
		packageInfo = pi;	
	}

}