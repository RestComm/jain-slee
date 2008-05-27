/*
 * Diameter Sh Resource Adaptor Type
 *
 * Copyright (C) 2006 Open Cloud Ltd.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of version 2.1 of the GNU Lesser 
 * General Public License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301  USA, or see the FSF site: http://www.fsf.org.
 */
package net.java.slee.resource.diameter.sh.client;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;
import net.java.slee.resource.diameter.sh.client.events.avp.SupportedApplicationsAvp;
import net.java.slee.resource.diameter.sh.client.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.sh.client.events.avp.UserIdentityAvp;


/**
 * Factory to support the creation of concrete instances of AVPs for Diameter Sh applications.
 *
 * @author Open Cloud
 */
public interface MessageFactory {

    /**
     * Get a factory to create AVPs and messages defined by Diameter Base.
     * @return base Diameter message factory
     */
    DiameterMessageFactory getBaseMessageFactory();
        
    /**
     * Create a SupportedFeatures (Grouped AVP) instance using required AVP values.
     */
    SupportedFeaturesAvp createSupportedFeatures(
        long vendorId
        , long featureListId
        , long featureList
    );

    /**
     * Create an empty SupportedFeatures (Grouped AVP) instance.
     */
    SupportedFeaturesAvp createSupportedFeatures();

    /**
     * Create a SupportedApplications (Grouped AVP) instance using required AVP values.
     */
    SupportedApplicationsAvp createSupportedApplications(
        long authApplicationId
        , long acctApplicationId
        , VendorSpecificApplicationIdAvp vendorSpecificApplicationId
    );

    /**
     * Create an empty SupportedApplications (Grouped AVP) instance.
     */
    SupportedApplicationsAvp createSupportedApplications();

    /**
     * Create an empty UserIdentity (Grouped AVP) instance.
     */
    UserIdentityAvp createUserIdentity();

}
