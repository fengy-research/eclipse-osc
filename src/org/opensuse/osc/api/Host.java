package org.opensuse.osc.api;

import java.util.List;

public class Host extends Object {
	
	Host(Api api) {
		super(api, null, "/");
	}
	@Override
	public void refresh(int rev) throws OSCException {
		Result r = api.issue("get", "/about");
		title = r.query("/about/title/text()");
		description = r.query("/about/description/text()");
		revision = r.query("/about/revision/text()");
	}
	
	private String title;
	private String description;
	private String revision;
	public String getRevision() { return revision; }
	public String getTitle() { return title; }
	public String getDescription() { return description; }

	public Project getProject(String projectName) throws OSCException {
		Project p = new Project(api, this, projectName);
		p.refresh();
		return p;
	}
	public List<String> getProjects() throws OSCException {
		Result r = api.issue("get", "/source");
		return r.queryList("//entry/@name");
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(api.url);
		return sb.toString();
	}
}
