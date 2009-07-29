package org.opensuse.osc.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public abstract class Object {
	protected Api api;
	protected String uri;
	protected Object owner;
	protected boolean outDated = true;
	
	protected Object(Api api, Object owner, String uri) {
		this.owner = owner;
		this.api = api;
		this.uri = uri;
	}

	public abstract void refresh(int rev) throws OSCException;

	public void refresh() throws OSCException {
		refresh(-1);
	}

	protected static String encodeURI(String foo) {
		try {
			return URLEncoder.encode(foo, "utf8");
		} catch (UnsupportedEncodingException e) {
			return foo;
		}
	}
}
