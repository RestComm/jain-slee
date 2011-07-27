/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.eclipslee.servicecreation.wizards.ratype;

import java.util.ArrayList;
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
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeClassesXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RaTypeWizard extends BaseWizard {

  public static final String RATYPE_ACIF_TEMPLATE = "/templates/RATypeACIF.template";
  public static final String RATYPE_PROVIDER_TEMPLATE = "/templates/RAProvider.template";
  public static final String RATYPE_ACTIVITY_TYPE_TEMPLATE = "/templates/RATypeActivityType.template";

  private RaTypeEventsPage raTypeEventsPage;
  private RaTypeActivityTypesPage raTypeActivityTypesPage;
  private RaTypeLibraryPage raTypeLibrariesPage;

  private HashMap[] raTypeEvents;
  private HashMap[] raTypeLibraries;
  private HashMap[] raActivityTypes;
  private boolean createRaInterface;

  public RaTypeWizard() {
    super();
    WIZARD_TITLE = "JAIN SLEE Resource Adaptor Type Wizard";
    ENDS = "ActivityContextInterfaceFactory.java";
  }

  public void addPages() {
    super.addPages(); // adds filename and name, vendor, version pages
    // Event Types Def
    raTypeLibrariesPage = new RaTypeLibraryPage(WIZARD_TITLE);
    addPage(raTypeLibrariesPage);
    // Event Types Def
    raTypeEventsPage = new RaTypeEventsPage(WIZARD_TITLE);
    addPage(raTypeEventsPage);
    // Activity Types
    raTypeActivityTypesPage = new RaTypeActivityTypesPage(WIZARD_TITLE);
    addPage(raTypeActivityTypesPage);
  }

  public boolean performFinish() {
    // Extract the data from the various pages.
    raTypeLibraries = raTypeLibrariesPage.getSelectedLibraries();

    raTypeEvents = raTypeEventsPage.getSelectedEvents();

    raActivityTypes = raTypeActivityTypesPage.getActivityTypes();

    createRaInterface = raTypeActivityTypesPage.getCreateAbstractClass(); 

    return super.performFinish();
  }

  public void doFinish(IProgressMonitor monitor) throws CoreException {
    try {
      IFolder folder = getSourceContainer().getFolder(new Path(""));//.getFolder(new Path(this.getPackageName().replaceAll("\\.", "/")));
      
      // This allows implicit package creation
      for(String path : this.getPackageName().split("\\.")) {
        folder = folder.getFolder(path);
        if(!folder.exists()) {
          folder.create(true, true, monitor);
        }
      }

      String raTypeBaseName = getFileName().substring(0, getFileName().indexOf(ENDS));
      String xmlFilename = /*raTypeBaseName + "-*/"resource-adaptor-type-jar.xml";
      String raTypeAcifFilename = getFileName();
      String raInterfaceFilename = raTypeBaseName + "Provider.java";
      
      String raTypeAcifClassName = Utils.getSafePackagePrefix(getPackageName()) + raTypeBaseName + "ActivityContextInterfaceFactory";
      String raInterfaceClassName = Utils.getSafePackagePrefix(getPackageName()) + raTypeBaseName + "Provider";

      // Calculate the number of stages
      int stages = 2; // ACIF + XML
      if (raTypeEvents.length > 0) stages++;
      if (createRaInterface) stages++;
      
      monitor.beginTask("Creating Resource Adaptor Type: " + raTypeBaseName, stages);

      // Substitution map
      HashMap<String, String> subs = new HashMap<String, String>();
      subs.put("__NAME__", raTypeBaseName);
      subs.put("__PACKAGE__", Utils.getPackageTemplateValue(getPackageName()));

      // ...
      subs.put("__ACI_GETTERS__", getAciGetters(raActivityTypes));
      
      // Get (or create if not present already) META-INF folder for storing resource-adaptor-type-jar.xml
      IFolder resourceFolder = getSourceContainer().getFolder(new Path("../resources/META-INF"));
      if (!resourceFolder.exists()) {
        resourceFolder.create(true, true, monitor);
      }

      // Reuse existing XML descriptor file if present or create new one
      IFile raTypeJarFile = resourceFolder.getFile(xmlFilename);
      // We must create the RaType XML before trying to add events.
      ResourceAdaptorTypeJarXML raTypeJarXML = raTypeJarFile.exists() ? new ResourceAdaptorTypeJarXML(raTypeJarFile) : new ResourceAdaptorTypeJarXML();
      ResourceAdaptorTypeXML raType = raTypeJarXML.addResourceAdaptorType();
      
      // Create the RA Type XML.
      raType.setName(getComponentName());
      raType.setVendor(getComponentVendor());
      raType.setVersion(getComponentVersion());
      raType.setDescription(getComponentDescription());
      
      ResourceAdaptorTypeClassesXML raTypeClassesXML = raType.addResourceAdaptorTypeClasses();
      raTypeClassesXML.setActivityContextInterfaceFactoryInterface(raTypeAcifClassName);

      for (HashMap activityType : raActivityTypes) {
        raTypeClassesXML.addActivityType((String) activityType.get("Activity Type"));
      }

      if(createRaInterface) {
        raTypeClassesXML.setResourceAdaptorInterface(raInterfaceClassName);
      }

      // Libraries
      for (HashMap raTypeLibrary : raTypeLibraries) {
        LibraryJarXML xml = (LibraryJarXML) raTypeLibrary.get("XML");
        String name = (String) raTypeLibrary.get("Name");
        String vendor = (String) raTypeLibrary.get("Vendor");
        String version = (String) raTypeLibrary.get("Version");
        raType.addLibraryRef(xml.getLibrary(name, vendor, version));
      }

      // Events
      for (HashMap raTypeEvent : raTypeEvents) {
        String eventName = (String) raTypeEvent.get("Name");
        String eventVendor = (String) raTypeEvent.get("Vendor");
        String eventVersion = (String) raTypeEvent.get("Version");
        raType.addEvent(eventName, eventVendor, eventVersion);
      }

      final IFile raTypeAcifFile;
      final IFile raInterfaceFile;
      final ArrayList<IFile> activityTypeFiles = new ArrayList<IFile>();

      // Create the ACIF file.
      raTypeAcifFile = FileUtil.createFromTemplate(folder, new Path(raTypeAcifFilename), new Path(RATYPE_ACIF_TEMPLATE), subs, monitor);
      monitor.worked(1); // done with ACIF. worked++

      // Create Activity Types
      for (HashMap activityType : raActivityTypes) {
        if((Boolean) activityType.get("Create")) {
          String type =  (String) activityType.get("Activity Type");
          String pakkage = type.substring(0, type.lastIndexOf('.'));
          String clazz = type.replace(pakkage+ ".", "");

          IFolder actFolder = getSourceContainer().getFolder(new Path(""));
          
          // This allows implicit package creation
          for(String path : pakkage.split("\\.")) {
            actFolder = actFolder.getFolder(path);
            if(!actFolder.exists()) {
              actFolder.create(true, true, monitor);
            }
          }

          subs.put("__ACTIVITY_PACKAGE__", Utils.getPackageTemplateValue(pakkage));
          subs.put("__ACTIVITY_NAME__", clazz);
          activityTypeFiles.add(FileUtil.createFromTemplate(actFolder, new Path(clazz + ".java"), new Path(RATYPE_ACTIVITY_TYPE_TEMPLATE), subs, monitor));
          subs.remove("__ACTIVITY_PACKAGE__");
          subs.remove("__ACTIVITY_NAME__");
        }
      }
      
      // Create the RA Interface file.
      if(createRaInterface) {
        raInterfaceFile = FileUtil.createFromTemplate(folder, new Path(raInterfaceFilename), new Path(RATYPE_PROVIDER_TEMPLATE), subs, monitor);
        monitor.worked(1); // done with RA Interface. worked++
      }
      else {
        raInterfaceFile = null;
      }

      // Done with XML. worked++
      FileUtil.createFromInputStream(resourceFolder, new Path(xmlFilename), raTypeJarXML.getInputStreamFromXML(), monitor);
      monitor.worked(1);

      // Open files for editing
      monitor.setTaskName("Opening JAIN SLEE Resource Adaptor Type for editing...");
      getShell().getDisplay().asyncExec(new Runnable() {
        public void run() {
          IWorkbenchPage page =
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          try {
            IDE.openEditor(page, raTypeAcifFile, true);
            for(IFile activityTypeFile : activityTypeFiles) {
              IDE.openEditor(page, activityTypeFile, true);
            }
            if (createRaInterface) {
              IDE.openEditor(page, raInterfaceFile, true);
            }
          }
          catch (PartInitException e) {
          }
        }
      });
      monitor.worked(1);
    }
    catch (Exception e) {
      throw newCoreException("Unable to create resource adaptor type", e);
    }
  }

  public static String getAciGetters(HashMap[] types) {
    String methods = "";
    
    for (int i = 0; i < types.length; i++) {
      HashMap map = types[i];
      
      String activityType = (String) map.get("Activity Type");
      
      methods += "\n\tpublic ActivityContextInterface getActivityContextInterface(\n" +
      "\t\t" + activityType + " activity) throws NullPointerException,\n" +
      "\t\tUnrecognizedActivityException, FactoryException;\n";
    }

    return methods;
  }
}
