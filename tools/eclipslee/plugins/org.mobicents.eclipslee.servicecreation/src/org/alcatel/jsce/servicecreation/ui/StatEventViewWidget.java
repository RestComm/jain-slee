package org.alcatel.jsce.servicecreation.ui;

import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.statevent.EventCatalog;
import org.alcatel.jsce.statevent.EventStatParser;
import org.alcatel.jsce.statevent.StatEvent;
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

public class StatEventViewWidget extends Composite {

	private Group tableGroup = null;
	private Table statTable = null;
	private boolean editable = false;
	private List events = null;
	private EventCatalog catalog = null;
	
	/**
	 * @param parent
	 * @param style the SWT style
	 * @param editable defines whether the table is editable or not
	 * @param catalog the stat event catalog containing the event
	 */
	public StatEventViewWidget(Composite parent, int style, List events, boolean editable, EventCatalog catalog) {
		super(parent, style);
		this.editable = editable;
		this.events = events;
		this.catalog = catalog;
		initialize();
		loadStats(events);
	}

	/**
	 * Load the alarms list in the table.
	 * @param alarms the list of alarms to load.
	 */
	public void loadStats(List events) {
		statTable.removeAll();
		this.events = events;
		for (Iterator iter = events.iterator(); iter.hasNext();) {
			StatEvent event_i = (StatEvent) iter.next();
			TableItem item_i = new TableItem(statTable, SWT.NULL);
			String value =( new Integer (event_i.getValue())).toString();
			item_i.setText(new String[] { event_i.getName(), event_i.getParent(), value, event_i.getInc_type(), event_i.getDump_ind(), event_i.getDescription(), event_i.getSmp_inc_type(), event_i.getMacro()});
		}
		
	}

	private void initialize() {
		createTableGroup();
		setSize(new org.eclipse.swt.graphics.Point(622,325));
	}

	/**
	 * This method initializes tableGroup	
	 *
	 */
	private void createTableGroup() {
		tableGroup = new Group(this, SWT.NONE);
		tableGroup.setText("Stat Event description");
		createStatTable();
		tableGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(15,12,588,296));
	}

	/**
	 * This method initializes statTable	
	 *
	 */
	private void createStatTable() {
		statTable = new Table(tableGroup, SWT.NONE);
		statTable.setHeaderVisible(true);
		statTable.setLinesVisible(true);
		statTable.setBounds(new org.eclipse.swt.graphics.Rectangle(12,15,560,255));
		TableColumn nameColumn = new TableColumn(statTable, SWT.NONE);
		nameColumn.setWidth(90);
		nameColumn.setText("Name");
		TableColumn levelColumn = new TableColumn(statTable, SWT.NONE);
		levelColumn.setWidth(50);
		levelColumn.setText("Parent");
		TableColumn problemColumn = new TableColumn(statTable, SWT.NONE);
		problemColumn.setWidth(40);
		problemColumn.setText("Value");
		TableColumn typeColumn = new TableColumn(statTable, SWT.NONE);
		typeColumn.setWidth(60);
		typeColumn.setText("INCTYPE");
		TableColumn causeColumn = new TableColumn(statTable, SWT.NONE);
		causeColumn.setWidth(60);
		causeColumn.setText("DUMPID");
		TableColumn effecttableColumn = new TableColumn(statTable, SWT.NONE);
		effecttableColumn.setWidth(100);
		effecttableColumn.setText("Description");
		TableColumn actionColumn = new TableColumn(statTable, SWT.NONE);
		actionColumn.setWidth(80);
		actionColumn.setText("SMPINCTYPE");
		TableColumn numberColumn = new TableColumn(statTable, SWT.NONE);
		numberColumn.setWidth(90);
		numberColumn.setText("Display Name");
		
		/*Contextual menu*/
		if(editable){
			Menu tableMenu = new Menu(statTable);
			MenuItem item = new MenuItem(tableMenu, SWT.NONE);
			item.setText("Remove stat");
			item.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					int selection = statTable.getSelectionIndex();
					if(selection > -1){
						/*1. get the StatEvent*/
						StatEvent eventSelected = (StatEvent) events.get(selection);
						if(MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Alcatel SCE", "Are you sure you want delete "+eventSelected.getName()+ " ?" )){
							/*2. Remove the event from the list of events*/
							events.remove(eventSelected);
							/*3. Remove the event form the xml tree of the catalog*/
							EventStatParser.removeStatEvent(eventSelected,catalog);
							/*4. Re-write the new XML tree in an xml file*/
							ServiceCreationPlugin.getDefault().getMainControl().createStatEventCatalog(catalog);
							/*5. Re-load the alarm list*/
							loadStats(catalog.getAllStatEvent());
						}
					}
				}
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			MenuItem itemEdit = new MenuItem(tableMenu, SWT.NONE);
			
			itemEdit.setText("Edit stat");
			itemEdit.addSelectionListener(new SelectionListener() {
				public void widgetSelected(SelectionEvent e) {
					int selection = statTable.getSelectionIndex();
					if(selection > -1){
						/*1. get the alarm*/
						StatEvent eventSelected = (StatEvent) events.get(selection);
						/*2. Open the edit window*/
						EditStatEventDialog dialog = new EditStatEventDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), eventSelected, events,catalog, false );
						if(dialog.open() == Window.OK){
							//The flie has been modified and we must reload the list
							loadStats(events);
						}				
					}
				}
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			statTable.setMenu(tableMenu);
			}
	}

	public EventCatalog getCatalog() {
		return catalog;
	}

	public void setCatalog(EventCatalog catalog) {
		this.catalog = catalog;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
