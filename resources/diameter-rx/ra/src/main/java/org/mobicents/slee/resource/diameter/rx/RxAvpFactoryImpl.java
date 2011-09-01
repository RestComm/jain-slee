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

package org.mobicents.slee.resource.diameter.rx;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.rx.RxAvpFactory;
import net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp;
import net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp;
import net.java.slee.resource.diameter.rx.events.avp.FlowsAvp;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;
import net.java.slee.resource.diameter.rx.events.avp.MediaSubComponentAvp;
import net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp;
import net.java.slee.resource.diameter.rx.events.avp.SupportedFeaturesAvp;

import org.mobicents.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.DiameterRxAvpCodes;
import org.mobicents.slee.resource.diameter.rx.events.avp.FlowsAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.MediaSubComponentAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvpImpl;
import org.mobicents.slee.resource.diameter.rx.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation of {@link RxAvpFactory}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public class RxAvpFactoryImpl implements RxAvpFactory {

  // TODO: Add helper create methods for the composite AVPs
  protected DiameterAvpFactory baseAvpFactory;

  public RxAvpFactoryImpl(final DiameterAvpFactory baseAvpFactory) {
    // super(baseAvpFactory);

    // FIXME: isn't the super class doing this?
    this.baseAvpFactory = baseAvpFactory;
  }

  public DiameterAvpFactory getBaseFactory() {
    return this.baseAvpFactory;
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createAcceptableServiceInfo()
   */
  @Override
  public AcceptableServiceInfoAvp createAcceptableServiceInfo() {
    return (AcceptableServiceInfoAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.ACCEPTABLE_SERVICE_INFO,DiameterRxAvpCodes.TGPP_VENDOR_ID, null, AcceptableServiceInfoAvpImpl.class );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createAccessNetworkChargingIdentifier()
   */
  @Override
  public AccessNetworkChargingIdentifierAvp createAccessNetworkChargingIdentifier() {
    return (AccessNetworkChargingIdentifierAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.ACCESS_NETWORK_CHARGING_IDENTIFIER_VALUE,DiameterRxAvpCodes.TGPP_VENDOR_ID, null, AccessNetworkChargingIdentifierAvpImpl.class );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createFlows()
   */
  @Override
  public FlowsAvp createFlows() {
    return (FlowsAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.FLOWS,DiameterRxAvpCodes.TGPP_VENDOR_ID, null, FlowsAvpImpl.class );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createMediaComponentDescription()
   */
  @Override
  public MediaComponentDescriptionAvp createMediaComponentDescription() {
    return (MediaComponentDescriptionAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.MEDIA_COMPONENT_DESCRIPTION,DiameterRxAvpCodes.TGPP_VENDOR_ID, null, MediaComponentDescriptionAvpImpl.class );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createMediaSubComponent()
   */
  @Override
  public MediaSubComponentAvp createMediaSubComponent() {
    return (MediaSubComponentAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.MEDIA_SUBCOMPONENT,DiameterRxAvpCodes.TGPP_VENDOR_ID, null, MediaSubComponentAvpImpl.class );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createSponsoredConnectivityData()
   */
  @Override
  public SponsoredConnectivityDataAvp createSponsoredConnectivityData() {
    return (SponsoredConnectivityDataAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.SPONSORED_CONNECTIVITY_DATA,DiameterRxAvpCodes.TGPP_VENDOR_ID, null, SponsoredConnectivityDataAvpImpl.class );
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.rx.RxAvpFactory#createSupportedFeatures()
   */
  @Override
  public SupportedFeaturesAvp createSupportedFeatures() {
    return (SupportedFeaturesAvp)AvpUtilities.createAvp( DiameterRxAvpCodes.SUPPORTED_FEATURES,DiameterRxAvpCodes.TGPP_VENDOR_ID ,null, SupportedFeaturesAvpImpl.class );
  }

}
