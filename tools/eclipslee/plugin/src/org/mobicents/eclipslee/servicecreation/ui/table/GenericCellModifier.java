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

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author cath
 */
public class GenericCellModifier implements ICellModifier {
    public GenericCellModifier(EditableTableViewer viewer, String [] columnNames, int[] columnTypes) {
    	super();
    	this.tableViewer = viewer;
    	this.columnTypes = columnTypes;
    	this.columnNames = columnNames;
    }

    public boolean canModify(Object element, String property) {

    	for (int i = 0; i < columnNames.length; i++) {
    		if (columnNames[i].equals(property)) {
    			if (columnTypes[i] == EditableTableViewer.EDITOR_NONE)
    				return false;
    			else
    				return true;
    		}
    		
    	}
    	
    	return false;
    }
    
    public Object getValue(Object element, String property) {
    	HashMap data = (HashMap) element;
    	Object result = data.get(property);
    	tableViewer.getStore().onDataStoreItemChanged(data);
    	return result;  
    }

    public void modify(Object element, String property, Object value) {         	
    	TableItem item = (TableItem) element;
    	HashMap data = (HashMap) item.getData();
    	data.remove(property);
    	data.put(property, value);
     	tableViewer.getStore().onDataStoreItemChanged(data);
    }

    private EditableTableViewer tableViewer;
    private String[] columnNames;
    private int[] columnTypes;
}
