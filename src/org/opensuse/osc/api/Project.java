package org.opensuse.osc.api;

import java.util.List;

public class Project extends Object {
	
	String projectName;
	protected Project(Api api, Host host, String projectName) {
		super(api, host, "/source/" + encodeURI(projectName));
		this.projectName = projectName;
	}
	
	public String getName() {
		return projectName;
	}
	public Host getHost() {
		return (Host) owner;
	}
	@Override
	public void refresh(int rev) throws OSCException {
		Result result = api.issue("get", uri + "/_meta");
	}
	
	public Package getPackage(String packageName) throws OSCException {
		Package p = new Package(api, this, packageName);
		p.refresh();
		return p;
	}
	
	public List<String> getPackages() throws OSCException {
		Result r = api.issue("get", uri);
		return r.queryList("//entry/@name");
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getHost().toString());
		sb.append(':');
		sb.append(projectName);
		return sb.toString();
	}
}
