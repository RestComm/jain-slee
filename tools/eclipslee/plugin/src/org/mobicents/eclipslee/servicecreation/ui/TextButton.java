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

import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;

/**
 * @author cath
 *
 * A combination text/button widget. 
 */
public class TextButton extends org.eclipse.swt.widgets.Composite {

	/**
	 * Creates a combination text/button widget with an empty text field and the default
	 * button label of "Browse...".
	 * @param parent the container widget
	 * @param style the style of this widget
	 */
	
  public TextButton(Composite parent, int style, boolean editable) {
    this(parent, style);
    text.setEditable(editable);
  }

  public TextButton(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		
		text = new Text(this, SWT.BORDER|SWT.SINGLE);
		text.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL|GridData.FILL_HORIZONTAL|GridData.GRAB_HORIZONTAL));
		text.setEditable(false);
		
		button = new Button(this, 0);
		button.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));		
		button.setText("Browse...");
	}
	
	/**
	 * Creates a combination text/button widget with the text specified by 'text' in the
	 * text area and the text specified in 'buttonText' as the button label.
	 * @param parent the container widget
	 * @param style the style of this widget
	 * @param text the text to put in the text area
	 * @param buttonText the button label text
	 */
	
	public TextButton(Composite parent, int style, String text, String buttonText) {
		this(parent, style);
		
		setText(text);
		setButtonText(text);
	}

	/**
	 * Creates a combination text/button widget with the text specified by 'text' in the
	 * text area and the default button label of "Browse...".
	 * @param parent the container widget
	 * @param style the style of this widget
	 * @param text the text to put in the text area
	 */
	
	public TextButton(Composite parent, int style, String text) {
		this(parent, style);

		setText(text);
	}
	
	/**
	 * Adds a ModifyListener to the Text field.
	 * @param listener
	 */
	
	public void addTextListener(ModifyListener listener) {
		text.addModifyListener(listener);		
	}

	/**
	 * Adds a SelectionListener to the Button.
	 * @param listener
	 */
	
	public void addButtonListener(SelectionListener listener) {
		button.addSelectionListener(listener);
	}
	
	/**
	 * Gets the text currently in the widget's text field.
	 * @return the text in the text field
	 */
	
	public String getText() {
		return text.getText();
	}
	
	/**
	 * Sets the text field to the specified String.
	 * @param t the text to place in the text field
	 */
	public void setText(String t) {
		text.setText(t);
	}
	
	/**
	 * Sets the button's text label to the specified String.
	 * @param t the new button text
	 */
	
	public void setButtonText(String t) {
		button.setText(t);
	}
	
	private final Text text;
	private final Button button;
}
