package org.opensuse.osc.api;
import java.util.List;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;

public class Package {
	protected String project;
	protected String pac;
	protected Api api;
	private List<String> filenames;
	private Package linkTarget;

	public List<String> getFileList() {
		return filenames;
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
		for(String fn : filenames) {
			if(fn.equals(filename)) {
				return true;
			}
		}
		return false;
	}
	public Package branch() throws Exception {
		Call call = api.newCall("post", "/source/" + encode(project) + "/" + encode(pac) + "?cmd=branch");
		api.issue(call);
		return new Package(api, "home:" + api.getUsername() + ":branches:" + project, pac);
	}
	public InputStream checkout(String filename) throws Exception {
		Package p = this;
		while(p != null && !p.hasFile(filename)) {
			p = p.linkTarget;
		}
		Call call = api.newCall("get", "/source/" + p.project + "/" + p.pac + "/" + filename);
		api.issue(call);
		return call.result.stream;
	}

	public void fetch() throws Exception {
		Call call = api.newCall("get", "/source/" + encode(project) + "/" + encode(pac));
		api.issue(call);
		Result r = call.getResult();
		filenames = r.queryList("//entry/@name");
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
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Package:" + project + " " + pac +"\n");
		if(getIsLink()) {
			sb.append("Linked: " + linkTarget.project + " " + linkTarget.pac + "\n");
		}
		if(filenames != null) {
			sb.append("Files:\n");
			for(String s :filenames) {
				sb.append("  ");
				sb.append(s);
				sb.append('\n');
			}
		}
		return sb.toString();
	}
}
