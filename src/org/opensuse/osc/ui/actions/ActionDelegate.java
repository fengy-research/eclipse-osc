package org.opensuse.osc.ui.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class ActionDelegate implements IObjectActionDelegate {
	protected IResource resource;
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		Job job = new Job("OSC Job") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				return ActionDelegate.this.run(monitor);
			}
			
		};
		job.schedule();
		
	}
	public abstract IStatus run(IProgressMonitor monitor);

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub
		// Selection containing elements
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection iss = (IStructuredSelection) selection;
			action.setEnabled(iss.size() == 1);
			resource = (IResource) iss.getFirstElement();
		} else {
			// Other selections, for example containing text or of other kinds.
			action.setEnabled(false);
		}
	}

}
