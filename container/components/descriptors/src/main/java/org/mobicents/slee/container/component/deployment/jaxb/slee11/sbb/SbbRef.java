/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.17 at 04:43:59 PM WET 
//


package org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "sbbName",
    "sbbVendor",
    "sbbVersion",
    "sbbAlias"
})
@XmlRootElement(name = "sbb-ref")
public class SbbRef {

    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    protected Description description;
    @XmlElement(name = "sbb-name", required = true)
    protected SbbName sbbName;
    @XmlElement(name = "sbb-vendor", required = true)
    protected SbbVendor sbbVendor;
    @XmlElement(name = "sbb-version", required = true)
    protected SbbVersion sbbVersion;
    @XmlElement(name = "sbb-alias", required = true)
    protected SbbAlias sbbAlias;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the sbbName property.
     * 
     * @return
     *     possible object is
     *     {@link SbbName }
     *     
     */
    public SbbName getSbbName() {
        return sbbName;
    }

    /**
     * Sets the value of the sbbName property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbName }
     *     
     */
    public void setSbbName(SbbName value) {
        this.sbbName = value;
    }

    /**
     * Gets the value of the sbbVendor property.
     * 
     * @return
     *     possible object is
     *     {@link SbbVendor }
     *     
     */
    public SbbVendor getSbbVendor() {
        return sbbVendor;
    }

    /**
     * Sets the value of the sbbVendor property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbVendor }
     *     
     */
    public void setSbbVendor(SbbVendor value) {
        this.sbbVendor = value;
    }

    /**
     * Gets the value of the sbbVersion property.
     * 
     * @return
     *     possible object is
     *     {@link SbbVersion }
     *     
     */
    public SbbVersion getSbbVersion() {
        return sbbVersion;
    }

    /**
     * Sets the value of the sbbVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbVersion }
     *     
     */
    public void setSbbVersion(SbbVersion value) {
        this.sbbVersion = value;
    }

    /**
     * Gets the value of the sbbAlias property.
     * 
     * @return
     *     possible object is
     *     {@link SbbAlias }
     *     
     */
    public SbbAlias getSbbAlias() {
        return sbbAlias;
    }

    /**
     * Sets the value of the sbbAlias property.
     * 
     * @param value
     *     allowed object is
     *     {@link SbbAlias }
     *     
     */
    public void setSbbAlias(SbbAlias value) {
        this.sbbAlias = value;
    }

}
