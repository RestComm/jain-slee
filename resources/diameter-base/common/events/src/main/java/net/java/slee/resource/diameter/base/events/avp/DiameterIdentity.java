package net.java.slee.resource.diameter.base.events.avp;

import java.io.Serializable;

/**
 * Java class to represent the DiameterIdentity AVP type.
 * <p/>
 * The DiameterIdentity format is derived from the OctetString AVP Base Format.
 * <p/>
 * DiameterIdentity  = FQDN
 * <p/>
 * DiameterIdentity value is used to uniquely identify a Diameter
 * node for purposes of duplicate connection and routing loop
 * detection.
 * <p/>
 * The contents of the string MUST be the FQDN of the Diameter node.
 * If multiple Diameter nodes run on the same host, each Diameter
 * node MUST be assigned a unique DiameterIdentity.  If a Diameter
 * node can be identified by several FQDNs, a single FQDN should be
 * picked at startup, and used as the only DiameterIdentity for that
 * node, whatever the connection it is sent on.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DiameterIdentity implements Serializable
{
  private static final long serialVersionUID = -7248928425138452437L;

  private String identity;

  public DiameterIdentity(String identity) {
    if(identity == null) {
      throw new NullPointerException("The contents of the string MUST be the FQDN of the Diameter node, it was null.");
    }

    this.identity = identity;
  }

  public String toString() {
    return this.identity;
  }

  @Override
  public int hashCode() {
    return this.identity.hashCode();
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if(that instanceof DiameterIdentity) {
      DiameterIdentity other = (DiameterIdentity) that;

      return this.identity == other.identity || (this.identity != null && this.identity.equals(other.identity));
    }

    return false;
  }
}