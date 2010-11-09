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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * @author cath
 */
public class DataStore {

	public DataStore() {
		super();
	}
	
	public void add(Object item) {
		data.add(item);		
		Iterator iter = changeListeners.iterator();
		while (iter.hasNext())
			((DataStoreChangeListener) iter.next()).onDataStoreItemAdded(item);
	}
	
	public void remove(Object item) {
		data.remove(item);
		Iterator iter = changeListeners.iterator();
		while (iter.hasNext())
			((DataStoreChangeListener) iter.next()).onDataStoreItemRemoved(item);
	}

	public void onDataStoreItemChanged(Object item) {
		Iterator iter = changeListeners.iterator();
		while (iter.hasNext()) {
			DataStoreChangeListener listener = (DataStoreChangeListener) iter.next();
			listener.onDataStoreItemChanged(item); // was item
		}
	}
	
	public void removeChangeListener(DataStoreChangeListener viewer) {
		changeListeners.remove(viewer);
	}

	public void addChangeListener(DataStoreChangeListener viewer) {
		changeListeners.add(viewer);
	}
	
	public Object[] getElements() {
		return data.toArray(new Object[data.size()]);		
	}
	
	public void clear() {
		Object elements[] = getElements();
		for (int i = 0; i < elements.length; i++)
			remove(elements[i]);		
	}
	
	private Vector data = new Vector();
	private Set changeListeners = new HashSet();
}
