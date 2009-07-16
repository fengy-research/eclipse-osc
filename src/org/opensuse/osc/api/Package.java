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

	public List<String> getFilenames() {
		return filenames;
	}
	public Package (Api api, String project, String pac) {
		this.api = api;
		this.project = project;
		this.pac = pac;
	}
	public String getProject() { return project; }
	public String getPackage() { return pac; }

	protected static String encode(String foo) {
		try {
			return URLEncoder.encode(foo, "utf8");
		} catch(UnsupportedEncodingException e) {
			return foo;
		}
	}
	public Package branch() throws Exception {
		Call call = api.newCall("post", "/source/" + encode(project) + "/" + encode(pac) + "?cmd=branch");
		api.issue(call);
		return new Package(api, "home:" + api.getUsername() + ":branches:" + project, pac);
	}
	public InputStream checkout(String filename) throws Exception {
		boolean found = false;
		for(String s :filenames) {
			if(s.equals(filename)) {
				found = true;
			}
		}
		if(found) {
			Call call = api.newCall("get", "/source/" + encode(project) + "/" + encode(pac) + "/" + filename);
			api.issue(call);
			return call.result.stream;
		}
		return null;
	}
	public void update() throws Exception {
		Call call = api.newCall("get", "/source/" + encode(project) + "/" + encode(pac));
		api.issue(call);
		filenames = call.getResult().queryList("//entry/@name");
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(String s :filenames) {
			sb.append(s);
		}
		return sb.toString();
	}
}

