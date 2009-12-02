package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Originator-Address grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.70 Originator-Address AVP 
 * The Originator-Address AVP (AVP code 886) is of type Grouped. Its purpose is to identify the originator of a MM.
 * 
 * It has the following ABNF grammar: 
 *  Originator-Address ::= AVP Header: 886 
 *      [ Address-Type ] 
 *      [ Address-Data ] 
 *      [ Address-Domain ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface OriginatorAddress extends GroupedAvp {

  /**
   * Returns the value of the Address-Data AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getAddressData();

  /**
   * Returns the value of the Address-Domain AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract AddressDomain getAddressDomain();

  /**
   * Returns the value of the Address-Type AVP, of type Enumerated. A return value of null implies that the AVP has not been set.
   */
  abstract AddressType getAddressType();

  /**
   * Returns true if the Address-Data AVP is present in the message.
   */
  abstract boolean hasAddressData();

  /**
   * Returns true if the Address-Domain AVP is present in the message.
   */
  abstract boolean hasAddressDomain();

  /**
   * Returns true if the Address-Type AVP is present in the message.
   */
  abstract boolean hasAddressType();

  /**
   * Sets the value of the Address-Data AVP, of type UTF8String.
   */
  abstract void setAddressData(String addressData);

  /**
   * Sets the value of the Address-Domain AVP, of type Grouped.
   */
  abstract void setAddressDomain(AddressDomain addressDomain);

  /**
   * Sets the value of the Address-Type AVP, of type Enumerated.
   */
  abstract void setAddressType(AddressType addressType);

}
