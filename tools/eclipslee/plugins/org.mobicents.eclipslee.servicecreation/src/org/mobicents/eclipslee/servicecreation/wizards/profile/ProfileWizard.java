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

package org.mobicents.eclipslee.servicecreation.wizards.profile;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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
import org.mobicents.eclipslee.util.slee.xml.ant.AntBuildTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntCleanTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntInitTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntJavacXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntPathXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProfileSpecJarXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntTargetXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.util.slee.xml.components.DuplicateComponentException;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.EventJarXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;


/**
 * @author cath
 */
public final class ProfileWizard extends BaseWizard {
	
	public static final String CMP_TEMPLATE = "/templates/ProfileCMP.template";
	public static final String MGMT_TEMPLATE = "/templates/ProfileManagement.template";
	//public static final String ABSTRACT_TEMPLATE = "/templates/ProfileManagementImpl.template";
	public static final String ABSTRACT_TEMPLATE = "/templates/ProfileImpl.template";
	
	public ProfileWizard() {
		super();
		WIZARD_TITLE = "JAIN SLEE Profile Specification Wizard";
		ENDS = "ProfileCMP.java";
	}

	public void addPages() {
		super.addPages();
		cmpPage = new ProfileCMPPage(WIZARD_TITLE);
		addPage(cmpPage);
	}
	
	public boolean performFinish() {
		createProfileAbstractClass = cmpPage.getCreateAbstractClass();
		return super.performFinish();		
	}
	
	public void doFinish(IProgressMonitor monitor) throws CoreException {

		HashMap cmpFields[] = cmpPage.getCMPFields();
				
		String profileBaseName = getFileName().substring(0, getFileName().indexOf(ENDS));
		//String abstractFilename = profileBaseName + "ProfileManagementImpl.java";
		String abstractFilename = profileBaseName + "ProfileImpl.java";
		String interfaceFilename = profileBaseName + "ProfileManagement.java";
    String xmlFilename = /*profileBaseName + "-*/ "profile-spec-jar.xml";

		boolean createMgmtIface = false;
		
		// foreach cmp field
		//   count++
		//   if visible
		//     visible++
		Vector visibleFields = new Vector();
		for (int i = 0; i < cmpFields.length; i++) {
			HashMap cmpData = cmpFields[i];
			boolean visible = ((Boolean) cmpData.get("Visible")).booleanValue();			
			if (visible)
				visibleFields.add(cmpData);
		}		
		
		// if (visible < count)
		//   make_mgmt_iface = true		
		if (visibleFields.size() < cmpFields.length)
			createMgmtIface = true;
		
		// How many stages will there be?
		int stages = 3; // ProfileCMP and ProfileXML
		if (createProfileAbstractClass) stages++; // mgmt abstract class
		if (createMgmtIface) stages++; // mgmt interface
		
		monitor.beginTask("Creating JAIN SLEE Profile Specification: " + profileBaseName, stages);
		
		HashMap map = new HashMap();
		map.put("__PACKAGE__", Utils.getPackageTemplateValue(getPackageName()));
		map.put("__NAME__", getFileName().substring(0, getFileName().length() - ENDS.length()));

		String accessors[] = CMPUtil.getAccessors(cmpFields);
		String tmp = "";
		for (int i = 0; i < accessors.length; i++)
			tmp += accessors[i];
		map.put("__CMP_FIELDS__", tmp);
		map.put("__VISIBLE_CMP_FIELDS__", CMPUtil.getAccessors(visibleFields));
		
		String impls = profileBaseName + "ProfileCMP";
		if (createMgmtIface)
			impls += ", " + profileBaseName + "ProfileManagement";
		
		map.put("__IMPLEMENTS__", impls);
		
    IFolder folder = getSourceContainer().getFolder(new Path(""));//.getFolder(new Path(this.getPackageName().replaceAll("\\.", "/")));
    
    // This allows implicit package creation
    for(String path : this.getPackageName().split("\\.")) {
      folder = folder.getFolder(path);
      if(!folder.exists()) {
        folder.create(true, true, monitor);
      }
    }

		try {
			final IFile cmpFile = FileUtil.createFromTemplate(folder, new Path(getFileName()), new Path(CMP_TEMPLATE), map, monitor);
			monitor.worked(1);		
			
			if (createProfileAbstractClass) {
				FileUtil.createFromTemplate(folder, new Path(abstractFilename), new Path(ABSTRACT_TEMPLATE), map, monitor);
				monitor.worked(1);
			}
			
			if (createMgmtIface) {
				FileUtil.createFromTemplate(folder, new Path(interfaceFilename), new Path(MGMT_TEMPLATE), map, monitor);
				monitor.worked(1);
			}
			
			// Create the profile spec jar xml data
      // Get (or create if not present already) META-INF folder for storing event-jar.xml
      IFolder resourceFolder = getSourceContainer().getFolder(new Path("../resources/META-INF"));
      if (!resourceFolder.exists()) {
        resourceFolder.create(true, true, monitor);
      }

      IFile profileSpecJarFile = resourceFolder.getFile("profile-spec-jar.xml");
      ProfileSpecJarXML profileXML = profileSpecJarFile.exists() ? new ProfileSpecJarXML(profileSpecJarFile) : new ProfileSpecJarXML();
			ProfileSpecXML spec = profileXML.addProfileSpec(getComponentName(), getComponentVendor(), getComponentVersion(), getComponentDescription());
			spec.setCMPInterfaceName(Utils.getSafePackagePrefix(getPackageName()) + profileBaseName + "ProfileCMP");
			// if make_abstract_class
			if (createProfileAbstractClass)
				spec.setManagementAbstractClassName(Utils.getSafePackagePrefix(getPackageName()) + profileBaseName + "ProfileManagementImpl");
			// if make_mgmt_iface
			if (createMgmtIface)
				spec.setManagementInterfaceName(Utils.getSafePackagePrefix(getPackageName()) + profileBaseName + "ProfileManagement");
			
			// for each cmp field
			//   if indexed == true
			//     spec.addIndexedAttribute(attrName, booleanUnique);
			for (int i = 0; i < cmpFields.length; i++) {
				HashMap cmpData = cmpFields[i];
				boolean indexed = ((Boolean) cmpData.get("Indexed")).booleanValue();
				if (indexed) {
					boolean unique = ((Boolean) cmpData.get("Unique")).booleanValue();
					try {
						spec.addIndexedAttribute((String) cmpData.get("Name"), unique);
					} catch (DuplicateComponentException e) {
						// Throw an error
						throw newCoreException("Unable to create profile specification", e);
					}
				}
			}
			
			// Create profile spec jar XML file
			FileUtil.createFromInputStream(resourceFolder, new Path(xmlFilename), profileXML.getInputStreamFromXML(), monitor);
			monitor.worked(1);
			
	    /* ammendonca: removed, it's handled by maven now
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
			
				String shortName = profileBaseName + "-profile-spec";
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
				
				// Just include the SBB java files				
				String packageDir = getPackageName().replaceAll("\\.", "/");
				Vector javaFiles = new Vector();
				javaFiles.add(Utils.getSafePackageDir(packageDir) + getFileName()); // Abstract class
				if (createMgmtAbstractClass)
					javaFiles.add(Utils.getSafePackageDir(packageDir) + abstractFilename);
				if (createMgmtIface)
					javaFiles.add(Utils.getSafePackageDir(packageDir) + interfaceFilename);
				javacXML.setIncludes((String []) javaFiles.toArray(new String[javaFiles.size()]));
				javacXML.addPathXML(sleePathXML);

				try {
					AntPathXML externalComponentsPath = initXML.getPathID("ExternalComponents");
					javacXML.addPathXML(externalComponentsPath);;
				} catch (ComponentNotFoundException e) {
				}
				
				AntProfileSpecJarXML antProfileXML = buildXML.addProfileSpecJar();
				antProfileXML.setDestfile(jarName);
				antProfileXML.setClasspath(classesDir);
				antProfileXML.setXML(sourceDir + "/" + Utils.getSafePackageDir(packageDir) + profileBaseName + "-profile-spec-jar.xml");
				
				FileUtil.createFromInputStream(getSourceContainer().getProject(), antBuildPath, projectXML.getInputStreamFromXML(), monitor);
				monitor.worked(1);
			} catch (Exception e) {
				throwCoreException("Unable to modify Ant Build file '/build.xml'", e);
			}		
			*/
			monitor.worked(1);
			
			// Open the ProfileCMP file
			monitor.setTaskName("Opening JAIN SLEE Profile Specification CMP Interface for editing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, cmpFile, true);
					} catch (PartInitException e) {
					}
				}
			});
			monitor.worked(1);
			
			monitor.done();
			
			
		} catch (Exception e) {
			throw newCoreException("Unable to create profile specification", e);
		}
	}

	
	private ProfileCMPPage cmpPage;
	private boolean createProfileAbstractClass;
}
