package org.opensuse.osc.api;

import org.w3c.dom.Element;

public class LinkAction {
	public static enum Type {
		ADD, DELETE, PATCH
	}

	protected Package owner;
	protected String targetFilename;
	protected Type type;

	public LinkAction(Package owner, String targetFilename) {
		this.owner = owner;
		this.targetFilename = targetFilename;
	}

	public LinkAction(Element element) {
		if ("add".equals(element.getTagName())) {
			type = Type.ADD;
		}
		if ("delete".equals(element.getTagName())) {
			type = Type.DELETE;
		}
		if ("apply".equals(element.getTagName())) {
			type = Type.PATCH;
		}
		targetFilename = element.getAttribute("name");
	}

	public Type getType() {
		return type;
	}

	public String getTargetFilename() {
		return targetFilename;
	}

	public Package getOwner() {
		return owner;
	}
}
