package javax.slee.management;

/**
 * The <code>SleeProviderFactory</code> provides a static factory method for
 * instantiating {@link SleeProvider} peer classes.  The peer class provides
 * a management client with enough information to begin interacting with a
 * particular vendor's SLEE management subsystem.  Every SLEE vendor must provide
 * an implementation of the <code>SleeProvider</code> interface (a peer class)
 * and document the its fully-qualifed class name.
 */
public final class SleeProviderFactory {
    /**
     * Get an instance of a <code>SleeProvider</code> peer class.  This method is
     * equivalent to {@link #getSleeProvider(String, ClassLoader) getSleeProvider(peerClassName,
     * SleeProviderFactory.class.getClassLoader())}.  In the case where the
     * SleeProviderFactory's class loader is <code>null</code>,
     * <code>ClassLoader.getSystemClassLoader()</code> is used instead.
     * @param peerClassName the fully-qualified class name of a class that
     *        implements the <code>SleeProvider</code> interface.
     * @return an instance of the specified class.
     * @throws NullPointerException if <code>peerClassName</code> is <code>null</code>.
     * @throws PeerUnavailableException if the peer class could not be instantiated
     *        or did not implement the <code>SleeProvider</code> interface.
     */
    public static SleeProvider getSleeProvider(String peerClassName) throws NullPointerException, PeerUnavailableException {
        ClassLoader classloader = SleeProviderFactory.class.getClassLoader();
        if (classloader == null) classloader = ClassLoader.getSystemClassLoader();

        return getSleeProvider(peerClassName, classloader);
    }

    /**
     * Get an instance of a <code>SleeProvider</code> peer class using the specified
     * class loader.
     * @param peerClassName the fully-qualified class name of a class that
     *        implements the <code>SleeProvider</code> interface.
     * @param classloader the classloader to use to load the peer class.
     * @return an instance of the specified class.
     * @throws NullPointerException if <code>peerClassName</code> or <code>classloader</code>
     *         is <code>null</code>.
     * @throws PeerUnavailableException if the peer class could not be instantiated
     *        or did not implement the <code>SleeProvider</code> interface.
     */
    public static SleeProvider getSleeProvider(String peerClassName, ClassLoader classloader) throws NullPointerException, PeerUnavailableException {
        if (peerClassName == null) throw new NullPointerException("peerClassName is null");

        if (peerClassName.length() == 0) throw new PeerUnavailableException("peerClassName is zero-length");

        try {
            return (SleeProvider)classloader.loadClass(peerClassName).newInstance();
        }
        catch (Throwable t) {
            throw new PeerUnavailableException("Peer class could not be instantiated", t);
        }
    }
}

