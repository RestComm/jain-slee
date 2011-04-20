/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors by the
 * @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.eclipslee.servicecreation.wizards.ra;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.ResourceAdaptorRaTypesPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ResourceAdaptorTypeFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorRaTypesDialog extends Dialog {

  private static final String DIALOG_TITLE = "Select the Resource Adaptor Types that this Resource implements";

  public ResourceAdaptorRaTypesDialog(Shell parent, ResourceAdaptorResourceAdaptorTypeXML[] selectedRaTypes, String projectName) {
    super(parent);
    setBlockOnOpen(true);
    setRaTypes(selectedRaTypes);
    this.projectName = projectName;
    parent.setSize(640, 480);
  }

  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);

    panel = new ResourceAdaptorRaTypesPanel(composite, 0, null);

    // Find all available ratypes.
    DTDXML xml[] = ResourceAdaptorTypeFinder.getDefault().getComponents(BaseFinder.ALL/* BINARY */, projectName);
    for (int i = 0; i < xml.length; i++) {
      ResourceAdaptorTypeJarXML raTypeJar = (ResourceAdaptorTypeJarXML) xml[i];
      ResourceAdaptorTypeXML[] raTypes = raTypeJar.getResourceAdaptorTypes();
      for (int j = 0; j < raTypes.length; j++) {
        panel.addResourceAdaptorType(raTypeJar, raTypes[j]);
      }
    }

    // Foreach selected ra type, select it (and remove from available)
    for (int i = 0; i < selectedRaTypes.length; i++) {
      panel.select((HashMap) selectedRaTypes[i]);
    }

    panel.repack();

    composite.setSize(640, 480);
    return composite;
  }

  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(DIALOG_TITLE);
  }

  protected void setShellStyle(int newStyle) {
    super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX);
  }

  public void okPressed() {
    selectedRaTypes = panel.getSelectedRaTypes();
    super.okPressed();
  }

  public HashMap[] getSelectedRaTypes() {
    return selectedRaTypes;
  }

  private void setRaTypes(ResourceAdaptorResourceAdaptorTypeXML[] raTypes) {
    selectedRaTypes = new HashMap[raTypes.length];

    for (int i = 0; i < raTypes.length; i++) {
      selectedRaTypes[i] = new HashMap<String, String>();
      selectedRaTypes[i].put("Name", raTypes[i].getName());
      selectedRaTypes[i].put("Vendor", raTypes[i].getVendor());
      selectedRaTypes[i].put("Version", raTypes[i].getVersion());
    }
  }

  private ResourceAdaptorRaTypesPanel panel;
  private HashMap<String, String>[] selectedRaTypes;
  private String projectName;
}
