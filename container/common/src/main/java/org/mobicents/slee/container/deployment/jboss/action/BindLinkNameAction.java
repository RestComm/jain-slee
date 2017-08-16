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

package org.mobicents.slee.container.deployment.jboss.action;

import org.mobicents.slee.container.management.ResourceManagement;

/**
 * 
 * @author martins
 * 
 */
public class BindLinkNameAction extends ResourceManagementAction {

	private final String linkName;
	private final String raEntity;

	public BindLinkNameAction(String linkName, String raEntity,
			ResourceManagement resourceManagement) {
		super(resourceManagement);
		this.linkName = linkName;
		this.raEntity = raEntity;
	}

	@Override
	public void invoke() throws Exception {
		getResourceManagement().bindLinkName(linkName, raEntity);
	}

	@Override
	public String toString() {
		return "BindLinkNameAction[" + linkName + "," + raEntity + "]";
	}

}
