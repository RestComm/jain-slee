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
