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
import java.util.List;

import org.alcatel.jsce.object.ObjectIndex;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

/**
 *  Description:
 * <p>
 * Composite widget used to set OSP object indexList on attributes.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class OSPObjectIndexWidget extends Composite {

	private Table indexTable = null;
	private Button addIndexButton = null;
	private Button removeButton = null;
	
	/** List of index*/
	private List indexList = null;
	/** The lsit of profile specs*/
	private HashMap[] profilesMap = null;
	/** The row which is currently selected*/
	private int  selectedRow = -1;

	/**
	 * @param parent
	 * @param style
	 */
	public OSPObjectIndexWidget(Composite parent, int style) {
		super(parent, style);
		indexList = new  ArrayList();
		initialize();
	}

	private void initialize() {
		createIndexTable();
		setSize(new org.eclipse.swt.graphics.Point(510,185));
		addIndexButton = new Button(this, SWT.NONE);
		addIndexButton.setBounds(new org.eclipse.swt.graphics.Rectangle(420,45,68,23));
		addIndexButton.setText("add");
		addIndexButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				IndexDialog dialog = new IndexDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), profilesMap, (ObjectIndex[]) indexList.toArray(new ObjectIndex[indexList.size()]));
				if(dialog.open() == Dialog.OK){
					addIndexInfo(dialog.getSelected());
				}
			}
			
		});
		removeButton = new Button(this, SWT.NONE);
		removeButton.setBounds(new org.eclipse.swt.graphics.Rectangle(420,90,68,23));
		removeButton.setText("remove");
		removeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(selectedRow>-1){
					ObjectIndex index = (ObjectIndex) indexList.get(selectedRow);
					if(index.getName().equals("i_ri")){
						MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Alcatel SCE Warning", "You can not remove the Mandatory index ri");
					}else{
						indexList.remove(selectedRow);
						indexTable.remove(selectedRow);
					}
				}
			}
		});
	}
	
	/**
	 * Add an index.
	 * @param name the index ,name
	 * @param unic the unicity of the field
	 * @param attribute_names the list whose the index is created
	 */
	public void addIndexInfo(String name, boolean unic, String[] attribute_names, boolean isSlee, boolean isSmf, boolean isKey){
		ObjectIndex index = new ObjectIndex(attribute_names, name, unic);
		indexList.add(index);
		TableItem item = new TableItem(indexTable, SWT.NONE);
		 item.setText(new String[] { name,getStringAttribute( unic),getStringAttribute(isSlee), getStringAttribute(isSmf), getStringAttribute(isKey),  getStringAttributes(attribute_names)});
	}
	
	public void addIndexInfo(ObjectIndex index) {
		indexList.add(index);
		TableItem item = new TableItem(indexTable, SWT.NONE);
		 item.setText(new String[] { index.getName(), getStringAttribute(index.isUnicity()), getStringAttribute(index.isSlee()), getStringAttribute(index.isSmf()), getStringAttribute(index.isKey()),  getStringAttributes(index.getAttributes())});
		
	}
	
	public TableItem getIndexByName(String name){
		TableItem[]  items = indexTable.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item_i = items[i];
			if(item_i.getText(0).equals(name)){
				return item_i;
			}
		}
		return null;
	}

	private String getStringAttributes(String[] attribute_names) {
		String result = "";
		for (int i = 0; i < attribute_names.length; i++) {
			String attr_i = attribute_names[i];
			result+=";"+ attr_i;
		}
		return result;
	}

	private String getStringAttribute(boolean unic) {
		if(unic)
			return "true";
		else return "false";
	}

	/**
	 * This method initializes indexTable	
	 *
	 */
	private void createIndexTable() {
		indexTable = new Table(this, SWT.NONE);
		indexTable.setHeaderVisible(true);
		indexTable.setLinesVisible(true);
		indexTable.setBounds(new org.eclipse.swt.graphics.Rectangle(8,6,404,163));
		indexTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				selectedRow = indexTable.getSelectionIndex();
			}
		});
		TableColumn indexNameTableColumn = new TableColumn(indexTable, SWT.NONE);
		indexNameTableColumn.setWidth(80);
		indexNameTableColumn.setText("Index name");
		TableColumn indexUnicityTableColumn = new TableColumn(indexTable, SWT.NONE);
		indexUnicityTableColumn.setWidth(50);
		indexUnicityTableColumn.setText("Unicity");
		TableColumn isSleeTableColumn = new TableColumn(indexTable, SWT.NONE);
		isSleeTableColumn.setWidth(50);
		isSleeTableColumn.setText("Is SLEE");
		TableColumn isSMFTableColumn = new TableColumn(indexTable, SWT.NONE);
		isSMFTableColumn.setWidth(50);
		isSMFTableColumn.setText("Is SMF");
		TableColumn pkTableColumn = new TableColumn(indexTable, SWT.NONE);
		pkTableColumn.setWidth(60);
		pkTableColumn.setText("Is P.Key");
		TableColumn indexAttributesTableColumn = new TableColumn(indexTable, SWT.NONE);
		indexAttributesTableColumn.setWidth(130);
		indexAttributesTableColumn.setText("Attributes");
	}

	/**
	 * Set the array of profile specs.
	 * @param tableRows an array of profile spec
	 */
	public void setProfileSpec(HashMap[] tableRows) {
		this.profilesMap = tableRows;	
	}

	/**
	 * Clear the table.
	 */
	public void removeAll() {
		this.indexTable.removeAll();
		
	}
	
	/**@return the set of indexes*/
	public ObjectIndex[] getSelectedIndex(){
		return (ObjectIndex[]) indexList.toArray(new ObjectIndex[indexList.size()]);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
