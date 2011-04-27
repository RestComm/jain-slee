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
 * Java class to represent the Metering-Method enumerated type.
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class MeteringMethod implements Enumerated, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int value = 0;
    public static final int DURATION = 0;
    public static final int VOLUME = 1;
    public static final int DURATION_VOLUME = 2;

    public static final MeteringMethod _DURATION = new MeteringMethod(DURATION);
    public static final MeteringMethod _VOLUME = new MeteringMethod(VOLUME);
    public static final MeteringMethod _DURATION_VOLUME = new MeteringMethod(DURATION_VOLUME);

    private MeteringMethod(int v) {
        value =v;
    }

    public int getValue() {
        return value;
    }

      /**
     * Return the value of this instance of this enumerated type.
     */
    public static MeteringMethod fromInt(int type) {
        switch (type) {
            case DURATION:
                return _DURATION;

            case VOLUME:
                return _VOLUME;

            case DURATION_VOLUME:
                return _DURATION_VOLUME;

           default:
                throw new IllegalArgumentException("Invalid MeteringMethod value: " + type);
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
            case DURATION:
                return "DURATION";

            case VOLUME:
                return "VOLUME";

            case DURATION_VOLUME:
                return "DURATION_VOLUME";

            default:
                return "<Invalid Value>";
        }
    }

}
