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

package org.mobicents.slee.resource.diameter.sh.events.avp.userdata;

import javax.xml.bind.JAXBElement;

import net.java.slee.resource.diameter.sh.events.avp.userdata.ApplicationServer;
import net.java.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ChargingInformation;
import net.java.slee.resource.diameter.sh.events.avp.userdata.DSAI;
import net.java.slee.resource.diameter.sh.events.avp.userdata.Extension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.Header;
import net.java.slee.resource.diameter.sh.events.avp.userdata.IFCs;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ISDNAddress;
import net.java.slee.resource.diameter.sh.events.avp.userdata.InitialFilterCriteria;
import net.java.slee.resource.diameter.sh.events.avp.userdata.PSLocationInformation;
import net.java.slee.resource.diameter.sh.events.avp.userdata.PublicIdentity;
import net.java.slee.resource.diameter.sh.events.avp.userdata.PublicIdentityExtension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.PublicIdentityExtension2;
import net.java.slee.resource.diameter.sh.events.avp.userdata.SePoTri;
import net.java.slee.resource.diameter.sh.events.avp.userdata.SePoTriExtension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ServiceData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.SessionDescription;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension2;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShIMSData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShIMSDataExtension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShIMSDataExtension2;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShIMSDataExtension3;
import net.java.slee.resource.diameter.sh.events.avp.userdata.TransparentData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.Trigger;
import net.java.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory;

public class UserDataObjectFactoryImpl implements UserDataObjectFactory {

  ObjectFactory objectFactory = null;

  public UserDataObjectFactoryImpl(ObjectFactory objectFactory) {
    this.objectFactory = objectFactory;
  }

  public ChargingInformation createChargingInformation() {
    return objectFactory.createChargingInformation();
  }

  public Trigger createTrigger() {
    return objectFactory.createTrigger();
  }

  public SePoTriExtension createSePoTriExtension() {
    return objectFactory.createSePoTriExtension();
  }

  public ApplicationServer createApplicationServer() {
    return objectFactory.createApplicationServer();
  }

  public IFCs createIFCs() {
    return objectFactory.createIFCs();
  }

  public PublicIdentityExtension createPublicIdentityExtension() {
    return objectFactory.createPublicIdentityExtension();
  }

  public ServiceData createServiceData() {
    return objectFactory.createServiceData();
  }

  public ShDataExtension2 createShDataExtension2() {
    return objectFactory.createShDataExtension2();
  }

  public ShIMSDataExtension createShIMSDataExtension() {
    return objectFactory.createShIMSDataExtension();
  }

  public ShDataExtension createShDataExtension() {
    return objectFactory.createShDataExtension();
  }

  public CSLocationInformation createCSLocationInformation() {
    return objectFactory.createCSLocationInformation();
  }

  public InitialFilterCriteria createInitialFilterCriteria() {
    return objectFactory.createInitialFilterCriteria();
  }

  public Header createHeader() {
    return objectFactory.createHeader();
  }

  public PublicIdentity createPublicIdentity() {
    return objectFactory.createPublicIdentity();
  }

  public SessionDescription createSessionDescription() {
    return objectFactory.createSessionDescription();
  }

  public PSLocationInformation createPSLocationInformation() {
    return objectFactory.createPSLocationInformation();  }

  public ShData createShData() {
    return objectFactory.createShData();
  }

  public ShIMSData createShIMSData() {
    return objectFactory.createShIMSData();
  }

  public ShIMSDataExtension3 createShIMSDataExtension3() {
    return objectFactory.createShIMSDataExtension3();
  }

  public ShIMSDataExtension2 createShIMSDataExtension2() {
    return objectFactory.createShIMSDataExtension2();
  }

  public ISDNAddress createISDNAddress() {
    return objectFactory.createISDNAddress();
  }

  public Extension createExtension() {
    return objectFactory.createExtension();
  }

  public PublicIdentityExtension2 createPublicIdentityExtension2() {
    return objectFactory.createPublicIdentityExtension2();
  }

  public DSAI createDSAI() {
    return objectFactory.createDSAI();
  }

  public TransparentData createTransparentData() {
    return objectFactory.createTransparentData();  }

  public SePoTri createSePoTri() {
    return objectFactory.createSePoTri();
  }

  public JAXBElement<TShData> createShData(TShData value) {
    return objectFactory.createShData(value);
  }

}
