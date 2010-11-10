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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.alarm.Alarm;
import org.alcatel.jsce.alarm.AlarmsCatalog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Description:
 * <p>
 * Widget for the alarm selection
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 * 
 */
public class OSPSbbAlarmCatalogWidget extends Composite {

	/** The list of presents catalogs */
	private List catalogsList = null;

	/** Alarm icon */
	private Image imageAlarm = null;

	/** Represents the current list of event (depends on the catalog selected) */
	private List eventAlarmsList = null;

	/** Represents the list of alarms currently selected */
	private List selectedAlamrs = null;

	/** The current line selected in the event table */
	private int currentAlarmsRowSelected = -1;

	/**
	 * The current line selected in the selected table. This int will be used in
	 * the deslect action
	 */
	private int currentdeslectAlarmsRowSelected = -1;

	/**
	 * Map an alarm selected in tn the alarm list with the original list [Alarm,
	 * local copy of the alarm list where the alarm comes]
	 */
	private HashMap selectionMap = null;

	private Group catalogGroup = null;

	private Table eventTable = null;

	private Group eventGroup = null;

	private Group selectedGroup = null;

	private Table selectedTable = null;

	private Composite catalogComposite = null;

	private Label label = null;

	private CCombo catalogCombo = null;

	private Composite descrComposite = null;

	private Label label1 = null;

	private Composite buttonComposite = null;

	private Button selectButton = null;

	private Button viewButton = null;

	private Button deselectButton = null;

	private Text desctextArea = null;

	private Button viewAllButton = null;

	/**
	 * @param parent
	 * @param style
	 * @param catalogs
	 *            the list of
	 * @link org.alcatel.jsce.alarm.AlarmsCatalog
	 */
	public OSPSbbAlarmCatalogWidget(Composite parent, int style, List catalogs) {
		super(parent, style);
		eventAlarmsList = new ArrayList();// init
		selectedAlamrs = new ArrayList();
		selectionMap = new HashMap();
		initialize();
		loadCatalogList(catalogs);
	}

	/**
	 * Loads the differents alarm catalogs in the combo list.
	 * 
	 * @param catalogs
	 */
	public void loadCatalogList(List catalogs) {
		this.catalogsList = catalogs;
		catalogCombo.removeAll();
		for (Iterator iter = catalogs.iterator(); iter.hasNext();) {
			AlarmsCatalog catalog_i = (AlarmsCatalog) iter.next();
			/* TODO To verrify if the name is not already used */
			catalogCombo.add(catalog_i.getCatalogName());
		}
		if (catalogs.size() > 0) {
			/*
			 * catalogCombo.select(0); catalogCombo.notify();
			 */
		}

	}

	private void initialize() {
		createCatalogGroup();
		createSelectedGroup();
		createButtonComposite();
		this.setSize(new org.eclipse.swt.graphics.Point(525, 367));
	}

	/**
	 * This method initializes catalogGroup
	 * 
	 */
	private void createCatalogGroup() {
		catalogGroup = new Group(this, SWT.NONE);
		createCatalogComposite();
		createDescrComposite();
		createEventGroup();
		catalogGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(15, 11, 331, 335));
		catalogGroup.setText("Catalog selection");
	}

	/**
	 * This method initializes eventTable
	 * 
	 */
	private void createEventTable() {
		eventTable = new Table(eventGroup, SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE);
		eventTable.setHeaderVisible(true);
		eventTable.setLinesVisible(true);
		eventTable.setBounds(new org.eclipse.swt.graphics.Rectangle(15, 25, 293, 104));
		eventTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				currentAlarmsRowSelected = eventTable.getSelectionIndex();
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		TableColumn nameColumn = new TableColumn(eventTable, SWT.NONE);
		nameColumn.setWidth(90);
		nameColumn.setText("Name");
		TableColumn levelColumn = new TableColumn(eventTable, SWT.NONE);
		levelColumn.setWidth(60);
		levelColumn.setText("Level");
		TableColumn problemColumn = new TableColumn(eventTable, SWT.NONE);
		problemColumn.setWidth(120);
		problemColumn.setText("Problem");
	}

	/**
	 * This method initializes eventGroup
	 * 
	 */
	private void createEventGroup() {
		eventGroup = new Group(catalogGroup, SWT.NONE);
		createEventTable();
		eventGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(8, 153, 315, 172));
		eventGroup.setText("Alarms");
		viewButton = new Button(eventGroup, SWT.NONE);
		viewButton.setBounds(new org.eclipse.swt.graphics.Rectangle(48, 139, 64, 23));
		viewButton.setText("View Alarm");
		viewAllButton = new Button(eventGroup, SWT.NONE);
		viewAllButton.setBounds(new org.eclipse.swt.graphics.Rectangle(153, 139, 64, 23));
		viewAllButton.setText("View All");
		viewAllButton.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				AlarmViewDialog dialog = new AlarmViewDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell(), eventAlarmsList, false, new AlarmsCatalog());
				dialog.open();

			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		viewButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (currentAlarmsRowSelected > -1 && eventAlarmsList.size() >= currentAlarmsRowSelected) {
					List toview = new ArrayList();
					Alarm alarmSelected = (Alarm) eventAlarmsList.get(currentAlarmsRowSelected);
					toview.add(alarmSelected);
					AlarmViewDialog dialog = new AlarmViewDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
							.getShell(), toview, false, new AlarmsCatalog());
					dialog.open();
				}

			}
		});
	}

	/**
	 * This method initializes selectedGroup
	 * 
	 */
	private void createSelectedGroup() {
		selectedGroup = new Group(this, SWT.NONE);
		selectedGroup.setText("Selected Alarms");
		createSelectedTable();
		selectedGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(353, 71, 159, 158));
	}

	/**
	 * This method initializes selectedTable
	 * 
	 */
	private void createSelectedTable() {
		selectedTable = new Table(selectedGroup, SWT.NONE);
		selectedTable.setHeaderVisible(true);
		selectedTable.setLinesVisible(true);
		selectedTable.setBounds(new org.eclipse.swt.graphics.Rectangle(14, 26, 109, 110));
		TableColumn nameColumn = new TableColumn(selectedTable, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		selectedTable.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				currentdeslectAlarmsRowSelected = selectedTable.getSelectionIndex();
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * This method initializes catalogComposite
	 * 
	 */
	private void createCatalogComposite() {
		catalogComposite = new Composite(catalogGroup,SWT.NONE);
		catalogComposite.setBounds(new org.eclipse.swt.graphics.Rectangle(6, 15, 306, 54));
		label = new Label(catalogComposite, SWT.NONE);
		label.setBounds(new org.eclipse.swt.graphics.Rectangle(14,24,83,13));
		label.setText("Alarm Catalogs:");
		catalogCombo = new CCombo(catalogComposite,  SWT.READ_ONLY | SWT.BORDER);
		catalogCombo.setBounds(new org.eclipse.swt.graphics.Rectangle(105,22,192,17));
		//catalogCombo.setEditable(true);
		catalogCombo.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				/* 1. Get the currecnt selection */
				int selectionRow = catalogCombo.getSelectionIndex();
				if (selectionRow > -1) {
					Object object_i = catalogsList.get(selectionRow);
					if (object_i instanceof AlarmsCatalog) {
						AlarmsCatalog catalog_i = (AlarmsCatalog) object_i;
						/* Load this catalog in the widget */
						loadCatalog(catalog_i);
					}
				}
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
	}

	/**
	 * Load this catalog in the different fields of the widget. If the list of
	 * event
	 * 
	 * @param catalog_i
	 */
	private void loadCatalog(AlarmsCatalog catalog_i) {
		/* 0. Load the events alarm of the catalog */
		eventAlarmsList = catalog_i.getAlarmEvents();
		/* 1. Load the description */
		String desc = catalog_i.getLongDescription();
		processtxt(desc);
		desctextArea.setText(desc);

		/* 2. Load the event in the table */
		eventTable.removeAll();
		List events = catalog_i.getAlarmEvents();
		for (Iterator iter = events.iterator(); iter.hasNext();) {
			Alarm alarm_i = (Alarm) iter.next();
			TableItem item_i = new TableItem(eventTable, SWT.NULL);
			item_i.setText(new String[] { alarm_i.getName(), alarm_i.getLevel(), alarm_i.getProblem() });
			item_i.setImage(imageAlarm);
		}

	}

	private void processtxt(String desc) {
		desc.replaceAll("'\n", " ");
		desc.replaceAll("\t", "");

	}

	/**
	 * This method initializes descrComposite
	 * 
	 */
	private void createDescrComposite() {
		descrComposite = new Composite(catalogGroup, SWT.NONE);
		descrComposite.setBounds(new org.eclipse.swt.graphics.Rectangle(7, 78, 302, 72));
		label1 = new Label(descrComposite, SWT.NONE);
		label1.setBounds(new org.eclipse.swt.graphics.Rectangle(12, 17, 57, 13));
		label1.setText("Description:");
		desctextArea = new Text(descrComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL|  SWT.BORDER);
		desctextArea.setBounds(new org.eclipse.swt.graphics.Rectangle(75, 18, 221, 44));
		desctextArea.setEditable(false);
	}

	/**
	 * This method initializes buttonComposite
	 * 
	 */
	private void createButtonComposite() {
		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setBounds(new org.eclipse.swt.graphics.Rectangle(370, 238, 106, 84));
		selectButton = new Button(buttonComposite, SWT.NONE);
		selectButton.setBounds(new org.eclipse.swt.graphics.Rectangle(7, 5, 71, 23));
		selectButton.setText(">>");
		selectButton.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (currentAlarmsRowSelected > -1 && eventAlarmsList.size() >= currentAlarmsRowSelected) {
					Alarm alarmSelected = (Alarm) eventAlarmsList.get(currentAlarmsRowSelected);
					eventAlarmsList.remove(currentAlarmsRowSelected);
					selectedAlamrs.add(alarmSelected);
					eventTable.remove(currentAlarmsRowSelected);
					TableItem item_i = new TableItem(selectedTable, SWT.NULL);
					item_i.setText(new String[] { alarmSelected.getName() });
					// keep a track of the original list. If not, when
					// user changes the current catalog
					// we don't know nothing about where is the orginal
					// list.
					selectionMap.put(alarmSelected, eventAlarmsList);
					currentAlarmsRowSelected = -1;
				}
			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		deselectButton = new Button(buttonComposite, SWT.NONE);
		deselectButton.setBounds(new org.eclipse.swt.graphics.Rectangle(6, 39, 71, 23));
		deselectButton.setText("<<");
		deselectButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (currentdeslectAlarmsRowSelected > -1 && selectedAlamrs.size() >= currentdeslectAlarmsRowSelected) {
					Alarm alarmSelected = (Alarm) selectedAlamrs.get(currentdeslectAlarmsRowSelected);
					selectedAlamrs.remove(currentdeslectAlarmsRowSelected);
					selectedTable.remove(currentdeslectAlarmsRowSelected);

					currentdeslectAlarmsRowSelected = -1;
					/* 1.Find the oringnal list */
					List originList = (List) selectionMap.get(alarmSelected);
					if (originList != null) {
						originList.add(alarmSelected);
						/* 2. Remove from the map */
						selectionMap.remove(alarmSelected);
						/* 3. Is it hte current list */
						if (originList.equals(eventAlarmsList)) {
							TableItem item_i = new TableItem(eventTable, SWT.NULL);
							item_i.setText(new String[] { alarmSelected.getName(), alarmSelected.getLevel(),
									alarmSelected.getProblem() });
						} else {
							// no refresh.
						}

					}
				}
			}
		});
	}

	public void dispose() {
		if (imageAlarm != null) {
			imageAlarm.dispose();
			imageAlarm = null;
		}
		super.dispose();
	}

	private List copy(List from) {
		List to = new ArrayList();
		for (Iterator iter = from.iterator(); iter.hasNext();) {
			Object element = iter.next();
			to.add(element);
		}
		return to;
	}

	/**
	 * @return the selected alarms.
	 */
	public Alarm[] getSelectedAlarms() {
		Alarm selected[] = new Alarm[selectedAlamrs.size()];
		for (int i = 0; i < selectedAlamrs.size(); i++) {
			selected[i] = ((Alarm) selectedAlamrs.get(i));
		}
		return selected;
	}

	/**
	 * Load a set of predefined selected alarms.
	 * @param selectedAlarms
	 */
	public void loadAlarmsSelected(List selectedAlarms) {
		for (Iterator iter = selectedAlarms.iterator(); iter.hasNext();) {
			Alarm alarm_i = (Alarm) iter.next();
			//Lookup the contained catalog (but in the class here)
			AlarmsCatalog containedCat = lookupCatalog(alarm_i.getCatalog());
			//remove from this catalog
			if(containedCat!=null){
				//Lookup the alarm in the list of alarm events
				int index = lookupAlarm(alarm_i, containedCat);
				if(index >-1){
					containedCat.getAlarmEvents().remove(index);
					selectedAlamrs.add(alarm_i);
					TableItem item_i = new TableItem(selectedTable, SWT.NULL);
					item_i.setText(new String[] { alarm_i.getName() });
					// keep a track of the original list. If not, when
					// user changes the current catalog
					// we don't know nothing about where is the orginal
					// list.
					selectionMap.put(alarm_i, containedCat.getAlarmEvents());
				}
			}
		}
	}

	private int lookupAlarm(Alarm alarm_i, AlarmsCatalog containedCat) {
		int i=0;
		for (Iterator iter = containedCat.getAlarmEvents().iterator(); iter.hasNext();i++) {
			Alarm alarm = (Alarm) iter.next();
			if(alarm_i.getName().equals(alarm.getName()) && alarm_i.getAlarmNumber() == alarm.getAlarmNumber()){
				return i;
			}
			
		}
		return -1;
	}

	private AlarmsCatalog lookupCatalog(AlarmsCatalog catalogs) {
		for (Iterator iter = catalogsList.iterator(); iter.hasNext();) {
			AlarmsCatalog cat = (AlarmsCatalog) iter.next();
			if(cat.getCatalogName().equals(catalogs.getCatalogName())){
				return cat;
			}
		}
		return null;
	}

} //  @jve:decl-index=0:visual-constraint="7,15"
