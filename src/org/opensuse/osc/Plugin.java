package org.opensuse.osc;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Plugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.opensuse.osc"; //$NON-NLS-1$
	private static Plugin fgPlugin;

	public Plugin () {
		fgPlugin = this;
	}

	public static Plugin getDefault() {
		return fgPlugin;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("Starting the OSC core plugin");
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		System.out.println("Stopped the OSC core plugin");
	}
}
