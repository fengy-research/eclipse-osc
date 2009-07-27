package org.opensuse.osc.core;

import java.util.List;

import org.eclipse.core.resources.IFile;

public class LinkInfo extends XMLFile {

	public LinkInfo(IFile file) {
		super(file);
		// TODO Auto-generated constructor stub
	}
	public List<String> getApplies() {
		return getList("/link/patches/apply/@name");
	}
	
	@Override
	public String getTemplate() {
		
		return "<link project=\"\" package=\"\" baserev=\"\">" +
  "<patches>" +
    "<delete name=\"\" />" +
    "<apply name=\"\" />" +
  "</patches>" +
"</link>";

	}

}
