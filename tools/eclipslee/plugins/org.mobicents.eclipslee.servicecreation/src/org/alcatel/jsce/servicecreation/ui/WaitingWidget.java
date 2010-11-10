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

import org.alcatel.jsce.util.image.ImageManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 *  Description:
 * <p>
 *  Wigdet used when subsystem must complete a long task.
 * <p>
 * 
 * @author Skhiri dit Gabouje Sabri
 *
 */
public class WaitingWidget extends Composite {
	private String waitMessage="A job is currently running. It could take several seconds.";
	private Label waitLabel = null;
	private Label message = null;
	private Group group = null;  //  @jve:decl-index=0:visual-constraint="425,219"
	private Composite composite = null;
	private Text customMessage1 = null;
	private Label imageLabe = null;
	private Image image = null;
	private  Font font =null;
	private Composite spaceComposite = null;
	/**
	 * @param parent
	 * @param style
	 */
	public WaitingWidget(Composite parent, int style, String customMessage) {
		super(parent, style);
		initialize(customMessage);
	}

	private void initialize(String customMessage) {
		waitLabel = new Label(this, SWT.NONE);
		waitLabel.setText("Please Wait");
		message = new Label(this, SWT.NONE);
		message.setBounds(new org.eclipse.swt.graphics.Rectangle(30,62,288,30));
		message.setText(waitMessage);
		FontData newFont = new FontData("Arial", 25, SWT.BOLD);
		font = new Font(getDisplay(), newFont);
		waitLabel.setFont(font);
		waitLabel.setBounds(new org.eclipse.swt.graphics.Rectangle(73,7,223,49));
		createGroup(customMessage);
		this.setSize(new org.eclipse.swt.graphics.Point(355,308));
		imageLabe = new Label(this, SWT.NONE);
		imageLabe.setBounds(new org.eclipse.swt.graphics.Rectangle(122,102,90,90));
		createSpaceComposite();
		image = (ImageManager.getInstance()
				.getImgeDescriptor("alcatel/waiting80x80.gif")).createImage();
		imageLabe.setImage(image);

	}

	/**
	 * This method initializes group	
	 *
	 */
	private void createGroup(String customMessage) {
		group = new Group(this, SWT.NONE);
		group.setText("Current Task");
		createComposite(customMessage);
		group.setBounds(new org.eclipse.swt.graphics.Rectangle(18,200,319,86));
	}

	/**
	 * This method initializes composite	
	 * @param customMessage 
	 *
	 */
	private void createComposite(String customMessage) {
		composite = new Composite(group, SWT.NONE);
		composite.setBounds(new org.eclipse.swt.graphics.Rectangle(17,17,298,64));
		customMessage1 = new Text(composite, SWT.NONE);
		customMessage1.setEditable(false);
		customMessage1.setBounds(new org.eclipse.swt.graphics.Rectangle(7,10,281,38));
		customMessage1.setText(customMessage);
	}
	
	public void dispose() {
		if(image!=null){
			image.dispose();
		}
		if(font!=null){
			font.dispose();
		}
		super.dispose();
	}

	/**
	 * This method initializes spaceComposite	
	 *
	 */
	private void createSpaceComposite() {
		spaceComposite = new Composite(this, SWT.NONE);
		spaceComposite.setBounds(new org.eclipse.swt.graphics.Rectangle(17,294,331,12));
	}

	public void setMsg(String msg) {
		this.customMessage1.setText(msg);
		this.update();
		
	}
	
	public void setTaskName(String name) {
		group.setText(name);
		this.update();
		
	}

}  //  @jve:decl-index=0:visual-constraint="33,10"
