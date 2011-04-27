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

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TCSLocationInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPSLocationInformation;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentity;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShIMSData;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TTransparentData;
import org.w3c.dom.Element;

/**
 * <p>Interface for tSh-Data complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tSh-Data">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PublicIdentifiers" type="{}tPublicIdentity" minOccurs="0"/>
 *         &lt;element name="RepositoryData" type="{}tTransparentData" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Sh-IMS-Data" type="{}tShIMSData" minOccurs="0"/>
 *         &lt;element name="CSLocationInformation" type="{}tCSLocationInformation" minOccurs="0"/>
 *         &lt;element name="PSLocationInformation" type="{}tPSLocationInformation" minOccurs="0"/>
 *         &lt;element name="CSUserState" type="{}tCSUserState" minOccurs="0"/>
 *         &lt;element name="PSUserState" type="{}tPSUserState" minOccurs="0"/>
 *         &lt;element name="Extension" type="{}tSh-Data-Extension" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ShData {

  /**
   * Gets the value of the publicIdentifiers property.
   * 
   * @return
   *     possible object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract PublicIdentity getPublicIdentifiers();

  /**
   * Sets the value of the publicIdentifiers property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract void setPublicIdentifiers(PublicIdentity value);

  /**
   * Gets the value of the repositoryData property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the repositoryData property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getRepositoryData().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link TTransparentData }
   * 
   * 
   */
  public abstract List<TTransparentData> getRepositoryData();

  /**
   * Gets the value of the shIMSData property.
   * 
   * @return
   *     possible object is
   *     {@link TShIMSData }
   *     
   */
  public abstract ShIMSData getShIMSData();

  /**
   * Sets the value of the shIMSData property.
   * 
   * @param value
   *     allowed object is
   *     {@link TShIMSData }
   *     
   */
  public abstract void setShIMSData(ShIMSData value);

  /**
   * Gets the value of the csLocationInformation property.
   * 
   * @return
   *     possible object is
   *     {@link TCSLocationInformation }
   *     
   */
  public abstract CSLocationInformation getCSLocationInformation();

  /**
   * Sets the value of the csLocationInformation property.
   * 
   * @param value
   *     allowed object is
   *     {@link TCSLocationInformation }
   *     
   */
  public abstract void setCSLocationInformation(CSLocationInformation value);

  /**
   * Gets the value of the psLocationInformation property.
   * 
   * @return
   *     possible object is
   *     {@link TPSLocationInformation }
   *     
   */
  public abstract PSLocationInformation getPSLocationInformation();

  /**
   * Sets the value of the psLocationInformation property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPSLocationInformation }
   *     
   */
  public abstract void setPSLocationInformation(PSLocationInformation value);

  /**
   * Gets the value of the csUserState property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
  public abstract Short getCSUserState();

  /**
   * Sets the value of the csUserState property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
  public abstract void setCSUserState(Short value);

  /**
   * Gets the value of the psUserState property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
  public abstract Short getPSUserState();

  /**
   * Sets the value of the psUserState property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
  public abstract void setPSUserState(Short value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TShDataExtension }
   *     
   */
  public abstract ShDataExtension getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TShDataExtension }
   *     
   */
  public abstract void setExtension(ShDataExtension value);

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