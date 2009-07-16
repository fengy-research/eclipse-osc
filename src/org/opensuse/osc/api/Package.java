package org.opensuse.osc.api;
import java.util.List;
import java.util.ArrayList;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;

public class Package {
	protected String project;
	protected String pac;
	protected Api api;
	private List<String> localFiles;
	private ArrayList<String> visibleLocalFiles;
	private ArrayList<String> recursiveFiles;

	private Package linkTarget;

	public List<String> getLocalFiles() {
		return localFiles;
	}
	public List<String> getFiles() {
		return recursiveFiles;
	}

	public Package (Api api, String project, String pac) {
		this.api = api;
		this.project = project;
		this.pac = pac;
	}
	public String getProject() { return project; }
	public String getPackage() { return pac; }
	public boolean getIsLink() { return linkTarget != null; }

	protected static String encode(String foo) {
		try {
			return URLEncoder.encode(foo, "utf8");
		} catch(UnsupportedEncodingException e) {
			return foo;
		}
	}
	public boolean hasFile(String filename) {
		return getFiles().indexOf(filename) != -1;
	}
	public boolean hasLocalFile(String filename) {
		return getLocalFiles().indexOf(filename) != -1;
	}

	public Package branch() throws Exception {
		Call call = api.newCall("post", "/source/" + encode(project) + "/" + encode(pac) + "?cmd=branch");
		api.issue(call);
		return new Package(api, "home:" + api.getUsername() + ":branches:" + project, pac);
	}
	public InputStream checkout(String filename) throws Exception {
		Package p = this;
		while(p != null && !p.hasLocalFile(filename)) {
			p = p.linkTarget;
		}
		if(p == null) {
			/* Not found, resort to this project to obtain a fancy error report from osc */
			p = this;
		}
		Call call = api.newCall("get", "/source/" + p.project + "/" + p.pac + "/" + filename);
		api.issue(call);
		return call.result.stream;
	}

	public void fetch() throws Exception {
		Call call = api.newCall("get", "/source/" + encode(project) + "/" + encode(pac));
		api.issue(call);
		Result r = call.getResult();
		localFiles = r.queryList("//entry/@name");
		visibleLocalFiles = new ArrayList<String>();
		recursiveFiles = new ArrayList<String>();
		for(String s : localFiles) {
			if(s.startsWith("_")) continue;
			visibleLocalFiles.add(s);
			recursiveFiles.add(s);
		}
		try {
			String link_project = r.query("//linkinfo/@project");
			String link_package = r.query("//linkinfo/@package");
			if(link_project != null && link_package != null) {
				linkTarget = api.refPackage(link_project, link_package);
				linkTarget.fetch();
			}
		} catch (Exception e) {
			linkTarget = null;
			e.printStackTrace();
		}
		if(linkTarget != null) 
			for(String s : linkTarget.getFiles()) {
				if(recursiveFiles.indexOf(s) == -1) {
					recursiveFiles.add(s);
				}
			}
		
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Package:" + project + " " + pac +"\n");
		if(getIsLink()) {
			sb.append("Linked: " + linkTarget.project + " " + linkTarget.pac + "\n");
		}
		sb.append("Files:\n");
		for(String s : getFiles()) {
			sb.append("  ");
			sb.append(s);
			sb.append('\n');
		}
		return sb.toString();
	}
}

