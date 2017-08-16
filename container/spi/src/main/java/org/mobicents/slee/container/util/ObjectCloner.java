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

/**
 *
 */
package org.mobicents.slee.container.util;

//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.slee.SLEEException;

import org.apache.log4j.Logger;
//import org.jboss.serial.io.JBossObjectInputStream;
//import org.jboss.serial.io.JBossObjectOutputStream;

/**
 * @author martins
 */
public class ObjectCloner {

    protected static final Logger logger = Logger.getLogger(ObjectCloner.class);

    public static <T> T makeDeepCopy(final T orig) {

        if (System.getSecurityManager() != null) {
            try {
                return AccessController.doPrivileged(new PrivilegedExceptionAction<T>() {

                    public T run() throws Exception {

                        return _makeDeepCopy(orig);
                    }
                });
            } catch (PrivilegedActionException e) {
                if (e.getCause() instanceof RuntimeException) {
                    throw (RuntimeException) e.getCause();
                } else {
                    throw new SLEEException("Failed to create object copy.", e);
                }
            }
        } else {
            return _makeDeepCopy(orig);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T _makeDeepCopy(T orig) {
        T copy = null;
        if (orig != null) {
            /*
            ByteArrayOutputStream baos = null;
            JBossObjectOutputStream out = null;
            JBossObjectInputStream in = null;
            try {
                baos = new ByteArrayOutputStream();
                out = new JBossObjectOutputStream(baos);
                out.writeObject(orig);
                out.close();
                in = new JBossObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
                copy = (T) in.readObject();
                in.close();
            } catch (Throwable e) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e1) {
                        logger.error(e.getMessage(), e);
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e1) {
                        logger.error(e.getMessage(), e);
                    }
                }
                throw new SLEEException("Failed to create object copy.", e);
            }
            */

            try {
                logger.info("ObjectCloner._makeDeepCopy: "+orig.getClass().getCanonicalName());
                logger.info("ObjectCloner._makeDeepCopy: "+orig.toString());
                copy = (T) org.apache.commons.lang.SerializationUtils.clone((Serializable)orig);
            } catch (Throwable e) {
                throw new SLEEException("Failed to create object copy.", e);
            }
        }
        return copy;
    }

}
