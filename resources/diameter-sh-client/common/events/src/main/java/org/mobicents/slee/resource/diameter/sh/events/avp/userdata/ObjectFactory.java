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
package org.mobicents.slee.resource.diameter.sh.events.avp.userdata;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

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


/**
 * This object contains factory methods for each  Java content interface and Java element interface 
 * generated in the org.mobicents.slee.resource.diameter.sh.events.avp.userdata package. 
 * <p>An ObjectFactory allows you to programatically construct new instances of the Java representation 
 * for XML content. The Java representation of XML content can consist of schema derived interfaces 
 * and classes representing the binding of schema type definitions, element declarations and model 
 * groups.  Factory methods for each of these are provided in this class.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
@XmlRegistry
public class ObjectFactory implements UserDataObjectFactory {

    private final static QName _ShData_QNAME = new QName("", "Sh-Data");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes
     * for package: org.mobicents.slee.resource.diameter.sh.events.avp.userdata
     * 
     */
    public ObjectFactory() {
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createChargingInformation()
     */
    public ChargingInformation createChargingInformation() {
        return new TChargingInformation();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createTrigger()
     */
    public Trigger createTrigger() {
        return new TTrigger();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createSePoTriExtension()
     */
    public SePoTriExtension createSePoTriExtension() {
        return new TSePoTriExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createApplicationServer()
     */
    public ApplicationServer createApplicationServer() {
        return new TApplicationServer();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createIFCs()
     */
    public IFCs createIFCs() {
        return new TIFCs();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPublicIdentityExtension()
     */
    public PublicIdentityExtension createPublicIdentityExtension() {
        return new TPublicIdentityExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createServiceData()
     */
    public ServiceData createServiceData() {
        return new TServiceData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShDataExtension2()
     */
    public ShDataExtension2 createShDataExtension2() {
        return new TShDataExtension2();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSDataExtension()
     */
    public ShIMSDataExtension createShIMSDataExtension() {
        return new TShIMSDataExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShDataExtension()
     */
    public ShDataExtension createShDataExtension() {
        return new TShDataExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createCSLocationInformation()
     */
    public CSLocationInformation createCSLocationInformation() {
        return new TCSLocationInformation();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createInitialFilterCriteria()
     */
    public InitialFilterCriteria createInitialFilterCriteria() {
        return new TInitialFilterCriteria();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createHeader()
     */
    public Header createHeader() {
        return new THeader();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPublicIdentity()
     */
    public PublicIdentity createPublicIdentity() {
        return new TPublicIdentity();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createSessionDescription()
     */
    public SessionDescription createSessionDescription() {
        return new TSessionDescription();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPSLocationInformation()
     */
    public PSLocationInformation createPSLocationInformation() {
        return new TPSLocationInformation();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShData()
     */
    public ShData createShData() {
        return new TShData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSData()
     */
    public ShIMSData createShIMSData() {
        return new TShIMSData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSDataExtension3()
     */
    public ShIMSDataExtension3 createShIMSDataExtension3() {
        return new TShIMSDataExtension3();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSDataExtension2()
     */
    public ShIMSDataExtension2 createShIMSDataExtension2() {
        return new TShIMSDataExtension2();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createISDNAddress()
     */
    public ISDNAddress createISDNAddress() {
        return new TISDNAddress();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createExtension()
     */
    public Extension createExtension() {
        return new TExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPublicIdentityExtension2()
     */
    public PublicIdentityExtension2 createPublicIdentityExtension2() {
        return new TPublicIdentityExtension2();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createDSAI()
     */
    public DSAI createDSAI() {
        return new TDSAI();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createTransparentData()
     */
    public TransparentData createTransparentData() {
        return new TTransparentData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createSePoTri()
     */
    public SePoTri createSePoTri() {
        return new TSePoTri();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShData(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShData)
     */
    @XmlElementDecl(namespace = "", name = "Sh-Data")
    public JAXBElement<TShData> createShData(TShData value) {
        return new JAXBElement<TShData>(_ShData_QNAME, TShData.class, null, value);
    }

}
