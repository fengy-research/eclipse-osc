package org.opensuse.osc.api.tests;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.Host;
import org.opensuse.osc.api.OSCException;
import org.opensuse.osc.api.Project;

public class HostTest {
	static Api api;
	static Host host;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = new Api();
		api.setURL("https://api.opensuse.org");
		api.login("rainwoodman", "bullshits2");
		host = api.getHost();
	}

	@Test
	/*
	 * * Test GetProject Here we assume the user rainwoodman have access to
	 * home:rainwoodman, and the project home:rainwoodman-is-not-there doesn't
	 * exist.
	 */
	public void testGetProject() {

		try {
			Project project = host.getProject("home:rainwoodman");
			project.refresh();
		} catch (OSCException e) {
			fail(e.getMessage());
		}
		boolean caught = false;
		try {
			Project project = host.getProject("home:rainwoodman-is-not-there");
			project.refresh();
		} catch (OSCException e) {
			System.out.println(e.toString());
			caught = true;
		}
		if (!caught) {
			fail("Should fail on non-existing projects");
		}
	}

	@Test
	/*
	 * * Test getProjects we assume api.opensuse.org always hosts more than 10
	 * projects (as the time this is written, it is hosting 7199 projects
	 */
	public void testGetProjects() {
		try {
			host.refresh();
			List<String> projectNames = host.getProjects();

			System.out.println(String.valueOf(projectNames.size()));
			assert (projectNames.size() >= 10);
		} catch (OSCException e) {
			fail(e.getMessage());
		}

	}

}
