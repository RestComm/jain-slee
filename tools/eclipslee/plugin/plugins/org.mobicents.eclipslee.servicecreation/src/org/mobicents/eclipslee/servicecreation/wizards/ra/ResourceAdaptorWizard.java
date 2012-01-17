/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorClassXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorClassesXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorXML;
import org.mobicents.eclipslee.xml.DeployConfigXML;
import org.mobicents.eclipslee.xml.LibraryJarXML;
import org.mobicents.eclipslee.xml.LibraryPomXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorJarXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;

/**
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ResourceAdaptorWizard extends BaseWizard {

  public static final String RESOURCE_ADAPTOR_TEMPLATE = "/templates/ResourceAdaptor.template";
  public static final String RESOURCE_ADAPTOR_PROVIDER_IMPL_TEMPLATE = "/templates/ResourceAdaptorProviderImpl.template";
  public static final String RESOURCE_ADAPTOR_MARSHALER_TEMPLATE = "/templates/ResourceAdaptorMarshaler.template";

  private ResourceAdaptorRaTypesPage resourceAdaptorRaTypesPage;
  private ResourceAdaptorConfigPropertiesPage resourceAdaptorConfigPropertiesPage;
  private ResourceAdaptorLibraryPage resourceAdaptorLibrariesPage;

  private HashMap[] resourceAdaptorRaTypes;
  private HashMap[] resourceAdaptorLibraries;
  private HashMap[] resourceAdaptorConfigProperties;
  private boolean resourceAdaptorSupportActiveReconfig;
  private boolean faultTolerantResourceAdaptor;

  public ResourceAdaptorWizard() {
    super();
    WIZARD_TITLE = "JAIN SLEE Resource Adaptor Wizard";
    ENDS = "ResourceAdaptor.java";
  }

  public void addPages() {
    super.addPages(); // adds filename and name, vendor, version pages
    // Libraries
    resourceAdaptorLibrariesPage = new ResourceAdaptorLibraryPage(WIZARD_TITLE);
    addPage(resourceAdaptorLibrariesPage);
    // RA Types Def
    resourceAdaptorRaTypesPage = new ResourceAdaptorRaTypesPage(WIZARD_TITLE);
    addPage(resourceAdaptorRaTypesPage);
    // Config Properties
    resourceAdaptorConfigPropertiesPage = new ResourceAdaptorConfigPropertiesPage(WIZARD_TITLE);
    addPage(resourceAdaptorConfigPropertiesPage);
  }

  public boolean performFinish() {
    // Extract the data from the various pages.

    resourceAdaptorRaTypes = resourceAdaptorRaTypesPage.getSelectedRaTypes();

    resourceAdaptorLibraries = resourceAdaptorLibrariesPage.getSelectedLibraries();

    resourceAdaptorConfigProperties = resourceAdaptorConfigPropertiesPage.getConfigProperties();

    resourceAdaptorSupportActiveReconfig = resourceAdaptorConfigPropertiesPage.getActiveReconfiguration();
    
    faultTolerantResourceAdaptor = filePage.getFaultTolerantResourceAdaptor();

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

      String resourceAdaptorBaseName = getFileName().substring(0, getFileName().indexOf(ENDS));
      String xmlFilename = /*resourceAdaptorBaseName + "-*/"resource-adaptor-jar.xml";
      String deployConfigXmlFilename = /*resourceAdaptorBaseName + "-*/"deploy-config.xml";
      String resourceAdaptorFilename = getFileName();
      String resourceAdaptorMarshalerFilename = resourceAdaptorBaseName + "Marshaler.java";

      String resourceAdaptorClassName = Utils.getSafePackagePrefix(getPackageName()) + resourceAdaptorBaseName + "ResourceAdaptor";
      String resourceAdaptorMarshalerName = Utils.getSafePackagePrefix(getPackageName()) + resourceAdaptorBaseName + "Marshaler";

      // Calculate the number of stages
      int stages = 3; // RA Class + Marshaler + XML

      monitor.beginTask("Creating Resource Adaptor: " + resourceAdaptorBaseName, stages);

      // Substitution map
      HashMap<String, String> subs = new HashMap<String, String>();
      subs.put("__NAME__", resourceAdaptorBaseName);
      subs.put("__PACKAGE__", Utils.getPackageTemplateValue(getPackageName()));

      // Get (or create if not present already) META-INF folder for storing resource-adaptor-jar.xml
      IFolder resourceFolder = getSourceContainer().getFolder(new Path("../resources/META-INF"));
      if (!resourceFolder.exists()) {
        resourceFolder.create(true, true, monitor);
      }

      // Reuse existing XML descriptor file if present or create new one
      IFile resourceAdaptorJarFile = resourceFolder.getFile(xmlFilename);
      // We must create the Resource Adaptor XML ..
      ResourceAdaptorJarXML resourceAdaptorJarXML = resourceAdaptorJarFile.exists() ? new ResourceAdaptorJarXML(resourceAdaptorJarFile) : new ResourceAdaptorJarXML();
      ResourceAdaptorXML resourceAdaptorXML = resourceAdaptorJarXML.addResourceAdaptor();

      // Create the Resource Adaptor XML.
      resourceAdaptorXML.setName(getComponentName());
      resourceAdaptorXML.setVendor(getComponentVendor());
      resourceAdaptorXML.setVersion(getComponentVersion());
      resourceAdaptorXML.setDescription(getComponentDescription());

      ResourceAdaptorClassesXML resourceAdaptorClassesXML = resourceAdaptorXML.addResourceAdaptorClasses();
      ResourceAdaptorClassXML resourceAdaptorClassXML = resourceAdaptorClassesXML.addResourceAdaptorClass(resourceAdaptorSupportActiveReconfig);
      resourceAdaptorClassXML.setResourceAdaptorClassName(resourceAdaptorClassName);

      ArrayList<String> raTypeResourceAdaptorInterfaces = new ArrayList<String>();

      for (HashMap raType : resourceAdaptorRaTypes) {
        String raTypeName = (String) raType.get("Name");
        String raTypeVendor = (String) raType.get("Vendor");
        String raTypeVersion = (String) raType.get("Version");
        resourceAdaptorXML.addResourceAdaptorType(raTypeName, raTypeVendor, raTypeVersion);

        ResourceAdaptorTypeJarXML raTypeJarXML = (ResourceAdaptorTypeJarXML) raType.get("XML");
        ResourceAdaptorTypeXML raTypeXML = raTypeJarXML.getResourceAdaptorType(raTypeName, raTypeVendor, raTypeVersion);
        raTypeResourceAdaptorInterfaces.add(raTypeXML.getResourceAdaptorTypeClasses().getResourceAdaptorInterface());
      }

      // Libraries
      for (HashMap resourceAdaptorLibrary : resourceAdaptorLibraries) {
        Object entryXML = resourceAdaptorLibrary.get("XML");
        String name = (String) resourceAdaptorLibrary.get("Name");
        String vendor = (String) resourceAdaptorLibrary.get("Vendor");
        String version = (String) resourceAdaptorLibrary.get("Version");
        if(entryXML instanceof LibraryJarXML) {
          LibraryJarXML xml = (LibraryJarXML) entryXML;
          resourceAdaptorXML.addLibraryRef(xml.getLibrary(name, vendor, version));
        }
        else if(entryXML instanceof LibraryPomXML){
          LibraryPomXML xml = (LibraryPomXML) entryXML;
          resourceAdaptorXML.addLibraryRef(xml.getLibrary(name, vendor, version));
        }
      }

      String subConfigProperties = "";
      String subRaConfigureMethod = "";

      for (HashMap configProperty : resourceAdaptorConfigProperties) {
        String configPropertyName = (String) configProperty.get("Name");
        String configPropertyType = (String) configProperty.get("Type");
        String configPropertyDefaultValue = (String) configProperty.get("Default Value");
        resourceAdaptorXML.addConfigProperty(configPropertyName, configPropertyType, configPropertyDefaultValue);
        // FIXME: Make this a method in ConfigPropertiesUtil...
        /*
        String value = "null";
        if(configPropertyType.equals("java.lang.String")) {
          value = "\"" + configPropertyDefaultValue + "\"";
        }
        else if (configPropertyType.equals("java.lang.Character")) {
          value = "'" + configPropertyDefaultValue + "'";
        }
        else if (configPropertyType.equals("java.lang.Long")) {
          value = configPropertyDefaultValue + "L";
        }
        else if (configPropertyType.equals("java.lang.Float")) {
          value = configPropertyDefaultValue + "F";  
        }
        else if (configPropertyType.equals("java.lang.Double")) {
          value = configPropertyDefaultValue + "D";  
        }
        else if (configPropertyType.equals("java.lang.Boolean")) {
          value = configPropertyDefaultValue.toLowerCase();
        }
        else {
          value = configPropertyDefaultValue;
        }
         */
        String simpleType = configPropertyType.replaceAll("java\\.lang\\.", "");

        subConfigProperties += "\tprivate " + simpleType + " " + configPropertyName /* + " = " + value  */ + ";\n";

        subRaConfigureMethod += "\n\t\tthis." + configPropertyName + " = (" + simpleType + ") properties.getProperty(\"" + configPropertyName + "\").getValue();\n";
        /*subRaConfigureMethod += "\t\tif(this." + configPropertyName + ".equals(\"\")) {\n";
        subRaConfigureMethod += "\t\t\t// TODO: define behavior when value is not present\n";
        subRaConfigureMethod += "\t\t\tthis." + configPropertyName + " = null;\n";
        subRaConfigureMethod += "\t\t}\n";*/
      }

      subs.put("__CONFIG_PROPERTIES__", subConfigProperties);
      subs.put("__RA_CONFIGURE_IMPL__", subRaConfigureMethod);
      
      if(faultTolerantResourceAdaptor) {
        subs.put("__FAULT_TOLERANT_IMPORT__", "\nimport java.io.Serializable;\n\nimport org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptor;\nimport org.mobicents.slee.resource.cluster.FaultTolerantResourceAdaptorContext;\n");
        subs.put("__RA_INTERFACE__", "FaultTolerantResourceAdaptor<Serializable, Serializable>");
        subs.put("__FAULT_TOLERANT_IMPLEMENTATION__", "\n\t// Fault Tolerant RA Callbacks --------------------------------------------\n" +
            "\n" +
        		"\tprivate FaultTolerantResourceAdaptorContext ftRaContext;\n" +
            "\n" +
            "\tpublic void setFaultTolerantResourceAdaptorContext(FaultTolerantResourceAdaptorContext<Serializable, Serializable> context) {\n" +
            "\t\tthis.ftRaContext = context;\n" +
            "\t}\n" +
            "\n" +
            "\tpublic void unsetFaultTolerantResourceAdaptorContext() {\n" +
            "\t\tthis.ftRaContext = null;\n" +
            "\t}\n" +
            "\n" +
            "\tpublic void dataRemoved(Serializable key) {\n" +
            "\t\t// TODO: implement this optional callback from SLEE when the replicated\n" +
            "\t\t//       data key was removed from the cluster, this may be helpful when\n" +
            "\t\t//       the local RA maintains local state.\n" +
            "\t}\n" +
            "\n" +
            "\tpublic void failOver(Serializable key) {\n" +
            "\t\t// TODO: implement the callback from SLEE when the local RA was selected\n" +
            "\t\t//       to recover the state for a replicated data key, which was owned\n" +
            "\t\t//       by a cluster member that failed.\n" +
            "\t}\n");
      }
      else {
        subs.put("__FAULT_TOLERANT_IMPORT__", "");
        subs.put("__RA_INTERFACE__", "ResourceAdaptor");
        subs.put("__FAULT_TOLERANT_IMPLEMENTATION__", "");
      }

      final IFile resourceAdaptorFile;
      final IFile resourceAdaptorMarshalerFile;
      final ArrayList<IFile> raTypeRaInterfaceImplFiles = new ArrayList<IFile>();
      //final IFile raInterfaceFile;

      // Create Marshaler
      resourceAdaptorMarshalerFile = FileUtil.createFromTemplate(folder, new Path(resourceAdaptorMarshalerFilename), new Path(RESOURCE_ADAPTOR_MARSHALER_TEMPLATE), subs, monitor);
      monitor.worked(1); // done with Resource Adaptor Marshaler file. worked++

      // Create Provider Impls
      String subGetResourceAdaptorInterfaceImpl = "";
      for(String raTypeResourceAdaptorInterface : raTypeResourceAdaptorInterfaces) {
        if(raTypeResourceAdaptorInterface != null) {
          String pakkage = raTypeResourceAdaptorInterface.substring(0, raTypeResourceAdaptorInterface.lastIndexOf('.'));
          String clazz = raTypeResourceAdaptorInterface.replace(pakkage + ".", "");

          String raTypeRaInterfaceImplFilename = clazz + "Impl.java";

          subs.put("__PROVIDER_NAME__", clazz);

          subs.put("__PROVIDER_IMPORTS__", "import " + raTypeResourceAdaptorInterface + ";");
          subs.put("__PROVIDER_METHODS__", "  // TODO: Fill with proper methods...");
          IFile raTypeRaInterfaceImplFile = FileUtil.createFromTemplate(folder, new Path(raTypeRaInterfaceImplFilename), new Path(RESOURCE_ADAPTOR_PROVIDER_IMPL_TEMPLATE), subs, monitor);
          raTypeRaInterfaceImplFiles.add(raTypeRaInterfaceImplFile);

          subGetResourceAdaptorInterfaceImpl += ("if(\"" + raTypeResourceAdaptorInterface + "\".equals(className)) {\n" +
              "\t\t\treturn new " + clazz + "Impl();\n" +
          "\t\t}\n");
        }
      }

      subGetResourceAdaptorInterfaceImpl += "\n\t\treturn null;";
      subs.put("__GET_RESOURCE_ADAPTOR_INTERFACE_IMPL__", subGetResourceAdaptorInterfaceImpl);

      // Create the Resource Adaptor file.
      resourceAdaptorFile = FileUtil.createFromTemplate(folder, new Path(resourceAdaptorFilename), new Path(RESOURCE_ADAPTOR_TEMPLATE), subs, monitor);
      monitor.worked(1); // done with Resource Adaptor file. worked++

      // Done with XML. worked++
      FileUtil.createFromInputStream(resourceFolder, new Path(xmlFilename), resourceAdaptorJarXML.getInputStreamFromXML(), monitor);
      monitor.worked(1);

      IFolder duFolder = getSourceContainer().getProject().getFolder(new Path("du/src/main/resources/META-INF"));
      if(!duFolder.exists()) {
        duFolder.create(true, true, monitor);
      }

      // Reuse existing XML descriptor file if present or create new one
      IFile deployConfigFile = duFolder.getFile(deployConfigXmlFilename);

      // Generate Deploy Config XML ..
      DeployConfigXML deployConfigXML = deployConfigFile.exists() ? new DeployConfigXML(deployConfigFile) : new DeployConfigXML();
      deployConfigXML.addResourceAdaptorEntity(getComponentName(), getComponentVendor(), getComponentVersion(), resourceAdaptorBaseName + "RA");

      // Create deploy-config.xml..
      FileUtil.createFromInputStream(duFolder, new Path(deployConfigXmlFilename), deployConfigXML.getInputStreamFromXML(), monitor);
      monitor.worked(1);

      // Open files for editing
      monitor.setTaskName("Opening JAIN SLEE Resource Adaptor for editing...");
      getShell().getDisplay().asyncExec(new Runnable() {
        public void run() {
          IWorkbenchPage page =
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          try {
            IDE.openEditor(page, resourceAdaptorMarshalerFile, true);
            for(IFile raTypeRaInterfaceImplFile : raTypeRaInterfaceImplFiles) {
              IDE.openEditor(page, raTypeRaInterfaceImplFile, true);
            }
            IDE.openEditor(page, resourceAdaptorFile, true);
          }
          catch (PartInitException e) {
          }
        }
      });
      monitor.worked(1);
    }
    catch (Exception e) {
      throw newCoreException("Unable to create resource adaptor", e);
    }
  }

}
