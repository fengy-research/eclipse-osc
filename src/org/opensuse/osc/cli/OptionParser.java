package org.opensuse.osc.cli;

import java.util.HashMap;
import java.util.List;

class OptionParser {
	/* Parse the arg, returns the remaining args */
	public static HashMap<String, String> Parse(List<String> args,
			OptionEntry[] entries) {
		int i = 0;
		OptionEntry last_entry = null;
		HashMap<String, String> hash = new HashMap<String, String>();

		while (i < args.size()) {
			String arg = args.get(i);
			boolean parsed = false;

			if (last_entry != null) {
				last_entry.value = arg;
				parsed = true;
				hash.put(last_entry.name, arg);
				last_entry = null;
			} else
				for (OptionEntry e : entries) {
					if (arg.compareTo("--" + e.name) == 0
							|| arg.compareTo("-" + e.short_name) == 0) {
						parsed = true;
						last_entry = e;
					}
				}
			if (parsed) {
				args.remove(i);
			} else {
				i++;
			}
		}
		return hash;
	}
}
