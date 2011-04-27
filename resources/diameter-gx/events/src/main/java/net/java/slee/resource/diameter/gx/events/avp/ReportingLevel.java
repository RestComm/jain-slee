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
