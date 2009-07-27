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
	}
	
	public InputStream checkout(int rev) throws OSCException {
		return api.issue("get", uri).stream;
	}
	public Package getPackage() {
		return (Package) owner;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getPackage().toString());
		sb.append(':');
		sb.append(filename);
		
		return sb.toString();
	}

}
