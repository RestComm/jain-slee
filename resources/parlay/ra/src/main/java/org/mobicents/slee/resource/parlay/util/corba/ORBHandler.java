package org.mobicents.slee.resource.parlay.util.corba;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.UserException;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.POAManagerPackage.AdapterInactive;

/**
 * This class handles ORB initialisation and shutdown. Accessor methods are
 * provided for the ORB resources.
 */
public class ORBHandler implements Runnable {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ORBHandler.class);

    /**
     * Thread used to handle remote requests
     */
    private transient Thread orbThread = null;

    /**
     * A flag so that the other threads can see that this thread is ready
     */
    private transient boolean isServerReady = false;

    private transient org.omg.CORBA.ORB orb = null;

    private transient POA rootPOA = null;

    //private String[] args = null;

    private Properties orbProperties;

    private static ORBHandler instance = null;

    public static synchronized ORBHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new ORBHandler();
            final Properties props = new Properties();
            props.load(instance.getClass().getResourceAsStream("parlayra-orb.properties"));

//            final ResourceBundle rb = ResourceBundle.getBundle("parlayra-orb");
//
//            for (Enumeration keys = rb.getKeys(); keys.hasMoreElements();) {
//                final String key = (String) keys.nextElement();
//                final String value = rb.getString(key);
//
//                props.put(key, value);
//            }

            instance.setOrbProperties(props);
        }
        return instance;
    }

    /**
     * @see java.lang.Object#Object()
     */
    private ORBHandler() {
        super();
    }

    public synchronized void init() throws UserException {
        if (!isServerReady) {
            try {
                initialiseCorbaSetup();
            }
            catch (UserException e) {
                throw e;
            }

            orbThread = new Thread(this);
            orbThread.setName("Parlay RA Orb Thread");
            orbThread.start();
        }
    }

    /**
     * Code to execute when thread is started.
     */
    public void run() {

        if (logger.isDebugEnabled()) {
            logger.debug("Running the CORBA handler thread ...");
        }
        try {

            isServerReady = true;

            synchronized (this) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Notifying waiting thread ...");
                }
                notify();
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Orb is ready for CORBA requests...");
            }

            orb.run();

            isServerReady = false;

            if (logger.isDebugEnabled()) {
                logger.debug("ORB.run has terminated");
            }

        }
        catch (RuntimeException ex) {
            // Log and don't rethrow as this is not an application thread
            logger.error("Unexpected Exception during CORBA initialization: "
                    + ex);
        }
    }

    /**
     * set up a POA, create and export object references.
     * 
     * @throws InvalidName
     * @throws AdapterInactive
     *  
     */
    private void initialiseCorbaSetup() throws InvalidName, AdapterInactive {

        if (logger.isDebugEnabled()) {
            logger.debug("Initializing for CORBA...");
        }

        orbInit();

        initRootPOA();

        rootPOA.the_POAManager().activate();

        isServerReady = true;

        if (logger.isDebugEnabled()) {
            logger.debug("... initialised ok");
        }
    }

    /**
     * Initialises the ORB. If now ORB properties have been specified then the
     * Iona defaults will be used. The orb arguments are specified from the
     * ORB.properties class.
     *  
     */
    private void orbInit() {

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising Parlay RA ORB ...");
        }
        orb = ORB.init(new String[0], orbProperties);

        if (logger.isDebugEnabled()) {
            logger.debug("...ORB.init was OK");
            logger.debug("Running ORB " + orb.toString());
        }
    }

    /**
     * @throws InvalidName
     */
    private void initRootPOA() throws InvalidName {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialising Root POA ...");
        }

        final Object rootPoaRef = orb.resolve_initial_references("RootPOA");

        rootPOA = POAHelper.narrow(rootPoaRef);

        if (logger.isDebugEnabled()) {
            logger.debug("... Root POA init was OK");
        }
    }

    /**
     * Find out whether this thread is ready for use
     * 
     * @return true is this thread is up and ready
     * @todo should be usinga wait/notify instead of allowing other parts of the
     *       PCP peroidically check this property
     */
    public synchronized boolean getIsServerReady() {

        return isServerReady;
    }

    /**
     * Gets the GlobalOrb attribute. This should really be a non static method
     * and this class be a singleton.
     * 
     * @return The GlobalOrb value
     */
    public org.omg.CORBA.ORB getOrb() {
        return orb;
    }

    /**
     * Returns the Root POA
     * 
     * @return
     */
    public org.omg.PortableServer.POA getRootPOA() {
        return rootPOA;
    }

    /**
     * @return
     */
    public Properties getOrbProperties() {
        return orbProperties;
    }

    /**
     * Shutdown the ORB
     */
    public synchronized void shutdown() {

        if (orb != null) {

            if (logger.isDebugEnabled()) {
                logger.debug("Shutting down the ORB...");
            }
            try {
                orb.shutdown(true);
            }
            catch (RuntimeException e) {
                logger.error("Exception shutting down ORB.", e);
            }
            try {
                orb.destroy();
            }
            catch (RuntimeException e) {
                logger.error("Exception destroying down ORB.", e);
            }

            isServerReady = false;
            rootPOA = null;
            orbThread = null;

            orb = null;

            if (logger.isDebugEnabled()) {
                logger.debug("...ORB shutdown OK");
            }
        }
    }

    /**
     * @param orbProperties
     *            The orbProperties to set.
     */
    public void setOrbProperties(final Properties orbProperties) {
        this.orbProperties = orbProperties;
    }
}