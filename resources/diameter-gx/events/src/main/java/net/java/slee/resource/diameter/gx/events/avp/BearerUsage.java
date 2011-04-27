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

package net.java.slee.resource.diameter.gx.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the BearerUsage enumerated type.
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class BearerUsage implements Enumerated, Serializable {

    public static final int GENERAL = 0;
    public static final int IMS_SIGNALLING = 1;
    public static final BearerUsage _GENERAL = new BearerUsage(GENERAL);
    public static final BearerUsage _IMS_SIGNALLING = new BearerUsage(GENERAL);
    private static final long serialVersionUID = 1727547260959850765L;

    private int value;

    private BearerUsage(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    /**
     * Return the value of this instance of this enumerated type.
     */
    public static BearerUsage fromInt(int type) {
        switch (type) {
            case GENERAL:
                return _GENERAL;

            case IMS_SIGNALLING:
                return _IMS_SIGNALLING;

            default:
                throw new IllegalArgumentException("Invalid bearerusage value: " + type);
        }
    }

    private Object readResolve() throws StreamCorruptedException {
        try {
            return fromInt(value);
        } catch (IllegalArgumentException iae) {
            throw new StreamCorruptedException("Invalid internal state found: " + value);
        }
    }

    @Override
    public String toString() {
        switch (value) {
            case GENERAL:
                return "GENERAL";

            case IMS_SIGNALLING:
                return "IMS_SIGNALLING";

            default:
                return "<Invalid Value>";
        }
    }
}
