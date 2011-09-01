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
import net.java.slee.resource.diameter.base.events.avp.IPFilterRule;

/**
 * 
 * 5.3.18 Media-Sub-Component AVP
 * 
 * The Media-Sub-Component AVP (AVP code 519) is of type Grouped, and it contains the requested 
 * bitrate and filters for the set of IP flows identified by their common Flow-Identifier. 
 * The Flow-Identifier is defined in Annex B.
 * 
 * Possible Bandwidth information and Flow-Status information provided within the 
 * Media-Sub-Component AVP takes precedence over information within the encapsulating Media 
 * Component Description AVP. If a Media-Sub-Component- AVP is not supplied, or if optional AVP(s) 
 * within a Media-Sub-Component AVP are omitted, but corresponding information has been provided in 
 * previous Diameter messages, the previous information for the corresponding IP flow(s) remains 
 * valid, unless new information is provided within the encapsulating Media Component-Description 
 * AVP. If Flow-Description AVP(s) are supplied, they replace all previous Flow Description AVP(s), 
 * even if a new Flow-Description AVP has the opposite direction as the previous Flow Description 
 * AVP.
 * 
 * All IP flows within a Media-Sub-Component- AVP are permanently disabled by supplying a Flow 
 * Status AVP with value "REMOVED". The server may delete corresponding filters and state 
 * information.
 * 
 * <pre>
 * AVP format:
 * Media-Sub-Component ::= < AVP Header: 519 >
 *                         { Flow-Number }            ; Ordinal number of the IP flow
 *                      0*2[ Flow-Description ]       ; UL and/or DL
 *                         [ Flow-Status ]
 *                         [ Flow-Usage ]
 *                         [ Max-Requested-Bandwidth-UL ]
 *                         [ Max-Requested-Bandwidth-DL ]
 *                        *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface MediaSubComponentAvp extends GroupedAvp {

  /**
   * Returns the value of the Flow-Number AVP (AVP code 509) , of type
   * Unsigned32. A return value of null implies that the AVP has not been set.
   */
  public long getFlowNumber();

  /**
   * Returns true if the Flow-Number AVP (AVP code 509) is present in the
   * message.
   */
  public boolean hasFlowNumber();

  /**
   * Sets the value of the Flow-Number AVP (AVP code 509) , of type
   * Unsigned32.
   */
  public void setFlowNumber(long flowNumber);

  /**
   * Returns the value of the Flow-Description UL AVP (AVP code 507) , of type IPFilterRule. 
   */
  abstract IPFilterRule[] getFlowDescriptions();

  /**
   * Sets the value of the Flow-Description AVP (AVP code 507) , of type IP Filter Rule.
   */
  abstract void setFlowDescriptions(IPFilterRule[] flowDescriptions);

  /**
   * Sets the value of the Flow-Description AVP (AVP code 507) , of type IP Filter Rule.
   */
  abstract void setFlowDescription(IPFilterRule flowDescription);

  /**
   * Checks if Flow-Description AVP (AVP code 507)  is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasFlowDesription();

  /**
   * Checks if the Flow-Status AVP (AVP code 511) is present in Grouped AVP.
   * @return
   */
  public boolean hasFlowStatus();

  /**
   * Sets value of the Flow-Status AVP (AVP code 511), of type Enumerated;
   * @param fs
   */
  public void setFlowStatus(FlowStatus fs);

  /**
   * Fetches value of the Flow-Status AVP (AVP code 511), of type Enumerated;
   * @return
   */
  public FlowStatus getFlowStatus();

  /**
   * Checks if the Flow-Usage AVP (AVP code 512) is present in Grouped AVP.
   * @return
   */
  public boolean hasFlowUsage();

  /**
   * Sets value of the Flow-Usage AVP (AVP code 512), of type Enumerated;
   * @param fs
   */
  public void setFlowUsage(FlowUsage fu);

  /**
   * Fetches value of the Flow-Usage AVP (AVP code 512), of type Enumerated;
   * @return
   */
  public FlowUsage getFlowUsage();

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
   * Sets value of the Max -Bandwidth-UL AVP (AVP code 516),of type
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

  /**
   * Checks if the AF-Signaling-Protocol AVP (AVP code 529)  is present
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasAFSignalingProtocol();

  /**
   * Sets value of the AF-Signaling-Protocol AVP (AVP code 529), of type Enumerated.
   * @param afsp
   */
  public void setAFSignalingProtocol(AFSignalingProtocol afsp);

  /**
   * Fetches value of the AF-Signaling-Protocol AVP (AVP code 529);
   * @return
   */
  public AFSignalingProtocol getAFSignalingProtocol();

}
