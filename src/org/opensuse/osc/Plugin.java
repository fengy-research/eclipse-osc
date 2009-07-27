package org.opensuse.osc;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.opensuse.osc.core.PackageInfo;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.Package;
import org.opensuse.osc.core.LinkInfo;
import org.opensuse.osc.core.Model;
import org.opensuse.osc.core.PackageInfo;
import org.opensuse.osc.core.Project;
import org.opensuse.osc.core.ProjectNature;
import org.osgi.framework.BundleContext;


public class Plugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.opensuse.osc"; //$NON-NLS-1$
	private static Plugin fgPlugin;
	public static Model fgModel;
	private static Api fgApi;
	public Plugin () {
		fgPlugin = this;
	}

	public static Plugin getDefault() {
		return fgPlugin;
	}
	public static Model getModel() {
		return fgModel;		
	}
	public static Api getApi() {
		return fgApi;
	}
	public void start(BundleContext context) throws Exception {
		super.start(context);
		fgModel = new Model();
		fgApi = new Api();
		System.out.println("Starting the OSC core plugin");
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		System.out.println("Stopped the OSC core plugin");
	}
	
	public IProject createProject(
		    final IProjectDescription description,
		    final IProject projectHandle,
		    final PackageInfo pi,
		    IProgressMonitor monitor,
		    final String projectID)
		throws CoreException, OperationCanceledException {
			  
			  ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			    public void run(IProgressMonitor monitor) throws CoreException {
			      try {
			        if (monitor == null) {
			          monitor = new NullProgressMonitor();
			        }
			        monitor.beginTask("Creating OSC Project...", 4); //$NON-NLS-1$
			        if (!projectHandle.exists()) {
			          projectHandle.create(description, new SubProgressMonitor(monitor, 1));
			        }
			        
			        if (monitor.isCanceled()) {
			          throw new OperationCanceledException();
			        }

			        // Open first.
			        projectHandle.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1));

			        // Add OSC Nature ... does not add duplicates
			        getApi().setHost(pi.getHost());
			        getApi().login(pi.getUsername(), pi.getPassword());
			        Package pac = getApi().refPackage(pi.getProjectName(), pi.getPackageName());
			       
			        pi.save();
			        /* Fetch the package information from build service */
			        pac.fetch();
			        /* Checkout the latest files, all files including _link and such */
			        List<String> filenames = pac.getFiles();
			        for(String filename : filenames) {
			        	InputStream ins = pac.checkout(filename);
			        	IFile file = projectHandle.getFile(filename);
				        file.create(ins, false, new SubProgressMonitor(monitor, 1));	
			        } 
			        
			        ProjectNature.addNature(projectHandle, new SubProgressMonitor(monitor, 1));
			        /* preLoad the project */
			        Project p = getModel().getProject(projectHandle);
			        List<String> applies = p.getLinkInfo().getApplies();
			        System.out.print(applies.toString());
			   			        
			    } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
			        monitor.done();
			      }
			    }
			  }, ResourcesPlugin.getWorkspace().getRoot(), 0, monitor);
			  return projectHandle;
	}
}
