package org.opensuse.osc.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;


import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class CheckinActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (resource instanceof IFile) {
			try {
				IFile file = (IFile) resource;
				monitor.beginTask("Checking in " + file.getName() + " ...", 2);
				
				OSCProject p = Plugin.getModel().getProject((IProject) resource.getProject());
				PackageInfo packageInfo = p.getPackageInfo();
				
				org.opensuse.osc.api.File apiFile = packageInfo.getApiPackage().getFile(file.getName());
				apiFile.checkin(file.getContents());
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new Status(Status.ERROR, null, null, e);
			} finally {
				monitor.done();
			}
		}
		return Status.OK_STATUS;
	}




}
