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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.AllocationRetentionPriorityAvp;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.EPSSubscribedQoSProfileAvp;
import net.java.slee.resource.diameter.s6a.events.avp.QoSClassIdentifier;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link EPSSubscribedQoSProfileAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class EPSSubscribedQoSProfileAvpImpl extends GroupedAvpImpl implements EPSSubscribedQoSProfileAvp {

  public EPSSubscribedQoSProfileAvpImpl() {
    super();
  }

  public EPSSubscribedQoSProfileAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasQoSClassIdentifier() {
    return hasAvp(DiameterS6aAvpCodes.QOS_CLASS_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setQoSClassIdentifier(QoSClassIdentifier qci) {
    addAvp(DiameterS6aAvpCodes.QOS_CLASS_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, qci.getValue());
  }

  public QoSClassIdentifier getQoSClassIdentifier() {
    return (QoSClassIdentifier) getAvpAsEnumerated(DiameterS6aAvpCodes.QOS_CLASS_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, QoSClassIdentifier.class);
  }

  public boolean hasAllocationRetentionPriority() {
    return hasAvp(DiameterS6aAvpCodes.ALLOCATION_RETENTION_POLICY, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setAllocationRetentionPriority(AllocationRetentionPriorityAvp arp) {
    addAvp(arp);
  }

  public AllocationRetentionPriorityAvp getAllocationRetentionPriority() {
    return (AllocationRetentionPriorityAvp) getAvpAsCustom(DiameterS6aAvpCodes.ALLOCATION_RETENTION_POLICY, DiameterS6aAvpCodes.S6A_VENDOR_ID, AllocationRetentionPriorityAvpImpl.class);
  }

}
