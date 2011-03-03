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

package org.mobicents.eclipslee.servicecreation.wizards.service;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;
import org.mobicents.eclipslee.servicecreation.wizards.WizardChangeListener;
import org.mobicents.eclipslee.servicecreation.wizards.generic.BaseWizard;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.util.slee.xml.components.Service;
import org.mobicents.eclipslee.xml.SbbJarXML;
import org.mobicents.eclipslee.xml.ServiceXML;


/**
 * @author cath
 */
public class ServiceWizard extends BaseWizard {
	
	private static final String WIZARD_TITLE = "JAIN SLEE Service Wizard";
	
	public ServiceWizard() {
		super();
		setNeedsProgressMonitor(true);
		ENDS = "-service.xml";
	}
	
	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */
	
	public void doFinish(IProgressMonitor monitor) throws CoreException {
		
		try {
			monitor.beginTask("Creating JAIN SLEE Service " + getFileName(), 2);
			
			ServiceXML serviceXML = new ServiceXML();
			Service service = serviceXML.addService();
			
			String name = (String) rootSbb.get("Name");
			String vendor = (String) rootSbb.get("Vendor");
			String version = (String) rootSbb.get("Version");
			SbbJarXML sbbJarXML = (SbbJarXML) rootSbb.get("XML");
			SbbXML sbbXML = sbbJarXML.getSbb(name, vendor, version);
			
			service.setName(getComponentName());
			service.setVendor(getComponentVendor());
			service.setVersion(getComponentVersion());
			service.setDescription(getComponentDescription());
			
			service.setRootSbb(sbbXML);
			service.setDefaultPriority(defaultPriority);
			
			if (createAddressProfileTable)
				service.setAddressProfileTable(sbbXML.getAddressProfileSpecAliasRef().getAlias());

			// Save...
      // Get (or create if not present already) META-INF folder for storing service.xml
      IFolder resourceFolder = getSourceContainer().getFolder(new Path("../resources/services"));
      if (!resourceFolder.exists()) {
        resourceFolder.create(true, true, monitor);
      }

      IFolder folder = getSourceContainer().getFolder(new Path(""));//.getFolder(new Path(this.getPackageName().replaceAll("\\.", "/")));
      
      // This allows implicit package creation
      for(String path : this.getPackageName().split("\\.")) {
        folder = folder.getFolder(path);
        if(!folder.exists()) {
          folder.create(true, true, monitor);
        }
      }

			final IFile xmlFile = FileUtil.createFromInputStream(resourceFolder, new Path(getFileName()), serviceXML.getInputStreamFromXML(), monitor);

			monitor.worked(1);
			
			// Open...
			monitor.setTaskName("Opening JAIN SLEE Service XML for viewing...");
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page =
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, xmlFile, true);
					} catch (PartInitException e) {
					}
				}
			});
			
			monitor.worked(1);
			monitor.done();
		} catch (Exception e) {
			e.printStackTrace();
			throw newCoreException("Error", e);
		}	
	}

	
	/**
	 * If you override this method you must call super.addPages() if you want to use
	 * the standard Filename and Identity Pages provided in this abstract class.
	 */
	
	public void addPages() {
		super.addPages();
	
		rootSbbPage = new ServiceRootSbbPage(WIZARD_TITLE);
		addPage(rootSbbPage);
	}
	
	/**
	 * When a page's contents change in a way that might impact other pages
	 * this method should be called so that other pages can react.
	 * @param page
	 */
	public void pageChanged(WizardPage page) {
		IWizardPage pages[] = this.getPages();
		for (int i= 0; i < pages.length; i++) {
			if (pages[i] instanceof WizardChangeListener) {
				((WizardChangeListener) pages[i]).onWizardPageChanged(page);
			}
		}
	}
	
	/**
	 * This method calls doFinish() in a new thread.
	 */
	
	public boolean performFinish() {
		defaultPriority = rootSbbPage.getDefaultPriority();
		createAddressProfileTable = rootSbbPage.getCreateAddressProfileTable();
		rootSbb = rootSbbPage.getRootSbb();
		
		return super.performFinish();
	}

	private ServiceRootSbbPage rootSbbPage;	
	private boolean createAddressProfileTable;
	private int defaultPriority;
	private HashMap rootSbb;
}
