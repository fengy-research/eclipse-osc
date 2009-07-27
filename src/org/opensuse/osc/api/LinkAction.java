package org.opensuse.osc.api;

import org.w3c.dom.Element;

public class LinkAction {
	public enum Type {
		ADD,
		DELETE,
		PATCH
	}
	protected Package owner;
	protected String targetFilename;
	protected Type type;
	public LinkAction(Package owner, String targetFilename) {
		this.owner = owner;
		this.targetFilename = targetFilename;
	}
	public LinkAction(Element element) {
		if("add".equals(element.getTagName())) {
			type = Type.ADD;
		}
		if("delete".equals(element.getTagName())) {
			type = Type.DELETE;
		} 
		if("patch".equals(element.getTagName())) {
			type = Type.PATCH;
		} 
		targetFilename = element.getAttribute("name");
	}
	public void apply() {
		throw new UnsupportedOperationException("not implemneted");
	}
}
