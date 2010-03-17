package javax.slee.management;

/**
 * This exception is thrown by the {@link DeploymentMBean#install} method if an
 * attempt is made to install a deployable unit or component with the same
 * identity as an entity already installed in the SLEE.
 */
public class AlreadyDeployedException extends DeploymentException {
    /**
     * Constructs an <code>AlreadyDeployedException</code> with a detail message.
     * @param message the message.
     */
    public AlreadyDeployedException(String message) {
        super(message);
    }
}

