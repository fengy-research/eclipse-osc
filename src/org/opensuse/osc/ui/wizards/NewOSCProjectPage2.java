package org.opensuse.osc.ui.wizards;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class NewOSCProjectPage2 extends WizardPage {
	private Text oscprojectNameField;
	private Text oscpackageNameField;
	private Text hostField;
	private Text usernameField;
	private Text passwordField;

	protected NewOSCProjectPage2(String pagename) {
		super(pagename);
		setTitle("The OSC API Host");
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		initializeDialogUnits(parent);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		hostField = NewOSCProjectWizard.createSomeTextGroup(composite,
				"API Host:", "https://api.opensuse.org", null);
		usernameField = NewOSCProjectWizard.createSomeTextGroup(composite,
				"Username:", "rainwoodman", null);
		passwordField = NewOSCProjectWizard.createSomeTextGroup(composite,
				"Password:", null, null);

		setPageComplete(true);
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	public String getHost() {
		return hostField.getText();
	}

	public String getUsername() {
		return usernameField.getText();
	}

	public String getPassword() {
		return passwordField.getText();
	}

}
