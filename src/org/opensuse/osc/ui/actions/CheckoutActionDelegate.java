package org.opensuse.osc.ui.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;


import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class CheckoutActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {	
		if (resource instanceof IFile) {
			try {
				OSCProject p = Plugin.getModel().getProject((IProject) resource.getProject());
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
				OSCProject p = Plugin.getModel().getProject((IProject) resource);
				PackageInfo packageInfo = p.getPackageInfo();
				org.opensuse.osc.api.Package apiPackage =packageInfo.getApiPackage();
				apiPackage.refresh();
				List<String> filenames = apiPackage.getFiles();
				for(String filename: filenames) {
					IFile file = ((IProject) resource).getFile(filename);
					if(file.exists()) continue;
					org.opensuse.osc.api.File apiFile =  apiPackage.getFile(filename);
					file.create(apiFile.checkout(), true, null);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return new Status(Status.ERROR, null, null, e);
			}
		}
		return Status.OK_STATUS;
	}

}
