package org.opensuse.osc.api.tests;

import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.File;
import org.opensuse.osc.api.Host;
import org.opensuse.osc.api.OSCException;
import org.opensuse.osc.api.Package;
import org.opensuse.osc.api.Project;

public class FileTest {

	static Api api;
	static Host host;
	static Project project;
	static Package p;
	static File f;
	private InputStream is;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = new Api();
		api.setURL("https://api.opensuse.org");
		api.login("rainwoodman", "bullshits2");
		host = api.getHost();
		project = host.getProject("home:rainwoodman:branches:openSUSE:11.1");
		p = project.getPackage("evince");
		f = p.getFile("_meta");
	}

	@Test
	public void testCheckoutInt() {
		fail("Not yet implemented");
	}

	@Test
	public void testCheckout() {
		try {
			is = f.checkout();
		} catch (OSCException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testCheckin() {
		try {
			f = p.getFile("_metaCopy");
			f.checkin(is);
			f.checkout();
		} catch (OSCException e) {
			fail(e.getMessage());
		}
	}

}
