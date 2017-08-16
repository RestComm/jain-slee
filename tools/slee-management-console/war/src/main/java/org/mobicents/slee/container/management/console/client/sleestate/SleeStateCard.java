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

package org.mobicents.slee.container.management.console.client.sleestate;

import org.mobicents.slee.container.management.console.client.common.Card;

import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeStateCard extends Card {

  private VerticalPanel rootPanel = new VerticalPanel();

  private SleeStatePanel sleeStatePanel;

  public SleeStateCard() {
    super();
    initWidget(rootPanel);
  }

  public void onHide() {
    sleeStatePanel.stopUpdating();
  }

  public void onInit() {
    sleeStatePanel = new SleeStatePanel();
    rootPanel.add(sleeStatePanel);
  }

  public void onShow() {
    sleeStatePanel.startUpdating();
  }
}
