
package org.mobicents.eclipslee.servicecreation.importing.wizard;

/**

 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.wizards.datatransfer.*;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.wizards.datatransfer.DataTransferMessages;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.wizards.generic.BaseWizard;
import org.mobicents.eclipslee.servicecreation.wizards.generic.FilenamePage;
import org.mobicents.eclipslee.servicecreation.wizards.generic.IdentityPage;
import org.mobicents.eclipslee.util.slee.xml.ant.AntBuildTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntCleanTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntInitTargetXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntJavacXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntPathXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntProjectXML;
import org.mobicents.eclipslee.util.slee.xml.ant.AntTargetXML;
import org.mobicents.eclipslee.util.slee.xml.components.ComponentNotFoundException;
import org.mobicents.eclipslee.xml.EventJarXML;
import java.util.List;

/**
 * @author Paolo 
 * Wizard for importing Jain SLEE components from the local file system
 * into a Jain SLEE Project.
 * 
 * During the call to <code>open</code>, the wizard dialog is presented to the
 * user. When the user hits Finish, the user-selected files are imported
 * into the workspace, the Ant scripts are generated, the dialog closes, and the call to <code>open</code>
 * returns.
 * </p>
 */
public abstract class ImportSLEEWizard  extends Wizard implements IImportWizard {
	
	public static String WIZARD_ID = "ImportSLEEWizard"; 
	
    protected IWorkbench workbench;
    protected IStructuredSelection selection;
    
    /**
     * Creates a wizard for importing Jain SLEE components into the workspace from
     * the file system.
     */
    public ImportSLEEWizard () {
    	IDialogSettings workbenchSettings = ServiceCreationPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings
                .getSection(WIZARD_ID);//$NON-NLS-1$
        if (section == null)
            section = workbenchSettings.addNewSection(WIZARD_ID);//$NON-NLS-1$
        setDialogSettings(section);
        setNeedsProgressMonitor(true);
        setHelpAvailable(false); // TODO Create help system
    }

    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public abstract void addPages();

    /* (non-Javadoc)
     * Method declared on IWorkbenchWizard.
     */
    public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
        this.workbench = workbench;
        this.selection = currentSelection;

        List selectedResources = IDE.computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty()) {
            this.selection = new StructuredSelection(selectedResources);
        }
        setWindowTitle(DataTransferMessages.DataTransfer_importTitle );
        try {
        	IPath resourcePath = new Path(getIconPath());
        	InputStream is = ServiceCreationPlugin.getDefault().openStream(resourcePath);
        	setDefaultPageImageDescriptor(ImageDescriptor.createFromImageData(new ImageData(is)));}
        catch (IOException e){e.printStackTrace();}
        
       setNeedsProgressMonitor(true);
    }
    
    /* (non-Javadoc)
     * Method declared on IWizard.
     */
    public boolean performFinish() {
    	IWizardPage page = getContainer().getCurrentPage();
        if (page instanceof ISleeImportPage) {
        		ISleeImportPage sleeImportPage = (ISleeImportPage)page;
        		return sleeImportPage.finish();
        }
        return false;
    }
    
    public abstract String getIconPath();
    
    // TODO Integrate this logic (copied from the SBB wizard) in the import operation 
	public void doFinish(IProgressMonitor monitor) throws CoreException {
		/*
		String eventClassName = getPackageName() + "." + getFileName().substring(0, getFileName().indexOf(".java"));
		String baseName = getFileName().substring(0, getFileName().indexOf(ENDS));
		
		// Create the Event Java file.
		monitor.beginTask("Creating JAIN SLEE Event " + getComponentName(), 4);
		HashMap map = new HashMap();
		map.put("__PACKAGE__", getPackageName());
		map.put("__NAME__", getFileName().substring(0, getFileName().length() - ENDS.length()));

		IFolder folder = getSourceContainer().getFolder(new Path(this.getPackageName().replaceAll("\\.", "/")));

		final IFile javaFile;
		try {
			javaFile = FileUtil.createFromTemplate(folder, new Path(getFileName()), new Path(EVENT_TEMPLATE), map, monitor);		
		} catch (IOException e) {
			e.printStackTrace();
			throw newCoreException("Failed to create JAIN SLEE Event Java: ", e);			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		monitor.worked(1);
		
		// Create the event-jar.xml file.
		monitor.setTaskName("Creating JAIN SLEE Event XML");		
		try {
			EventJarXML eventXML = new EventJarXML();
			eventXML.addEvent(getComponentName(), getComponentVendor(), getComponentVersion(),
					getComponentDescription(), eventClassName);

			FileUtil.createFromInputStream(folder, new Path(baseName + "-event-jar.xml"), eventXML.getInputStreamFromXML(), monitor);
		} catch (IOException e) {
				throwCoreException("Failed to create JAIN SLEE Event XML: ", e);
		} catch (Exception e) {
			throwCoreException("Failed to create JAIN SLEE Event XML: ", e);
		}
		monitor.worked(1);

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
		
			String shortName = baseName + "-event";
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
			// Just include the event java files
			
			String packageDir = getPackageName().replaceAll("\\.", "/");
			
			javacXML.setIncludes(new String[] { packageDir + "/" + getFileName() });
			javacXML.addPathXML(sleePathXML);
			
			
			try {
				AntPathXML externalComponentsPath = initXML.getPathID("ExternalComponents");
				javacXML.addPathXML(externalComponentsPath);;
			} catch (ComponentNotFoundException e) {
			}
			
			org.mobicents.eclipslee.util.slee.xml.ant.AntEventJarXML eventXML = buildXML.addEventJar();
			eventXML.setDestfile(jarName);
			eventXML.setClasspath(classesDir);
			eventXML.setXML(sourceDir + "/" + packageDir + "/" + baseName + "-event-jar.xml");
			
			FileUtil.createFromInputStream(getSourceContainer().getProject(), antBuildPath, projectXML.getInputStreamFromXML(), monitor);
			monitor.worked(1);
		} catch (Exception e) {
			throwCoreException("Unable to modify Ant Build file '/build.xml'", e);
		}		
		
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
		monitor.worked(1);*/
	}
}

