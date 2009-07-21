package org.opensuse.osc;

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
import org.opensuse.osc.core.ProjectNature;
import org.osgi.framework.BundleContext;

public class Plugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.opensuse.osc"; //$NON-NLS-1$
	private static Plugin fgPlugin;

	public Plugin () {
		fgPlugin = this;
	}

	public static Plugin getDefault() {
		return fgPlugin;
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		System.out.println("Starting the OSC core plugin");
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		System.out.println("Stopped the OSC core plugin");
	}
	
	public IProject createProject(
		    final IProjectDescription description,
		    final IProject projectHandle,
		    IProgressMonitor monitor,
		    final String projectID)
		throws CoreException, OperationCanceledException {
			  
			  ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			    public void run(IProgressMonitor monitor) throws CoreException {
			      try {
			        if (monitor == null) {
			          monitor = new NullProgressMonitor();
			        }
			        monitor.beginTask("Creating OSC Project...", 3); //$NON-NLS-1$
			        if (!projectHandle.exists()) {
			          projectHandle.create(description, new SubProgressMonitor(monitor, 1));
			        }
			        
			        if (monitor.isCanceled()) {
			          throw new OperationCanceledException();
			        }

			        // Open first.
			        projectHandle.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1));

			        // Add OSC Nature ... does not add duplicates
			        
			        ProjectNature.addNature(projectHandle, new SubProgressMonitor(monitor, 1));
			        
			      } finally {
			        monitor.done();
			      }
			    }
			  }, ResourcesPlugin.getWorkspace().getRoot(), 0, monitor);
			  return projectHandle;
	}
}
