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

	public static ClassLoader getCurrentThreadClassLoader() {
		if (System.getSecurityManager()!=null)
			return (ClassLoader) AccessController
					.doPrivileged(new PrivilegedAction() {
						public Object run() {
							return Thread.currentThread()
									.getContextClassLoader();
						}
					});
		else
			return Thread.currentThread().getContextClassLoader();
	}
}
