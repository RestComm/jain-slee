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
import java.util.ArrayList;
import java.util.List;

import org.alcatel.jsce.statevent.StatEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 * Dialog used to edit an already-created Sbb in order to add/remove 
 * statistic events.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ModifySbbStatEventDialog extends Dialog {
	/** Temporary variable, list of all present catalogs*/
	private List catalogs = null;
	/** Defines whether the loading has been already done*/
	private boolean initialized = false;
	/** The stat event panel*/
	private OSPStatEventCatalogWidget statEventPanel = null;
	/** Already-selected events*/
	private List selectedEvents = null;

	/**
	 * 
	 */
	public ModifySbbStatEventDialog(Shell parent) {
		super(parent);
	}

	/**
	 * @see org.eclipse.jface.window.Window#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		//		1. get the lsit of all catalogs
		statEventPanel = new OSPStatEventCatalogWidget(parent, SWT.NONE, loadCatalogs());
		if (selectedEvents != null)
			statEventPanel.loadSelectedEvents(selectedEvents);
		return super.createContents(parent);
	}

	/**Loads all the catalogs in the panel.*/
	private List loadCatalogs() {
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				catalogs = ServiceCreationPlugin.getDefault().getMainControl().getAllStatEventCatalogs();
				initialized = true;
			}
		};
		WaitDialog waitDialog = new WaitDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Open External Stat Event catalogs...");
		waitDialog.open();
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
		try {
			//	1. get the lsit of all catalogs
			progressService.busyCursorWhile(runnable);
			waitDialog.close();
			return catalogs;
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		waitDialog.close();
		return new ArrayList();
	}

	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}

	/**
	 * @return the list of selected stat events.
	 */
	public StatEvent[] getSelectedStatEvents() {

		if (statEventPanel == null) {
			return new StatEvent[] {};
		} else {
			return (statEventPanel).getSelectedStatEvents();
		}
	}

	/**
	 * Loads already-selected stat events
	 * @param selected_stat
	 */
	public void loadSelectedStatEvent(List selected_stat) {
		this.selectedEvents = selected_stat;
	}

}
