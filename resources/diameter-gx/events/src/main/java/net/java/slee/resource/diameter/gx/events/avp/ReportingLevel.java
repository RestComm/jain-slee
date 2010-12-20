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
 * Java class to represent the Reporting-Level enumerated type.
 *
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class ReportingLevel implements Enumerated, java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int value = 0;
    public static final int CHARGING_RULE_LEVEL = 0;
    public static final int RATING_GROUP_LEVEL = 1;
    public static final ReportingLevel _CHARGING_RULE_LEVEL = new ReportingLevel(CHARGING_RULE_LEVEL);
    public static final ReportingLevel _RATING_GROUP_LEVEL = new ReportingLevel(RATING_GROUP_LEVEL);

    private ReportingLevel(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    /**
     * Return the value of this instance of this enumerated type.
     */
    public static ReportingLevel fromInt(int type) {
        switch (type) {
            case CHARGING_RULE_LEVEL:
                return _CHARGING_RULE_LEVEL;

            case RATING_GROUP_LEVEL:
                return _RATING_GROUP_LEVEL;

            default:
                throw new IllegalArgumentException("Invalid ReportingLevel value: " + type);
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
            case CHARGING_RULE_LEVEL:
                return "CHARGING_RULE_LEVEL";

            case RATING_GROUP_LEVEL:
                return "RATING_GROUP_LEVEL";

            default:
                return "<Invalid Value>";
        }
    }

}
