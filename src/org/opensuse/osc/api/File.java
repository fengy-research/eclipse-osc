package org.opensuse.osc.api;

import java.io.InputStream;

public class File extends Object {
	String filename;

	File(Api api, Package owner, String filename) {
		super(api, owner, owner.uri + "/" + encodeURI(filename));
		this.filename = filename;
	}

	@Override
	public void refresh(int rev) throws OSCException {
		// TODO Auto-generated method stub
		outDated = false;
	}

	public InputStream checkout(int rev) throws OSCException {
		return api.issue("get", uri, Result.Type.STREAM).stream;
	}

	public InputStream checkout() throws OSCException {
		return api.issue("get", uri, Result.Type.STREAM).stream;
	}

	public void delete() throws OSCException {
		api.issue("delete", uri);
	}

	public void checkin(InputStream stream) throws OSCException {
		api.issue("put", uri, stream);
	}

	public Package getPackage() {
		return (Package) owner;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(getPackage().toString());
		sb.append(':');
		sb.append(filename);

		return sb.toString();
	}

}
