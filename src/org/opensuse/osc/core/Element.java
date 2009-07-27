package org.opensuse.osc.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.PlatformObject;

public class Element {
	protected IResource resource;
	public IResource getResource() { return resource; }
	public Element(IResource resource) {
		this.resource = resource;
	}
	
}
