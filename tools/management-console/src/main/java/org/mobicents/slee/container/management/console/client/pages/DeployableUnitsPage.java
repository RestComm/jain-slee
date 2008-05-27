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
package org.mobicents.slee.container.management.console.client.pages;

import org.mobicents.slee.container.management.console.client.common.CardControl;
import org.mobicents.slee.container.management.console.client.common.SmartTabPage;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitBrowserCard;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitInstallCard;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitSearchCard;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitsPage extends SmartTabPage {
	
	private CardControl cardControl = new CardControl();
	
	private DeployableUnitsPage() {
		initWidget(cardControl);
	}

	public static SmartTabPageInfo getInfo() {
		return new SmartTabPageInfo("<image src='images/deployableunits.gif' /> Deployable Units",
				"Deployable Units") {
			protected SmartTabPage createInstance() {
				return new DeployableUnitsPage();
			}
		};
	}
	
	public void onInit() {
		DeployableUnitBrowserCard deployableUnitBrowserCard = new DeployableUnitBrowserCard();
		cardControl.add(deployableUnitBrowserCard, "<image align='absbottom' src='images/deployableunits.gif' /> Browse", true);
		
		DeployableUnitSearchCard deployableUnitSearchCard = new DeployableUnitSearchCard();
		cardControl.add(deployableUnitSearchCard, "<image align='absbottom' src='images/search.gif' /> Search", true);
		
		DeployableUnitInstallCard deployableUnitInstallCard = new DeployableUnitInstallCard();
		cardControl.add(deployableUnitInstallCard, "<image align='absbottom' src='images/deployableunits.install.gif' /> Install", true);
				
		cardControl.selectTab(0);
		cardControl.setWidth("100%");
	}

	public void onHide() {
		cardControl.onHide();
	}

	public void onShow() {
		cardControl.onShow();
	}	
}
