package org.opensuse.osc;

import java.io.*;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import org.opensuse.osc.api.*;

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
			new OptionEntry("username", "U"),
			new OptionEntry("password", "P"),
			new OptionEntry("host", "H"),
			new OptionEntry("query", "Q"),
			new OptionEntry("mode", "M"),
			new OptionEntry("project", "j"),
			new OptionEntry("package", "p"),
			new OptionEntry("filename", "f")
			
		};

		Map<String, String> arg_map = OptionParser.Parse(args, entries);
		Map<String, String> env = System.getenv();
		
		String host = arg_map.get("host");
		String username = arg_map.get("username");
		String password = arg_map.get("password");
		String query = arg_map.get("query");
		String mode = arg_map.get("mode");


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
		api.login(username, password);

		if(mode != null && mode.equals("call")) {
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
		} else {
			org.opensuse.osc.api.Package pac = null;
			String project = arg_map.get("project");
			String pacname = arg_map.get("package");
			String c = null;
			pac = api.refPackage(project, pacname);
			pac.update();
			if(args.size() != 0) {
				c = args.get(0);
			}
			if(c != null) {
				if(c.equals("query")) {
					System.out.println(pac.toString());
				}
				if(c.equals("branch")) {
					pac.branch();
				}
				if(c.equals("checkout")) {
					for(String fn : pac.getFilenames()) {
						InputStream stream = pac.checkout(fn);
						System.out.println(stream.toString());
						FileOutputStream fos = new FileOutputStream(fn);
						BufferedInputStream bis = new BufferedInputStream(stream);
						int n;
						byte [] buffer = new byte[1000];
						while((n = bis.read(buffer)) != -1) {
							fos.write(buffer, 0, n);
						}
						bis.close();
						stream.close();
						fos.close();
					}
				}
			} else {
				System.out.println("wrong command line");
			}

		}
	}
}

