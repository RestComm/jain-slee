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

/*
 * Created on May 23, 2005
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */

public class SleeContainerUtils {

	public static String toHex(String str) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (Character.isLetter(str.charAt(i))/*|| str.charAt(i) == '-'*/
					|| Character.isDigit(str.charAt(i))) {
				buff.append(str.charAt(i));
			} else {
				buff.append("\\u00");
				buff.append(Integer.toHexString(str.charAt(i)));
			}
		}

		return buff.toString();
	}

	public static String fromHex(String str) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\\') {
				buff.append(Integer.valueOf(str.substring(i + 4, i + 6), 16));
			} else {
				buff.append(str.charAt(i));
			}
		}
		//logger.debug(buff);
		return buff.toString();
	}

	/**
	 * Retrieves the current thread {@link ClassLoader}, securely. 
	 * @return
	 */
	public static ClassLoader getCurrentThreadClassLoader() {
		if (System.getSecurityManager()!=null)
			return AccessController
					.doPrivileged(new PrivilegedAction<ClassLoader>() {
						public ClassLoader run() {
							return Thread.currentThread()
									.getContextClassLoader();
						}
					});
		else
			return Thread.currentThread().getContextClassLoader();
	}

	/**
	 * Sets the current thread class loader securely
	 * @param classLoader
	 */
	public static void setCurrentThreadClassLoader(final ClassLoader classLoader) {
		if (System.getSecurityManager() != null)
            AccessController.doPrivileged(new PrivilegedAction<Object>() {
                public Object run() {
                    Thread.currentThread().setContextClassLoader(classLoader);
                    return null;
                }
            });
        else
            Thread.currentThread().setContextClassLoader(classLoader);
	}
}
