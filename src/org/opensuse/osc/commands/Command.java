package org.opensuse.osc.commands;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import org.opensuse.osc.api.*;

public abstract class Command {
	protected String project;
	protected String pac;
	protected Api api;
	public Command(Api api, String project, String pac) {
		this.api = api;
		this.project = project;
		this.pac = pac;
	}
	public String getProject() { return project; }
	public String getPackage() { return pac; }
	public abstract void execute() throws Exception;
	protected static String encode(String foo) {
		try {
			return URLEncoder.encode(foo, "utf8");
		} catch(UnsupportedEncodingException e) {
			return foo;
		}
	}
}

