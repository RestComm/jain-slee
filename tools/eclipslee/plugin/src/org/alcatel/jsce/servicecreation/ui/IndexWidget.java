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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.alcatel.jsce.object.ObjectIndex;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


/**
 *  Description:
 * <p>
 *  Widget used to edit or create an OSP object index.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class IndexWidget extends Composite {

	private Group indexGroup = null;
	private Label nameLabel = null;
	private Text nameText = null;
	private Label unicityLabel = null;
	private Button unicityCheckBox = null;
	private Label attributesLabel = null;
	private List attrList = null;
	private Button addButton = null;
	private Button removeButton = null;
	private Label errorsLabel = null;
	private Label errorImglabel = null;
	private Image erroImage = null;
	
	private HashMap[] profilesSpecAttr = new HashMap[0];
	private ObjectIndex index = null;
	private int selectedIndex = -1;
	private Label isSmfLabel = null;
	private Button isSMFCheckBox = null;
	private Label isSLEElabel = null;
	private Button isSLEEcheckBox = null;
	private Label isPKlabel = null;
	private Button isPKcheckBox = null;
	/** The list of existing index*/
	private ObjectIndex[] indexList = null;
	
	/**
	 * @param parent the parent composite
	 * @param style the SWT Style
	 * @param maps the list of profile spec attributes
	 */
	public IndexWidget(Composite parent, int style, HashMap[] maps,  ObjectIndex[] indexes) {
		super(parent, style);
		this.profilesSpecAttr = maps;
		initialize();
		index = new ObjectIndex();
		index.setUnicity(false);
		index.setSlee(true);
		index.setSmf(true);
		index.setKey(false);
		this.indexList = indexes;
	}

	private void initialize() {
		createIndexGroup();
		setSize(new org.eclipse.swt.graphics.Point(446,242));
		errorsLabel = new Label(this, SWT.NONE);
		errorsLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(81,220,353,13));
		errorsLabel.setText("");
		errorImglabel = new Label(this, SWT.NONE);
		errorImglabel.setBounds(new org.eclipse.swt.graphics.Rectangle(37,220,25,13));
		ImageDescriptor errorD = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_ERROR_TSK);
		erroImage  = errorD.createImage();
		errorImglabel.setImage(erroImage);
	}

	/**
	 * This method initializes indexGroup	
	 *
	 */
	private void createIndexGroup() {
		indexGroup = new Group(this, SWT.NONE);
		indexGroup.setText("Index informations");
		indexGroup.setBounds(new org.eclipse.swt.graphics.Rectangle(7,13,428,196));
		nameLabel = new Label(indexGroup, SWT.NONE);
		nameLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(12,31,76,13));
		nameLabel.setText("Index name: ");
		nameText = new Text(indexGroup, SWT.BORDER);
		nameText.setBounds(new org.eclipse.swt.graphics.Rectangle(109,31,211,19));
		nameText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				index.setName(nameText.getText());
				isPageComplete();
			}
		});
		unicityLabel = new Label(indexGroup, SWT.NONE);
		unicityLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(12,62,73,13));
		unicityLabel.setText("Unicity:");
		unicityCheckBox = new Button(indexGroup, SWT.CHECK);
		unicityCheckBox.setBounds(new org.eclipse.swt.graphics.Rectangle(94,62,13,16));
		unicityCheckBox.setSelection(false);
		unicityCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				index.setUnicity(unicityCheckBox.getSelection());
				isPageComplete();
			}
		});
		attributesLabel = new Label(indexGroup, SWT.NONE);
		attributesLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(12,94,68,13));
		attributesLabel.setText("Attributes: ");
		attrList = new List(indexGroup, SWT.NONE);
		attrList.setBounds(new org.eclipse.swt.graphics.Rectangle(109,92,215,89));
		attrList.setToolTipText("Defines the list of attributes compsing the index");
		attrList.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				isPageComplete();
				selectedIndex = attrList.getSelectionIndex();
			}
		});
		addButton = new Button(indexGroup, SWT.NONE);
		addButton.setBounds(new org.eclipse.swt.graphics.Rectangle(338,97,69,23));
		addButton.setText("add");
		addButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				//Profile Not yert selected
				HashMap [] availableProfile = getNotYetSelected(profilesSpecAttr, attrList.getItems());
				IndexAttributeSelectionDialog indexDialog = new IndexAttributeSelectionDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), availableProfile);
				if(indexDialog.open() == Dialog.OK){
					String [] attributes = indexDialog.getSelected();
					for (int i = 0; i < attributes.length; i++) {
						String attr_name = attributes[i];
						attrList.add(attr_name);
					}
					index.setAttributes(attrList.getItems());
					isPageComplete();
				}
				
			}
		});
		removeButton = new Button(indexGroup, SWT.NONE);
		removeButton.setBounds(new org.eclipse.swt.graphics.Rectangle(338,137,69,23));
		removeButton.setText("remove");
		isSmfLabel = new Label(indexGroup, SWT.NONE);
		isSmfLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(127,63,49,13));
		isSmfLabel.setText("Is SMF:");
		isSmfLabel.setToolTipText("Defines wheter the index will be build in the SMF side");
		isSMFCheckBox = new Button(indexGroup, SWT.CHECK);
		isSMFCheckBox.setBounds(new org.eclipse.swt.graphics.Rectangle(176,61,13,16));
		isSMFCheckBox.setSelection(true);
		isSMFCheckBox.setToolTipText("Defines wheter the index will be build in the SMF side");
		isSMFCheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				index.setSmf(isSMFCheckBox.getSelection());
				isPageComplete();
			}
		});
		isSLEElabel = new Label(indexGroup, SWT.NONE);
		isSLEElabel.setBounds(new org.eclipse.swt.graphics.Rectangle(207,62,46,13));
		isSLEElabel.setText("Is SLEE:");
		isSLEElabel.setToolTipText("Defines wheter the index will be build in the SMF side");
		isSLEEcheckBox = new Button(indexGroup, SWT.CHECK);
		isSLEEcheckBox.setBounds(new org.eclipse.swt.graphics.Rectangle(260,62,13,16));
		isSLEEcheckBox.setSelection(true);
		isSLEEcheckBox.setToolTipText("Defines wheter the index will be build in the SLEE side");
		isPKlabel = new Label(indexGroup, SWT.NONE);
		isPKlabel.setBounds(new org.eclipse.swt.graphics.Rectangle(297,62,72,13));
		isPKlabel.setText("Is Primary key:");
		isPKcheckBox = new Button(indexGroup, SWT.CHECK);
		isPKcheckBox.setBounds(new org.eclipse.swt.graphics.Rectangle(383,62,13,16));
		isPKcheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				index.setKey(isPKcheckBox.getSelection());
				isPageComplete();
			}
		});
		isSLEEcheckBox.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				index.setSlee(isSLEEcheckBox.getSelection());
				isPageComplete();
			}
		});
		removeButton.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if(selectedIndex> -1){
					attrList.remove(selectedIndex);
					index.setAttributes(attrList.getItems());
				}
				isPageComplete();
			}
		});
	}
	
	private HashMap[] getNotYetSelected(HashMap[] profilesSpecAttr, String[] items) {

		HashMap [] result = new HashMap[6];
		return result;
	}

	private boolean arrayContains(String string, String[] items) {
		for (int i = 0; i < items.length; i++) {
			String name = items[i];
			if(name.equals(string)){
				return true;
			}
		}
		return false;
	}
	
	public void loadIndex(ObjectIndex index){
		this.index = index;
		attrList.removeAll();
		nameText.setText(index.getName());
		unicityCheckBox.setSelection(index.isUnicity());
		attrList.removeAll();
		String[] attrs = index.getAttributes();
		for (int i = 0; i < attrs.length; i++) {
			String attr_i = attrs[i];
			attrList.add(attr_i);
			
		}
	}
	
	/**
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		if(erroImage!=null){
			erroImage.dispose();
		}
		super.dispose();
	}
	
	///////////////////////////////////////////
	//
	// Error management
	//
	//////////////////////////////////////////
	
	public boolean isPageComplete() {
		if (nameText.getText().length() < 1) {
			setErrorMessage("Set the index name");
			setErrorImage(true);
			return false;
		} else {
			if (nameText.getText().equals("i_ri")) {
				setErrorMessage("This index name is reserved");
				setErrorImage(true);
			}
		}

		// verrify if the types of the attribute list are compliants with SMF value
		String[] names = attrList.getItems();
		
		if (attrList.getItemCount() < 1) {
			setErrorMessage("Select at least one attribute reference");
			setErrorImage(true);
			return false;
		}
		if (isPKcheckBox.getSelection()) {
			for (int i = 0; i < indexList.length; i++) {
				ObjectIndex index_i = indexList[i];
				if (index_i.isKey()) {
					setErrorImage(true);
					setErrorMessage("The Primary key index has already been defined");
					return false;
				}
			}
		}
		setErrorMessage("");
		setErrorImage(false);
		return true;
	}
	
	private void setErrorImage(boolean show) {
		if(show){
			if(erroImage!=null){
				errorImglabel.setImage(erroImage);
		}else{
			ImageDescriptor errorD = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_ERROR_TSK);
			erroImage = errorD.createImage();
			errorImglabel.setImage(erroImage);
		}
	}else {
		errorImglabel.setImage(null);
	}
			
		
	}
	
	private void setErrorMessage(String msg){
		this.errorsLabel.setText(msg);
	}

	/**
	 * @return theindex created.
	 */
	public ObjectIndex getSelectedIndex() {
		if (index.isKey()) {
		}
		return index;
	}

	private HashMap getAttributeMap(String attr_name_i) {

		return null;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
