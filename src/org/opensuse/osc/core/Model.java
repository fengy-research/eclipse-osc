package org.opensuse.osc.core;

import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class Model extends Element {
	private HashMap<IProject, OSCProject> projectHash = new HashMap<IProject, OSCProject>();

	public Model() {
		this(ResourcesPlugin.getWorkspace().getRoot());
	}

	public Model(IWorkspaceRoot root) {
		super(root);
	}

	public OSCProject getProject(IProject res) throws Exception {
		OSCProject p = projectHash.get(res);
		if (p == null) {
			p = new OSCProject(res);
			projectHash.put(res, p);
			PackageInfo pi = new PackageInfo(res);
			p.packageInfo = pi;
		}
		p.packageInfo.refresh();

		return p;
	}

}
