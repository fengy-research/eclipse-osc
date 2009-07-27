package org.opensuse.osc.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class Project extends Element {
	protected PackageInfo packageInfo;
	protected LinkInfo linkInfo;
	
	public IProject getProject() {
		return (IProject) resource;
	}
	public Project(IProject project) {
		super(project);
	}

	
	public PackageInfo getPackageInfo() {
		return packageInfo;
	}
	public LinkInfo getLinkInfo() {
		return linkInfo;
	}
	public void setPackageInfo(PackageInfo pi) {
		packageInfo = pi;	
	}
	public void setLinkInfo(LinkInfo li) {
		linkInfo = li;
	}

}