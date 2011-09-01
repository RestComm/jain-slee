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

/**
 * The Acceptable-Service-Info AVP (AVP code 526) is of type Grouped, and
 * contains the maximum bandwidth for an AF session and/or for specific media
 * components that will be authorized by the PCRF. The
 * Max-Requested-Bandwidth-DL AVP and Max-Requested-Bandwidth-UL AVP directly
 * within the Acceptable-Service-Info AVP indicate the acceptable bandwidth for
 * the entire AF session. The Max-Requested-Bandwidth-DL AVP and
 * Max-Requested-Bandwidth-UL AVP within a Media-Component-Description AVP
 * included in the Acceptable-Service-Info AVP indicate the acceptable bandwidth
 * for the corresponding media component. If the acceptable bandwidth applies to
 * one or more media components, only the Media-Component-Description AVP will
 * be provided. If the acceptable bandwidth applies to the whole AF session,
 * only the Max-Requested-Bandwidth-DL AVP and Max-Requested-Bandwidth-UL AVP
 * will be included.
 * 
 * <pre>
 * Acceptable-Service-Info::= < AVP Header: 526 >
 *                           *[ Media-Component-Description ]
 *                            [ Max-Requested-Bandwidth-DL ]
 *                            [ Max-Requested-Bandwidth-UL ]
 *                           *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface AcceptableServiceInfoAvp extends GroupedAvp {

  /**
   * Checks if The Media-Component-Description AVP (AVP code 517) is present
   * in Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMediaComponentDescription();

  public void setMediaComponentDescription(MediaComponentDescriptionAvp mcd);

  /**
   * Sets values of the Media-Component-Description AVP (AVP code 517), of
   * type Grouped.Values contain service information for a single media
   * component within an AF session or the AF signalling information.
   * 
   * @param mcds
   */
  public void setMediaComponentDescriptions(MediaComponentDescriptionAvp[] mcds);

  public MediaComponentDescriptionAvp[] getMediaComponentDescriptions();

  /**
   * Checks if the Max-Requested-Bandwidth-DL AVP (AVP code 515) is present
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMaxRequestedBandwidthDL();

  /**
   * Sets value of the Max-Requested-Bandwidth-DL AVP (AVP code 515), of type
   * Unsigned32. It indicates the maximum bandwidth in bits per second for a
   * downlink IP flow. The bandwidth contains all the overhead coming from the
   * IP-layer and the layers above, e.g. IP, UDP, RTP and RTP payload. When
   * provided in an AA-Request, it indicates the maximum requested bandwidth.
   * When provided in an AA-Answer, it indicates the maximum bandwidth
   * acceptable by PCRF.
   * 
   * @param mrbdl
   */
  public void setMaxRequestedBandwidthDL(long mrbdl);

  /**
   * Fetches value of the Max-Requested-Bandwidth-DL AVP (AVP code 515).
   * 
   * @return
   */
  public long getMaxRequestedBandwidthDL();

  /**
   * Checks if the Max-Requested-Bandwidth-UL AVP (AVP code 516) is present
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMaxRequestedBandwidthUL();

  /**
   * Sets value of Tthe Max -Bandwidth-UL AVP (AVP code 516),of type
   * Unsigned32, and it indicates the maximum requested bandwidth in bits per
   * second for an uplink IP flow. The bandwidth contains all the overhead
   * coming from the IP-layer and the layers above, e.g. IP, UDP, RTP and RTP
   * payload. When provided in an AA-Request, it indicates the maximum
   * requested bandwidth. When provided in an AA-Answer, it indicates the
   * maximum bandwidth acceptable by PCRF.
   * 
   * 
   * @param mrbUL
   */
  public void setMaxRequestedBandwidthUL(long mrbUL);

  /**
   * Fetches value of the Max-Requested-Bandwidth-UL AVP (AVP code 516).
   * 
   * @return
   */
  public long getMaxRequestedBandwidthUL();

}
