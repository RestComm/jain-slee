
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

import org.alcatel.jsce.object.ObjectIndex;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 *  Description:
 * <p>
 * Dialog used to create or edit osp object dialog.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class IndexDialog extends Dialog {
	/** The contained-widget*/
	private IndexWidget indexWidget = null;
	/** The list of profile spec attributes*/
	private HashMap[] profilesAttr = null;
	/** The list of existing indexes*/
	private ObjectIndex[] indexList = null;

	/**
	 * @param parentShell
	 * @param attrs the list of attributes
	 * @param indexes the list of existing indexex
	 */
	public IndexDialog(Shell parentShell, HashMap [] attrs,  ObjectIndex[] indexes ) {
		super(parentShell);
		this.profilesAttr = attrs;
		this.indexList = indexes;
	}
	
	protected Control createContents(Composite parent) {
		indexWidget = new IndexWidget(parent, SWT.NONE, this.profilesAttr, indexList);
		indexWidget.isPageComplete();
		return super.createContents(parent);
	}
	
	/**
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Alcatel Eclipse SCE ");
	}

	/**
	 * @return the list of attributes name selected as index.
	 */
	public  ObjectIndex getSelected() {
		return indexWidget.getSelectedIndex();
	}
	
	protected void okPressed() {
		if(indexWidget.isPageComplete()){
			super.okPressed();
		}
	}

}
