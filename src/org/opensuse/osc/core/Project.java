package org.opensuse.osc.core;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class Project extends Element {
	protected ArrayList<Patch> patches;
	protected ArrayList<Source> sources;
	
	public IProject getProject() {
		return (IProject) resource;
	}
	public Project(IProject project) {
		super(project);
	}
	public Element computeChild(IResource res) {
		switch(res.getType()) {
		case IResource.FILE:
			IFile file = (IFile)res;
			String name = file.getName();
			if(name.endsWith(".patch")) {
				return new Patch(file);			
			}
			if(name.endsWith(".tar.gz")) {
				return new Source(file);
			}
			if(name.endsWith(".spec")) {
				return new Spec(file);
			}
		}
		return null;
		
	}
}