/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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
