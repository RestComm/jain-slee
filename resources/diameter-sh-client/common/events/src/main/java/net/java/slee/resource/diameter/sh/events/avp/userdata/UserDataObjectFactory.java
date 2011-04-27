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

package net.java.slee.resource.diameter.sh.events.avp.userdata;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TApplicationServer;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TCSLocationInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TChargingInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TDSAI;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.THeader;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TIFCs;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TISDNAddress;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TInitialFilterCriteria;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPSLocationInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentity;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentityExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentityExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSePoTri;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSePoTriExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TServiceData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSessionDescription;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension3;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TTransparentData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TTrigger;


public interface UserDataObjectFactory {

  /**
   * Create an instance of {@link TChargingInformation }
   * 
   */
  public abstract ChargingInformation createChargingInformation();

  /**
   * Create an instance of {@link TTrigger }
   * 
   */
  public abstract Trigger createTrigger();

  /**
   * Create an instance of {@link TSePoTriExtension }
   * 
   */
  public abstract SePoTriExtension createSePoTriExtension();

  /**
   * Create an instance of {@link TApplicationServer }
   * 
   */
  public abstract ApplicationServer createApplicationServer();

  /**
   * Create an instance of {@link TIFCs }
   * 
   */
  public abstract IFCs createIFCs();

  /**
   * Create an instance of {@link TPublicIdentityExtension }
   * 
   */
  public abstract PublicIdentityExtension createPublicIdentityExtension();

  /**
   * Create an instance of {@link TServiceData }
   * 
   */
  public abstract ServiceData createServiceData();

  /**
   * Create an instance of {@link TShDataExtension2 }
   * 
   */
  public abstract ShDataExtension2 createShDataExtension2();

  /**
   * Create an instance of {@link TShIMSDataExtension }
   * 
   */
  public abstract ShIMSDataExtension createShIMSDataExtension();

  /**
   * Create an instance of {@link TShDataExtension }
   * 
   */
  public abstract ShDataExtension createShDataExtension();

  /**
   * Create an instance of {@link TCSLocationInformation }
   * 
   */
  public abstract CSLocationInformation createCSLocationInformation();

  /**
   * Create an instance of {@link TInitialFilterCriteria }
   * 
   */
  public abstract InitialFilterCriteria createInitialFilterCriteria();

  /**
   * Create an instance of {@link THeader }
   * 
   */
  public abstract Header createHeader();

  /**
   * Create an instance of {@link TPublicIdentity }
   * 
   */
  public abstract PublicIdentity createPublicIdentity();

  /**
   * Create an instance of {@link TSessionDescription }
   * 
   */
  public abstract SessionDescription createSessionDescription();

  /**
   * Create an instance of {@link TPSLocationInformation }
   * 
   */
  public abstract PSLocationInformation createPSLocationInformation();

  /**
   * Create an instance of {@link TShData }
   * 
   */
  public abstract ShData createShData();

  /**
   * Create an instance of {@link TShIMSData }
   * 
   */
  public abstract ShIMSData createShIMSData();

  /**
   * Create an instance of {@link TShIMSDataExtension3 }
   * 
   */
  public abstract ShIMSDataExtension3 createShIMSDataExtension3();

  /**
   * Create an instance of {@link TShIMSDataExtension2 }
   * 
   */
  public abstract ShIMSDataExtension2 createShIMSDataExtension2();

  /**
   * Create an instance of {@link TISDNAddress }
   * 
   */
  public abstract ISDNAddress createISDNAddress();

  /**
   * Create an instance of {@link TExtension }
   * 
   */
  public abstract Extension createExtension();

  /**
   * Create an instance of {@link TPublicIdentityExtension2 }
   * 
   */
  public abstract PublicIdentityExtension2 createPublicIdentityExtension2();

  /**
   * Create an instance of {@link TDSAI }
   * 
   */
  public abstract DSAI createDSAI();

  /**
   * Create an instance of {@link TTransparentData }
   * 
   */
  public abstract TransparentData createTransparentData();

  /**
   * Create an instance of {@link TSePoTri }
   * 
   */
  public abstract SePoTri createSePoTri();

  /**
   * Create an instance of {@link JAXBElement }{@code <}{@link TShData }{@code >}}
   * 
   */
  @XmlElementDecl(namespace = "", name = "Sh-Data")
  public abstract JAXBElement<TShData> createShData(TShData value);

}