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

package org.mobicents.slee.container.component.validator.profile.tableinterface;

import javax.slee.Address;

public interface ProfileCMPInterfaceAddress {

	public void setSimpleAddressCMP(Address b);

	public void setWrapperBooleanCMP(Boolean b);

	public Address getSimpleAddressCMP();

	public Boolean getWrapperBooleanCMP();

	public void setSimpleStringCMP(String t);

	public String getSimpleStringCMP();

	public void setSimpleIntegerCMP(int b);

	public void setWrapperIntegerCMP(Integer b);

	public int getSimpleIntegerCMP();

	public Integer getWrapperIntegerCMP();

}
