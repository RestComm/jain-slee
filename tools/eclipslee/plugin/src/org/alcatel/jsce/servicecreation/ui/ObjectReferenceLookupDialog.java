
/**
 *   Copyright 2005 Alcatel, OSP.
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
package org.alcatel.jsce.servicecreation.ui;

import java.lang.reflect.InvocationTargetException;

import org.alcatel.jsce.interfaces.com.IPageAdaptor;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.mobicents.eclipslee.servicecreation.ui.SbbProfilePanel;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.ProfileSpecFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.ProfileSpecXML;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;

/**
 *  Description:
 * <p>
 * Dialog used to browse the OSP object present in the Jar directory.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectReferenceLookupDialog extends Dialog implements IPageAdaptor{
	/** The OSP object selection panel*/
	private SbbProfilePanel panel = null;
	/** Defines the found-XML files*/
	private DTDXML xml[] = {};
	/** The project in which we are looking for reference object*/
	private String projectName = null;

	/**
	 * @param parentShell the parent Shell
	 */
	public  ObjectReferenceLookupDialog(Shell parentShell, String projectName) {
		super(parentShell);
		this.projectName = projectName;
		parentShell.setSize(350, 700);
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		loadProfileSpec();
		panel.isEnabled();
		return super.createContents(parent);
	}
	
	private void loadProfileSpec() {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				// We are extracting the profile spec only in the Jars directory
				xml =ProfileSpecFinder.getDefault().getComponents(BaseFinder.JAR_DIR , projectName, monitor); //wihout  BaseFinder.SLEEDTD_DIR
			}
		};
		/*1. Launch the wait dialog box*/
		WaitDialog waitDialog = new WaitDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),"Searching for Profile Specification components ..." );
		waitDialog.open();
		IProgressService progressService = PlatformUI.getWorkbench()
				.getProgressService();
		try {
			/*2. launch the bysy cursor, block the ui Thread, allow us to keep an GUI responding*/
			progressService.busyCursorWhile(runnable);
			/*3. Refressh the SBB event panel, this operation must be outside of the progressive service*/
			if(panel!=null){
				panel.clearProfiles();
				for (int i = 0; i < xml.length; i++) {
					ProfileSpecJarXML ev = (ProfileSpecJarXML) xml[i];
					ProfileSpecXML[] events = ev.getProfileSpecs();
					for (int j = 0; j < events.length; j++) {
						panel.addAvailableProfile(ev, events[j]);
					}
				}
				panel.repack();
			}
			if(xml.length  == 0){
				setErrorMessage("No JAIN Profile specification were found in the Jars diretory. Please create a profile specification before.");
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		waitDialog.close();
		
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#setShellStyle(int)
	 */
	protected void setShellStyle(int newStyle) {
		super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX );
	}
	

	public void setErrorMessage(String msg) {
		// TODO Auto-generated method stub
		
	}

	public void setPageComplete(boolean complete) {
		// TODO Auto-generated method stub
		
	}

}
