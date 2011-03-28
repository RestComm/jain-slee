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

package org.mobicents.eclipslee.servicecreation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.mobicents.eclipslee.servicecreation.util.FileUtil;


/**
 * @author cath
 */
public class APIDialog extends Dialog implements SelectionListener {
	
  public static final String SLEE_JAR_1_0 = "slee.jar";
  public static final String SLEE_JAR_1_1 = "jain-slee-1.1.jar";
  
  // Which JAIN SLEE jar to be used
  public static final String SLEE_JAR = SLEE_JAR_1_1;

	public static final String SLEE_API_ZIP_1_0 = "jain_slee-1_0-fr-api.zip";
	public static final String SLEE_API_ZIP_1_1 = "jslee-1_1-fr-api.zip";

	// which JSLEE ZIP file to use - maybe extended later to set dynamically
  public static final String SLEE_API_ZIP = SLEE_API_ZIP_1_1;
    
  private static final String DIALOG_TITLE = "Configure the JAIN SLEE Plug-In";
	private static final String WARNING_MESSAGE = "The JAIN SLEE Plug-In needs to know where " + SLEE_API_ZIP + " is located.  This file can be downloaded from http://jcp.org/aboutJava/communityprocess/final/jsr240/index.html." +
				"\n\nThe Plug-In will still function without this file, but with the following limitations:\n\n" +
				"\tNo auto-completion of the javax.slee package\n" +
				"\tNo Java Doc for the javax.slee package\n" +
				"\tComponents created cannot be compiled\n";	
	
	public APIDialog(Shell parent) {	
		super(parent);			
		setBlockOnOpen(true);
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		Label label = new Label(composite, SWT.WRAP);
		label.setText(WARNING_MESSAGE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(data);
		
		label = new Label(composite, SWT.NONE);				
		label.setText("Location of " + SLEE_API_ZIP + ":");
		data = new GridData(GridData.FILL_HORIZONTAL);
		label.setLayoutData(data);
		
		Composite hbox = new Composite(composite, SWT.NONE);
		data = new GridData(GridData.FILL_HORIZONTAL|GridData.GRAB_HORIZONTAL);
		hbox.setLayoutData(data);
		GridLayout hlayout = new GridLayout();
		hlayout.numColumns = 2;
		hbox.setLayout(hlayout);
				
		text = new Text(hbox, SWT.SINGLE|SWT.LEFT);
		text.setText("");
		data = new GridData(GridData.FILL_HORIZONTAL|GridData.GRAB_HORIZONTAL);
		text.setLayoutData(data);
		
		button = new Button(hbox, SWT.NONE);
		button.setText("Browse...");
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		button.setLayoutData(data);	
		button.addSelectionListener(this);
	
		return composite;
	}
		
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(DIALOG_TITLE);	
	}

	protected void setShellStyle(int newStyle) {
		super.setShellStyle(newStyle | SWT.RESIZE | SWT.MAX );
	}
	
	public void okPressed() {			
		if (!unpackZip()) {
			MessageDialog.openError(getShell(), "Error", "'" + text.getText() + "' is not a valid SLEE API Zip file.\n\nPlease check that the file exists, is a ZIP or JAR archive and contains " + SLEE_JAR);
			return;			
		}
		super.okPressed();
	}
	
	private boolean unpackZip() {
		String zip = text.getText();
		if (!isSleeZip(zip))
			return false;
		
		try {
			copySleeZip(zip);
		} catch (Exception e) {			
			return false;
		}
		return true;
	}
	
	private boolean isSleeZip(String zip) {		
		try {
			ZipFile zipFile = new ZipFile(zip);
	        ZipEntry sleeJar = zipFile.getEntry("lib/" + SLEE_JAR);
	        if (sleeJar != null) {
	            return true;
	        }
		} catch (IOException e) {
			return false;
		}
		return false;
	}
	
	private void copySleeZip(String zip) throws IOException, ZipException {		
		IPath dir = ServiceCreationPlugin.getDefault().getStateLocation();
		ZipFile zipFile = new ZipFile(zip);
		
        ZipEntry sleeJar = zipFile.getEntry("lib/" + SLEE_JAR);
        if (sleeJar != null) {
			// Found slee.jar, copy it.
			InputStream is = zipFile.getInputStream(sleeJar);
			FileUtil.createFromInputStream(dir, new Path(SLEE_JAR), is, null);
		}
		
		// Now copy the .zip to the plugin resources dir.
		InputStream is = new FileInputStream(zip);
		FileUtil.createFromInputStream(dir, new Path(SLEE_API_ZIP), is, null);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
	}
	
	public void widgetSelected(SelectionEvent e) {
		
		// open a file selection dialog with modal = true and file = text.getText();
		FileDialog dialog = new FileDialog(this.getShell(), SWT.OPEN);
		dialog.setFileName(text.getText());
		
		// Get selected file and put into the text box.
		if (dialog.open() != null) {
			text.setText(dialog.getFilterPath() + File.separator + dialog.getFileName());			
		}
	}
	
	private Button button;
	private Text text;
	
}
