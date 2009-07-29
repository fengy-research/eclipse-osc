package org.opensuse.osc.ui.actions;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;


import org.opensuse.osc.Plugin;
import org.opensuse.osc.api.utils.Patch;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class PatchActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (resource instanceof IProject) {
			monitor.beginTask("Patching files", 1);
			try {
				IProject projectHandle = (IProject) resource;
			
				IFolder linkFolderHandle = projectHandle.getFolder("link");
				if(linkFolderHandle.exists()) {
	
					Patch patch = new Patch(linkFolderHandle.getFullPath().toOSString(), 
							projectHandle.getFile("patch.diff").getContents());

					patch.apply();
				}
			} catch (CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				monitor.done();
			}
		}
		return Status.OK_STATUS;
	}


}
