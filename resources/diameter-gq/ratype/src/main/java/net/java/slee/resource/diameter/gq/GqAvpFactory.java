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

package net.java.slee.resource.diameter.gq;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.BindingInputList;
import net.java.slee.resource.diameter.gq.events.avp.BindingOutputList;
import net.java.slee.resource.diameter.gq.events.avp.FlowGrouping;
import net.java.slee.resource.diameter.gq.events.avp.Flows;
import net.java.slee.resource.diameter.gq.events.avp.GloballyUniqueAddress;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;
import net.java.slee.resource.diameter.gq.events.avp.MediaSubComponent;
import net.java.slee.resource.diameter.gq.events.avp.V4TransportAddress;
import net.java.slee.resource.diameter.gq.events.avp.V6TransportAddress;


public interface GqAvpFactory {

  public DiameterAvpFactory getBaseFactory();

  /**
   * Create an empty BindingInformation (Grouped AVP) instance.
   * 
   * @return
   */
  public BindingInformation createBindingInformation();

  /**
   * Create an empty BindingInputList (Grouped AVP) instance.
   * 
   * @return
   */
  public BindingInputList createBindingInputList();

  /**
   * Create an empty BindingOutputList (Grouped AVP) instance.
   * 
   * @return
   */
  public BindingOutputList createBindingOutputList();

  /**
   * Create an empty FlowGrouping (Grouped AVP) instance.
   * 
   * @return
   */
  public FlowGrouping createFlowGrouping();

  /**
   * Create an empty Flows (Grouped AVP) instance.
   * 
   * @return
   */
  public Flows createFlows();

  /**
   * Create an empty GloballyUniqueAddress (Grouped AVP) instance.
   * 
   * @return
   */
  public GloballyUniqueAddress createGloballyUniqueAddress();

  /**
   * Create an empty MediaComponentDescription (Grouped AVP) instance.
   * 
   * @return
   */
  public MediaComponentDescription createMediaComponentDescription();

  /**
   * Create an empty MediaSubComponent (Grouped AVP) instance.
   * 
   * @return
   */
  public MediaSubComponent createMediaSubComponent();

  /**
   * Create an empty V4TransportAddress (Grouped AVP) instance.
   * 
   * @return
   */
  public V4TransportAddress createV4TransportAddress();

  /**
   * Create an empty V6TransportAddress (Grouped AVP) instance.
   * 
   * @return
   */
  public V6TransportAddress createV6TransportAddress();
}
