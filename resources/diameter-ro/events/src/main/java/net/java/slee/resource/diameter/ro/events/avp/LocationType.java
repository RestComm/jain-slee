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

package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Location-Type grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification: 
 * <pre>
 * 7.2.54 Location-Type AVP 
 * The Location-Type AVP (AVP code 1244) is of type Grouped and indicates the type of location estimate required by the
 * LCS client. 
 * 
 * It has the following ABNF grammar: 
 *  Location-Type::= AVP Header: 1244 
 *      [ Location-Estimate-Type ] 
 *      [ Deferred-Location-Event-Type ]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface LocationType extends GroupedAvp {
    /**
     * Returns the value of the Deferred-Location-Event-Type AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
     */
    abstract String getDeferredLocationEventType();

    /**
     * Returns the value of the Location-Estimate-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
     */
    abstract LocationEstimateType getLocationEstimateType();

    /**
     * Returns true if the Deferred-Location-Event-Type AVP is present in the message.
     */
    abstract boolean hasDeferredLocationEventType();

    /**
     * Returns true if the Location-Estimate-Type AVP is present in the message.
     */
    abstract boolean hasLocationEstimateType();

    /**
     * Sets the value of the Deferred-Location-Event-Type AVP, of type UTF8String.
     */
    abstract void setDeferredLocationEventType(String deferredLocationEventType);

    /**
     * Sets the value of the Location-Estimate-Type AVP, of type Enumerated.
     */
    abstract void setLocationEstimateType(LocationEstimateType locationEstimateType);

}
