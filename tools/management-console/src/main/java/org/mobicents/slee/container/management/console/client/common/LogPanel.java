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

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LogPanel extends VerticalPanel {

	private StringBuffer stringBuffer = new StringBuffer();

	private HTML logHTML = new HTML();

	private ScrollPanel scrollPanel = new ScrollPanel(logHTML);
	
	private HorizontalPanel header = new HorizontalPanel();

	public LogPanel() {

		Hyperlink clean = new Hyperlink("Clean", "Clean");
		clean.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				onClean();
			}
		});
		
		Label padding = new Label();
		
		header.add(padding);
		header.add(clean);

		header.setStyleName("common-LogPanel-header");
		header.setCellWidth(padding, "100%");

		scrollPanel.setStyleName("common-LogPanel-area");

		add(header);
		add(scrollPanel);

		setStyleName("common-LogPanel");
		setCellHeight(scrollPanel, "100%");
	}

	public void info(String s) {
		println("[INFO] " + s, "black", "images/log.info.gif");
	}

	public void warning(String s) {
		println("[WARNING] " + s, "#FF9900", "images/log.warning.gif");
	}

	public void error(String s) {
		println("[ERROR] " + s, "red", "images/log.error.gif");
	}

	private void println(String s, String color, String image) {
		stringBuffer.append("<image align='absbottom' src='" + image
				+ "'/><font color='" + color + "'>");
		stringBuffer.append(s);
		stringBuffer.append("</font><br>");
		logHTML.setHTML(stringBuffer.toString());
		scrollPanel.setScrollPosition(100000);
	}

	private void onClean() {
		stringBuffer = new StringBuffer();
		logHTML.setHTML("");
		scrollPanel.setScrollPosition(0);
	}
}
