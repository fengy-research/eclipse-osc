package org.opensuse.osc.api;

public abstract class Command {
	protected String project;
	protected String pac;
	public String getProject() { return project; }
	public void setProject(String value) { project = value; }
	public String getPackage() { return pac; }
	public void setPackage(String value) { pac = value; }
}

