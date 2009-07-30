package org.opensuse.osc.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.PackageInfo;

public class NewOSCProjectWizard extends BasicNewProjectResourceWizard {

	protected NewOSCProjectPage mainPage;
	protected NewOSCProjectPage2 secondPage;
	protected NewOSCProjectPage3 thirdPage;

	private IProject newProject;

	public NewOSCProjectPage2 getApiPage() {
		return secondPage;
	}

	@Override
	public void addPages() {
		mainPage = new NewOSCProjectPage("New OSC Project");
		addPage(mainPage);
		secondPage = new NewOSCProjectPage2("New OSC Project Step 2");
		addPage(secondPage);
		thirdPage = new NewOSCProjectPage3("New OSC Project Step 3");
		addPage(thirdPage);

	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// this.selection = selection;
		super.init(workbench, selection);
		setNeedsProgressMonitor(true);
		setWindowTitle("OSC Project Wizard"); //$NON-NLS-1$
		setDefaultPageImageDescriptor(ImageDescriptor.createFromFile(
				NewOSCProjectWizard.class, "newfolder_wiz.gif"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (!invokeRunnable(getRunnable())) {
			return false;
		}
		IResource resource = getSelectedResource();
		selectAndReveal(resource);
		if (resource != null && resource.getType() == IResource.FILE) {
			IFile file = (IFile) resource;
			// Open editor on new file.
			IWorkbenchWindow dw = getWorkbench().getActiveWorkbenchWindow();
			if (dw != null) {
				try {
					IWorkbenchPage page = dw.getActivePage();
					if (page != null)
						IDE.openEditor(page, file, true);
				} catch (PartInitException e) {
					e.printStackTrace();
					MessageDialog.openError(dw.getShell(), "Error", e
							.getMessage());
				}
			}
		}
		return true;
	}

	protected IResource getSelectedResource() {
		return getNewProject();
	}

	public IRunnableWithProgress getRunnable() {
		return new WorkspaceModifyDelegatingOperation(
				new IRunnableWithProgress() {
					public void run(IProgressMonitor imonitor)
							throws InvocationTargetException,
							InterruptedException {
						final Exception except[] = new Exception[1];
						// ugly, need to make the wizard page run in a non ui
						// thread so that this can go away!!!
						getShell().getDisplay().syncExec(new Runnable() {
							public void run() {
								IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(
										new IRunnableWithProgress() {
											public void run(
													IProgressMonitor monitor)
													throws InvocationTargetException,
													InterruptedException {

												final IProgressMonitor fMonitor;
												if (monitor == null) {
													fMonitor = new NullProgressMonitor();
												} else {
													fMonitor = monitor;
												}
												fMonitor.beginTask(
														"Creating project", 3);
												try {
													createNewProject(new SubProgressMonitor(
															fMonitor, 1));
												} catch (CoreException e) {
													except[0] = e;
												}
												fMonitor.done();
											}
										});
								try {
									getContainer().run(false, true, op);
								} catch (InvocationTargetException e) {
									except[0] = e;
								} catch (InterruptedException e) {
									except[0] = e;
								}
							}
						});
						if (except[0] != null) {
							if (except[0] instanceof InvocationTargetException) {
								throw (InvocationTargetException) except[0];
							}
							if (except[0] instanceof InterruptedException) {
								throw (InterruptedException) except[0];
							}
							throw new InvocationTargetException(except[0]);
						}
					}
				});
	}

	/**
	 * Utility method: call a runnable in a WorkbenchModifyDelegatingOperation
	 */
	protected boolean invokeRunnable(IRunnableWithProgress runnable) {
		IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(
				runnable);
		try {
			getContainer().run(true, true, op);
		} catch (InvocationTargetException e) {
			Shell shell = getShell();
			e.printStackTrace();
			MessageDialog.openError(shell, e.toString(), e.getMessage());

			try {
				mainPage.getProjectHandle().delete(false, false, null);
			} catch (CoreException ignore) {
			} catch (UnsupportedOperationException ignore) {
			}
			return false;
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}

	protected void doRun(IProgressMonitor monitor) throws CoreException {
		createNewProject(monitor);
	}

	/**
	 * Creates a new project resource with the selected name.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on the pages currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this wizard caches the new project once it has been
	 * successfully created; subsequent invocations of this method will answer
	 * the same project resource without attempting to create it again.
	 * </p>
	 * 
	 * @return the created project resource, or <code>null</code> if the project
	 *         was not created
	 */
	protected IProject createNewProject(IProgressMonitor monitor)
			throws CoreException {

		if (getNewProject() != null)
			return getNewProject();

		// get a project handle
		IProject newProjectHandle = null;
		try {
			newProjectHandle = mainPage.getProjectHandle();
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		}

		// get a project descriptor
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProjectDescription description = workspace
				.newProjectDescription(newProjectHandle.getName());
		description.setLocation(null);

		PackageInfo pi = new PackageInfo(newProjectHandle);
		pi.refresh();
		pi.setHost(secondPage.getHost());
		pi.setPackageName(thirdPage.getPackageName());
		pi.setProjectName(thirdPage.getProjectName());
		pi.setUsername(secondPage.getUsername());
		pi.setPassword(secondPage.getPassword());

		newProject = Plugin.getDefault().createProject(description,
				newProjectHandle, pi, monitor, "OSC");
		System.out.println("Creating a OSC project.");
		return newProject;

	}

	public static Text createSomeTextGroup(Composite parent, String labelText,
			String initialValue, Listener listener) {

		Composite oscGroup = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		oscGroup.setLayout(layout);
		oscGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// new project label
		Label projectLabel = new Label(oscGroup, SWT.NONE);
		projectLabel.setText(labelText);
		projectLabel.setFont(parent.getFont());

		// new project name entry field
		Text someField = new Text(oscGroup, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = 80;
		someField.setLayoutData(data);
		someField.setFont(parent.getFont());

		// Set the initial value first before listener
		// to avoid handling an event during the creation.
		if (initialValue != null) {
			someField.setText(initialValue);
		}
		if (listener != null)
			someField.addListener(SWT.Modify, listener);
		return someField;

	}
}
