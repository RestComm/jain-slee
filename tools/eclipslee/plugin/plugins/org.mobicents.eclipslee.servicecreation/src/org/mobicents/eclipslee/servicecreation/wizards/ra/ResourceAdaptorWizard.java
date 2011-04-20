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

  private HashMap[] resourceAdaptorRaTypes;
  private HashMap[] resourceAdaptorConfigProperties;
  private boolean resourceAdaptorSupportActiveReconfig;

  public ResourceAdaptorWizard() {
    super();
    WIZARD_TITLE = "JAIN SLEE Resource Adaptor Wizard";
    ENDS = "ResourceAdaptor.java";
  }

  public void addPages() {
    super.addPages(); // adds filename and name, vendor, version pages
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

    resourceAdaptorConfigProperties = resourceAdaptorConfigPropertiesPage.getConfigProperties();

    resourceAdaptorSupportActiveReconfig = resourceAdaptorConfigPropertiesPage.getActiveReconfiguration(); 

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
      ResourceAdaptorXML resourceAdaptor = resourceAdaptorJarXML.addResourceAdaptor();
      
      // Create the Resource Adaptor XML.
      resourceAdaptor.setName(getComponentName());
      resourceAdaptor.setVendor(getComponentVendor());
      resourceAdaptor.setVersion(getComponentVersion());
      resourceAdaptor.setDescription(getComponentDescription());
      
      ResourceAdaptorClassesXML resourceAdaptorClassesXML = resourceAdaptor.addResourceAdaptorClasses();
      ResourceAdaptorClassXML resourceAdaptorClassXML = resourceAdaptorClassesXML.addResourceAdaptorClass(resourceAdaptorSupportActiveReconfig);
      resourceAdaptorClassXML.setResourceAdaptorClassName(resourceAdaptorClassName);

      ArrayList<String> raTypeResourceAdaptorInterfaces = new ArrayList<String>();
      
      for (HashMap raType : resourceAdaptorRaTypes) {
        String raTypeName = (String) raType.get("Name");
        String raTypeVendor = (String) raType.get("Vendor");
        String raTypeVersion = (String) raType.get("Version");
        resourceAdaptor.addResourceAdaptorType(raTypeName, raTypeVendor, raTypeVersion);
        
        ResourceAdaptorTypeJarXML raTypeJarXML = (ResourceAdaptorTypeJarXML) raType.get("XML");
        ResourceAdaptorTypeXML raTypeXML = raTypeJarXML.getResourceAdaptorType(raTypeName, raTypeVendor, raTypeVersion);
        raTypeResourceAdaptorInterfaces.add(raTypeXML.getResourceAdaptorTypeClasses().getResourceAdaptorInterface());
      }
      
      String subConfigProperties = "";
      String subRaConfigureMethod = "";

      for (HashMap configProperty : resourceAdaptorConfigProperties) {
        String configPropertyName = (String) configProperty.get("Name");
        String configPropertyType = (String) configProperty.get("Type");
        String configPropertyDefaultValue = (String) configProperty.get("Default Value");
        resourceAdaptor.addConfigProperty(configPropertyName, configPropertyType, configPropertyDefaultValue);
        // FIXME: Make this a method in ConfigPropertiesUtil...
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
        String simpleType = configPropertyType.replaceAll("java\\.lang\\.", "");

        subConfigProperties += "\tprivate " + simpleType + " " + configPropertyName + " = " + value + ";\n";

        subRaConfigureMethod += "\n\t\tthis." + configPropertyName + " = (" + simpleType + ") properties.getProperty(\"" + configPropertyName + "\").getValue();\n";
        subRaConfigureMethod += "\t\tif(this." + configPropertyName + ".equals(\"\")) {\n";
        subRaConfigureMethod += "\t\t\t// TODO: define behavior when value is not present\n";
        subRaConfigureMethod += "\t\t\tthis." + configPropertyName + " = null;\n";
        subRaConfigureMethod += "\t\t}\n";
      }

      subs.put("__CONFIG_PROPERTIES__", subConfigProperties);
      subs.put("__RA_CONFIGURE_IMPL__", subRaConfigureMethod);
      
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
