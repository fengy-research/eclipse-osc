package org.opensuse.osc.ui.wizards;



import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewProjectPage extends WizardNewProjectCreationPage
{
  IWorkbench workbench;
  String nature;
  private Text oscprojectNameField;
  private Text oscpackageNameField;
  private Text hostField;
  private Text usernameField;
  private Text passwordField;
  
  
  public NewProjectPage(String pageName)
  {
    super("Project Creation Page");
    setTitle("Create a new OSC project");
  }
  
  @Override
public void createControl(Composite parent) {
	  super.createControl(parent);
	  Composite composite = (Composite) getControl();
	  hostField = createSomeTextGroup(composite, "API Host:", "https://api.opensuse.org", null);
	  usernameField = createSomeTextGroup(composite, "Username:", "rainwoodman", null);
	  passwordField = createSomeTextGroup(composite, "Password:", null, null);
	  oscprojectNameField = createSomeTextGroup(composite, "OSC Project name:", "home:rainwoodman:branches:openSUSE:11.1", null);
	  oscpackageNameField = createSomeTextGroup(composite, "OSC Package name:", "gnome-do", null);
	  Composite listGroup = new Composite(composite, SWT.NONE);
	  GridLayout layout = new GridLayout();
	  layout.numColumns=2;
	  
	  listGroup.setLayout(layout);
	  Combo combo = new Combo(listGroup, SWT.DROP_DOWN);
	  
	  combo.setItems(new String[]{"shit", "fuck"});
  }
  
  public String getOSCProjectName() {
	  return oscprojectNameField.getText();	  
  }
  public String getOSCPackageName() {
	  return oscpackageNameField.getText();
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
  private Text createSomeTextGroup(Composite parent, String labelText, String initialValue, Listener listener) {
	  
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
      if(listener != null)
    	  someField.addListener(SWT.Modify, listener);      
	  return someField;
	  
  }

}

