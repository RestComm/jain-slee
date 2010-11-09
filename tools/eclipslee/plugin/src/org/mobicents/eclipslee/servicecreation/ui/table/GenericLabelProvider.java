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

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * @author cath
 */
public class GenericLabelProvider extends LabelProvider implements ITableLabelProvider {

	public GenericLabelProvider(String columnNames[], int columnEditors[], Object [][]columnValues) {
		super();
		this.columnNames = columnNames;
		this.columnEditors = columnEditors;
		this.columnValues = columnValues;
	}
	
	public String getColumnText(Object element, int columnIndex) {		
		HashMap map = (HashMap) element;
		Object data = map.get(columnNames[columnIndex]);
		String output = null;
		
		switch (columnEditors[columnIndex]) {
			case EditableTableViewer.EDITOR_TEXT:
			case EditableTableViewer.EDITOR_NONE:
				output = (String) data;
				break;
				
			case EditableTableViewer.EDITOR_CHOICE:
				Integer val = (Integer) data;
				output = (String) columnValues[columnIndex][val.intValue()];
				break;
			
			case EditableTableViewer.EDITOR_CHECKBOX:
				Boolean value = (Boolean) data;
				if (value.booleanValue() == true)
					output = "Yes";
				else
					output = "No";
				break;

			case EditableTableViewer.EDITOR_BUTTON:
				return ""; // Blank as the button is the only visible part of this cell.
			
			default:
				output = "Unsupported column type";	
				break;
		}

		return output;
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
	
	private final String columnNames[];
	private final int columnEditors[];
	private final Object columnValues[][];
}
