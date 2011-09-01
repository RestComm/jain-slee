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

package net.java.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.cca.events.avp.GrantedServiceUnitAvp;
import net.java.slee.resource.diameter.cca.events.avp.UsedServiceUnitAvp;

/**
 * 
 * The Sponsored-Connectivity-Data AVP (AVP code 530) is of type Grouped, and
 * indicates the data associated with the sponsored data connectivity that the
 * AF is providing to the PCRF. The Sponsor-Identity AVP identifies the sponsor.
 * It shall be included by the AF in the Sponsored-Connectivity-Data AVP. The
 * Application-Service-Provider-Identity AVP identifies the application service
 * provider. It shall be included by the AF in the Sponsored-Connectivity-Data
 * AVP. The Granted-Service-Unit AVP shall be used by the AF to provide usage
 * threshold level to the PCRF if the volume of traffic allowed during the
 * sponsored data connectivity is to be monitored. The Used-Service-Unit AVP
 * shall be used by the PCRF to provide the measured usage to the PCRF.
 * Reporting shall be done, as requested by the AF, in CC-Total-Octets,
 * CC-Input-Octets or CC-Output-Octets of the Used-Service-Unit AVP. AVP format:
 * 
 * <pre>
 * Sponsored-Connectivity-Data::= < AVP Header: 530 >
 *                                [ Sponsor-Identity ]
 *                                [ Application-Service-Provider-Identity ]
 *                                [ Granted-Service-Unit ]
 *                                [ Used-Service-Unit ]
 *                                [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface SponsoredConnectivityDataAvp extends GroupedAvp {

  /**
   * Checks if the Sponsor-Identity AVP (AVP code 531) is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasSponsorIdentity();

  /**
   * Sets value of the Sponsor-Identity AVP (AVP code 531), of type UTF8String. It is used for sponsored data connectivity purposes as an identifier of the sponsor.
   * @param id
   */
  public void setSponsorIdentity(String id);

  /**
   * Fetches value of the Sponsor-Identity AVP (AVP code 531);
   * @return
   */
  public String getSponsorIdentity();

  /**
   * Checks if the Application-Service-Provider-Identity AVP (AVP code 532) is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasApplicationServiceProviderIdentity();

  /**
   * Sets value of the Application-Service-Provider-Identity AVP (AVP code 532), of type UTF8String. It is used for sponsored data connectivity purposes as an identifier of the sponsor.
   * @param id
   */
  public void setApplicationServiceProviderIdentity(String id);

  /**
   * Fetches value of the Application-Service-Provider-Identity AVP (AVP code 532);
   * @return
   */
  public String getApplicationServiceProviderIdentity();

  /**
   * Checks if the Granted-Service-Unit AVP (AVP Code 431) is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasGrantedServiceUnit();

  /**
   * Sets value of the Granted-Service-Unit AVP (AVP Code 431), of type Grouped. It is used for sponsored data connectivity purposes as an identifier of the sponsor.
   * @param id
   */
  public void setGrantedServiceUnit(GrantedServiceUnitAvp gsu);

  /**
   * Fetches value of the Granted-Service-Unit AVP (AVP Code 431);
   * @return
   */
  public GrantedServiceUnitAvp getGrantedServiceUnit();

  /**
   * Returns the set of Used-Service-Unit AVPs. The returned array contains
   * the AVPs in the order they appear in the message. A return value of null
   * implies that no Used-Service-Unit AVPs have been set. <br>
   * See: {@link UsedServiceUnitAvp}.
   * 
   * @return
   */
  public UsedServiceUnitAvp getUsedServiceUnit();

  /**
   * Sets  Used-Service-Unit AVP in the message, of type Grouped. <br>
   * See: {@link TariffChangeUsageType}
   * 
   * @param usedServiceUnit
   */
  public void setUsedServiceUnit(UsedServiceUnitAvp usedServiceUnit);

  /**
   * Checks if the Used-Service-Unit is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasUsedServiceUnit();

}
