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

/**
 * <p>Interface for tSePoTri complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tSePoTri">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ConditionNegated" type="{}tBool" minOccurs="0"/>
 *         &lt;element name="Group" type="{}tGroupID" maxOccurs="unbounded"/>
 *         &lt;choice>
 *           &lt;element name="RequestURI" type="{}tString"/>
 *           &lt;element name="Method" type="{}tString"/>
 *           &lt;element name="SIPHeader" type="{}tHeader"/>
 *           &lt;element name="SessionCase" type="{}tDirectionOfRequest"/>
 *           &lt;element name="SessionDescription" type="{}tSessionDescription"/>
 *         &lt;/choice>
 *         &lt;element name="Extension" type="{}tSePoTriExtension" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
import java.util.List;

import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.THeader;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSePoTriExtension;
import org.mobicents.slee.resource.diameter.sh.events.avp.userdata.TSessionDescription;
import org.w3c.dom.Element;

public interface SePoTri {

  /**
   * Gets the value of the conditionNegated property.
   * 
   * @return
   *     possible object is
   *     {@link Boolean }
   *     
   */
  public abstract Boolean isConditionNegated();

  /**
   * Sets the value of the conditionNegated property.
   * 
   * @param value
   *     allowed object is
   *     {@link Boolean }
   *     
   */
  public abstract void setConditionNegated(Boolean value);

  /**
   * Gets the value of the group property.
   * 
   * <p>
   * This accessor method returns a reference to the live list,
   * not a snapshot. Therefore any modification you make to the
   * returned list will be present inside the JAXB object.
   * This is why there is not a <CODE>set</CODE> method for the group property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   *    getGroup().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list
   * {@link Integer }
   * 
   * 
   */
  public abstract List<Integer> getGroup();

  /**
   * Gets the value of the requestURI property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getRequestURI();

  /**
   * Sets the value of the requestURI property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setRequestURI(String value);

  /**
   * Gets the value of the method property.
   * 
   * @return
   *     possible object is
   *     {@link String }
   *     
   */
  public abstract String getMethod();

  /**
   * Sets the value of the method property.
   * 
   * @param value
   *     allowed object is
   *     {@link String }
   *     
   */
  public abstract void setMethod(String value);

  /**
   * Gets the value of the sipHeader property.
   * 
   * @return
   *     possible object is
   *     {@link THeader }
   *     
   */
  public abstract Header getSIPHeader();

  /**
   * Sets the value of the sipHeader property.
   * 
   * @param value
   *     allowed object is
   *     {@link THeader }
   *     
   */
  public abstract void setSIPHeader(Header value);

  /**
   * Gets the value of the sessionCase property.
   * 
   * @return
   *     possible object is
   *     {@link Short }
   *     
   */
  public abstract Short getSessionCase();

  /**
   * Sets the value of the sessionCase property.
   * 
   * @param value
   *     allowed object is
   *     {@link Short }
   *     
   */
  public abstract void setSessionCase(Short value);

  /**
   * Gets the value of the sessionDescription property.
   * 
   * @return
   *     possible object is
   *     {@link TSessionDescription }
   *     
   */
  public abstract SessionDescription getSessionDescription();

  /**
   * Sets the value of the sessionDescription property.
   * 
   * @param value
   *     allowed object is
   *     {@link TSessionDescription }
   *     
   */
  public abstract void setSessionDescription(SessionDescription value);

  /**
   * Gets the value of the extension property.
   * 
   * @return
   *     possible object is
   *     {@link TSePoTriExtension }
   *     
   */
  public abstract SePoTriExtension getExtension();

  /**
   * Sets the value of the extension property.
   * 
   * @param value
   *     allowed object is
   *     {@link TSePoTriExtension }
   *     
   */
  public abstract void setExtension(SePoTriExtension value);

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