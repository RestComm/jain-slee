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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 *  Description:
 * <p>
 * 
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class SubfolderWidget extends Composite {

	private Label nameLabel = null;
	private Text nameText = null;
	private Image erroImage = null;
	private Label errorIconLabel1 = null;
	private Label errorLabel = null;
	
	private String folder="";

	/**
	 * @param parent
	 * @param style
	 */
	public SubfolderWidget(Composite parent, int style) {
		super(parent, style);
		initialize();
		isPageComplete();
	}

	private void initialize() {
		nameLabel = new Label(this, SWT.NONE);
		nameLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(13,28,68,13));
		nameLabel.setText("Folder Name");
		nameText = new Text(this, SWT.BORDER);
		nameText.setBounds(new org.eclipse.swt.graphics.Rectangle(93,26,258,19));
		nameText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				isPageComplete();
			}
		});
		setSize(new org.eclipse.swt.graphics.Point(469,101));
		ImageDescriptor errorD = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
				ISharedImages.IMG_OBJS_ERROR_TSK);
		erroImage = errorD.createImage();
		errorIconLabel1 = new Label(this, SWT.NONE);
		errorIconLabel1.setBounds(new org.eclipse.swt.graphics.Rectangle(15,72,28,13));
		errorIconLabel1.setImage(erroImage);
		errorLabel = new Label(this, SWT.NONE);
		errorLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(67,72,366,13));
		errorLabel.setText("");
	}
	
	/**
	 * @return true if the page is complete
	 */
	public boolean isPageComplete() {
		if(nameText.getText().length() < 1){
			setErrorMessage("Set the sub directory of the catalog");
			setErrorImage(true);
			return false;
		}else{
			String rule2 ="(([a-z]|[A-Z]|[0-1]){1,}(/[a-z]|[A-Z]|[0-1]){0,}){1,}";
			 Pattern p = Pattern.compile(rule2);
			 Matcher matcher = p.matcher(nameText.getText());
			 if(!matcher.matches()){
				setErrorMessage("Non valid according to the rule: <String>/<String>");
				setErrorImage(true);
				 return false;
			 }
		}
		folder = nameText.getText().replaceAll("/","\\\\");
		setErrorMessage("");
		setErrorImage(false);
		return true;
	}
	
	public String getFolder(){
		return folder;
	}
	
	private void setErrorImage(boolean show) {
		if (show) {
			if (erroImage != null) {
				errorIconLabel1.setImage(erroImage);
			} else {
				ImageDescriptor errorD = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
						ISharedImages.IMG_OBJS_ERROR_TSK);
				erroImage = errorD.createImage();
				errorIconLabel1.setImage(erroImage);
			}
		} else {
			errorIconLabel1.setImage(null);
		}
	}

	private void setErrorMessage(String msg) {
		this.errorLabel.setText(msg);
	}

}  //  @jve:decl-index=0:visual-constraint="10,36"
