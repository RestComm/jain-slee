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

import net.java.slee.resource.diameter.sh.events.avp.userdata.PublicIdentity;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension2;


/**
 * <p>Java class for tSh-Data-Extension complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tSh-Data-Extension">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RegisteredIdentities" type="{}tPublicIdentity" minOccurs="0"/>
 *         &lt;element name="ImplicitIdentities" type="{}tPublicIdentity" minOccurs="0"/>
 *         &lt;element name="AllIdentities" type="{}tPublicIdentity" minOccurs="0"/>
 *         &lt;element name="AliasIdentities" type="{}tPublicIdentity" minOccurs="0"/>
 *         &lt;element name="AliasesRepositoryData" type="{}tTransparentData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tSh-Data-Extension2" minOccurs="0"/>
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
@XmlType(name = "tSh-Data-Extension", propOrder = {
    "registeredIdentities",
    "implicitIdentities",
    "allIdentities",
    "aliasIdentities",
    "aliasesRepositoryData",
    "extension",
    "any"
})
public class TShDataExtension implements ShDataExtension {

    @XmlElement(name = "RegisteredIdentities")
    protected TPublicIdentity registeredIdentities;
    @XmlElement(name = "ImplicitIdentities")
    protected TPublicIdentity implicitIdentities;
    @XmlElement(name = "AllIdentities")
    protected TPublicIdentity allIdentities;
    @XmlElement(name = "AliasIdentities")
    protected TPublicIdentity aliasIdentities;
    @XmlElement(name = "AliasesRepositoryData")
    protected List<TTransparentData> aliasesRepositoryData;
    @XmlElement(name = "Extension")
    protected TShDataExtension2 extension;
    @XmlAnyElement(lax = true)
    protected List<Object> any;

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getRegisteredIdentities()
     */
    public PublicIdentity getRegisteredIdentities() {
        return registeredIdentities;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#setRegisteredIdentities(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.PublicIdentity)
     */
    public void setRegisteredIdentities(PublicIdentity value) {
        this.registeredIdentities = (TPublicIdentity) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getImplicitIdentities()
     */
    public PublicIdentity getImplicitIdentities() {
        return implicitIdentities;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#setImplicitIdentities(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.PublicIdentity)
     */
    public void setImplicitIdentities(PublicIdentity value) {
        this.implicitIdentities = (TPublicIdentity) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getAllIdentities()
     */
    public PublicIdentity getAllIdentities() {
        return allIdentities;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#setAllIdentities(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.PublicIdentity)
     */
    public void setAllIdentities(PublicIdentity value) {
        this.allIdentities = (TPublicIdentity) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getAliasIdentities()
     */
    public PublicIdentity getAliasIdentities() {
        return aliasIdentities;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#setAliasIdentities(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.PublicIdentity)
     */
    public void setAliasIdentities(PublicIdentity value) {
        this.aliasIdentities = (TPublicIdentity) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getAliasesRepositoryData()
     */
    public List<TTransparentData> getAliasesRepositoryData() {
        if (aliasesRepositoryData == null) {
            aliasesRepositoryData = new ArrayList<TTransparentData>();
        }
        return this.aliasesRepositoryData;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getExtension()
     */
    public ShDataExtension2 getExtension() {
        return extension;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#setExtension(org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension2)
     */
    public void setExtension(ShDataExtension2 value) {
        this.extension = (TShDataExtension2) value;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.diameter.sh.events.avp.userdata.ShDataExtension#getAny()
     */
    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }

}
