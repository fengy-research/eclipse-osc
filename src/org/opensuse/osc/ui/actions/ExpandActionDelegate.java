package org.opensuse.osc.ui.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.api.File;
import org.opensuse.osc.api.Package;
import org.opensuse.osc.api.utils.Patch;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class ExpandActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (resource instanceof IProject) {
			IProject projectHandle = (IProject) resource;
			SubProgressMonitor mon;
			try {
				monitor.beginTask("Expanding a project", 5);
				
				monitor.subTask("Getting the package information");
				OSCProject p = Plugin.getModel().getProject(projectHandle);
				PackageInfo packageInfo = p.getPackageInfo();
				// TODO expand it, create a folder and checkout files!
				Package apiPackage = packageInfo.getApiPackage();
				
				/* Fetch package information from the server */
				apiPackage.refresh();
				monitor.worked(1);
				
				if(monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				
				/* Only for a 'link' package */
				if(!apiPackage.getIsLink()) {
					return new Status(IStatus.ERROR, "Not a link package.", null);
				}
				
				monitor.subTask("Creating the folder");
				/* Create the folder for the expanded files */
				IFolder folderHandle = projectHandle.getFolder("origin");
				mon = new SubProgressMonitor(monitor, 1);
				if(!folderHandle.exists()) {
					folderHandle.create(true, true, mon);
				} else {
					mon.done();
				}
				
				if(monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}

				monitor.subTask("Getting the link-target package's information");
				/* Fetch the link target package from the server */
				Package linkApiPackage = apiPackage.getLinkTarget();
				linkApiPackage.refresh();
				monitor.worked(1);

				if(monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				
				monitor.subTask("Checking out files");
				/* Check out all files in the link target package */
				List<String> filenames = linkApiPackage.getFiles();
				mon = new SubProgressMonitor(monitor, 1);	
				mon.beginTask("Checking out files", filenames.size());
				for(String filename : filenames) {
					IFile fileHandle = folderHandle.getFile(filename);
					File apiFile = linkApiPackage.getFile(filename);
					SubProgressMonitor mon1 = new SubProgressMonitor(mon, 1);
					if(!fileHandle.exists()) {
						fileHandle.create(apiFile.checkout(), true, mon1);
					} else {
						fileHandle.setContents(apiFile.checkout(), true, false, mon1);
					}

					if(monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
				}
				monitor.subTask("Copying files");
				mon = new SubProgressMonitor(monitor, 1);
				IFolder linkFolderHandle = projectHandle.getFolder("link");
				if(!linkFolderHandle.exists()) {
					folderHandle.copy(linkFolderHandle.getProjectRelativePath(), true, mon);
					monitor.subTask("Patching files");
					Patch patch = new Patch(linkFolderHandle.getFullPath().toOSString(), 
							projectHandle.getFile("patch.diff").getContents());
					patch.apply();
					
				} else 
					mon.done();
			} catch (Exception e) {
				return new Status(IStatus.ERROR, Plugin.PLUGIN_ID, e.getMessage(), e);
			} finally {
				monitor.done();
				
			}
		}
		return Status.OK_STATUS;
	}



}
