/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
