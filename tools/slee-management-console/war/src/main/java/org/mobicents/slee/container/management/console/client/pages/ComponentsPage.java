/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.management.console.client.pages;

import org.mobicents.slee.container.management.console.client.common.CardControl;
import org.mobicents.slee.container.management.console.client.common.SmartTabPage;
import org.mobicents.slee.container.management.console.client.components.ComponentBrowserCard;
import org.mobicents.slee.container.management.console.client.components.ComponentSearchCard;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ComponentsPage extends SmartTabPage {

  private CardControl cardControl = new CardControl();

  private ComponentsPage() {
    initWidget(cardControl);
  }

  public static SmartTabPageInfo getInfo() {
    return new SmartTabPageInfo("<image src='images/components.gif' /> Components", "Components") {
      protected SmartTabPage createInstance() {
        return new ComponentsPage();
      }
    };
  }

  public void onInit() {
    ComponentBrowserCard componentBrowserCard = new ComponentBrowserCard();
    cardControl.add(componentBrowserCard, "<image align='absbottom' src='images/components.gif' /> Browse Components", true);

    ComponentSearchCard componentSearchCard = new ComponentSearchCard();
    cardControl.add(componentSearchCard, "<image align='absbottom' src='images/search.gif' /> Search Components", true);

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
