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

import net.java.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation;
import net.java.slee.resource.diameter.sh.events.avp.userdata.Extension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ISDNAddress;


/**
 * <p>Java class for tCSLocationInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tCSLocationInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LocationNumber" type="{}tLocationNumber" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="CellGlobalId" type="{}tCellGlobalId" minOccurs="0"/>
 *           &lt;element name="ServiceAreaId" type="{}tServiceAreaId" minOccurs="0"/>
 *           &lt;element name="LocationAreaId" type="{}tLocationAreaId" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="GeographicalInformation" type="{}tGeographicalInformation" minOccurs="0"/>
 *         &lt;element name="GeodeticInformation" type="{}tGeodeticInformation" minOccurs="0"/>
 *         &lt;element name="VLRNumber" type="{}tISDNAddress" minOccurs="0"/>
 *         &lt;element name="MSCNumber" type="{}tISDNAddress" minOccurs="0"/>
 *         &lt;element name="CurrentLocationRetrieved" type="{}tBool" minOccurs="0"/>
 *         &lt;element name="AgeOfLocationInformation" type="{}tAgeOfLocationInformation" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tExtension" minOccurs="0"/>
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
@XmlType(name = "tCSLocationInformation", propOrder = {
    "locationNumber",
    "cellGlobalId",
    "serviceAreaId",
    "locationAreaId",
    "geographicalInformation",
    "geodeticInformation",
    "vlrNumber",
    "mscNumber",
    "currentLocationRetrieved",
    "ageOfLocationInformation",
    "extension",
    "any"
})
public class TCSLocationInformation implements CSLocationInformation {

    @XmlElement(name = "LocationNumber")
    protected String locationNumber;
    @XmlElement(name = "CellGlobalId")
    protected String cellGlobalId;
    @XmlElement(name = "ServiceAreaId")
    protected String serviceAreaId;
    @XmlElement(name = "LocationAreaId")
    protected String locationAreaId;
    @XmlElement(name = "GeographicalInformation")
    protected String geographicalInformation;
    @XmlElement(name = "GeodeticInformation")
    protected String geodeticInformation;
    @XmlElement(name = "VLRNumber")
    protected TISDNAddress vlrNumber;
    @XmlElement(name = "MSCNumber")
    protected TISDNAddress mscNumber;
    @XmlElement(name = "CurrentLocationRetrieved")
    protected Boolean currentLocationRetrieved;
    @XmlElement(name = "AgeOfLocationInformation")
    protected Integer ageOfLocationInformation;
    @XmlElement(name = "Extension")
    protected TExtension extension;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getLocationNumber()
     */
    public String getLocationNumber() {
        return locationNumber;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setLocationNumber(java.lang.String)
     */
    public void setLocationNumber(String value) {
        this.locationNumber = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getCellGlobalId()
     */
    public String getCellGlobalId() {
        return cellGlobalId;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setCellGlobalId(java.lang.String)
     */
    public void setCellGlobalId(String value) {
        this.cellGlobalId = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getServiceAreaId()
     */
    public String getServiceAreaId() {
        return serviceAreaId;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setServiceAreaId(java.lang.String)
     */
    public void setServiceAreaId(String value) {
        this.serviceAreaId = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getLocationAreaId()
     */
    public String getLocationAreaId() {
        return locationAreaId;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setLocationAreaId(java.lang.String)
     */
    public void setLocationAreaId(String value) {
        this.locationAreaId = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getGeographicalInformation()
     */
    public String getGeographicalInformation() {
        return geographicalInformation;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setGeographicalInformation(java.lang.String)
     */
    public void setGeographicalInformation(String value) {
        this.geographicalInformation = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getGeodeticInformation()
     */
    public String getGeodeticInformation() {
        return geodeticInformation;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setGeodeticInformation(java.lang.String)
     */
    public void setGeodeticInformation(String value) {
        this.geodeticInformation = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getVLRNumber()
     */
    public ISDNAddress getVLRNumber() {
        return vlrNumber;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setVLRNumber(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TISDNAddress)
     */
    public void setVLRNumber(ISDNAddress value) {
        this.vlrNumber = (TISDNAddress) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getMSCNumber()
     */
    public ISDNAddress getMSCNumber() {
        return mscNumber;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setMSCNumber(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TISDNAddress)
     */
    public void setMSCNumber(ISDNAddress value) {
        this.mscNumber = (TISDNAddress) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#isCurrentLocationRetrieved()
     */
    public Boolean isCurrentLocationRetrieved() {
        return currentLocationRetrieved;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setCurrentLocationRetrieved(java.lang.Boolean)
     */
    public void setCurrentLocationRetrieved(Boolean value) {
        this.currentLocationRetrieved = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getAgeOfLocationInformation()
     */
    public Integer getAgeOfLocationInformation() {
        return ageOfLocationInformation;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setAgeOfLocationInformation(java.lang.Integer)
     */
    public void setAgeOfLocationInformation(Integer value) {
        this.ageOfLocationInformation = value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getExtension()
     */
    public Extension getExtension() {
        return extension;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#setExtension(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TExtension)
     */
    public void setExtension(Extension value) {
        this.extension = (TExtension) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.CSLocationInformation#getAny()
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
