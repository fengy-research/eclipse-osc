package org.opensuse.osc.ui.wizards;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
public class NewProjectPage extends WizardNewProjectCreationPage
{
  IWorkbench workbench;
  String nature;

  public NewProjectPage(String pageName)
  {
    super("Project Creation Page");
    setTitle("Create a new OSC project");
  }
}

