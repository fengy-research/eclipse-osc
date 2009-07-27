package org.opensuse.osc.api.tests;

import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.File;
import org.opensuse.osc.api.Host;
import org.opensuse.osc.api.OSCException;
import org.opensuse.osc.api.Package;
import org.opensuse.osc.api.Project;

public class PackageTest {
	static Api api;
	static Host host;
	static Project project;
	static Package p;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = new Api();
		api.setURL("https://api.opensuse.org");
		api.login("rainwoodman", "bullshits2");
		host = api.getHost();
		project = host.getProject("home:rainwoodman:branches:openSUSE:11.1");
		p = project.getPackage("gnome-do");
	}

	@Test
	public void testGetIsLink() {
		assert(p.getIsLink() == true);
	}

	@Test
	public void testGetLinkTarget() {
		assert(p.getLinkTarget() != null);
	}

	@Test
	public void testBranch() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFile() {
		try {
			File file = p.getFile("patch.diff");
		} catch (OSCException e) {
			fail(e.getMessage());
		}
		boolean caught = false;
		try {
			File file = p.getFile("patch.diff-is-not-there");
		} catch (OSCException e) {
			System.out.println(e.getMessage());
			caught = true;
		}	
		if(!caught) {
			fail("should get an error");
		}
	}

	@Test
	public void testGetFiles() {
		try {
			assert(p.getFiles().size() >= 2);
		} catch (OSCException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testGetLinkActions() {
		assert(p.getLinkActions().size() >= 2 );
	}

}
