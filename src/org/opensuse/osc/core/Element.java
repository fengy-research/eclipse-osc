package org.opensuse.osc.core;

import org.eclipse.core.resources.IResource;
public class Element {
	protected IResource resource;
	public IResource getResource() { return resource; }
	public Element(IResource resource) {
		this.resource = resource;
	}
}
