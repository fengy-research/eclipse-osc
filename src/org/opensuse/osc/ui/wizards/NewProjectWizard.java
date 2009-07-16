package org.opensuse.osc.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;


public class NewProjectWizard extends BasicNewProjectResourceWizard {

	private NewProjectPage mainPage;

	public void addPages() {
		mainPage = new NewProjectPage("New OSC Project");
		addPage(mainPage);
	}
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("OSC Project Wizard"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(NewProjectWizard.class, "newfolder_wiz.gif"));
	}
	protected IProject createNewProject(IProgressMonitor monitor) throws CoreException {
		System.out.println("Create New Project is called");
		if (getNewProject() != null)
			return getNewProject();
		IProject newProjectHandle = null;
		try {
			newProjectHandle = mainPage.getProjectHandle();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProjectDescription description = workspace.newProjectDescription(newProjectHandle.getName());
		description.setLocation(null);

//		newProject = CCorePlugin.getDefault().createCProject(description, newProjectHandle, monitor, "C");
		System.out.println("Creating a BBCDT project.");
		return null;
	}
}
