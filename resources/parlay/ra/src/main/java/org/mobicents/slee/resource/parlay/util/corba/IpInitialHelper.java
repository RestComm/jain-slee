package org.mobicents.slee.resource.parlay.util.corba;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.csapi.fw.fw_access.trust_and_security.IpInitial;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;

/**
 * 
 * Class Description for IpInitialHelper
 */
public class IpInitialHelper {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(IpInitialHelper.class);

    public IpInitialHelper() {
        super();
    }
    /**
     * Locates a naming service from the specified host and port and then
     * resolves an IpInitial from the specified location.
     * 
     * @param orb
     * @param ipInitialURL
     *            [host]:[port]:[ns location of IpInitial]
     * @return @throws
     *         InvalidName
     * @throws NotFound
     * @throws CannotProceed
     * @throws IOException
     */
    public static IpInitial getIpInitialfromURL(final ORB orb, final String ipInitialURL)
            throws InvalidName, NotFound, CannotProceed, IOException {

        if (logger.isDebugEnabled()) {
            logger.debug("Resolving IpInitial from URL" + ipInitialURL);
        }

        IpInitial result = null;

        final String[] urlParts = ipInitialURL.split(":");
        if (urlParts.length != 3) {
            result = null;
        }
        else {
            final String host = urlParts[0];
            final int port = Integer.parseInt(urlParts[1]);
            final String nsLocation = urlParts[2];

            final NamingContextExt namingService = NamingServiceHelper
                    .getNamingService(orb, host, port);

            final org.omg.CORBA.Object object = NamingServiceHelper
                    .getCorbaObjectReference(nsLocation, namingService);

            try {
                result = org.csapi.fw.fw_access.trust_and_security.IpInitialHelper
                        .narrow(object);
            }
            catch (BAD_PARAM e) {
                logger.error("Resolved object was not the correct type.");
            }
        }

        return result;
    }

    public static IpInitial getIpInitialFromNamingService(final ORB orb,
            final String nsIOR, final String ipInitialLocation) throws CannotProceed,
            NotFound, InvalidName, org.omg.CORBA.ORBPackage.InvalidName {
        IpInitial result = null;

        final org.omg.CORBA.Object object = NamingServiceHelper
                .getCorbaObjectReference(ipInitialLocation, NamingServiceHelper
                        .getNamingService(orb, nsIOR));

        try {
            result = org.csapi.fw.fw_access.trust_and_security.IpInitialHelper
                    .narrow(object);
        }
        catch (BAD_PARAM e) {
            logger.error("Resolved object was not the correct type.");
        }

        return result;
    }
}