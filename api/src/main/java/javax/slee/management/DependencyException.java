package javax.slee.management;

/**
 * This exception is thrown by {@link DeploymentMBean#uninstall} method if an
 * attempt is made to uninstall a deployable unit when another component in
 * another deployable unit depends on a component contained within the deployable
 * unit being uninstalled.
 */
public class DependencyException extends DeploymentException {
    /**
     * Constructs a <code>DependencyException</code> with a detail message.
     * @param message the message.
     */
    public DependencyException(String message) {
        super(message);
    }
}

