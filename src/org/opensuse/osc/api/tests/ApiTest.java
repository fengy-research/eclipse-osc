package org.opensuse.osc.api.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.OSCException;

public class ApiTest {
	static Api api;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		api = new Api();
		api.setURL("https://api.opensuse.org");
	}

	@Test
	public void testLogin() {
	
		try {
			api.login("rainwoodman", "bullshits2");
		} catch (OSCException e) {
			// TODO Auto-generated catch block
			fail(e.getMessage());
		}
		assertNotNull(api.getHost().getTitle());
	}


}
