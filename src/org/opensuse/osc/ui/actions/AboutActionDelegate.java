package org.opensuse.osc.ui.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.opensuse.osc.Plugin;
import org.opensuse.osc.core.OSCProject;
import org.opensuse.osc.core.PackageInfo;

public class AboutActionDelegate implements IObjectActionDelegate {
	private IWorkbenchPart targetPart;
	private IStructuredSelection selection;
	private IResource resource;
	
	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		this.targetPart = targetPart;
	}

	@Override
	public void run(IAction action) {
		// TODO Auto-generated method stub
		System.out.println("Action run");
		if(resource instanceof IProject) {
			OSCProject p;
			try {
				p = Plugin.getModel().getProject((IProject)resource);
				PackageInfo packageInfo = p.getPackageInfo();
				System.out.println(packageInfo.getApiPackage().toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
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
