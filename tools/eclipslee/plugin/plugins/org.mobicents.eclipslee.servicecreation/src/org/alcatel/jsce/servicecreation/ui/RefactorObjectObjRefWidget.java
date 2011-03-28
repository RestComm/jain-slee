
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 *  Description:
 * <p>
 * Widget used in dialog to resolve object ref attribute.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class RefactorObjectObjRefWidget extends GenericOspDialogContent {
	/** The object reference widget page*/
	private ObjectRefWidget widget = null;
	/** The name of the proejct*/
	private String  projectName = null;

	public RefactorObjectObjRefWidget(Composite parent, int style, String projectName, int compositeW, int compositeH) {
		super(parent, style, "alcatel/ref2.png", "Resolve Object references", compositeW, compositeH);
		this.projectName = projectName;
		//createContentAreaComposite();
	}
	
	/**
	 * @see org.alcatel.jsce.servicecreation.ui.GenericOspDialogContent#createContentComposite()
	 */
	public Composite createContentAreaComposite() {
		Composite content =getContentComposite();
		return content;
	}

	
	/**
	 * @see org.alcatel.jsce.servicecreation.ui.GenericOspDialogContent#isPagecomplete()
	 */
	public boolean isPagecomplete() {
		if(widget.isPageComplete()){
			return true;
		}else{
			return false;
		}
		
	}

	/**
	 * @param attributeMaps
	 */
	public void setDataAttribute(HashMap[] attributeMaps) {
		if(widget == null){
			Composite content =getContentComposite();
			widget = new ObjectRefWidget(content, SWT.NONE, this, projectName);
		}
		widget.loadAttribute(attributeMaps);
		widget.isPageComplete();
		
	}


}
