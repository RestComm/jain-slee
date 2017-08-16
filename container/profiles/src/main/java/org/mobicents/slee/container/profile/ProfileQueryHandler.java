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
 * Project: restcomm-jainslee-server-core<br>
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
