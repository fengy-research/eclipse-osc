package org.opensuse.osc.api.tests;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.Host;
import org.opensuse.osc.api.OSCException;
import org.opensuse.osc.api.Package;
import org.opensuse.osc.api.Project;

public class ProjectTest {
	static Api api;
	static Host host;
	static Project project;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = new Api();
		api.setURL("https://api.opensuse.org");
		api.login("rainwoodman", "bullshits2");
		host = api.getHost();
		project = host.getProject("home:rainwoodman:branches:openSUSE:11.1");

	}

	@Test
	public void testGetPackage() {
		try {
			Package gnomeDO = project.getPackage("gnome-do");
			gnomeDO.refresh();
			assert (gnomeDO.getIsLink());
		} catch (OSCException e) {
			fail(e.getMessage());
		}
		boolean caught = false;
		try {
			project.getPackage("gnome-do-is-not-there").refresh();
			
		} catch (OSCException e) {
			System.out.println(e.getMessage());
			caught = true;
		}
		if (!caught) {
			fail("Should throw an exception");
		}

	}

	@Test
	public void testGetPackages() {
		List<String> packageNames;
		try {
			packageNames = project.getPackages();
			assert (packageNames.size() >= 2);
		} catch (OSCException e) {
			fail(e.getMessage());
		}

	}

}
