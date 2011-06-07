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

package org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;


/**
 * <p>Java class for documentType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="documentType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice minOccurs="0">
 *         &lt;element name="body-not-changed" type="{urn:ietf:params:xml:ns:xcap-diff}emptyType"/>
 *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *           &lt;choice>
 *             &lt;element name="add">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;extension base="{urn:ietf:params:xml:ns:xcap-diff}add">
 *                   &lt;/extension>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;element name="remove">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;extension base="{urn:ietf:params:xml:ns:xcap-diff}remove">
 *                   &lt;/extension>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;element name="replace">
 *               &lt;complexType>
 *                 &lt;complexContent>
 *                   &lt;extension base="{urn:ietf:params:xml:ns:xcap-diff}replace">
 *                   &lt;/extension>
 *                 &lt;/complexContent>
 *               &lt;/complexType>
 *             &lt;/element>
 *             &lt;any/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *       &lt;/choice>
 *       &lt;attribute name="sel" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="new-etag" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="previous-etag" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "documentType", propOrder = {
    "bodyNotChanged",
    "addOrRemoveOrReplace"
})
public class DocumentType {

    @XmlElement(name = "body-not-changed")
    protected EmptyType bodyNotChanged;
    @XmlElementRefs({
        @XmlElementRef(name = "remove", namespace = "urn:ietf:params:xml:ns:xcap-diff", type = JAXBElement.class),
        @XmlElementRef(name = "replace", namespace = "urn:ietf:params:xml:ns:xcap-diff", type = JAXBElement.class),
        @XmlElementRef(name = "add", namespace = "urn:ietf:params:xml:ns:xcap-diff", type = JAXBElement.class)
    })
    @XmlAnyElement(lax = true)
    protected List<Object> addOrRemoveOrReplace;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String sel;
    @XmlAttribute(name = "new-etag")
    protected String newEtag;
    @XmlAttribute(name = "previous-etag")
    protected String previousEtag;
    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap<QName, String>();

    /**
     * Gets the value of the bodyNotChanged property.
     * 
     * @return
     *     possible object is
     *     {@link EmptyType }
     *     
     */
    public EmptyType getBodyNotChanged() {
        return bodyNotChanged;
    }

    /**
     * Sets the value of the bodyNotChanged property.
     * 
     * @param value
     *     allowed object is
     *     {@link EmptyType }
     *     
     */
    public void setBodyNotChanged(EmptyType value) {
        this.bodyNotChanged = value;
    }

    /**
     * Gets the value of the addOrRemoveOrReplace property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addOrRemoveOrReplace property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddOrRemoveOrReplace().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * {@link JAXBElement }{@code <}{@link DocumentType.Remove }{@code >}
     * {@link Element }
     * {@link JAXBElement }{@code <}{@link DocumentType.Replace }{@code >}
     * {@link JAXBElement }{@code <}{@link DocumentType.Add }{@code >}
     * 
     * 
     */
    public List<Object> getAddOrRemoveOrReplace() {
        if (addOrRemoveOrReplace == null) {
            addOrRemoveOrReplace = new ArrayList<Object>();
        }
        return this.addOrRemoveOrReplace;
    }

    /**
     * Gets the value of the sel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSel() {
        return sel;
    }

    /**
     * Sets the value of the sel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSel(String value) {
        this.sel = value;
    }

    /**
     * Gets the value of the newEtag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewEtag() {
        return newEtag;
    }

    /**
     * Sets the value of the newEtag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewEtag(String value) {
        this.newEtag = value;
    }

    /**
     * Gets the value of the previousEtag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPreviousEtag() {
        return previousEtag;
    }

    /**
     * Sets the value of the previousEtag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPreviousEtag(String value) {
        this.previousEtag = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     * 
     * <p>
     * the map is keyed by the name of the attribute and 
     * the value is the string value of the attribute.
     * 
     * the map returned by this method is live, and you can add new attribute
     * by updating the map directly. Because of this design, there's no setter.
     * 
     * 
     * @return
     *     always non-null
     */
    public Map<QName, String> getOtherAttributes() {
        return otherAttributes;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{urn:ietf:params:xml:ns:xcap-diff}add">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Add
        extends org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Add
    {

        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{urn:ietf:params:xml:ns:xcap-diff}remove">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Remove
        extends org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Remove
    {

        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{urn:ietf:params:xml:ns:xcap-diff}replace">
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Replace
        extends org.mobicents.slee.enabler.xdmc.jaxb.xcapdiff.Replace
    {

        @XmlAnyAttribute
        private Map<QName, String> otherAttributes = new HashMap<QName, String>();

        /**
         * Gets a map that contains attributes that aren't bound to any typed property on this class.
         * 
         * <p>
         * the map is keyed by the name of the attribute and 
         * the value is the string value of the attribute.
         * 
         * the map returned by this method is live, and you can add new attribute
         * by updating the map directly. Because of this design, there's no setter.
         * 
         * 
         * @return
         *     always non-null
         */
        public Map<QName, String> getOtherAttributes() {
            return otherAttributes;
        }

    }

}
