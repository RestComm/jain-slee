package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Java class to represent the Adaptations enumerated type.
 * <pre>
 * 6.3.4 Server-Capabilities AVP
 * The Server-Capabilities AVP is of type Grouped. This AVP contains information 
 * to assist the I-CSCF in the selection of an S-CSCF.
 * 
 * AVP format
 * Server-Capabilities ::= <AVP header: 603 10415>
 *   *[Mandatory-Capability]
 *   *[Optional-Capability]
 *   *[Server-Name]
 *   *[AVP]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ServerCapabilities extends GroupedAvp {

  /**
   * Returns the value of the Mandatory-Capability AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getMandatoryCapability();

  /**
   * Returns the value of the Optional-Capability AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getOptionalCapability();

  /**
   * Returns the value of the Server-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getServerName();

  /**
   * Returns true if the Mandatory-Capability AVP is present in the message.
   */
  abstract boolean hasMandatoryCapability();

  /**
   * Returns true if the Optional-Capability AVP is present in the message.
   */
  abstract boolean hasOptionalCapability();

  /**
   * Returns the value of the Server-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract boolean hasServerName();

  /**
   * Sets the value of the Mandatory-Capability AVP, of type Unsigned32.
   */
  abstract void setMandatoryCapability(long mandatoryCapability);

  /**
   * Sets the value of the Optional-Capability AVP, of type Unsigned32.
   */
  abstract void setOptionalCapability(long optionalCapability);

  /**
   * Returns the value of the Server-Name AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract void setServerName(String serverName);
}
