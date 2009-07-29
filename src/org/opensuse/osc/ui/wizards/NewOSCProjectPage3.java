package org.opensuse.osc.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opensuse.osc.api.Api;
import org.opensuse.osc.api.Host;
import org.opensuse.osc.api.OSCException;

public class NewOSCProjectPage3 extends WizardPage {
	public NewOSCProjectPage3(String pageName) {
		super(pageName);
		setTitle("Select a Package");
	}

	protected org.eclipse.swt.widgets.List projectNameList;
	protected org.eclipse.swt.widgets.List packageNameList;
	protected Text projectNameField;
	protected Text packageNameField;

	public String getProjectName() {
		return projectNameField.getText();
	}

	public String getPackageName() {
		return packageNameField.getText();
	}

	public org.eclipse.swt.widgets.List createSomeList(Composite composite,
			String label, String buttonLabel, SelectionListener listListener,
			SelectionListener buttonListener) {
		Composite listGroup = new Composite(composite, SWT.FILL);
		org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
				composite, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL
						| SWT.H_SCROLL);
		GridData gridData = new GridData();
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.horizontalAlignment = SWT.FILL;
		list.addSelectionListener(listListener);
		list.setLayoutData(gridData);
		GridLayout listLayout = new GridLayout();
		listLayout.numColumns = 2;
		listGroup.setLayout(listLayout);
		Label listLabel = new Label(listGroup, SWT.NONE);
		listLabel.setText(label);
		Button listButton = new Button(listGroup, SWT.PUSH);
		listButton.setText(buttonLabel);
		listButton.addSelectionListener(buttonListener);

		return list;
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		initializeDialogUnits(parent);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		projectNameField = NewOSCProjectWizard.createSomeTextGroup(composite,
				"Project Name:", "home:rainwoodman:branches:openSUSE:11.1",
				null);

		projectNameList = createSomeList(composite, "Select from the server",
				"Fetch", new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						String[] selected = projectNameList.getSelection();
						if (selected.length > 0) {
							projectNameField.setText(selected[0]);
						}
					}
				}, new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						fetchProjects();
					}
				});

		packageNameField = NewOSCProjectWizard.createSomeTextGroup(composite,
				"Package name:", "gnome-do", null);

		packageNameList = createSomeList(composite, "Select from the server",
				"Fetch", new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						String[] selected = packageNameList.getSelection();
						if (selected.length > 0) {
							packageNameField.setText(selected[0]);
						}
					}
				}, new SelectionAdapter() {
					public void widgetSelected(SelectionEvent event) {
						fetchPackages();
					}
				});

		setPageComplete(true);
		// Show description on opening
		setErrorMessage(null);
		setMessage(null);
		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	public void fetchProjects() {
		IWizardContainer container = getContainer();
		NewOSCProjectWizard wizard = (NewOSCProjectWizard) getWizard();
		final String hostURL = wizard.secondPage.getHost();
		final String username = wizard.secondPage.getUsername();
		final String password = wizard.secondPage.getPassword();

		try {
			container.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Getting Project List", 2);
					try {

						Api api = new Api();
						api.setURL(hostURL);
						api.login(username, password);
						Host host = api.getHost();
						host.refresh();
						monitor.worked(1);
						List<String> names = host.getProjects();
						final String[] items = (String[]) names
								.toArray(new String[0]);
					
						
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								projectNameList.setItems(items);
							}
						});
						monitor.done();
					} catch (OSCException e) {
						throw new InvocationTargetException(e);
					}

				}
			});
		} catch (InvocationTargetException e) {
			setErrorMessage(e.getMessage());
		} catch (InterruptedException e) {
			setErrorMessage(e.getMessage());

		}
	}

	public void fetchPackages() {
		IWizardContainer container = getContainer();

		final String projectName = getProjectName();
		NewOSCProjectWizard wizard = (NewOSCProjectWizard) getWizard();
		final String hostURL = wizard.secondPage.getHost();
		final String username = wizard.secondPage.getUsername();
		final String password = wizard.secondPage.getPassword();
		try {
			container.run(true, true, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Getting Package List", 3);
					try {
						Api api = new Api();
						api.setURL(hostURL);
						api.login(username, password);
						Host host = api.getHost();
						
						org.opensuse.osc.api.Project project = host
								.getProject(projectName);
						project.refresh();
		
						List<String> names = project.getPackages();
						monitor.worked(1);

						final String[] items = (String[]) names
								.toArray(new String[0]);
				
						Display.getDefault().syncExec(new Runnable() {
							public void run() {
								packageNameList.setItems(items);
							}
						});
						monitor.done();
					} catch (OSCException e) {
						setErrorMessage(e.toString());
						monitor.setCanceled(true);
					}

				}
			});
		} catch (InvocationTargetException e) {
			setErrorMessage(e.getMessage());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			setErrorMessage(e.getMessage());

		}
	}

}
