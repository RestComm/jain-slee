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
import net.java.slee.resource.diameter.cca.events.avp.FinalUnitActionType;

/**
 * The Flows AVP (AVP code 510) is of type Grouped, and it indicates IP flows
 * via their flow identifiers. When reporting an out of credit condition, the
 * Final-Unit-Action AVP indicates the termination action applied to the
 * impacted flows. If no Flow-Number AVP(s) are supplied, the Flows AVP refers
 * to all Flows matching the media component number. AVP Format:
 * 
 * <pre>
 * Flows::= < AVP Header: 510 >
 *          { Media-Component-Number }
 *         *[ Flow-Number ]
 *          [ Final-Unit-Action ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface FlowsAvp extends GroupedAvp {

  /**
   * Checks if the Media-Component-Number AVP (AVP code 518) is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasMediaComponentNumber();

  /**
   * Sets value of the Media-Component-Number AVP (AVP code 518), of type Unsigned32. It contains the ordinal number of the media component.
   * @param l
   */
  public void setMediaComponentNumber(long l);

  /**
   * Fetches the Media-Component-Number AVP (AVP code 518);
   * @return
   */
  public long getMediaComponentNumber();

  /**
   * Checks if the Flow-Number AVP (AVP code 509) is present in Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasFlowNumber();

  /**
   * Sets value of the Flow-Number AVP (AVP code 509), of type Unsigned32. It contains the ordinal number of the IP flow(s);
   * @param l
   */
  public void setFlowNumber(long l);

  /**
   * Fetches value of the Flow-Number AVP (AVP code 509);
   * @return
   */
  public long[] getFlowNumbers();

  public void setFlowNumbers(long[] l);

  /**
   * Checks if the Final-Unit-Action AVP (AVP Code 449) is present in the Grouped AVP. In case it is, method returns true;
   * @return
   */
  public boolean hasFinalUnitAction();

  /**
   * Sets value of the Final-Unit-Action AVP (AVP Code 449), of type Enumerated.
   * @param l
   */
  public void setFinalUnitAction(FinalUnitActionType l);

  /**
   * Fetches value of the Final-Unit-Action AVP (AVP Code 449);
   * @return
   */
  public FinalUnitActionType getFinalUnitAction();

}
