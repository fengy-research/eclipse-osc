package org.opensuse.osc.ui.actions;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.api.LinkAction;
import org.opensuse.osc.api.OSCException;
import org.opensuse.osc.api.Package;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.Patch;

public class PatchActionDelegate extends ActionDelegate {
	@Override
	public IStatus run(IProgressMonitor monitor) {
		if (resource instanceof IProject) {
			monitor.beginTask("Patching files", 1);
			try {
				IProject projectHandle = (IProject) resource;

				IFolder linkFolderHandle = projectHandle.getFolder("link");
				IFolder originFolderHandle = projectHandle.getFolder("origin");

				OSCProject oscProject = Plugin.getModel().getProject(
						projectHandle);
				Package apiPackage = oscProject.getPackageInfo()
						.getApiPackage();
				monitor.subTask("Loading server information");
				apiPackage.refresh();
				monitor.worked(1);

				SubProgressMonitor mon;
				monitor.subTask("Copying files");
				mon = new SubProgressMonitor(monitor, 1);
				originFolderHandle.copy(linkFolderHandle
						.getProjectRelativePath(), true, mon);

				List<LinkAction> actions = apiPackage.getLinkActions();
				mon = new SubProgressMonitor(monitor, 1);
				mon.beginTask("Applying link actions", actions.size());
				for (LinkAction action : actions) {
					SubProgressMonitor mon1 = new SubProgressMonitor(mon, 1);
					switch (action.getType()) {
					case PATCH:
						Patch patch = new Patch(linkFolderHandle.getLocation()
								.toOSString(), projectHandle.getFile(
								action.getTargetFilename()).getContents());
						patch.apply();
						mon1.done();
						break;
					case DELETE:
						linkFolderHandle.getFile(action.getTargetFilename())
								.delete(true, mon1);
						break;
					}
				}
				mon = new SubProgressMonitor(monitor, 1);
				mon.beginTask("Copying new files", IProgressMonitor.UNKNOWN);
				for (IResource res : projectHandle.members()) {
					if (IResource.FILE == res.getType()) {
						SubProgressMonitor mon1 = new SubProgressMonitor(mon, 1);
						IFile file = (IFile) res;
						if (!shouldBeSkipped(file, apiPackage)) {
							file.copy(linkFolderHandle.getFile(file.getName())
									.getProjectRelativePath(), true, mon1);
						}
					}
				}

				mon.done();

			} catch (CoreException e) {

				return new Status(IStatus.ERROR, Plugin.PLUGIN_ID, e
						.getMessage(), e);
			} catch (OSCException e) {

				return new Status(IStatus.ERROR, Plugin.PLUGIN_ID, e
						.getMessage(), e);
			} finally {
				monitor.done();
			}
		}
		return Status.OK_STATUS;
	}

	private boolean shouldBeSkipped(IFile file, Package apiPackage) {
		List<LinkAction> actions = apiPackage.getLinkActions();
		for (LinkAction action : actions) {
			if (action.getTargetFilename().equals(file.getName()))
				return true;
		}
		if (file.getName().startsWith("_"))
			return true;
		if (file.getName().equals("package.xml"))
			return true;
		return false;
	}
}
