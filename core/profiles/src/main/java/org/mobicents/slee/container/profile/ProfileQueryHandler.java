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

package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.UnrecognizedQueryNameException;

/**
 * 
 * Start time:12:49:42 2009-03-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * This class handles queries call to certain ProfileTable impl objects. It must
 * be called within transaction
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileQueryHandler {

	/**
	 * Method that does lookup and creates PLOs
	 * 
	 * @param profileTable
	 * @param queryName
	 * @param arguments
	 * @return
	 * @throws InvalidArgumentException
	 * @throws AttributeTypeMismatchException
	 * @throws UnrecognizedQueryNameException
	 * @throws SLEEException
	 * @throws NullPointerException
	 */
	public static Collection<ProfileLocalObject> handle(
			ProfileTableImpl profileTable, String queryName, Object[] arguments)
			throws NullPointerException, TransactionRequiredLocalException, SLEEException,
			UnrecognizedQueryNameException, AttributeTypeMismatchException,
			InvalidArgumentException {
		return profileTable.getProfilesByStaticQuery(queryName, arguments);
	}

}
