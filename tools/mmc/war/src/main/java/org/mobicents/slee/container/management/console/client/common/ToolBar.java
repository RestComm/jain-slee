/*
 * Mobicents: The Open Source VoIP Middleware Platform
 *
 * Copyright 2003-2006, Mobicents, 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */
package org.mobicents.slee.container.management.console.client.common;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author Stefano Zappaterra
 *
 */
public abstract class ToolBar extends Composite {
	
	HorizontalPanel rootPanel = new HorizontalPanel();
	
	public ToolBar() {
		super();	
		initWidget(rootPanel);
		
		rootPanel.setSpacing(4); //IE fix
		rootPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		setStyleName("common-ToolBar");
	}
	/*
	public void addWidget(Widget widget) {
		rootPanel.add(widget);
	}
	*/
	public void add(Button button) {
		rootPanel.add(button);
	}
	
	public void add(TextBox textBox) {
		rootPanel.add(textBox);
	}
	
	public void addSeparator() {
		Label separator = new Label();
		separator.setStyleName("common-ToolBar-separator");
		rootPanel.add(separator);
	}
}
