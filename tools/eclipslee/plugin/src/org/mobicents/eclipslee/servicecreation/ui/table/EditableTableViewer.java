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

package org.mobicents.eclipslee.servicecreation.ui.table;

import java.util.HashMap;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author cath
 * @author Vladimir Ralev
 */
public class EditableTableViewer extends TableViewer implements SelectionListener {
	
	public static final int EDITOR_TEXT = 0;
	public static final int EDITOR_CHECKBOX = 1;
	public static final int EDITOR_CHOICE = 2;
	public static final int EDITOR_BUTTON = 3;
	public static final int EDITOR_NONE = 4; // Uneditable cell
	public static final int EDITOR_NUMERIC = 5;
	
	public static final int bigCellHeight = 22;
	public static final int bigCellButtonWidth = 100;
	
	Listener itemMeasureListener = new ItemMeasureListener();
	
	public EditableTableViewer(Composite parent, int style, String columnNames[], int columnEditors[], Object editorValues[][]) {
		super(parent, style|SWT.FULL_SELECTION);
		this.columnNames = columnNames;
		this.columnEditors = columnEditors;
		this.columnValues = editorValues;

		initialize(columnNames, columnEditors, editorValues);
	}

	public EditableTableViewer(Table table, String columnNames[], int columnEditors[], Object editorValues[][]) {
		super(table);

		this.columnNames = columnNames;
		this.columnEditors = columnEditors;
		this.columnValues = editorValues;

		initialize(columnNames, columnEditors, editorValues);
	}
	
	public void initialize(String [] columnNames, int columnEditors[], Object editorValues[][]) {

		if (columnNames.length != columnEditors.length && columnEditors.length != editorValues.length) {
			throw new IllegalArgumentException("The size of the columnNames, columnEditors and editorValues arrays must be the same.");			
		}
		getTable().addListener(SWT.MeasureItem, itemMeasureListener);
		
		
		// Don't use a hash lookup as we're storing HashMaps in the store and the
		// hashCode of these changes when the data inside them changes.
		setUseHashlookup(false);
		Table table = getTable();
		
		CellEditor editors[] = new CellEditor[columnNames.length];		
		TableColumn tableColumns[] = new TableColumn[columnNames.length];
		
		for (int i = 0; i < columnNames.length; i++) {
			tableColumns[i] = new TableColumn(table, SWT.NONE);
			tableColumns[i].setText(columnNames[i]);
			tableColumns[i].setWidth(150);
			tableColumns[i].setResizable(true);
			tableColumns[i].pack();
		}
		setColumnProperties(columnNames);
		
		for (int i = 0; i < columnEditors.length; i++) {
			switch (columnEditors[i]) {
				case EDITOR_TEXT:
					editors[i] = new TextCellEditor(table);
					break;
					
				case EDITOR_CHECKBOX:
					editors[i] = new CheckboxCellEditor(table);
					break;
					
				case EDITOR_CHOICE:
					String vals[] = new String[editorValues[i].length];
					for (int j = 0; j < editorValues[i].length; j++)
						vals[j] = (String) editorValues[i][j];
					
					editors[i] = new ComboBoxCellEditor(table, vals);
					break;
												
				case EDITOR_BUTTON:
				case EDITOR_NUMERIC:
					editors[i] = null;
					break;
					
				case EDITOR_NONE:
					editors[i] = null;
					break; // May need to use a TextCellEditor and make uneditable.
					
				default:
					// Error message about unrecognised cell editor.
					System.err.println("Invalid cell editor value: " + columnEditors[i]);
			}		
		}
		setCellEditors(editors);
				
		table.setHeaderVisible(true);
	
		table.addSelectionListener(this);		

		store = new DataStore();
		setContentProvider(new GenericContentProvider(this, store));
		setInput(store);

		setCellModifier(new GenericCellModifier(this, columnNames, columnEditors));
		setLabelProvider(new GenericLabelProvider(columnNames, columnEditors, editorValues));	
	}
	
	public void repack() {
		TableColumn[] cols = getTable().getColumns();
		for(int q=0; q<cols.length; q++) cols[q].pack();
		
		// Try to force a redraw of the table - this may not redraw the buttons though.
		getTable().layout(true);		

		// Try to force a redraw of the editors and buttons		
		Object data[] = store.getElements();
		for (int i= 0; i < data.length; i++) {
			HashMap map = (HashMap) data[i];
			
			for (int column = 0; column < columnEditors.length; column++) {
				if (columnEditors[column] == EDITOR_BUTTON) {
					
					Button button = (Button) map.get("Button_" + column);
					if (button != null) {
						button.redraw();
						button.getParent().layout(true);
					}
					TableEditor editor = (TableEditor) map.get("Editor_" + column);
					if (editor != null) {
						editor.layout();
					}					
				}				
				
				if (columnEditors[column] == EDITOR_NUMERIC) {
					Scale scale = (Scale) map.get("Scale_" + column);
					if (scale != null) {
						scale.redraw();
						scale.getParent().layout(false);
					}
					TableEditor editor = (TableEditor) map.get("Editor_" + column);
					if (editor != null) {
						editor.layout();
					}					
				}
			}
		}
		
	}
	
	public HashMap addRow(HashMap map) {
		// Make the table lines visible
		getTable().setLinesVisible(true);
		store.add(map);
		return map;
	}
	
	public HashMap addRow() {
		// Make the table lines visible
		getTable().setLinesVisible(true);
		HashMap map = new HashMap();

		// Initialize the HashMap with sensible values.
		for (int i = 0; i < columnNames.length; i++) {
			switch (columnEditors[i]) {
				case EDITOR_CHECKBOX:
					map.put(columnNames[i], Boolean.FALSE);
					break;
					
				case EDITOR_CHOICE:
					map.put(columnNames[i], new Integer(0));
					break;
					
				case EDITOR_NUMERIC:
					map.put(columnNames[i], new Integer(0));
					break;
					
				default:
					map.put(columnNames[i], "?");
					break;			
			}			
		}
		
		store.add(map);
		return map;		
	}
	
	public void removeRow(HashMap map) {
		store.remove(map);		
	}

	public void remove(Object o) {
		HashMap map = (HashMap) o;
		
		for (int column = 0; column < columnEditors.length; column++) {
			if (columnEditors[column] == EDITOR_BUTTON) {
				if (map.get("Editor_" + column) != null) {
					TableEditor editor = (TableEditor) map.get("Editor_" + column);
					Button button = (Button) map.get("Button_" + column);
					
					button.setVisible(false);
					button.dispose();
					editor.dispose();
					
					map.remove("Editor_" + column);
					map.remove("Button_" + column);
				}
			}
			if (columnEditors[column] == EDITOR_NUMERIC) {
				if (map.get("Editor_" + column) != null) {
					TableEditor editor = (TableEditor) map.get("Editor_" + column);
					Scale scale = (Scale) map.get("Scale_" + column);
					scale.setVisible(false);
					scale.dispose();
					editor.dispose();
					
					map.remove("Editor_" + column);
					map.remove("Scale_" + column);
				}
			}
		}
		
		/* 
		 * Ugly hack! Not needed for Eclipse 3.3, but Eclipse 3.2 has
		 * a bug http://dev.eclipse.org/newslists/news.eclipse.platform.swt/msg29738.html
		 * Reinstall the listener each time you remove seems to work.
		 */
		this.getTable().removeListener(SWT.MeasureItem, itemMeasureListener);
		super.remove(o);	
		this.getTable().addListener(SWT.MeasureItem, itemMeasureListener);
	}
	
	public void add(Object o) {

		super.add(o);

		// Get all existing rows and find row(s) without a defined button.
		// Create buttons for it.
		
		Table table = getTable();
		TableItem items[] = getTable().getItems();		
		for (int column = 0; column < columnEditors.length; column++) {
			if (columnEditors[column] == EDITOR_BUTTON) {
				
				for (int row = 0; row < items.length; row++) {
					TableItem item = items[row];					
					HashMap map = (HashMap) item.getData();
					if (map.get("Editor_" + column) == null) {
						TableEditor editor = new TableEditor(table);
						Button button = new Button(table, SWT.NONE);
						String buttonText = (String) map.get("ButtonText_" + column);
						if(buttonText == null)
							button.setText((String) columnValues[column][0]);
						else
							button.setText(buttonText);
						button.addSelectionListener((SelectionListener) columnValues[column][1]);
						button.pack();
						editor.grabHorizontal = true;
						editor.minimumWidth = button.getSize().x;
						editor.horizontalAlignment = SWT.LEFT;
						editor.setEditor(button, item, column); // TableItem.getItems(row), column
						map.put("Editor_" + column, editor);
						map.put("Button_" + column, button);
					}
				}
			}
			
			if (columnEditors[column] == EDITOR_NUMERIC) {
				
				for (int row = 0; row < items.length; row++) {
					TableItem item = items[row];
					HashMap map = (HashMap) item.getData();
					if (map.get("Editor_" + column) == null) {
						TableEditor editor = new TableEditor(table);
						Scale scale = new Scale(table, SWT.NONE);
						int min = ((Integer) columnValues[column][0]).intValue();
						int max = ((Integer) columnValues[column][1]).intValue();
						int increment = ((Integer) columnValues[column][2]).intValue();
						
						scale.setMinimum(min);
						scale.setMaximum(max);
						scale.setIncrement(increment);
						scale.pack();
						
						editor.grabHorizontal = true;
						editor.minimumWidth = scale.getSize().x;
						editor.horizontalAlignment = SWT.LEFT;
						editor.setEditor(scale, item, column);
						map.put("Editor_" + column, editor);
						map.put("Scale_" + column, scale);
					}
				}
			}			
		}
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {		
	}

	public void widgetSelected(SelectionEvent event) {		
		
		// SWT.CHECK in object detail field means a check button changed value		
		if ((event.detail & SWT.CHECK) == SWT.CHECK) {
			System.err.println("A checkbox changed value.");			
		}
	}
		
	public DataStore getStore() {
		return store;
	}
	
	private class ItemMeasureListener implements Listener
	{
		public void handleEvent(Event event) {
			if (bigCellHeight == SWT.DEFAULT) return;
			event.height = bigCellHeight;
			TableColumn column = getTable().getColumn(event.index);
			TableItem ti = (TableItem) event.item;
			HashMap map = (HashMap) ti.getData();
			if (map.get("Button_" + event.index) != null)
				event.width = bigCellButtonWidth;
		}
	}
	
	protected DataStore store;	
	private String columnNames[];
	private int columnEditors[];
	private Object columnValues[][];
}
