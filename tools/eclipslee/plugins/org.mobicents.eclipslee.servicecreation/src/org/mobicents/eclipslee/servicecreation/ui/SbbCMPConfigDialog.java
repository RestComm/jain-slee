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

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mobicents.eclipslee.servicecreation.util.SbbFinder;
import org.mobicents.eclipslee.util.slee.xml.DTDXML;
import org.mobicents.eclipslee.util.slee.xml.components.SbbXML;
import org.mobicents.eclipslee.xml.SbbJarXML;


/**
 * @author cath
 */
public class SbbCMPConfigDialog extends Dialog implements ModifyListener, SelectionListener {

	protected Control createButtonBar(Composite parent) {
		Control ctl = super.createButtonBar(parent);
		validation();
		return ctl;
	}

	private static final String DIALOG_TITLE = "Configure SBB CMP Field";
	
	public SbbCMPConfigDialog(Shell parent, HashMap cmpData, String project) {
		super(parent);
		setBlockOnOpen(true);

		this.name = (String) cmpData.get("Name");
		this.type = (String) cmpData.get("Type");
		
		this.sbbXML = (SbbXML) cmpData.get("SBB XML");
		this.scopedName = (String) cmpData.get("Scoped Name");		
		
		this.project = project;

		// Sanitise various stuff.
		if (name == null)
			name = "";
		if (type == null)
			type = "";
		if (scopedName == null)
			scopedName = "";
	
	}

	
	protected Control createDialogArea(Composite parent) {
		
		ModifyListener validationListener = new ModifyListener()
		{
			public void modifyText(ModifyEvent event)
			{
				validation();
			}
		};
		Composite composite = (Composite) super.createDialogArea(parent);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		
		Composite row = new Composite(composite, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		layout = new GridLayout();
		layout.numColumns = 2;
		row.setLayout(layout);
		row.setLayoutData(data);
		
		Label label = new Label(row, SWT.NONE);
		label.setText("&Name:");
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(data);
		
		nameText = new Text(row, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		nameText.setLayoutData(data);
		nameText.addModifyListener(validationListener);
		
		standardButton = new Button(composite, SWT.RADIO);
		standardButton.setText("This CMP field is a standard Java object or primitive");
		data = new GridData(GridData.FILL_HORIZONTAL);
		standardButton.setLayoutData(data);
		standardButton.addSelectionListener(this);
		
		row = new Composite(composite, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = 12;
		row.setLayoutData(data);
		
		layout = new GridLayout();
		layout.numColumns = 2;
		row.setLayout(layout);
		
		typeLabel = new Label(row, SWT.NONE);
		typeLabel.setText("&Type:");
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		typeLabel.setLayoutData(data);
		typeText = new Text(row, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		typeText.setLayoutData(data);
		typeText.addModifyListener(validationListener);
		
		sbbButton = new Button(composite, SWT.RADIO);
		sbbButton.setText("This CMP field holds an SbbLocalObject reference");
		data = new GridData(GridData.FILL_HORIZONTAL);
		sbbButton.setLayoutData(data);
		sbbButton.addSelectionListener(this);
		
		row = new Composite(composite, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalIndent = 12;
		row.setLayoutData(data);
		
		layout = new GridLayout();
		layout.numColumns = 2;
		row.setLayout(layout);
		
		sbbLabel = new Label(row, SWT.NONE);
		sbbLabel.setText("&SBB:");
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		sbbLabel.setLayoutData(data);
		
		sbbCombo = new Combo(row, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		sbbCombo.setLayoutData(data);
	
		scopedLabel = new Label(row, SWT.NONE);
		scopedLabel.setText("&Scoped Name:");
		data = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		scopedLabel.setLayoutData(data);
		
		scopedText = new Text(row, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL);
		scopedText.setLayoutData(data);
		scopedText.addModifyListener(validationListener);
		
		initialize();
		
		composite.setSize(640, 480);
		return composite;
	}
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE + ": " + name);
	}
	
	public void okPressed() {

		name = nameText.getText();
		type = typeText.getText();
		scopedName = scopedText.getText();
		if (sbbCombo.getSelectionIndex() >= 0)
			sbbXML = (SbbXML) xml[sbbCombo.getSelectionIndex()];
		else
			sbbXML = null;		
		
		if (isSbbLocalObject())
			type = "javax.slee.SbbLocalObject";
		
		super.okPressed();
	}
	
	private void validation()
	{
		
		Button okButton = this.getButton(IDialogConstants.OK_ID);
		if(okButton == null) return;
		try
		{
			boolean okEnabled = false;
			boolean primitive = standardButton.getSelection();

			String name = nameText.getText();
			String type = typeText.getText();
			String sname = scopedText.getText();
		
			if(primitive)
				okEnabled = name.length()>0 && type.length()>0;
			else
				okEnabled = name.length()>0 && sname.length()>0 &&
				sbbCombo.getSelectionIndex() >= 0;
			
			okButton.setEnabled(okEnabled);
		}
		catch(Exception ex)
		{
			okButton.setEnabled(false);
		}
	}
	public void modifyText(ModifyEvent event) {
		updateWidgets();
		validation();
	}
	
	public void widgetDefaultSelected(SelectionEvent event) {
		validation();
	}
	
	public void widgetSelected(SelectionEvent event) {
		updateWidgets();
		validation();
	}
	
	private void initialize() {

		// Get a list of available SBBs that can be stored as a local object.
		// Requires the 'project' variable.
		
		if (project == null) {
			xml = new DTDXML[0];
			String [] labels = new String[0];
			sbbCombo.setItems(labels);
			
		} else {			
			DTDXML jarXml[] = SbbFinder.getDefault().getComponents(project);
			Vector sbbs = new Vector();		
			for (int i = 0; i < jarXml.length; i++) {			
				SbbXML sbbXml[] = ((SbbJarXML) jarXml[i]).getSbbs();
				for (int j = 0; j < sbbXml.length; j++) {
					sbbs.add(sbbXml[j]);				
				}
			}		
			xml = (DTDXML []) sbbs.toArray(new DTDXML[sbbs.size()]);
			
			String [] labels = new String[sbbs.size()];
			for (int i = 0; i < sbbs.size(); i++) {
				labels[i] = ((SbbXML) sbbs.get(i)).toString();			
			}
			sbbCombo.setItems(labels);
			if (labels.length > 0)
				sbbCombo.select(0);		

		}
		
		nameText.setText(name);
		typeText.setText(type);
		scopedText.setText(scopedName);
		
		
		if (type.equals("javax.slee.SbbLocalObject"))
			standardButton.setSelection(false);
		else
			standardButton.setSelection(true);		
		
		
		// Determine which entry to select
		if (sbbXML != null) {
			String labels[] = sbbCombo.getItems();
			for (int i = 0; i < labels.length; i++) {
				if (sbbXML.toString().equals(labels[i])) {
					sbbCombo.select(i);
					break;
				}
			}
		}

		if (sbbCombo.getSelectionIndex() < 0 && sbbCombo.getItems().length > 0)
			sbbCombo.select(0);
		
		updateWidgets();
	}
	
	private void updateWidgets() {
		typeText.setEnabled(standardButton.getSelection());
		typeLabel.setEnabled(standardButton.getSelection());
		sbbCombo.setEnabled(sbbButton.getSelection());
		scopedText.setEnabled(sbbButton.getSelection());
		sbbLabel.setEnabled(sbbButton.getSelection());
		scopedLabel.setEnabled(sbbButton.getSelection());
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getScopedName() {
		return scopedName;
	}
	
	public SbbXML getSbbLocalObject() {
		return sbbXML;
	}
	
	public boolean isSbbLocalObject() {
		return sbbButton.getSelection();
	}
	
	private String name;
	private String type;
	private String scopedName;
	private SbbXML sbbXML;
	private String project;
	
	private DTDXML xml[]; // SbbXML array that corresponds to the combo labels.
	
	private Text nameText;
	private Text typeText;
	private Combo sbbCombo;
	private Text scopedText;
	private Button standardButton;
	private Button sbbButton;
	
	private Label sbbLabel;
	private Label scopedLabel;
	private Label typeLabel;

}
