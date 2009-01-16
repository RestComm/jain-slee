package javax.slee.management;

import javax.management.ObjectName;

/**
 * The <code>SleeProvider</code> interface provides a management client with
 * enough information to begin interacting with a SLEE's management subsystem.
 * Every SLEE vendor must provide at least one implementation of this interface
 * (a peer class) and document the peer class's fully-qualifed class name.  The
 * peer class must include a public no-arg constructor.
 * <p>
 * A management client typically uses the {@link SleeProviderFactory} to create
 * instances of a vendor's <code>SleeProvider</code> peer class.
 */
public interface SleeProvider {
    /**
     * Get the JMX Object Name of the SLEE vendor's {@link SleeManagementMBean} object.
     * @return the Object Name of the <code>SleeManagementMBean</code> object.
     */
    public ObjectName getSleeManagementMBean();
}

