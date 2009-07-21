/*******************************************************************************
 * Copyright (c) 2000, 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *******************************************************************************/

package org.opensuse.osc.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.opensuse.osc.Plugin;

public class ProjectNature implements IProjectNature {

  public static final String NATURE_ID = Plugin.PLUGIN_ID + ".nature"; //$NON-NLS-1$

  private IProject fProject;

  public ProjectNature() {
  }

  public ProjectNature(IProject project) {
    setProject(project);
  }

  public static void addNature(IProject project, IProgressMonitor mon) throws CoreException {
    addNature(project, NATURE_ID, mon);
  }

  public static void removeNature(IProject project, IProgressMonitor mon) throws CoreException {
    removeNature(project, NATURE_ID, mon);
  }

  /**
   * Utility method for adding a nature to a project.
   * 
   * @param proj
   *            the project to add the nature
   * @param natureId
   *            the id of the nature to assign to the project
   * @param monitor
   *            a progress monitor to indicate the duration of the operation,
   *            or <code>null</code> if progress reporting is not required.
   *  
   */
  public static void addNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
    IProjectDescription description = project.getDescription();
    String[] prevNatures = description.getNatureIds();
    for (int i = 0; i < prevNatures.length; i++) {
      if (natureId.equals(prevNatures[i]))
        return;
    }
    String[] newNatures = new String[prevNatures.length + 1];
    System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
    newNatures[prevNatures.length] = natureId;
    description.setNatureIds(newNatures);
    project.setDescription(description, monitor);
  }

  /**
   * Utility method for removing a project nature from a project.
   * 
   * @param proj
   *            the project to remove the nature from
   * @param natureId
   *            the nature id to remove
   * @param monitor
   *            a progress monitor to indicate the duration of the operation,
   *            or <code>null</code> if progress reporting is not required.
   */
  public static void removeNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
    IProjectDescription description = project.getDescription();
    String[] prevNatures = description.getNatureIds();
    ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(prevNatures));
	List<String> newNatures = arrayList;
    newNatures.remove(natureId);
    description.setNatureIds(newNatures.toArray(new String[newNatures.size()]));
    project.setDescription(description, monitor);
  }

  /**
   * @see IProjectNature#configure
   */
  public void configure() throws CoreException {
  }

  /**
   * @see IProjectNature#deconfigure
   */
  public void deconfigure() throws CoreException {
  }

  /**
   * @see IProjectNature#getProject
   */
  public IProject getProject() {
    return fProject;
  }

  /**
   * @see IProjectNature#setProject
   */
  public void setProject(IProject project) {
    fProject = project;
  }
}