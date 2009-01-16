package javax.slee.connection;

import javax.resource.ResourceException;

/**
 * A factory interface allowing the creation of SleeConnection instances.
 * This is modelled as a Connector Architecture non-CCI connection
 * factory interface. A SLEE vendor provides an implementation of this
 * interface if they support external interoperability.
 *<p>
 * An implementation of SleeConnectionFactory is expected to hold all the
 * information necessary to create connections on demand and will be bound
 * into JNDI. An EJB application retrieves the SleeConnectionFactory from
 * JNDI and uses it to create SleeConnection objects as needed.
 *<p>
 * Implementations of SleeConnectionFactory that are packaged as part of a
 * Connector Architecture Resource Adaptor should additionally implement
 * {@link java.io.Serializable} and {@link javax.resource.Referenceable}, as
 * described in the Connector Architecture specification.
 *
 * @see SleeConnection
 */
public interface SleeConnectionFactory {
    /**
     * Return a SleeConnection object that corresponds to a connection to
     * the SLEE this factory is associated with.
     *
     * @return the SleeConnection object.
     * @throws ResourceException if a SleeConnection cannot be created
     */
    SleeConnection getConnection()
        throws ResourceException;
}
