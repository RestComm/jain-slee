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

import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.alarm.Alarm;
import org.alcatel.jsce.alarm.AlarmCatalogParser;
import org.alcatel.jsce.alarm.AlarmsCatalog;
import org.alcatel.jsce.interfaces.com.IPageAdaptor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.mobicents.eclipslee.servicecreation.ServiceCreationPlugin;

/**
 *  Description:
 * <p>
 * Widget allowing to display a list of alarms but also to edit them.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class AlarmViewWidget extends Composite {

	private Group tableGroup = null;
	private Table alarmTable = null;
	private boolean editable = false;
	private List alarms = null;
	private AlarmsCatalog catalog = null;
	private IPageAdaptor adaptor = null;

	/**
	 * @param parent
	 * @param style
	 * @param editable 
	 */
	public AlarmViewWidget(Composite parent, int style, List alarms, boolean editable, AlarmsCatalog cat, IPageAdaptor adaptor) {
		super(parent, style);
		this.adaptor = adaptor;
		this.editable = editable;
		this.catalog = cat;
		initialize();
		loadAlarms(alarms);
	}

	private void validWidget() {
		for (Iterator iter = alarms.iterator(); iter.hasNext();) {
			Alarm alarm = (Alarm) iter.next();
			IStatus val = JavaConventions.validateMethodName(alarm.getName());
			if (val.getSeverity() == IStatus.ERROR) {
				this.adaptor.setErrorMessage("The alarm name will be used in  java methods of generated components and "+val.getMessage());
				this.adaptor.setPageComplete(false);
				return;
			}
		}
		this.adaptor.setErrorMessage(null);
		this.adaptor.setPageComplete(true);
	}

	/**
	 * Load the alarms list in the table.
	 * @param alarms the list of alarms to load.
	 */
	public void loadAlarms(List alarms) {
		this.alarms = alarms;
		alarmTable.removeAll();
		for (Iterator iter = alarms.iterator(); iter.hasNext();) {
			Alarm alarm_i = (Alarm) iter.next();
			TableItem item_i = new TableItem(alarmTable, SWT.NULL);
			String number =( new Integer (alarm_i.getAlarmNumber())).toString();
			item_i.setText(new String[] { alarm_i.getName(), number, alarm_i.getLevel(), alarm_i.getProblem(), alarm_i.getType(), alarm_i.getCause(), alarm_i.getEffect(), alarm_i.getAction()});
		}
		validWidget();
		
	}

	private void initialize() {
		createTableGroup();
		setSize(new org.eclipse.swt.graphics.Point(640,325));
	}

	/**
	 * This method initializes tableGroup	
	 *
	 */
	private void createTableGroup() {
		tableGroup = new Group(this, SWT.NONE);
		tableGroup.setText("Alarm description");
		createAlarmTable();
		tableGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(15,12,609,296));
	}
	
	public void setGroupSize(int width, int height){
		tableGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(15,12,width,height));
	}

	/**
	 * This method initializes alarmTable	
	 *
	 */
	private void createAlarmTable() {
		alarmTable = new Table(tableGroup, SWT.NONE);
		alarmTable.setHeaderVisible(true);
		alarmTable.setLinesVisible(true);
		alarmTable.setBounds(new org.eclipse.swt.graphics.Rectangle(12,15,582,255));
		TableColumn nameColumn = new TableColumn(alarmTable, SWT.NONE);
		nameColumn.setWidth(90);
		nameColumn.setText("Name");
		TableColumn levelColumn = new TableColumn(alarmTable, SWT.NONE);
		levelColumn.setWidth(50);
		levelColumn.setText("Number");
		TableColumn problemColumn = new TableColumn(alarmTable, SWT.NONE);
		problemColumn.setWidth(60);
		problemColumn.setText("Level");
		TableColumn typeColumn = new TableColumn(alarmTable, SWT.NONE);
		typeColumn.setWidth(90);
		typeColumn.setText("Problem");
		TableColumn causeColumn = new TableColumn(alarmTable, SWT.NONE);
		causeColumn.setWidth(60);
		causeColumn.setText("Type");
		TableColumn effecttableColumn = new TableColumn(alarmTable, SWT.NONE);
		effecttableColumn.setWidth(90);
		effecttableColumn.setText("Cause");
		TableColumn actionColumn = new TableColumn(alarmTable, SWT.NONE);
		actionColumn.setWidth(90);
		actionColumn.setText("Effect");
		TableColumn numberColumn = new TableColumn(alarmTable, SWT.NONE);
		numberColumn.setWidth(130);
		numberColumn.setText("Action");
		
		/*Contextual menu*/
		if(editable){
			Menu tableMenu = new Menu(alarmTable);
			MenuItem item = new MenuItem(tableMenu, SWT.NONE);
			item.setText("Remove alarm");
			item.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					int selection = alarmTable.getSelectionIndex();
					if(selection > -1){
						/*1. get the alarm*/
						Alarm alarmSelected = (Alarm) alarms.get(selection);
						if(MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Alcatel SCE", "Are you sure you want delete "+alarmSelected.getName()+ " ?" )){
							/*2. Remove the alarm from the list of event*/
							catalog.getAlarmEvents().remove(alarmSelected);
							/*3. Remove the alarm form the xml tree of the catalog*/
							AlarmCatalogParser.removeAlarm(alarmSelected, catalog.getDocument());
							/*4. Re-write the new WML tree in an wl file*/
							ServiceCreationPlugin.getDefault().getMainControl().createAlarmCatalog(catalog);
							/*5. Re-load the alarm list*/
							loadAlarms(catalog.getAlarmEvents());
						}
					}
				}
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			MenuItem itemEdit = new MenuItem(tableMenu, SWT.NONE);
			
			itemEdit.setText("Edit alarm");
			itemEdit.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					int selection = alarmTable.getSelectionIndex();
					if(selection >-1){
						/*1. get the alarm*/
						Alarm alarmSelected = (Alarm) alarms.get(selection);
						/*2. Open the edit window*/
						EditAlarmDialog dialog = new EditAlarmDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), alarmSelected,catalog, false);
						if(dialog.open() == Window.OK){
							//The flie has been modified and we must reload the list
							loadAlarms(catalog.getAlarmEvents());
						}				
					}
				}
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			alarmTable.setMenu(tableMenu);
		}
	}

	public AlarmsCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(AlarmsCatalog catalog) {
		this.catalog = catalog;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
