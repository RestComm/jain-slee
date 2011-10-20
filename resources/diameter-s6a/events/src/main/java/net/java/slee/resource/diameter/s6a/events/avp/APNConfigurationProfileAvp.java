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

package net.java.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the APN-Configuration grouped AVP type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.34 APN-Configuration-Profile
 * 
 * The APN-Configuration-Profile AVP is of type Grouped. It shall contain the information related
 * to the user's subscribed APN configurations for EPS. The Context-Identifier AVP within it shall
 * that identify the per subscriber’s default APN configuration.
 * 
 * The AVP format shall conform to:
 * APN-Configuration-Profile ::= < AVP header: 1429 10415 >
 *                               { Context-Identifier }
 *                               { All-APN-Configurations-Included-Indicator }
 *                             1*{APN-Configuration}
 *                              *[AVP]
 *                              
 * The Subscription-Data AVP associated with an IMSI contains one APN-Configuration-Profile AVP.
 * Each APN-Configuration-Profile AVP contains one or more APN-Configuration AVPs.
 * Each APN-Configuration AVP describes the configuration for a single APN.
 * Therefore, the cardinality of the relationship between IMSI and APN is one-to-many.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface APNConfigurationProfileAvp extends GroupedAvp {

    public boolean hasContextIdentifier();

    public long getContextIdentifier();

    public void setContextIdentifier(long contextIdentifier);

    public boolean hasAllAPNConfigurationsIncludedIndicator();

    public AllAPNConfigurationsIncludedIndicator getAllAPNConfigurationsIncludedIndicator();

    public void setAllAPNConfigurationsIncludedIndicator(AllAPNConfigurationsIncludedIndicator indicator);

    public boolean hasAPNConfiguration();

    public APNConfigurationAvp getAPNConfiguration();

    public void setAPNConfiguration(APNConfigurationAvp apnconfig);
}
