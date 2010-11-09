/**
 *   Copyright 2006 Alcatel, OSP.
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

import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

/**
 *  Description:
 * <p>
 * Visual class allowing to select the mode state developlment.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class SbbStatePanelWidget extends Composite {

	private Label stateModelLabel = null;
	private Label noStateLabel = null;
	private Label stateImgLabel = null;
	private Label noStateImgLabel = null;
	private Button stateRadioButton = null;
	private Button nostateRadioButton = null;
	private boolean isStateSelected = true;

	/**
	 * @param parent
	 * @param style
	 */
	public SbbStatePanelWidget(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		stateModelLabel = new Label(this, SWT.NONE);
		stateModelLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(45,25,169,28));
		stateModelLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		stateModelLabel.setText("State architecture");
		noStateLabel = new Label(this, SWT.NONE);
		noStateLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(357,25,177,32));
		noStateLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		noStateLabel.setText("Standard architecture");
		stateImgLabel = new Label(this, SWT.NONE);
		stateImgLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(7,80,333,270));
		stateImgLabel.setImage(ImageManager.getInstance().getImage("alcatel/state/states_icon.png"));
		noStateImgLabel = new Label(this, SWT.NONE);
		noStateImgLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(353,140,123,122));
		noStateImgLabel.setImage(ImageManager.getInstance().getImage("alcatel/state/nostate_icon.png"));
		stateRadioButton = new Button(this, SWT.RADIO);
		stateRadioButton.setBounds(new org.eclipse.swt.graphics.Rectangle(47,59,13,16));
		stateRadioButton.setSelection(true);
		stateRadioButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				isStateSelected = stateRadioButton.getSelection();
			}
		});
		nostateRadioButton = new Button(this, SWT.RADIO);
		nostateRadioButton.setBounds(new org.eclipse.swt.graphics.Rectangle(359,61,13,16));
		nostateRadioButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				isStateSelected = stateRadioButton.getSelection();
			}
		});
		this.setSize(new org.eclipse.swt.graphics.Point(552,397));
	}
	
	public boolean isSelectedtStateArchitecture(){
		return isStateSelected;
	}

}  //  @jve:decl-index=0:visual-constraint="9,18"
