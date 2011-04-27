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

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TChargingInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TIFCs;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSDataExtension;
import org.w3c.dom.Element;

/**
 * <p>Interface for tShIMSData complex type.
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
public interface ShIMSData {

  /**
   * Gets the value of the scscfName property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getSCSCFName();

  /**
   * Sets the value of the scscfName property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setSCSCFName(String value);

  /**
   * Gets the value of the ifCs property.
   * 
   * @return
   *     possible object is
   *     {@link TIFCs }
   *     
   */
  public abstract IFCs getIFCs();

  /**
   * Sets the value of the ifCs property.
   * 
   * @param value
   *     allowed object is
   *     {@link TIFCs }
   *     
   */
  public abstract void setIFCs(IFCs value);

  /**
   * Gets the value of the imsUserState property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
  public abstract Short getIMSUserState();

  /**
   * Sets the value of the imsUserState property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
  public abstract void setIMSUserState(Short value);

  /**
   * Gets the value of the chargingInformation property.
   * 
   * @return
   *     possible object is
   *     {@link TChargingInformation }
   *     
   */
  public abstract ChargingInformation getChargingInformation();

  /**
   * Sets the value of the chargingInformation property.
   * 
   * @param value
   *     allowed object is
   *     {@link TChargingInformation }
   *     
   */
  public abstract void setChargingInformation(ChargingInformation value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TShIMSDataExtension }
   *     
   */
  public abstract ShIMSDataExtension getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TShIMSDataExtension }
   *     
   */
  public abstract void setExtension(ShIMSDataExtension value);

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