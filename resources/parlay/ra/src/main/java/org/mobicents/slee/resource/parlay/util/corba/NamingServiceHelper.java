package org.mobicents.slee.resource.parlay.util.corba;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * Utility class for getting objects from corba naming service.
 */
public class NamingServiceHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(NamingServiceHelper.class);
    
    public NamingServiceHelper() {
        super();
    }

    public static NamingContextExt getNamingService(final ORB orb, final String host,
            final int port) throws IOException {

        NamingContextExt result = null;
        String ior = "";

        ior = readIORFromSocket(host, port);

        final org.omg.CORBA.Object objref = orb.string_to_object(ior);

        result = NamingContextExtHelper.narrow(objref);

        return result;
    }

    /**
     * @param host
     * @param port
     * @return @throws
     *         UnknownHostException
     * @throws IOException
     */
    private static String readIORFromSocket(final String host, final int port)
            throws UnknownHostException, IOException {
        String ior = "";

        if(logger.isDebugEnabled()) {
            logger.debug("Reading IOR from " + host + ":" + port);
        }
        Socket socket = null;

        try {
            socket = new Socket(host, port);

            final java.io.DataInputStream dis = new java.io.DataInputStream(socket
                    .getInputStream());

            int n = 1000;
            final byte[] stringifiedIOR = new byte[n];

            while ((n = dis.read(stringifiedIOR)) > 0) {
                ior += new String(stringifiedIOR, 0, n);
            }
            
            //subtract one to remove line end character
            ior = ior.substring(0, ior.length() - 1);

            if (logger.isDebugEnabled()) {
                logger.debug("Stringified IOR :  " + ior);
            }
        }
        finally {
            if (socket != null) {
                try {
                    socket.close();
                    socket = null;
                }
                catch (RuntimeException ex) {
                    socket = null;
                }
            }
        }
        return ior;
    }

    /**
     * Returns the NameService NamingContextExt.
     * 
     * @param orb
     * @return @throws
     *         InvalidName
     */
    public static NamingContextExt getDefaultNamingService(final ORB orb) throws InvalidName {

        NamingContextExt result = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Obtaining reference to the NameService ...");
        }

        final org.omg.CORBA.Object objref = orb
                .resolve_initial_references("NameService");

        if (logger.isDebugEnabled()) {
            logger.debug("Narrowing to a NamingContextExt ....");
        }

        result = NamingContextExtHelper.narrow(objref);

        return result;
    }

    /**
     * Returns the NameService NamingContextExt.
     * 
     * @param orb
     * @param nsIOR
     * @return @throws
     *         InvalidName
     */
    public static NamingContextExt getNamingService(final ORB orb, final String nsIOR) throws InvalidName {

        NamingContextExt result = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Obtaining reference to the NameService ...");
        }

        final org.omg.CORBA.Object objref = orb
                .string_to_object(nsIOR);

        if (logger.isDebugEnabled()) {
            logger.debug("Narrowing to a NamingContextExt ....");
        }

        result = NamingContextExtHelper.narrow(objref);

        return result;
    }

    /**
     * Resolves the supplied name to an object reference.
     * 
     * @param objectName
     * @param namingContextExt
     * @return @throws
     *         CannotProceed
     * @throws NotFound
     * @throws org.omg.CosNaming.NamingContextPackage.InvalidName
     */
    public static org.omg.CORBA.Object getCorbaObjectReference(
            final String objectName,
            final org.omg.CosNaming.NamingContextExt namingContextExt)
            throws CannotProceed, NotFound,
            org.omg.CosNaming.NamingContextPackage.InvalidName {

        org.omg.CORBA.Object result = null;

        if (logger.isDebugEnabled()) {
            logger.debug("Resolving the supplied string name... <" + objectName
                    + ">");
        }

        final NameComponent[] tmpName = namingContextExt.to_name(objectName);

        result = namingContextExt.resolve(tmpName);

        return result;
    }

}