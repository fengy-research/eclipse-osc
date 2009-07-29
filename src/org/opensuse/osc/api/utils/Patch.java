package org.opensuse.osc.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Patch {
	protected String chroot;
	protected InputStream patchContent;
	class StreamGobbler extends Thread
	{
	    InputStream is;
	    String type;
	    
	    StreamGobbler(InputStream is, String type)
	    {
	        this.is = is;
	        this.type = type;
	    }
	    
	    @Override
		public void run()
	    {
	        try
	        {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line=null;
	            while ( (line = br.readLine()) != null)
	                System.out.println(type + ">" + line);    
	            } catch (IOException ioe)
	              {
	                ioe.printStackTrace();  
	              }
	    }
	}

	public Patch(String chroot, InputStream patchContent) {
		this.patchContent = patchContent;
		this.chroot = chroot;
	}
	public void apply() {
		String[] cmdline = {
				"patch",
				"-p0",
				"-d " + chroot
		};
		
		try {
			Process process = Runtime.getRuntime().exec(cmdline);
			InputStream stderr = process.getErrorStream();
			InputStream stdout = process.getInputStream();
			OutputStream stdin = process.getOutputStream();
			StreamGobbler stderrGobbler = new StreamGobbler(stderr, "ERR");
			StreamGobbler stdoutGobbler = new StreamGobbler(stdout, "OUT");
			stderrGobbler.start();
			stdoutGobbler.start();
			byte[] buffer = new byte[65535];
			int n;
			while((n = patchContent.read(buffer)) > 0) {
				stdin.write(buffer, 0, n);
			}
			process.waitFor();
			stdin.close();
			stderr.close();
			stdout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
			
		}
		
		
	}
}
