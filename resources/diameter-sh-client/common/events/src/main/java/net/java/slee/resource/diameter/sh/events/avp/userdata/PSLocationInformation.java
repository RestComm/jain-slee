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

import java.util.List;

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TISDNAddress;
import org.w3c.dom.Element;

/**
 * <p>Interface for tPSLocationInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tPSLocationInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="CellGlobalId" type="{}tCellGlobalId" minOccurs="0"/>
 *           &lt;element name="ServiceAreaId" type="{}tServiceAreaId" minOccurs="0"/>
 *           &lt;element name="LocationAreaId" type="{}tLocationAreaId" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="RoutingAreaId" type="{}tRoutingAreaId" minOccurs="0"/>
 *         &lt;element name="GeographicalInformation" type="{}tGeographicalInformation" minOccurs="0"/>
 *         &lt;element name="GeodeticInformation" type="{}tGeodeticInformation" minOccurs="0"/>
 *         &lt;element name="SGSNNumber" type="{}tISDNAddress" minOccurs="0"/>
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
public interface PSLocationInformation {

  /**
   * Gets the value of the cellGlobalId property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getCellGlobalId();

  /**
   * Sets the value of the cellGlobalId property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setCellGlobalId(String value);

  /**
   * Gets the value of the serviceAreaId property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getServiceAreaId();

  /**
   * Sets the value of the serviceAreaId property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setServiceAreaId(String value);

  /**
   * Gets the value of the locationAreaId property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getLocationAreaId();

  /**
   * Sets the value of the locationAreaId property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setLocationAreaId(String value);

  /**
   * Gets the value of the routingAreaId property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getRoutingAreaId();

  /**
   * Sets the value of the routingAreaId property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setRoutingAreaId(String value);

  /**
   * Gets the value of the geographicalInformation property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getGeographicalInformation();

  /**
   * Sets the value of the geographicalInformation property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setGeographicalInformation(String value);

  /**
   * Gets the value of the geodeticInformation property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getGeodeticInformation();

  /**
   * Sets the value of the geodeticInformation property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setGeodeticInformation(String value);

  /**
   * Gets the value of the sgsnNumber property.
   * 
   * @return
   *     possible object is
   *     {@link TISDNAddress }
   *     
   */
  public abstract ISDNAddress getSGSNNumber();

  /**
   * Sets the value of the sgsnNumber property.
   * 
   * @param value
   *     allowed object is
   *     {@link TISDNAddress }
   *     
   */
  public abstract void setSGSNNumber(ISDNAddress value);

  /**
   * Gets the value of the currentLocationRetrieved property.
   * 
   * @return
   *     possible object is
   *     {@link Boolean }
   *     
   */
  public abstract Boolean isCurrentLocationRetrieved();

  /**
   * Sets the value of the currentLocationRetrieved property.
   * 
   * @param value
   *     allowed object is
   *     {@link Boolean }
   *     
   */
  public abstract void setCurrentLocationRetrieved(Boolean value);

  /**
   * Gets the value of the ageOfLocationInformation property.
   * 
   * @return
   *     possible object is
   *     {@link Integer }
   *     
   */
  public abstract Integer getAgeOfLocationInformation();

  /**
   * Sets the value of the ageOfLocationInformation property.
   * 
   * @param value
   *     allowed object is
   *     {@link Integer }
   *     
   */
  public abstract void setAgeOfLocationInformation(Integer value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TExtension }
   *     
   */
  public abstract Extension getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TExtension }
   *     
   */
  public abstract void setExtension(Extension value);

  /**
   * Gets the value of the any property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the any property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAny().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Object }
   * {@link Element }
   * 
   * 
   */
  public abstract List<Object> getAny();

}