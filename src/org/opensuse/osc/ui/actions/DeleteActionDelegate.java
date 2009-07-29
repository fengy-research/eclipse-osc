package org.opensuse.osc.ui.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class DeleteActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (resource instanceof IFile) {
			try {
				OSCProject p = Plugin.getModel().getProject(resource.getProject());
				PackageInfo packageInfo = p.getPackageInfo();
				IFile file = (IFile)resource;
				org.opensuse.osc.api.File apiFile = packageInfo.getApiPackage().getFile(file.getName());
				apiFile.delete();
			} catch (Exception e) {
				return new Status(IStatus.ERROR, null, null, e);
			}
		}
		return Status.OK_STATUS;
	}

	
}
