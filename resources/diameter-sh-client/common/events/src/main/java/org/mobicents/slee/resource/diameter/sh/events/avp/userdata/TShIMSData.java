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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import net.java.slee.resource.diameter.sh.events.avp.userdata.ChargingInformation;
import net.java.slee.resource.diameter.sh.events.avp.userdata.IFCs;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShIMSData;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShIMSDataExtension;


/**
 * <p>Java class for tShIMSData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tShIMSData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SCSCFName" type="{}tSIP_URL" minOccurs="0"/>
 *         &lt;element name="IFCs" type="{}tIFCs" minOccurs="0"/>
 *         &lt;element name="IMSUserState" type="{}tIMSUserState" minOccurs="0"/>
 *         &lt;element name="ChargingInformation" type="{}tChargingInformation" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tShIMSDataExtension" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tShIMSData", propOrder = {
    "scscfName",
    "ifCs",
    "imsUserState",
    "chargingInformation",
    "extension",
    "any"
})
public class TShIMSData implements ShIMSData {

    @XmlElement(name = "SCSCFName")
    protected String scscfName;
    @XmlElement(name = "IFCs")
    protected TIFCs ifCs;
    @XmlElement(name = "IMSUserState")
    protected Short imsUserState;
    @XmlElement(name = "ChargingInformation")
    protected TChargingInformation chargingInformation;
    @XmlElement(name = "Extension")
    protected TShIMSDataExtension extension;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#getSCSCFName()
     */
    public String getSCSCFName() {
        return scscfName;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#setSCSCFName(java.lang.String)
     */
    public void setSCSCFName(String value) {
        this.scscfName = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#getIFCs()
     */
    public IFCs getIFCs() {
        return ifCs;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#setIFCs(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.IFCs)
     */
    public void setIFCs(IFCs value) {
        this.ifCs = (TIFCs) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#getIMSUserState()
     */
    public Short getIMSUserState() {
        return imsUserState;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#setIMSUserState(java.lang.Short)
     */
    public void setIMSUserState(Short value) {
        this.imsUserState = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#getChargingInformation()
     */
    public ChargingInformation getChargingInformation() {
        return chargingInformation;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#setChargingInformation(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ChargingInformation)
     */
    public void setChargingInformation(ChargingInformation value) {
        this.chargingInformation = (TChargingInformation) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#getExtension()
     */
    public ShIMSDataExtension getExtension() {
        return extension;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#setExtension(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension)
     */
    public void setExtension(ShIMSDataExtension value) {
        this.extension = (TShIMSDataExtension) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShIMSData#getAny()
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
