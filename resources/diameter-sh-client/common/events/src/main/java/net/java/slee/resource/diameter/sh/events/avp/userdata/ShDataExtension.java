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

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TPublicIdentity;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TShDataExtension2;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TTransparentData;
import org.w3c.dom.Element;

/**
 * <p>Interface for tSh-Data-Extension complex type.
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
public interface ShDataExtension {

  /**
   * Gets the value of the registeredIdentities property.
   * 
   * @return
   *     possible object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract PublicIdentity getRegisteredIdentities();

  /**
   * Sets the value of the registeredIdentities property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract void setRegisteredIdentities(PublicIdentity value);

  /**
   * Gets the value of the implicitIdentities property.
   * 
   * @return
   *     possible object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract PublicIdentity getImplicitIdentities();

  /**
   * Sets the value of the implicitIdentities property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract void setImplicitIdentities(PublicIdentity value);

  /**
   * Gets the value of the allIdentities property.
   * 
   * @return
   *     possible object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract PublicIdentity getAllIdentities();

  /**
   * Sets the value of the allIdentities property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract void setAllIdentities(PublicIdentity value);

  /**
   * Gets the value of the aliasIdentities property.
   * 
   * @return
   *     possible object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract PublicIdentity getAliasIdentities();

  /**
   * Sets the value of the aliasIdentities property.
   * 
   * @param value
   *     allowed object is
   *     {@link TPublicIdentity }
   *     
   */
  public abstract void setAliasIdentities(PublicIdentity value);

  /**
   * Gets the value of the aliasesRepositoryData property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the aliasesRepositoryData property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getAliasesRepositoryData().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link TTransparentData }
   * 
   * 
   */
  public abstract List<TTransparentData> getAliasesRepositoryData();

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TShDataExtension2 }
   *     
   */
  public abstract ShDataExtension2 getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TShDataExtension2 }
   *     
   */
  public abstract void setExtension(ShDataExtension2 value);

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