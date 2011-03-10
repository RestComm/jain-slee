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

package org.mobicents.eclipslee.servicecreation.wizards.sbb;

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
import org.mobicents.eclipslee.servicecreation.util.CMPUtil;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.wizards.generic.BaseWizard;
import org.mobicents.eclipslee.util.Utils;
import org.mobicents.eclipslee.util.slee.xml.components.EventXML;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.util.slee.xml.components.ResourceAdaptorTypeXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbCMPField;
import org.mobicents.eclipslee.util.slee.xml.components.SbbChildRelationXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEnvEntryXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbEventXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbProfileCMPMethod;
import org.mobicents.eclipslee.util.slee.xml.components.SbbProfileSpecRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbRefXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorEntityBindingXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbResourceAdaptorTypeBindingXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;
import org.mobicents.eclipslee.xml.ResourceAdaptorTypeJarXML;
import org.mobicents.eclipslee.xml.SbbJarXML;

/**
 * @author cath
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SbbWizard extends BaseWizard {
	
	public static final String SBB_TEMPLATE = "/templates/SBB.template";
	public static final String SBB_USAGE_TEMPLATE = "/templates/SBBUsage.template";
	public static final String SBB_LOCAL_TEMPLATE = "/templates/SBBLocal.template";
	public static final String SBB_ACI_TEMPLATE = "/templates/SBBACI.template";
	
	public static final String SBB_USAGE_COMMENT = "\t/**\n\t * Method to retrieve a named SBB Usage Parameter set.\n\t * @param name the SBB Usage Parameter set to retrieve\n\t * @return the SBB Usage Parameter set\n\t * @throws javax.slee.usage.UnrecognizedUsageParameterSetNameException if the named parameter set does not exist\n\t */\n";
	public static final String SBB_DEFAULT_USAGE_COMMENT = "\t/**\n\t * Method to retrieve the default SBB usage parameter set.\n\t * @return the default SBB usage parameter set\n\t */\n";	
	
	public SbbWizard() {
		super();
		WIZARD_TITLE = "JAIN SLEE Service Building Block Wizard";
		ENDS = "Sbb.java";
	}

	public void addPages() {
		super.addPages();
		
		sbbClassesPage = new SbbClassesPage(WIZARD_TITLE);
		addPage(sbbClassesPage);
		
		sbbCMPPage = new SbbCMPPage(WIZARD_TITLE);
		addPage(sbbCMPPage);
		
		sbbUsagePage = new SbbUsagePage(WIZARD_TITLE);
		addPage(sbbUsagePage);

		sbbEventsPage = new SbbEventsPage(WIZARD_TITLE);
		addPage(sbbEventsPage);
		
		sbbProfilePage = new SbbProfilePage(WIZARD_TITLE);
		addPage(sbbProfilePage);
		
		sbbChildPage = new SbbChildPage(WIZARD_TITLE);
		addPage(sbbChildPage);
		
		sbbResourceAdaptorTypePage = new SbbResourceAdaptorTypePage(WIZARD_TITLE);
		addPage(sbbResourceAdaptorTypePage);

		sbbEnvEntryPage = new SbbEnvEntryPage(WIZARD_TITLE);
		addPage(sbbEnvEntryPage);
		
	}
	
	public boolean performFinish() {
		
		// Extract the data from the various pages.
		usageParams = sbbUsagePage.getUsageParameters();
		createUsageIface = sbbUsagePage.getCreateUsageInterface();
		
		cmpFields = sbbCMPPage.getCMPFields();
		
		createACI = sbbClassesPage.createActivityContextInterface();
		createLocalIface = sbbClassesPage.createSbbLocalObject();
		
		events = sbbEventsPage.getSelectedEvents();
		profiles = sbbProfilePage.getSelectedProfiles();
		
		addressProfile = sbbProfilePage.getAddressProfileSpec();
		
		children = sbbChildPage.getSelectedChildren();
		
		envEntries = sbbEnvEntryPage.getEnvEntries();
		
		raTypes = sbbResourceAdaptorTypePage.getResourceAdaptorTypes();
		
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

			String sbbBaseName = getFileName().substring(0, getFileName().indexOf(ENDS));
			//String abstractFilename = sbbBaseName + "Sbb.java";
			String usageFilename = sbbBaseName + "SbbUsage.java";
			String xmlFilename = /*sbbBaseName + "-*/"sbb-jar.xml";
			String localFilename = sbbBaseName + "SbbLocalObject.java";
			String aciFilename = sbbBaseName + "SbbActivityContextInterface.java";
			
			String abstractClassName = Utils.getSafePackagePrefix(getPackageName()) + sbbBaseName + "Sbb";
			String usageClassName = Utils.getSafePackagePrefix(getPackageName()) + sbbBaseName + "SbbUsage";
			String localClassName = Utils.getSafePackagePrefix(getPackageName()) + sbbBaseName + "SbbLocalObject";
			String aciClassName = Utils.getSafePackagePrefix(getPackageName()) + sbbBaseName + "SbbActivityContextInterface";
			
			// Calculate the number of stages.
			int stages = 3; // Abstract class + XML
			if (createUsageIface) stages++;
			if (createLocalIface) stages++;
			if (createACI) stages++;
			monitor.beginTask("Creating SBB: " + sbbBaseName, stages);

			// Substitution map
			HashMap<String, String> subs = new HashMap<String, String>();
			subs.put("__NAME__", sbbBaseName);
			subs.put("__PACKAGE__", Utils.getPackageTemplateValue(getPackageName()));
			
			// SBB Usage stuff.
			subs.put("__USAGE_METHODS__", getUsageMethods(usageParams));			
			String usageMethod = "";
			if (createUsageIface) {
				usageMethod += SBB_USAGE_COMMENT;
				usageMethod += "\n\tpublic abstract " + usageClassName + " getSbbUsageParameterSet(String name) throws javax.slee.usage.UnrecognizedUsageParameterSetNameException;\n\n";
				usageMethod += SBB_DEFAULT_USAGE_COMMENT;
				usageMethod += "\n\tpublic abstract " + usageClassName + " getDefaultSbbUsageParameterSet();\n";
			}
			subs.put("__USAGE_METHOD__", usageMethod);
			 
			// Make the CMP fields
			String accessors[] = CMPUtil.getAccessors(cmpFields);
			String tmp = "";
			for (int i = 0; i < accessors.length; i++)
				tmp += accessors[i];
			subs.put("__CMP_FIELDS__", tmp);
						
			if (createACI)
				subs.put("__ACI_METHOD__", "\tpublic abstract " + aciClassName + " asSbbActivityContextInterface(ActivityContextInterface aci);\n\n");
			else
				subs.put("__ACI_METHOD__", "");
			
      // Get (or create if not present already) META-INF folder for storing sbb-jar.xml
      IFolder resourceFolder = getSourceContainer().getFolder(new Path("../resources/META-INF"));
      if (!resourceFolder.exists()) {
        resourceFolder.create(true, true, monitor);
      }

      // Reuse existing XML descriptor file if present or create new one
      IFile sbbJarFile = resourceFolder.getFile(xmlFilename);
			// We must create the Sbb XML before trying to add events.
			SbbJarXML sbbJarXML = sbbJarFile.exists() ? new SbbJarXML(sbbJarFile) : new SbbJarXML();
			SbbXML sbb = sbbJarXML.addSbb();

			// Create the SBB XML.
			sbb.setName(getComponentName());
			sbb.setVendor(getComponentVendor());
			sbb.setVersion(getComponentVersion());
			sbb.setDescription(getComponentDescription());
			
			sbb.setAbstractClassName(abstractClassName);
			
			if (createUsageIface)
				sbb.setUsageInterfaceName(usageClassName);
			
			if (createLocalIface)
				sbb.setLocalInterfaceName(localClassName);
			
			if (createACI)
				sbb.setActivityContextInterfaceName(aciClassName);
			
			// Add the new CMP fields to the Sbb XML
			for (int i = 0; i < cmpFields.length; i++) {
				String type = (String) cmpFields[i].get("Type");
				
				SbbCMPField cmpField = sbb.addCMPField();
				cmpField.setName((String) cmpFields[i].get("Name"));
				
				if (type.equals("javax.slee.SbbLocalObject")) {

					SbbXML localObject = (SbbXML) cmpFields[i].get("SBB XML");
										
					SbbRefXML ref = sbb.addSbbRef();
					ref.setName(localObject.getName());
					ref.setVendor(localObject.getVendor());
					ref.setVersion(localObject.getVersion());
					ref.setAlias((String) cmpFields[i].get("Scoped Name"));
					
					cmpField.setSbbAliasRef(ref);						
				}					
			}

			// Events			
			String eventHandlers = "";
			String eventFirers = "";
			String iesMethods = "";
			for (int i = 0; i < events.length; i++) {
				EventJarXML xml = (EventJarXML) events[i].get("XML");
				String name = (String) events[i].get("Name");
				String vendor = (String) events[i].get("Vendor");
				String version = (String) events[i].get("Version");
				String direction = (String) events[i].get("Direction");
				boolean initialEvent = ((Boolean) events[i].get("Initial Event")).booleanValue();
				String[] selectors = (String []) events[i].get("Initial Event Selectors");
				
				EventXML event = xml.getEvent(name, vendor, version);
				SbbEventXML sbbEvent = sbb.addEvent(event);
				sbbEvent.setScopedName((String) events[i].get("Scoped Name"));
				int evDir = 0;
				
				if (direction.indexOf("Receive") != -1) {					
					// Determine the event's alias.  To do this we have to
					// add the event to the sbb-jar.xml.
					eventHandlers += "\tpublic void on" + Utils.capitalize(sbbEvent.getScopedName()) + "(" + event.getEventClassName() + " event, ActivityContextInterface aci) {\n\t}\n\n";					
					evDir |= SbbEventXML.RECEIVE;
				}
				
				if (direction.indexOf("Fire") != -1) {
					eventFirers += "\tpublic abstract void fire" + Utils.capitalize(sbbEvent.getScopedName()) + " (" + event.getEventClassName() + " event, ActivityContextInterface aci, Address address);\n";				
					evDir |= SbbEventXML.FIRE;
				}

				sbbEvent.setEventDirection(evDir);
				
				sbbEvent.setInitialEvent(initialEvent);
				if (initialEvent) {
					// Add the required selectors.
					for (int j = 0; j < selectors.length; j++) {
						if (selectors[j].equals("Custom")) {
							String methodName = "on" + Utils.capitalize(sbbEvent.getScopedName()) + "InitialEventSelector";							
							sbbEvent.setInitialEventSelectorMethod(methodName);
							iesMethods += "\tpublic InitialEventSelector " + methodName + "(InitialEventSelector ies) {\nreturn ies;\n\t}\n\n";
						} else {
							sbbEvent.addInitialEventSelector(selectors[j]);
						}
					}	
				}				
			}
			
			subs.put("__EVENT_HANDLERS__", eventHandlers);
			subs.put("__FIRE_EVENTS__", eventFirers);
			subs.put("__INITIAL_EVENT_SELECTOR__", iesMethods);
			
			// Profile Specs
			String profileCMP = "";
			SbbProfileSpecRefXML addressProfileRef = null;
			
			for (int i = 0; i < profiles.length; i++) {
				HashMap profile = profiles[i];
				
				String name = (String) profile.get("Name");
				String vendor = (String) profile.get("Vendor");
				String version = (String) profile.get("Version");
				String scopedName = (String) profile.get("Scoped Name");
				ProfileSpecJarXML profileJarXML = (ProfileSpecJarXML) profile.get("XML");
				ProfileSpecXML profileXML = profileJarXML.getProfileSpec(name, vendor, version);

				SbbProfileSpecRefXML profileRef = sbb.addProfileSpecRef();				
				profileRef.setName(name);
				profileRef.setVendor(vendor);
				profileRef.setVersion(version);
				profileRef.setAlias(scopedName);

				if (addressProfile != null && scopedName.equals(addressProfile))
					addressProfileRef = profileRef;
				
				SbbProfileCMPMethod profileMethodXML = sbb.addProfileCMPMethod(profileRef);
				
				String methodName = "get" + Utils.capitalize(scopedName) + "CMP";
				profileMethodXML.setProfileCMPMethodName(methodName);
				
				profileCMP += "\tpublic abstract " + profileXML.getCMPInterfaceName() + " " + methodName + "(javax.slee.profile.ProfileID profileID) throws javax.slee.profile.UnrecognizedProfileNameException, javax.slee.profile.UnrecognizedProfileTableNameException;\n";			
			}
			subs.put("__PROFILE_CMP__", profileCMP);
			
			if (addressProfileRef != null)
				sbb.setAddressProfileSpecAliasRef(addressProfileRef);			
			
			// Children
			String childRelation = "";
			for (int i = 0; i < children.length; i++) {
				
				HashMap child = children[i];
				
				String name = (String) child.get("Name");
				String vendor = (String) child.get("Vendor");
				String version = (String) child.get("Version");
				String scopedName = (String) child.get("Scoped Name");
				
				SbbRefXML ref = sbb.getSbbRef(name, vendor, version);
				if (ref == null) {				
					ref = sbb.addSbbRef();
					ref.setName(name);
					ref.setVendor(vendor);
					ref.setVersion(version);
					ref.setAlias(scopedName);
				}
				SbbChildRelationXML rel = sbb.addChildRelation(ref);
				rel.setChildRelationMethodName("get" + Utils.capitalize(scopedName));
				rel.setDefaultPriority(Integer.parseInt((String) child.get("Default Priority")));
				
				String methodName = "\tpublic abstract ChildRelation get" + Utils.capitalize(scopedName) + "();\n";
				childRelation += methodName;				
			}
			
			subs.put("__CHILD_RELATION__", childRelation);

			// Environment entries
			for (int i = 0; i < envEntries.length; i++) {
				HashMap map = (HashMap) envEntries[i];
				SbbEnvEntryXML xml = sbb.addEnvEntry();
			
				xml.setName((String) map.get("Name"));
				xml.setValue((String) map.get("Value"));
				xml.setType((String) map.get("Type"));
				xml.setDescription((String) map.get("Description"));				
			}
			
			// RA Types
			for (int i = 0; i < raTypes.length; i++) {
				HashMap map = (HashMap) raTypes[i];

				String name = (String) map.get("Name");
				String vendor = (String) map.get("Vendor");
				String version = (String) map.get("Version");
				String aciName = (String) map.get("ACI Factory Name");
				ResourceAdaptorTypeJarXML raJarXML = (ResourceAdaptorTypeJarXML) map.get("XML");
				ResourceAdaptorTypeXML raXML = raJarXML.getResourceAdaptorType(name, vendor, version);
								
				SbbResourceAdaptorTypeBindingXML xml = sbb.addResourceAdaptorTypeBinding();
				xml.setResourceAdaptorTypeRef(raXML);
				xml.setActivityContextInterfaceFactoryName(aciName.equals("") ? null : aciName);
				
				HashMap bindings[] = (HashMap []) map.get("Bindings");
				for (int j = 0; j < bindings.length; j++) {
					SbbResourceAdaptorEntityBindingXML entityXML = xml.addResourceAdaptorEntityBinding();
					entityXML.setResourceAdaptorEntityLink((String) bindings[j].get("Entity Link"));
					entityXML.setResourceAdaptorObjectName((String) bindings[j].get("Object Name"));
				}
			}
						
			final IFile abstractFile;
			final IFile localFile;
			final IFile aciFile;
			final IFile usageFile; 
			
			// Create the abstract class file.
			abstractFile = FileUtil.createFromTemplate(folder, new Path(getFileName()), new Path(SBB_TEMPLATE), subs, monitor);
			monitor.worked(1);
			
			// Create the SBB local interface file.
			if (createLocalIface) {
				localFile = FileUtil.createFromTemplate(folder, new Path(localFilename), new Path(SBB_LOCAL_TEMPLATE), subs, monitor);
				monitor.worked(1);
			} else
				localFile = null;
			
			// Create the SBB ACI.
			if (createACI) {
				aciFile = FileUtil.createFromTemplate(folder, new Path(aciFilename), new Path(SBB_ACI_TEMPLATE), subs, monitor);
				monitor.worked(1);
			} else
				aciFile = null;
			
			// Create the SBB usage interface file.
			if (createUsageIface) {			
				usageFile = FileUtil.createFromTemplate(folder, new Path(usageFilename), new Path(SBB_USAGE_TEMPLATE), subs, monitor);
				monitor.worked(1);			
			} else
				usageFile = null;
			
			FileUtil.createFromInputStream(resourceFolder, new Path(xmlFilename), sbbJarXML.getInputStreamFromXML(), monitor);
			monitor.worked(1);
			
		  /* ammendonca: removed for maven project style
			monitor.setTaskName("Creating Ant Build File");
			// Load the ant build file from the root of the project.
			try {
				IPath antBuildPath = new Path("/build.xml");
							
				String sourceDir = getSourceContainer().getName();
				
				IFile projectFile = getSourceContainer().getProject().getFile(antBuildPath);
				AntProjectXML projectXML = new AntProjectXML(projectFile.getContents());
				AntInitTargetXML initXML = (AntInitTargetXML) projectXML.getTarget("init");
				AntBuildTargetXML buildXML = projectXML.addBuildTarget();
				AntCleanTargetXML cleanXML = projectXML.addCleanTarget();
				AntTargetXML allXML = projectXML.getTarget("all");
				AntTargetXML cleanAllXML = projectXML.getTarget("clean");			
				AntPathXML sleePathXML = initXML.getPathID("slee");
				
				String shortName = sbbBaseName + "-sbb";
				String classesDir = "classes/" + shortName;
				String jarDir = "jars/";
				String jarName = jarDir + shortName + ".jar";
				
				buildXML.setName("build-" + shortName);
				cleanXML.setName("clean-" + shortName);
				allXML.addAntTarget(buildXML);
				cleanAllXML.addAntTarget(cleanXML);
				
				cleanXML.addFile(jarName);
				cleanXML.addDir(classesDir);
				
				buildXML.addMkdir(classesDir);
				buildXML.addMkdir(jarDir);
				buildXML.setDepends(new String[] { "init" });
				AntJavacXML javacXML = buildXML.createJavac();
				javacXML.setDestdir(classesDir);
				javacXML.setSrcdir(sourceDir);
				
				// See if we need to add to the classpath.
				AntPathXML sbbPath = null;				
				for (int i = 0; i < events.length; i++) {
					EventJarXML xml = (EventJarXML) events[i].get("XML");
					String path = xml.getPath();
					if (path.startsWith("jar:")) {
						if (sbbPath == null) {
							sbbPath = initXML.addPath("path-" + shortName);
							javacXML.addPathXML(sbbPath);
						}
						sbbPath.addPathElement(path.substring(4, path.indexOf("!")));						
					}
				}
				
				try {
					AntPathXML externalComponentsPath = initXML.getPathID("ExternalComponents");
					javacXML.addPathXML(externalComponentsPath);;
				} catch (ComponentNotFoundException e) {
				}
				
				// Just include the SBB java files				
				String packageDir = getPackageName().replaceAll("\\.", "/");
				Vector javaFiles = new Vector();
				javaFiles.add(Utils.getSafePackageDir(packageDir) + abstractFilename); // Abstract class
				if (createUsageIface)
					javaFiles.add(Utils.getSafePackageDir(packageDir) + usageFilename);
				if (createLocalIface)
					javaFiles.add(Utils.getSafePackageDir(packageDir) + localFilename);
				if (createACI)
					javaFiles.add(Utils.getSafePackageDir(packageDir) + aciFilename);							
				javacXML.setIncludes((String []) javaFiles.toArray(new String[javaFiles.size()]));
				javacXML.addPathXML(sleePathXML);
				
				org.mobicents.eclipslee.util.slee.xml.ant.AntSbbJarXML antSbbXML = buildXML.addSbbJar();
				antSbbXML.setDestfile(jarName);
				antSbbXML.setClasspath(classesDir);
				antSbbXML.setXML(sourceDir + "/" + Utils.getSafePackageDir(packageDir) + sbbBaseName + "-sbb-jar.xml");
				
				FileUtil.createFromInputStream(getSourceContainer().getProject(), antBuildPath, projectXML.getInputStreamFromXML(), monitor);
			} catch (Exception e) {
				throwCoreException("Unable to modify Ant Build file '/build.xml'", e);
			}		
			*/
      monitor.worked(1);
			
			// Open the __NAME__Sbb.java file
			monitor.setTaskName("Opening JAIN SLEE Service Building Block for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, abstractFile, true);
						if (createACI)
							IDE.openEditor(page, aciFile, true);
						if (createLocalIface)
							IDE.openEditor(page, localFile, true);
						if (createUsageIface)
							IDE.openEditor(page, usageFile, true);
					} catch (PartInitException e) {
					}
				}
			});
			monitor.worked(1);
			
		} catch (Exception e) {
			throw newCoreException("Unable to create service building block", e);
		}
	}
	
	public static String getUsageMethods(HashMap params[]) {
		
		String methods = "";
		
		for (int i = 0; i < params.length; i++) {
			HashMap map = params[i];
			
			String paramName = Utils.capitalize((String) map.get("Name"));
			int paramIndex = ((Integer) map.get("Type")).intValue();;
			
			switch (paramIndex) {
				case 0: // increment
					methods += "\tpublic void increment" + paramName + "(long value);\n";
					break;
				case 1: // sample
					methods += "\tpublic void sample" + paramName + "(long value);\n";
					break;
					
				default:
					break; // Ignore this one.
			}
		
		}

		return methods;
	}
	
	private SbbUsagePage sbbUsagePage;
	private SbbCMPPage sbbCMPPage;
	private SbbClassesPage sbbClassesPage;
	private SbbEventsPage sbbEventsPage;
	private SbbProfilePage sbbProfilePage;
	private SbbChildPage sbbChildPage;
	private SbbEnvEntryPage sbbEnvEntryPage;
	private SbbResourceAdaptorTypePage sbbResourceAdaptorTypePage;
	
	private HashMap cmpFields[];
	private HashMap usageParams[];
	private HashMap events[];
	private HashMap profiles[];
	private HashMap children[];
	private HashMap envEntries[];
	private HashMap raTypes[];
	
	private String addressProfile;
	private boolean createUsageIface;
	private boolean createLocalIface;
	private boolean createACI;
}
