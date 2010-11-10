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

package org.mobicents.eclipslee.servicecreation.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;

import org.alcatel.jsce.servicecreation.ui.WaitDialog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.mobicents.eclipslee.servicecreation.ui.table.EditableTableViewer;
import org.mobicents.eclipslee.servicecreation.util.BaseFinder;
import org.mobicents.eclipslee.servicecreation.util.DUFinder;
import org.mobicents.eclipslee.servicecreation.wizards.sbb.SbbEventsPage;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.xml.DeployableUnitXML;


/**
 * @author Vladimir Ralev
 */
public class DeployPanel extends Composite implements SelectionListener {
	private String projectName;
	
	private static final String BUTTON_DEPLOY = "Deploy";
	private static final String BUTTON_UNDEPLOY = "Undeploy";
	
	private HashMap duToDUDescription = new HashMap();
	private HashMap duToDUID = new HashMap();
	
	private static final String[] COLUMN_NAMES = { "Name", "Status", "Action" };
	private static final int[] COLUMN_EDITORS = {
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_NONE,
			EditableTableViewer.EDITOR_BUTTON
	};
	private Object[][] COLUMN_VALUES = {
			{},
			{},
			{}
	};
	
	MBeanServerConnection mbeanServer;
	ObjectName deploymentMBean;
	
	public DeployPanel(Composite parent, int style, String projectName) {
		super(parent, style);
		this.projectName = projectName;
		COLUMN_VALUES[2] = new Object[2];		
		COLUMN_VALUES[2][0] = BUTTON_DEPLOY;
		COLUMN_VALUES[2][1] = this;
		
		GridLayout layout = new GridLayout();
		setLayout(layout);
		
		GridData data = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		setLayoutData(data);

		Label label = new Label(this, SWT.NONE);
		label.setText("Available deployable units:");
		data = new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING);		
		label.setLayoutData(data);
			
		// Available events table, placed above the button box.
		availableDeployableUnits = new EditableTableViewer(this, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE, 
				COLUMN_NAMES, 
				COLUMN_EDITORS,
				COLUMN_VALUES);
		data = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL);
		data.heightHint = availableDeployableUnits.getTable().getItemHeight() * 8 + 5;
		data.widthHint = 500;
		availableDeployableUnits.getTable().setLayoutData(data);
		availableDeployableUnits.getTable().getColumn(0).setWidth(400);
		availableDeployableUnits.getTable().getColumn(1).setWidth(100);
		availableDeployableUnits.getTable().getColumn(2).setWidth(100);
		init();
	}
	
	private void init()
	{
		IRunnableWithProgress runnable = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) {
				initMBeanServer();
				Display.getDefault().asyncExec(new Runnable() {
					 public void run() {
						 initDUs();
					 }
					});
			}
		};
		WaitDialog waitDialog = new WaitDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),"Connecting to Local SLEE Server..." );
		waitDialog.open();
		IProgressService progressService = PlatformUI.getWorkbench()
				.getProgressService();
		try {
				progressService.busyCursorWhile(runnable);
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		waitDialog.close();
	}
	
	public void pack() {
		System.err.println("DeployPanel.pack() called");
	}
	
	public void repack() {
		availableDeployableUnits.repack();
	}
	
	public void clearEvents() {
		availableDeployableUnits.getStore().clear();
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {
	
	}
	
	public void setBlockingError(String error)
	{
		wizardPage.setErrorMessage(error);
		wizardPage.setPageComplete(false);
	}
	
	public void unsetBlockingError()
	{
		wizardPage.setErrorMessage(null);
		wizardPage.setPageComplete(true);
	}
	public void widgetSelected(SelectionEvent event) {
		Table table = availableDeployableUnits.getTable();
		TableItem items[] = table.getItems();
		for (int row = 0; row < items.length; row++) {
			HashMap map = (HashMap) items[row].getData();
	
			for (int column = 0; column < COLUMN_EDITORS.length; column++) {			
				Button button = (Button) map.get("Button_" + column);
				if (button != null && button.equals(event.getSource()) && button.getText().equals(BUTTON_DEPLOY)) {
			
					TableItem item = items[row];
					String file = (String) map.get("Name");
					deploy(file);
					initDUs();
				}
				if (button != null && button.equals(event.getSource()) && button.getText().equals(BUTTON_UNDEPLOY)) {
					
					TableItem item = items[row];
					String file = (String) map.get("Name");
					String key = getDUFileName(file);
					Object value = duToDUID.get(key);
					if(value != null)
					{
						undeploy((DeployableUnitID)value);
						initDUs();
					}	
				}
			}
		}		
	}
	
	private MBeanServerConnection initMBeanServer()
	{
		try {
			System.setSecurityManager(new DummySecurityManager());
			
			Properties props = new Properties();
			props.put(Context.INITIAL_CONTEXT_FACTORY,"org.jnp.interfaces.NamingContextFactory");
			props.put(Context.PROVIDER_URL, "jnp://localhost:1099");
			props.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
			props.put("jnp.disableDiscovery", "true"); 
			InitialContext ctx;
			ctx = new InitialContext(props);
			
			mbeanServer = (MBeanServerConnection) ctx
					.lookup("jmx/rmi/RMIAdaptor");
			
			deploymentMBean = new ObjectName("slee:name=DeploymentMBean");

			return mbeanServer;
		} catch (final Exception e) {
			Display.getDefault().asyncExec(new Runnable() {
				 public void run() {
					 MessageDialog.openError(new Shell(), "Connection failed", e.toString());
				 }
				});
		}
		return null;
	}
	
	private void deploy(String file)
	{
		try {
			if(mbeanServer == null) throw new Exception("Not connected to server.");
			
			String url = file.startsWith("file:/") ? file : "file:///"+file;
			
			Object result = (Object) mbeanServer.invoke(deploymentMBean,
						"install", new Object[] { url },
						new String[] { String.class.getName() });
		} catch (Exception e) {
			MessageDialog.openError(new Shell(), "Deployment failed", e.toString());
		}
	}
	
	private void undeploy(DeployableUnitID id)
	{
		try {
			if(mbeanServer == null) throw new Exception("Not connected to server.");
			
			Object result = (Object) mbeanServer.invoke(deploymentMBean,
						"uninstall", new Object[] { id },
						new String[] { DeployableUnitID.class.getName() });
		} catch (Exception e) {
			MessageDialog.openError(new Shell(), "Undeployment failed", e.toString());
		}
	}
	private void addRemoteDUs() throws Exception
	{
		DeployableUnitID[] duIDs = (DeployableUnitID[])
			mbeanServer.getAttribute(deploymentMBean, "DeployableUnits");
		
		DeployableUnitDescriptor[] descriptors= new DeployableUnitDescriptor[duIDs.length];
		
		for(int q=0; q<duIDs.length; q++)
		{
			try
			{
				DeployableUnitDescriptor descriptor = 
					(DeployableUnitDescriptor) mbeanServer.invoke(deploymentMBean,
							"getDescriptor", new Object[] {duIDs[q]},
							new String[] {DeployableUnitID.class.getName()});
				descriptors[q] = descriptor;
			}
			catch(Exception e){}// ID -1 causes exception so ignore it
		}
		
		for(int q=0; q<duIDs.length; q++)
		{
			if(descriptors[q] != null)
			{
				duToDUDescription.put(
					getDUFileName(descriptors[q].getURL()), descriptors[q]);
				duToDUID.put(getDUFileName(descriptors[q].getURL()), duIDs[q]);
				addDURow(descriptors[q].getURL(), "Deployed", "Undeploy");
			}
		}
	}
	private void initDUs()
	{
		duToDUDescription.clear();
		duToDUID.clear();
		
		Object it;
		
		do
		{
			it = availableDeployableUnits.getElementAt(0);
			if( it != null)
				availableDeployableUnits.remove(it);
		}
		while (it != null);
		
		try {
			if(mbeanServer != null)
				addRemoteDUs();
			addUndeployedItems();
			
			update();
			
			TableColumn[] cols = availableDeployableUnits.getTable().getColumns();
			for(int q=0; q<cols.length; q++) cols[q].pack();
		} catch (Exception e) {
			MessageDialog.openWarning(new Shell(), "Connection error", 
					"Error retrieving the installed deployable units from the SLEE server." 
					+ e.toString());
		}
	}
	
	private void addUndeployedItems()
	{
		DTDXML xml[] = DUFinder.getDefault().getComponents(BaseFinder.BINARY, projectName);
		for (int i = 0; i < xml.length; i++) {
			DeployableUnitXML du = (DeployableUnitXML) xml[i];
			addDU(du.getJarLocation(), "Not deployed");
		}
	}
	
	private static String getDUFileName(String duURL)
	{
		int idx = Math.max(duURL.lastIndexOf("/"), duURL.lastIndexOf("\\"));
		String fileName = duURL.substring(idx + 1);
		return fileName;
	}
	
	public void addDU(String duURL, String status) {	
		if(duToDUDescription.get(getDUFileName(duURL)) != null) return;
		addDURow(duURL, status);

	}
	
	private void addDURow(String duURL, String status)
	{
		addDURow(duURL, status, null);
	}
	
	private void addDURow(String duURL, String status, String button)
	{
		HashMap map = new HashMap();		
		map.put("Name", duURL);
		map.put("Status", status);
		if(button != null) map.put("ButtonText_2", button);
		availableDeployableUnits.addRow(map);
	}
	
	public void update() {
		super.update();
		
		if (wizardPage != null)
			wizardPage.dialogChanged();
	}
	
	
	private EditableTableViewer availableDeployableUnits;
	private Button selectButton;
	private Button deselectButton;
	private SbbEventsPage wizardPage;

}