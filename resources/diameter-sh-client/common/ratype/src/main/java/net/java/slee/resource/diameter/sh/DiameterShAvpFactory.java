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

package net.java.slee.resource.diameter.sh;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;
import net.java.slee.resource.diameter.sh.events.avp.SupportedApplicationsAvp;
import net.java.slee.resource.diameter.sh.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.sh.events.avp.UserIdentityAvp;

/**
 * 
 * Diameter Sh AVP factory interface defining methods to create Sh specific avps.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface DiameterShAvpFactory {

  public DiameterAvpFactory getBaseFactory();

  /**
   * Create a SupportedFeatures (Grouped AVP) instance using required AVP values.
   */
  SupportedFeaturesAvp createSupportedFeatures(long vendorId, long featureListId, long featureList);

  /**
   * Create an empty SupportedFeatures (Grouped AVP) instance.
   */
  SupportedFeaturesAvp createSupportedFeatures();

  /**
   * Create a SupportedApplications (Grouped AVP) instance using required AVP values.
   */
  SupportedApplicationsAvp createSupportedApplications(long authApplicationId, long acctApplicationId, VendorSpecificApplicationIdAvp vendorSpecificApplicationId);

  /**
   * Create an empty SupportedApplications (Grouped AVP) instance.
   */
  SupportedApplicationsAvp createSupportedApplications();

  /**
   * Create an empty UserIdentity (Grouped AVP) instance.
   */
  UserIdentityAvp createUserIdentity();

  /**
   * Validates User data against XML schema.
   * 
   * @return - Returns <b>true</b> xml validation passed - this requiers data
   *         to be valid xml document and must follow user data xml schema.
   */
  boolean validateUserData(byte[] b);

}
