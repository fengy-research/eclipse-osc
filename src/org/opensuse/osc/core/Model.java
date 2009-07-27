package org.opensuse.osc.core;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class Model extends Element {
	private HashMap<IProject, Project> projectHash = new HashMap<IProject, Project>();
	
	public Model() {
		this(ResourcesPlugin.getWorkspace().getRoot());
	}
	public Model(IWorkspaceRoot root) {
		super(root);
	}
	
	public Project getProject(IProject res) throws Exception {
		Project p = projectHash.get(res);
		if(p == null) {
			p = new Project(res);
			projectHash.put(res, p);
			PackageInfo pi = new PackageInfo(res.getFile("project.xml"));
			LinkInfo li = new LinkInfo(res.getFile("_link"));
		    p.packageInfo = pi;
		    p.linkInfo = li;
		}
		p.packageInfo.refresh();
		p.linkInfo.refresh();
		return p;
	}
	
}
