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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.mobicents.eclipslee.xml.ProfileSpecJarXML;

/**
 *  Description:
 * <p>
 *  Widget used to select index attribute in order to create 
 *  an OSP index object.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class IndexAttributeSelectionWidget extends Composite {

	private Table presentTable = null;
	private Button selectButton = null;
	private Button deselectButton = null;
	private Table selectedTable = null;
	private Label selectedLabel = null;
	
	private int selectedIndex = -1, presentIndex = -1;
	private  List profilesAttributes = null;
	private List selectedAttributes=null;

	/**
	 * @param parent
	 * @param style
	 */
	public IndexAttributeSelectionWidget(Composite parent, int style, HashMap[] profilesAttributes) {
		super(parent, style);
		this.profilesAttributes = copy(profilesAttributes);
		initialize();
		loadAttributes();
		selectedAttributes = new ArrayList();
		pack();
	}

	private void loadAttributes() {
		
	}

	private List copy(HashMap[]array) {
		List  copy = new ArrayList();
		for (int i = 0; i < array.length; i++) {
			HashMap map = array[i];
			copy.add( map);
		}
		return copy;
	}

	private void initialize() {
	
	}

	/**
	 * This method initializes presentTable	
	 *
	 */
	private void createPresentTable() {
		presentTable = new Table(this, SWT.BORDER);
		presentTable.setHeaderVisible(true);
		presentTable.setLinesVisible(true);
		presentTable.setBounds(new org.eclipse.swt.graphics.Rectangle(16,13,293,121));
		presentTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				presentIndex = presentTable.getSelectionIndex();
			}
		});
		TableColumn presentNameTableColumn = new TableColumn(presentTable, SWT.NONE);
		presentNameTableColumn.setWidth(90);
		presentNameTableColumn.setText("Name");
		TableColumn presentTypeTableColumn = new TableColumn(presentTable, SWT.NONE);
		presentTypeTableColumn.setWidth(160);
		presentTypeTableColumn.setText("Interface");
	}

	/**
	 * This method initializes selectedTable	
	 *
	 */
	private void createSelectedTable() {
		selectedTable = new Table(this, SWT.BORDER);
		selectedTable.setHeaderVisible(true);
		selectedTable.setLinesVisible(true);
		selectedTable.setBounds(new org.eclipse.swt.graphics.Rectangle(16,236,285,121));
		selectedTable.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				selectedIndex = selectedTable.getSelectionIndex();
			}
		});
		TableColumn selectedNameTableColumn = new TableColumn(selectedTable, SWT.NONE);
		selectedNameTableColumn.setWidth(90);
		selectedNameTableColumn.setText("Name");
		TableColumn selectedTypeTableColumn = new TableColumn(selectedTable, SWT.NONE);
		selectedTypeTableColumn.setWidth(120);
		selectedTypeTableColumn.setText("Interface");
	}

	public String[] getSelected() {
		String [] result = new String[selectedAttributes.size()];

		return result;
	}

}  //  @jve:decl-index=0:visual-constraint="44,11"
