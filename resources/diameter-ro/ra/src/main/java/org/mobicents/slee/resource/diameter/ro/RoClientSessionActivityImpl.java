/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.slee.resource.diameter.ro;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.cca.CreditControlAVPFactory;
import net.java.slee.resource.diameter.cca.CreditControlMessageFactory;
import net.java.slee.resource.diameter.cca.events.CreditControlRequest;
import net.java.slee.resource.diameter.ro.RoClientSessionActivity;
import net.java.slee.resource.diameter.ro.RoMessageFactory;

import org.jdiameter.api.Stack;
import org.jdiameter.api.cca.ClientCCASession;
import org.mobicents.slee.resource.diameter.base.DiameterMessageFactoryImpl;
import org.mobicents.slee.resource.diameter.cca.CreditControlClientSessionImpl;

/**
 * Implementation of {@link RoClientSessionActivity}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class RoClientSessionActivityImpl extends CreditControlClientSessionImpl implements RoClientSessionActivity {

  private static final long serialVersionUID = 6391181777305378777L;

  protected transient RoMessageFactory roMessageFactory = null;

  /**
   * 
   * @param messageFactory
   * @param avpFactory
   * @param session
   * @param destinationHost
   * @param destinationRealm
   * @param endpoint
   * @param stack
   */
  public RoClientSessionActivityImpl(CreditControlMessageFactory messageFactory, CreditControlAVPFactory avpFactory, ClientCCASession session, DiameterIdentity destinationHost, DiameterIdentity destinationRealm, Stack stack) {
    super(messageFactory, avpFactory, session, destinationHost, destinationRealm);

    this.roMessageFactory = new RoMessageFactoryImpl((DiameterMessageFactoryImpl) messageFactory.getBaseMessageFactory(), session.getSessions().get(0), stack, avpFactory);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoClientSession#sendEventCreditControlRequest(net.java.slee.resource.diameter.cca.events.CreditControlRequest)
   */
  public void sendEventCreditControlRequest(CreditControlRequest ccr) throws IOException {
    super.sendCreditControlRequest(ccr);
  }

  /*
   * (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.RoSession#getRoMessageFactory()
   */
  public RoMessageFactory getRoMessageFactory() {
    return this.roMessageFactory;
  }

  public void setRoMessageFactory(RoMessageFactory roMessageFactory) {
    this.roMessageFactory = roMessageFactory;
  }

  @Override
  public CreditControlMessageFactory getCCAMessageFactory() {
    return (CreditControlMessageFactory) this.roMessageFactory;
  }

}
