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

import java.util.HashMap;

import org.alcatel.jsce.interfaces.com.IPageAdaptor;
import org.alcatel.jsce.object.ObjectReference;
import org.eclipse.jface.wizard.WizardDialog;
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
 *  Wizard used to select referenced objects.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class ObjectRefWidget extends Composite {

	private Table referenceTable = null;
	private Button defineButton = null;
	private IPageAdaptor page = null;
	
	/** The row selected in the table*/
	private int  selectedRow = -1;
	/** The project in which we are looking for reference objects*/
	private String projectName = null;
	/** The object attributes for which we must resolve references*/
	private HashMap[] profileAttributes;

	/**
	 * @param parent the SWT parent
	 * @param style the SWT Style
	 * @param page the page containing the widget
	 * @param the project name in which we are looking for referenced OSP object
	 */
	public ObjectRefWidget(Composite parent, int style, IPageAdaptor page, String project) {
		super(parent, style);
		this.page = page;
		this.projectName = project;
		initialize();
	}

	private void initialize() {
		createReferenceTable();
	}

	/**
	 * This method initializes referenceTable	
	 *
	 */
	private void createReferenceTable() {
		referenceTable = new Table(this, SWT.NONE);
		referenceTable.setHeaderVisible(true);
		referenceTable.setLinesVisible(true);
		referenceTable.setBounds(new org.eclipse.swt.graphics.Rectangle(7,9,348,250));
		referenceTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				selectedRow = referenceTable.getSelectionIndex();
			}
		});
		TableColumn attrNametableColumn = new TableColumn(referenceTable, SWT.NONE);
		attrNametableColumn.setWidth(90);
		attrNametableColumn.setText("Attribute name");
		TableColumn ObjectRefTableColumn = new TableColumn(referenceTable, SWT.NONE);
		ObjectRefTableColumn.setWidth(110);
		ObjectRefTableColumn.setText("Object Referenced");
		TableColumn attrReftableColumn = new TableColumn(referenceTable, SWT.NONE);
		attrReftableColumn.setWidth(115);
		attrReftableColumn.setText("Attribute Referenced");
	}
	
	/**
	 * This method load profile attributes which are Object_ref type
	 * @param attributes a list of hashmap of profile attribute.
	 */
	public void loadAttribute(HashMap[] profileAttributes){
		referenceTable.removeAll();
		this.profileAttributes = profileAttributes;
	}
	
	public boolean isPageComplete(){
		boolean complete = true;
		TableItem[] items = referenceTable.getItems();
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			if(item.getText(1).equals("x") && item.getText(2).equals("x")){
				complete = false;
				page.setErrorMessage("Resolve references to OSP Objects");
				page.setPageComplete(false);
				return false;
			}
		}
		page.setErrorMessage(null);
		page.setPageComplete(true);
		return complete;
	}

	public void setProject(String project) {
		this.projectName = project;
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
