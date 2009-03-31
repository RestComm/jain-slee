/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.sleetck.lib.resource.adaptor;

import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import com.opencloud.logging.StdErrLog;
import com.opencloud.logging.Logable;

/**
 * Sets up the JAIN SLEE TCK 1.0 resource to be run as a standalone process.
 * This class creates the resource and binds its test interface
 * and resource adaptor interface to given ports in the local rmiregistry.
 */
public class StandaloneTCKResourceSetup {
 
    /**
     * Delegates to setup() with the arguments passed on the command line.
     *
     * Syntax: <test-insterface-port> <resource-adaptor-interface-port>
     */
    public static void main(String[] args) { 
       String syntaxString = "java StandaloneTCKResourceSetup test-interface-port resource-adaptor-interface-port";
       try {       
           int testInterfacePort = Integer.parseInt(args[0]);
           int resourceAdaptorPort = Integer.parseInt(args[1]);
           new StandaloneTCKResourceSetup().setup(testInterfacePort,resourceAdaptorPort);
       } catch(Exception e) {
           e.printStackTrace();
           System.err.println(syntaxString);
           System.exit(1);
       }
    }

    /**
     * Creates a JAIN SLEE TCK 1.0 resource and binds its test and resource adaptor interfaces in the rmiregsitry.
     */
    public void setup(int testInterfacePort, int resourceAdaptorPort) throws Exception {
        // validate configuration
        if ( testInterfacePort < 0 || testInterfacePort > 65535 )
            throw new Exception("Invalid port value for testInterfacePort: "+testInterfacePort);
        if ( resourceAdaptorPort < 0 || resourceAdaptorPort > 65535 ) 
            throw new Exception("Invalid port value for resourceAdaptorPort: "+resourceAdaptorPort);

        // create the resource
        TCKResourceSetupInterface resourceSetup = TCKResourceFactory.createResource();
        resourceSetup.setLog(log);

        // get the resource adaptor interface and bind it in the rmi registry
        TCKResourceAdaptorInterface raInterface = resourceSetup.getResourceAdaptorInterface();
        log.finer("StandaloneTCKResourceSetup: Locating registry at port "+resourceAdaptorPort+" on localhost...");
        rmiRegistryForRAInterface = LocateRegistry.getRegistry(resourceAdaptorPort);
        log.finer("StandaloneTCKResourceSetup: Registry located");
        log.finer("StandaloneTCKResourceSetup: Binding resource adaptor interface...");
        rmiRegistryForRAInterface.rebind(TCKResourceAdaptorInterface.RMI_NAME,raInterface);
        log.fine("StandaloneTCKResourceSetup: bound resource adaptor interface in rmi registry");

        // get the test interface and bind it in the rmi registry
        TCKResourceTestInterface testInterface = resourceSetup.getTestInterface();
        if(testInterfacePort == resourceAdaptorPort) {
          rmiRegistryForTestInterface = rmiRegistryForRAInterface;
        } else {
          log.finer("StandaloneTCKResourceSetup: Locating registry at port "+testInterfacePort+" on localhost...");
          rmiRegistryForTestInterface = LocateRegistry.getRegistry(testInterfacePort);
        }
        log.finer("StandaloneTCKResourceSetup: Registry located");
        log.finer("StandaloneTCKResourceSetup: Binding test interface...");
        rmiRegistryForTestInterface.rebind(TCKResourceTestInterface.RMI_NAME,testInterface);
        log.fine("StandaloneTCKResourceSetup: bound test interface in rmi registry");
    }

    /**
     * Unbinds both resource interfaces
     */
    public void stop() {
        // disconnect/unbind resources
        try {
            rmiRegistryForTestInterface.unbind(TCKResourceTestInterface.RMI_NAME);
            rmiRegistryForRAInterface.unbind(TCKResourceAdaptorInterface.RMI_NAME);
        } catch(Exception e) {
            log.warning("Caught exception while unbinding TCK resource interfaces: "+e);
        }

        log = null;
        rmiRegistryForRAInterface = rmiRegistryForTestInterface = null;
    }

    private Logable log = new StdErrLog();
    private Registry rmiRegistryForTestInterface;
    private Registry rmiRegistryForRAInterface;
}


