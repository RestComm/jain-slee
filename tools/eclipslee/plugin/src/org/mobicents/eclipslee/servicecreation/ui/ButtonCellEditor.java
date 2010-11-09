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

package org.mobicents.eclipslee.servicecreation.ui;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author cath
 */
public class ButtonCellEditor extends CellEditor {

	private ButtonCellEditor() {
		
	}
	
	public ButtonCellEditor(Composite parent, String buttonText, SelectionListener listener) {
		this.buttonText = buttonText;
		this.listener = listener;
	}
	
	protected Control createControl(Composite parent) {
		button = new Button(parent, SWT.NONE);
		button.setText(buttonText);
		button.addSelectionListener(listener);
		
		return button;
	}
	
	protected Object doGetValue() {
		return buttonText;
	}
	
	protected void doSetFocus() {
		button.setFocus();
	}
	
	protected void doSetValue(Object value) {
	}
	
	private String buttonText;
	private Button button;
	private SelectionListener listener;
}
