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

import org.mobicents.slee.container.management.console.client.activity.ActivityCard;
import org.mobicents.slee.container.management.console.client.common.CardControl;
import org.mobicents.slee.container.management.console.client.common.SmartTabPage;

public class ActivityPage extends SmartTabPage {

  private CardControl cardControl = new CardControl();

  public ActivityPage() {
    initWidget(cardControl);
  }

  public static SmartTabPageInfo getInfo() {
    return new SmartTabPageInfo("<image src='images/activity.context.gif' /> Activities", "Activities") {
      protected SmartTabPage createInstance() {
        return new ActivityPage();
      }
    };
  }

  public void onInit() {
    ActivityCard activityBrowserCard = new ActivityCard();
    cardControl.add(activityBrowserCard, "<image align='absbottom' src='images/activity.context.gif' /> Browse Activities", true);

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
