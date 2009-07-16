package org.opensuse.osc.api;

import java.util.*;

class Cli {
	private static class OptionEntry {
		public String name;
		public String short_name;
		public String value;
		public OptionEntry(String name, String short_name) {
			this.name = name;
			this.short_name = short_name;
		}
	}

	private static class OptionParser {
		/* Parse the arg, returns the remaining args */
		public static HashMap<String, String> Parse(List<String> args, OptionEntry[] entries) {
			int i = 0;
			OptionEntry last_entry = null;
			HashMap<String, String> hash = new HashMap<String, String>();
			
			while(i < args.size()) {
				String arg = args.get(i);
				boolean parsed = false;

				if(last_entry != null) {
					last_entry.value = arg;
					parsed = true;
					hash.put(last_entry.name, arg);
					last_entry = null;
				} else for(OptionEntry e: entries) {
					if(arg.compareTo("--" + e.name) == 0
					|| arg.compareTo("-" + e.short_name) == 0) {
						parsed = true;
						last_entry = e;
					}
				}
				if(parsed){ 
					args.remove(i);
				} else {
					i++;
				}
			}
			return hash;
		}

	}
	public static void main(String[] raw_args) throws Exception {
		List<String> args = new ArrayList<String>(Arrays.asList (raw_args));
		OptionEntry [] entries = {
			new OptionEntry("username", "u"),
			new OptionEntry("password", "p"),
			new OptionEntry("host", "h"),
			new OptionEntry("query", "q")
		};
		Map<String, String> arg_map = OptionParser.Parse(args, entries);
		Map<String, String> env = System.getenv();
		
		String host = arg_map.get("host");
		String username = arg_map.get("username");
		String password = arg_map.get("password");
		String query = arg_map.get("query");


		if(host == null) {
			host = env.get("OSC_HOST");
		}
		if(username == null) {
			username = env.get("OSC_USER");
		}
		if(password == null) {
			password = env.get("OSC_PASS");
		}

		Api api = new Api(host);
		api.setAuth(username, password);

		Call call;
		if(args.size() != 0) {
			call = api.newCall(args.get(0), args.get(1));
		} else {
			call = api.newCall("get", "about");
		}
		api.issue(call);

		System.out.println(call.getResult().getStatus().toString());
		ArrayList<String> list;
		if(query != null) {
			list = call.getResult().queryList(query);
		} else {
			list = call.getResult().queryList("//text()");
		}
		for(String str: list) {
			System.out.println(str);
		}
	}
}

