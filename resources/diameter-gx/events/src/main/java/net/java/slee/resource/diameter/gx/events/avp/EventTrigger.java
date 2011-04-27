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
 * Java class to represent the Event-Trigger enumerated type.
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class EventTrigger implements Enumerated, java.io.Serializable{

    private static final long serialVersionUID = 1L;

    private int value = 0;
    public static final int SGSN_CHANGE = 0;
    public static final int QOS_CHANGE = 1;
    public static final int RAT_CHANGE = 2;
    public static final int TFT_CHANGE = 3;
    public static final int PLMN_CHANGE = 4;

    public static final EventTrigger _SGSN_CHANGE = new EventTrigger(SGSN_CHANGE);
    public static final EventTrigger _QOS_CHANGE = new EventTrigger(QOS_CHANGE);
    public static final EventTrigger _RAT_CHANGE = new EventTrigger(RAT_CHANGE);
    public static final EventTrigger _TFT_CHANGE = new EventTrigger(TFT_CHANGE);
    public static final EventTrigger _PLMN_CHANGE = new EventTrigger(PLMN_CHANGE);

    private EventTrigger(int v) {
        value =v;
    }

    public int getValue() {
        return value;
    }

     /**
     * Return the value of this instance of this enumerated type.
     */
    public static EventTrigger fromInt(int type) {
        switch (type) {
            case SGSN_CHANGE:
                return _SGSN_CHANGE;

            case QOS_CHANGE:
                return _QOS_CHANGE;

            case RAT_CHANGE:
                return _RAT_CHANGE;

            case TFT_CHANGE:
                return _TFT_CHANGE;

            case PLMN_CHANGE:
                return _PLMN_CHANGE;

            default:
                throw new IllegalArgumentException("Invalid EventTrigger value: " + type);
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
            case SGSN_CHANGE:
                return "SGSN_CHANGE";

            case QOS_CHANGE:
                return "QOS_CHANGE";

            case RAT_CHANGE:
                return "RAT_CHANGE";

            case TFT_CHANGE:
                return "TFT_CHANGE";

            case PLMN_CHANGE:
                return "PLMN_CHANGE";

            default:
                return "<Invalid Value>";
        }
    }

}
