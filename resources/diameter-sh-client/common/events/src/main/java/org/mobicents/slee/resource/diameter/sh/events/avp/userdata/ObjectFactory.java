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
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

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
public class ObjectFactory {

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
    public TChargingInformation createChargingInformation() {
        return new TChargingInformation();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createTrigger()
     */
    public TTrigger createTrigger() {
        return new TTrigger();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createSePoTriExtension()
     */
    public TSePoTriExtension createSePoTriExtension() {
        return new TSePoTriExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createApplicationServer()
     */
    public TApplicationServer createApplicationServer() {
        return new TApplicationServer();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createIFCs()
     */
    public TIFCs createIFCs() {
        return new TIFCs();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPublicIdentityExtension()
     */
    public TPublicIdentityExtension createPublicIdentityExtension() {
        return new TPublicIdentityExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createServiceData()
     */
    public TServiceData createServiceData() {
        return new TServiceData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShDataExtension2()
     */
    public TShDataExtension2 createShDataExtension2() {
        return new TShDataExtension2();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSDataExtension()
     */
    public TShIMSDataExtension createShIMSDataExtension() {
        return new TShIMSDataExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShDataExtension()
     */
    public TShDataExtension createShDataExtension() {
        return new TShDataExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createCSLocationInformation()
     */
    public TCSLocationInformation createCSLocationInformation() {
        return new TCSLocationInformation();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createInitialFilterCriteria()
     */
    public TInitialFilterCriteria createInitialFilterCriteria() {
        return new TInitialFilterCriteria();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createHeader()
     */
    public THeader createHeader() {
        return new THeader();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPublicIdentity()
     */
    public TPublicIdentity createPublicIdentity() {
        return new TPublicIdentity();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createSessionDescription()
     */
    public TSessionDescription createSessionDescription() {
        return new TSessionDescription();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPSLocationInformation()
     */
    public TPSLocationInformation createPSLocationInformation() {
        return new TPSLocationInformation();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShData()
     */
    public TShData createShData() {
        return new TShData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSData()
     */
    public TShIMSData createShIMSData() {
        return new TShIMSData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSDataExtension3()
     */
    public TShIMSDataExtension3 createShIMSDataExtension3() {
        return new TShIMSDataExtension3();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createShIMSDataExtension2()
     */
    public TShIMSDataExtension2 createShIMSDataExtension2() {
        return new TShIMSDataExtension2();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createISDNAddress()
     */
    public TISDNAddress createISDNAddress() {
        return new TISDNAddress();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createExtension()
     */
    public TExtension createExtension() {
        return new TExtension();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createPublicIdentityExtension2()
     */
    public TPublicIdentityExtension2 createPublicIdentityExtension2() {
        return new TPublicIdentityExtension2();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createDSAI()
     */
    public TDSAI createDSAI() {
        return new TDSAI();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createTransparentData()
     */
    public TTransparentData createTransparentData() {
        return new TTransparentData();
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.UserDataObjectFactory#createSePoTri()
     */
    public TSePoTri createSePoTri() {
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
