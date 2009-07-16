package org.opensuse.osc.commands;
import org.opensuse.osc.api.*;

public class Branch extends Command {
	public Branch(Api api, String project, String pac) {
		super(api, project, pac);
	}
	public void execute () throws Exception {
		Call call = api.newCall("post", "/source/" + encode(project) + "/" + encode(pac) + "?cmd=branch");
		api.issue(call);
		System.out.println(call.getResult().toString());
	}
}
