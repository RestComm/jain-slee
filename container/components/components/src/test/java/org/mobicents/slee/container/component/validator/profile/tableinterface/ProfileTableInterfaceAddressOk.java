/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.component.validator.profile.tableinterface;

import javax.slee.SLEEException;
import javax.slee.profile.ProfileTable;


public interface ProfileTableInterfaceAddressOk extends ProfileTable {

	//<query-parameter name="booleanParameter" type="boolean" />
	//<query-parameter name="booleanWParameter" type="java.lang.Boolean" />
	//<query-parameter name="stringParameter" type="java.lang.String" />
	public java.util.Collection queryFirstQuery(javax.slee.Address address,Boolean booleanWParameter,String stringParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
//	<query-parameter name="stringParameter" type="java.lang.String" />
//	<query-parameter name="integerWParameter" type="java.lang.Integer" />
	public java.util.Collection querySecondQuery(String stringParameter,Integer integerWParameter) throws javax.slee.TransactionRequiredLocalException,SLEEException;
	
}
