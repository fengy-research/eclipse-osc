package org.opensuse.osc.commands;
import java.util.List;
import org.opensuse.osc.api.*;

public class Checkout extends Command {
	protected String rev = null;
	protected String destdir = ".";

	public Checkout(Api api, String project, String pac) {
		super(api, project, pac);
	}
	public void execute () throws Exception {
		Call call = api.newCall("get", "/source/" + encode(project) + "/" + encode(pac));
		api.issue(call);
		System.out.println(call.getResult().toString());
/*
		List<String> filenames = call.result.queryList("//entry/text()");
		for(String s :filenames) {
			System.out.println(s);
		}*/
	}
}
