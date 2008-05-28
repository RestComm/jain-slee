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


import com.google.gwt.user.client.ui.Composite;

public abstract class SmartTabPage extends Composite implements CommonControl {
	
	public abstract static class SmartTabPageInfo {

		private SmartTabPage instance;
		private String tabText;
		private String title;
		
		public SmartTabPageInfo(String tabText, String title) {
			super();
			this.tabText = tabText;
			this.title = title;
		}

		protected abstract SmartTabPage createInstance();
		
		public final SmartTabPage getInstance() {
			if (instance != null)
				return instance;
			
			instance = createInstance();
			instance.onInit();
			return instance;
		}

		public String getTabText() {
			return tabText;
		}

		public String getTitle() {
			return title;
		}
	}
	
	public void onHide() {
	}

	public void onInit() {
	}

	public void onShow() {
	}
}
