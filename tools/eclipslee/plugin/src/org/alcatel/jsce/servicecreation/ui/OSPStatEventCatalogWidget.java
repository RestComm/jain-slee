package org.alcatel.jsce.servicecreation.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.alcatel.jsce.statevent.EventCatalog;
import org.alcatel.jsce.statevent.StatEvent;
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

public class OSPStatEventCatalogWidget extends Composite {

	/** The list of presents catalogs */
	private List catalogsList = null;

	/** Alarm icon */
	private Image imageAlarm = null;

	/** Represents the current list of event (depends on the catalog selected) */
	private List eventStatList = null;

	/** Represents the list of alarms currently selected */
	private List selectedStatEvent = null;

	/** The current line selected in the event table */
	private int currentAlarmsRowSelected = -1;

	/**
	 * The current line selected in the selected table. This int will be used in the deslect action
	 */
	private int currentdeslectAlarmsRowSelected = -1;

	/** Represents the current catalog */
	private EventCatalog currentCatalog = null;

	/**
	 * Map an alarm selected in tn the alarm list with the original list [Alarm, local copy of the alarm list where the
	 * alarm comes]
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
	 *            the SWT parent
	 * @param style
	 *            the SWT style
	 * @param catalogs
	 *            the list of all stat event catalogs available the list of
	 * @link org.alcatel.jsce.statevent.EventCatalog
	 */
	public OSPStatEventCatalogWidget(Composite parent, int style, List catalogs) {
		super(parent, style);
		eventStatList = new ArrayList();// init
		selectedStatEvent = new ArrayList();
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
			EventCatalog catalog_i = (EventCatalog) iter.next();
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
		this.setSize(new org.eclipse.swt.graphics.Point(518, 367));
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
		levelColumn.setText("Parent");
		TableColumn problemColumn = new TableColumn(eventTable, SWT.NONE);
		problemColumn.setWidth(120);
		problemColumn.setText("Macro");
	}

	/**
	 * This method initializes eventGroup
	 * 
	 */
	private void createEventGroup() {
		eventGroup = new Group(catalogGroup, SWT.NONE);
		createEventTable();
		eventGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(8, 153, 315, 172));
		eventGroup.setText("Stat Event");
		viewButton = new Button(eventGroup, SWT.NONE);
		viewButton.setBounds(new org.eclipse.swt.graphics.Rectangle(48, 139, 64, 23));
		viewButton.setText("View Event");
		viewAllButton = new Button(eventGroup, SWT.NONE);
		viewAllButton.setBounds(new org.eclipse.swt.graphics.Rectangle(153, 139, 64, 23));
		viewAllButton.setText("View All");
		viewAllButton.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (currentCatalog != null) {
					StatEventViewDialog dialog = new StatEventViewDialog(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), eventStatList, false, new EventCatalog());
					dialog.open();
				}

			}

			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});
		viewButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (currentAlarmsRowSelected > -1 && eventStatList.size() >= currentAlarmsRowSelected) {
					List toview = new ArrayList();
					StatEvent eventSelected = (StatEvent) eventStatList.get(currentAlarmsRowSelected);
					toview.add(eventSelected);
					StatEventViewDialog dialog = new StatEventViewDialog(PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getShell(), toview, false, new EventCatalog());
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
		selectedGroup.setText("Selected Stat Events");
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
		catalogComposite = new Composite(catalogGroup, SWT.NONE);
		catalogComposite.setBounds(new org.eclipse.swt.graphics.Rectangle(6, 15, 306, 54));
		label = new Label(catalogComposite, SWT.NONE);
		label.setBounds(new org.eclipse.swt.graphics.Rectangle(5, 24, 124, 13));
		label.setText("Statistic Event Catalogs:");
		catalogCombo = new CCombo(catalogComposite, SWT.READ_ONLY | SWT.BORDER);
		catalogCombo.setBounds(new org.eclipse.swt.graphics.Rectangle(132,24,166,17));
		//catalogCombo.setEditable(true);
		catalogCombo.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				/* 1. Get the currecnt selection */
				int selectionRow = catalogCombo.getSelectionIndex();
				if (selectionRow > -1) {
					Object object_i = catalogsList.get(selectionRow);
					if (object_i instanceof EventCatalog) {
						EventCatalog catalog_i = (EventCatalog) object_i;
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
	 * Load this catalog in the different fields of the widget. If the list of event
	 * 
	 * @param catalog_i
	 */
	private void loadCatalog(EventCatalog catalog_i) {
		currentCatalog = catalog_i;
		/* 0. Load the events alarm of the catalog */
		eventStatList = catalog_i.getAllStatEvent();
		/* 1. Load the description */
		String desc = catalog_i.getDocName();
		processtxt(desc);
		desctextArea.setText(desc);

		/* 2. Load the event in the table */
		eventTable.removeAll();
		// List events = catalog_i.getAlarmEvents();
		for (Iterator iter = eventStatList.iterator(); iter.hasNext();) {
			StatEvent event_i = (StatEvent) iter.next();
			TableItem item_i = new TableItem(eventTable, SWT.NULL);
			item_i.setText(new String[] { event_i.getName(), event_i.getParent(), event_i.getMacro() });
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
		desctextArea = new Text(descrComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL| SWT.BORDER);
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
				if (currentAlarmsRowSelected > -1 && eventStatList.size() >= currentAlarmsRowSelected) {
					StatEvent statEventSelected = (StatEvent) eventStatList.get(currentAlarmsRowSelected);
					eventStatList.remove(currentAlarmsRowSelected);
					selectedStatEvent.add(statEventSelected);
					eventTable.remove(currentAlarmsRowSelected);
					TableItem item_i = new TableItem(selectedTable, SWT.NULL);
					item_i.setText(new String[] { statEventSelected.getName() });
					// keep a track of the original list. If not, when
					// user changes the current catalog
					// we don't know nothing about where is the orginal
					// list.
					selectionMap.put(statEventSelected, eventStatList);
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
				if (currentdeslectAlarmsRowSelected > -1 && selectedStatEvent.size() >= currentdeslectAlarmsRowSelected) {
					StatEvent statEventSelected = (StatEvent) selectedStatEvent.get(currentdeslectAlarmsRowSelected);
					selectedStatEvent.remove(currentdeslectAlarmsRowSelected);
					selectedTable.remove(currentdeslectAlarmsRowSelected);

					currentdeslectAlarmsRowSelected = -1;
					/* 1.Find the oringnal list */
					List originList = (List) selectionMap.get(statEventSelected);
					if (originList != null) {
						originList.add(statEventSelected);
						/* 2. Remove from the map */
						selectionMap.remove(statEventSelected);
						/* 3. Is it hte current list */
						if (originList.equals(eventStatList)) {
							TableItem item_i = new TableItem(eventTable, SWT.NULL);
							item_i.setText(new String[] { statEventSelected.getName(), statEventSelected.getParent(),
									statEventSelected.getMacro() });
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
	public StatEvent[] getSelectedStatEvents() {
		StatEvent selected[] = new StatEvent[selectedStatEvent.size()];
		for (int i = 0; i < selectedStatEvent.size(); i++) {
			selected[i] = ((StatEvent) selectedStatEvent.get(i));
		}
		return selected;
	}

	/**
	 * Loads already-selected stat events
	 * 
	 * @param selected_stat
	 *            the list of [event, catalog caontaining this event]
	 */
	public void loadSelectedEvents(List selectedEvents) {
		for (Iterator iter = selectedEvents.iterator(); iter.hasNext();) {
			HashMap data = (HashMap) iter.next();
			for (Iterator iterator = data.keySet().iterator(); iterator.hasNext();) {
				// Extract the event and the associeted catalog
				StatEvent event_i = (StatEvent) iterator.next();
				EventCatalog catalog = (EventCatalog) data.get(event_i);
				List catalogEventList = catalog.getAllStatEvent();
				// Remove the event from the catalog list
				catalogEventList.remove(event_i);

				int index_cat = lookup(catalogsList, catalog);
				if (index_cat > -1) {
					// Get the list of event of this catalog (catalog of the class)
					EventCatalog catalog_i = (EventCatalog) catalogsList.get(index_cat);
					int index_event = lookupEvent(catalog_i.getAllStatEvent(), event_i);
					if (index_event > -1) {
						catalog_i.getAllStatEvent().remove(index_event);
						// keep a track of the original list. If not, when
						// user changes the current catalog
						// we don't know nothing about where is the orginal list.
						selectionMap.put(event_i, catalog_i.getAllStatEvent());
					}
				}

				// Select the event
				selectedStatEvent.add(event_i);
				TableItem item_i = new TableItem(selectedTable, SWT.NULL);
				item_i.setText(new String[] { event_i.getName() });

			}
		}

	}

	private int lookupEvent(List allStatEvent, StatEvent event_i) {
		int i = 0;
		for (Iterator iter = allStatEvent.iterator(); iter.hasNext(); i++) {
			StatEvent event_j = (StatEvent) iter.next();
			if (event_i.getParent().equals(event_j.getParent()))
				if (event_i.getValue() == (event_j.getValue()))
					return i;
		}
		return -1;
	}

	private int lookup(List listCat, EventCatalog catalog) {
		int i = 0;
		for (Iterator iter = listCat.iterator(); iter.hasNext(); i++) {
			EventCatalog cat_i = (EventCatalog) iter.next();
			if (cat_i.getCatalogName().equals(catalog.getCatalogName()))
				return i;
		}
		return -1;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
