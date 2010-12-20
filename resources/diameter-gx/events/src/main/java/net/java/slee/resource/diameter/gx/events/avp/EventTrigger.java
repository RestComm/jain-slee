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
