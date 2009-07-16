package org.opensuse.osc.cli;

class OptionEntry {
	public String name;
	public String short_name;
	public String value;
	public OptionEntry(String name, String short_name) {
		this.name = name;
		this.short_name = short_name;
	}
}

