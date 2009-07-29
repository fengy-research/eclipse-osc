package org.opensuse.osc.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class AboutActionDelegate extends ActionDelegate {

	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (resource instanceof IProject) {
			try {
				OSCProject p = Plugin.getModel().getProject((IProject) resource);
				PackageInfo packageInfo = p.getPackageInfo();
				System.out.println(packageInfo.getApiPackage().toString());
			} catch (Exception e) {
				return new Status(IStatus.ERROR, null, null, e);
			}
		}
		return Status.OK_STATUS;
	}



}
