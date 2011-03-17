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
package org.mobicents.eclipslee.servicecreation.wizards.ratype;

import java.util.HashMap;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.mobicents.eclipslee.servicecreation.ui.RaTypeEventsPanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.EventFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.xml.EventJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RaTypeEventsDialog extends Dialog {

  private static final String DIALOG_TITLE = "Select Events fired by Resource Adaptor Type";

  public RaTypeEventsDialog(Shell parent, SbbEventXML[] selectedEvents, String projectName) {
    super(parent);
    setBlockOnOpen(true);
    setEvents(selectedEvents);
    this.projectName = projectName;
    parent.setSize(640, 480);
  }

  protected Control createDialogArea(Composite parent) {
    Composite composite = (Composite) super.createDialogArea(parent);

    panel = new RaTypeEventsPanel(composite, 0, null);

    // Find all available events.
    DTDXML xml[] = EventFinder.getDefault().getComponents(BaseFinder.ALL/* BINARY */, projectName);
    for (int i = 0; i < xml.length; i++) {
      EventJarXML ev = (EventJarXML) xml[i];
      EventXML[] events = ev.getEvents();
      for (int j = 0; j < events.length; j++) {
        panel.addEvent(ev, events[j]);
      }
    }

    // Foreach selected event, select it (and remove from available)
    for (int i = 0; i < selectedEvents.length; i++) {
      panel.select((HashMap) selectedEvents[i]);
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
    selectedEvents = panel.getSelectedEvents();
    super.okPressed();
  }

  public HashMap[] getSelectedEvents() {
    return selectedEvents;
  }

  private void setEvents(SbbEventXML[] events) {
    selectedEvents = new HashMap[events.length];

    for (int i = 0; i < events.length; i++) {

      String tmp[] = events[i].getInitialEventSelectors();
      String selectors[];
      if (events[i].getInitialEventSelectorMethod() != null) {
        selectors = new String[tmp.length + 1];
        for (int j = 0; j < tmp.length; j++) {
          selectors[j] = tmp[j];
        }
        selectors[tmp.length] = "Custom";
      }
      else { 
        selectors = tmp;
      }

      selectedEvents[i] = new HashMap<String, String>();
      selectedEvents[i].put("Name", events[i].getName());
      selectedEvents[i].put("Vendor", events[i].getVendor());
      selectedEvents[i].put("Version", events[i].getVersion());
    }
  }

  private RaTypeEventsPanel panel;
  private HashMap<String, String>[] selectedEvents;
  private String projectName;
}
