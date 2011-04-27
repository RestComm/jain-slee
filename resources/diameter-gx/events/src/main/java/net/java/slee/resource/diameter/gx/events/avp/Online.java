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

import java.io.StreamCorruptedException;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the Online enumerated type.
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class Online implements Enumerated, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int value = 0;
    public static final int DISABLE_ONLINE = 0;
    public static final int ENABLE_ONLINE = 1;
    public static final Online _DISABLE_ONLINE = new Online(DISABLE_ONLINE);
    public static final Online _ENABLE_ONLINE = new Online(ENABLE_ONLINE);

    private Online(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    /**
     * Return the value of this instance of this enumerated type.
     */
    public static Online fromInt(int type) {
        switch (type) {
            case DISABLE_ONLINE:
                return _DISABLE_ONLINE;

            case ENABLE_ONLINE:
                return _ENABLE_ONLINE;

            default:
                throw new IllegalArgumentException("Invalid online value: " + type);
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
            case DISABLE_ONLINE:
                return "DISABLE_ONLINE";

            case ENABLE_ONLINE:
                return "ENABLE_ONLINE";

            default:
                return "<Invalid Value>";
        }
    }

}
