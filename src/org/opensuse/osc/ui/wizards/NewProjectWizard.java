package org.opensuse.osc.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;


public class NewProjectWizard extends BasicNewProjectResourceWizard {
	private static final String OP_ERROR= "ProjectWizard.op_error"; //$NON-NLS-1$

	private NewProjectPage mainPage;
	private IProject newProject;

	public void addPages() {
		mainPage = new NewProjectPage("New OSC Project");
		addPage(mainPage);
	}
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		super.init(workbench, selection);
		setWindowTitle("OSC Project Wizard"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(NewProjectWizard.class, "newfolder_wiz.gif"));
	}

}
