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

import javax.slee.SLEEException;
import javax.slee.profile.ProfileTable;


public interface ProfileTableInterfaceNotMatchedQuery extends ProfileTable {

	//<query-parameter name="booleanParameter" type="boolean" />
	//<query-parameter name="booleanWParameter" type="java.lang.Boolean" />
	//<query-parameter name="stringParameter" type="java.lang.String" />
	public java.util.Collection queryFirstQuery(boolean booleanParameter,Boolean booleanWParameter,String stringParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
//	<query-parameter name="stringParameter" type="java.lang.String" />
//	<query-parameter name="integerWParameter" type="java.lang.Integer" />
	public java.util.Collection querySecondQuery(String stringParameter,Integer integerWParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
	public java.util.Collection queryNotMatchedQuery(String stringParameter,Integer integerWParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
	
}
