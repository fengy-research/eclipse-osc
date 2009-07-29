package org.opensuse.osc.ui.wizards;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewOSCProjectPage extends WizardNewProjectCreationPage {
	IWorkbench workbench;
	String nature;

	public NewOSCProjectPage(String pagename) {
		super(pagename);
		setTitle("Create a new OSC project");
	}

}
