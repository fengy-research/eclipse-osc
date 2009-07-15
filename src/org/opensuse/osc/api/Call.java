package org.opensuse.osc.api;

class Call {
	protected Call(Api api, String command) {
		this.api = api;
		this.command = command;
	}
	protected Api api;
	protected String command;

	protected boolean hasData;
	public Api getApi() { return api; }
	public String getCommand() { return command; }

	protected Result result = null;
	public Result getResult() {
		return result;
	}

}
