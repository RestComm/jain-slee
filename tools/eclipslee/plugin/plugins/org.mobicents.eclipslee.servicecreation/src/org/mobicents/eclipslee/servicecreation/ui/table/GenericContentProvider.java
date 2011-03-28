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

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author cath
 */
public class GenericContentProvider implements IStructuredContentProvider, DataStoreChangeListener {

	public GenericContentProvider(EditableTableViewer tableViewer, DataStore store) {
		this.store = store;
		this.tableViewer = tableViewer;
	}
		
    public void inputChanged(Viewer v, Object oldInput, Object newInput) {
     	
        if (newInput != null)
        	((DataStore) newInput).addChangeListener(this);
        
        if (oldInput != null)
        	((DataStore) oldInput).removeChangeListener(this);
    }

    public void dispose() {
    	store.removeChangeListener(this);
    }
 
    public Object[] getElements(Object parent) {
    	return store.getElements();
    }
    
	public void onDataStoreItemAdded(Object item) {
		tableViewer.add(item);
	}

	public void onDataStoreItemRemoved(Object item) {
		tableViewer.remove(item);
	}
	
	public void onDataStoreItemChanged(Object item) {
		tableViewer.update(item, null);
	}
	
	private final EditableTableViewer tableViewer;
	private final DataStore store;	
}
