/**
 *   Copyright 2005 Open Cloud Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.mobicents.eclipslee.servicecreation.wizards.event;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.wizards.generic.BaseWizard;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;

/**
 * Wizard for JAIN SLEE Events
 *  
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventWizard extends BaseWizard {

  private static final String EVENT_TEMPLATE = "/templates/Event.template";

  private EventLibraryPage eventLibraryPage;
  private HashMap[] libraries; 

  /**
   * Constructor for EventWizard.
   */
  public EventWizard() {
    super();
    setNeedsProgressMonitor(true);
    WIZARD_TITLE = "JAIN SLEE Event Wizard";
    ENDS = "Event.java";
    MAVEN_MODULE = "events";
  }

  public void addPages() {
    super.addPages();

    eventLibraryPage = new EventLibraryPage(WIZARD_TITLE);
    addPage(eventLibraryPage);
  }    

  public boolean performFinish() {
    // Extract the data from the various pages.
    libraries = eventLibraryPage.getSelectedLibraries();

    return super.performFinish();   
  }

  /**
   * The worker method. It will find the container, create the
   * file if missing or just replace its contents, and open
   * the editor on the newly created file.
   */
  public void doFinish(IProgressMonitor monitor) throws CoreException {

    String eventClassName = Utils.getSafePackagePrefix(getPackageName()) + getFileName().substring(0, getFileName().indexOf(".java"));
    //String baseName = getFileName().substring(0, getFileName().indexOf(ENDS));

    // Create the Event Java file.
    monitor.beginTask("Creating JAIN SLEE Event " + getComponentName(), 4);

    HashMap<String, String> subs = new HashMap<String, String>();
    subs.put("__PACKAGE__", Utils.getPackageTemplateValue(getPackageName()));
    subs.put("__NAME__", getFileName().substring(0, getFileName().length() - ENDS.length()));

    IFolder folder = getSourceContainer().getFolder(new Path(""));//.getFolder(new Path(this.getPackageName().replaceAll("\\.", "/")));

    // This allows implicit package creation
    for(String path : this.getPackageName().split("\\.")) {
      folder = folder.getFolder(path);
      if(!folder.exists()) {
        folder.create(true, true, monitor);
      }
    }

    final IFile javaFile;
    try {
      javaFile = FileUtil.createFromTemplate(folder, new Path(getFileName()), new Path(EVENT_TEMPLATE), subs, monitor);		
    }
    catch (IOException e) {
      e.printStackTrace();
      throw newCoreException("Failed to create JAIN SLEE Event Java: ", e);			
    }
    catch (Exception e) {
      e.printStackTrace();
      return;
    }
    monitor.worked(1);

    // Create the event-jar.xml file.
    monitor.setTaskName("Creating JAIN SLEE Event XML");		
    try {
      // Get (or create if not present already) META-INF folder for storing event-jar.xml
      IFolder resourceFolder = getSourceContainer().getFolder(new Path("../resources/META-INF"));
      if (!resourceFolder.exists()) {
        resourceFolder.create(true, true, monitor);
      }

      // Reuse existing XML descriptor file if present or create new one
      IFile eventJarFile = resourceFolder.getFile("event-jar.xml");
      EventJarXML eventXML = eventJarFile.exists() ? new EventJarXML(eventJarFile) : new EventJarXML();
      EventXML event = eventXML.addEvent(getComponentName(), getComponentVendor(), getComponentVersion(), getComponentDescription(), eventClassName);

      // Libraries
      for (int i = 0; i < libraries.length; i++) {
        LibraryJarXML xml = (LibraryJarXML) libraries[i].get("XML");
        String name = (String) libraries[i].get("Name");
        String vendor = (String) libraries[i].get("Vendor");
        String version = (String) libraries[i].get("Version");
        eventXML.addLibraryRef(xml.getLibrary(name, vendor, version));
      }

      FileUtil.createFromInputStream(resourceFolder, new Path(/*baseName + "-*/"event-jar.xml"), eventXML.getInputStreamFromXML(), monitor);
    }
    catch (IOException e) {
      throwCoreException("Failed to create JAIN SLEE Event XML: ", e);
    }
    catch (Exception e) {
      throwCoreException("Failed to create JAIN SLEE Event XML: ", e);
    }
    monitor.worked(1);

    // Open the .java file for editing
    monitor.setTaskName("Opening JAIN SLEE Event Java file for editing...");
    getShell().getDisplay().asyncExec(new Runnable() {
      public void run() {
        IWorkbenchPage page =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
          IDE.openEditor(page, javaFile, true);
        } catch (PartInitException e) {
        }
      }
    });
    monitor.worked(1);
  }

}