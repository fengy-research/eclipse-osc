package org.opensuse.osc.ui.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class CheckoutActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {	
		if (resource instanceof IFile) {
			try {
				OSCProject p = Plugin.getModel().getProject(resource.getProject());
				PackageInfo packageInfo = p.getPackageInfo();
				IFile file = (IFile)resource;
				org.opensuse.osc.api.File apiFile = packageInfo.getApiPackage().getFile(file.getName());
				file.create(apiFile.checkout(), true, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (resource instanceof IProject) {
			try {
				monitor.beginTask("Checking out a package", 2);
				OSCProject p = Plugin.getModel().getProject((IProject) resource);
				PackageInfo packageInfo = p.getPackageInfo();
				org.opensuse.osc.api.Package apiPackage =packageInfo.getApiPackage();
				apiPackage.refresh();
				monitor.worked(1);
				SubProgressMonitor mon = new SubProgressMonitor(monitor, 1);
				
				List<String> filenames = apiPackage.getFiles();
				mon.beginTask("Checking out files", filenames.size());

				for(String filename: filenames) {
					SubProgressMonitor mon1 = new SubProgressMonitor(mon, 1);
					IFile file = ((IProject) resource).getFile(filename);
					if(file.exists()) continue;
					org.opensuse.osc.api.File apiFile =  apiPackage.getFile(filename);
					file.create(apiFile.checkout(), true, mon1);
					
				}
				mon.done();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return new Status(IStatus.ERROR, null, null, e);
			}
		}
		return Status.OK_STATUS;
	}

}
