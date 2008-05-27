/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-3-26                           *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.test.suite.tckwrapper;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.resource.InstalledResourceAdaptor;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.resource.TCKActivityContextInterfaceFactoryImpl;

import sun.rmi.registry.RegistryImpl;

import com.opencloud.sleetck.lib.infra.sleeplugin.SleeTCKPlugin;

/**
 * 
 * This class wraps the TCKPlugin which needs the RMI registry to expose 
 * server side test classes to the testing client
 * 
 * @author Francesco Moggia 
 * @author Ivelin Ivanov
 *
 * 
 */
public class SleeTCKPluginWrapper extends ServiceMBeanSupport implements SleeTCKPluginWrapperMBean 
{
    
    private String tckPluginMBObjName;
    private ObjectInstance tckPluginMBean; 
    private String tckPluginClassName;
    private int rmiRegistryPort;
    private String sleeProviderImpl;   
    private Registry rmiRegistry;
    
    public void startService() throws Exception
    {
        
        try
        {
        new RegistryImpl(getRMIRegistryPort());
        }
        catch (RemoteException re)
        {
            logger.info("RMIRegistry failed to bind on port " + getRMIRegistryPort() + ". This is expected in case of redeployment. The exception message is " + re.getMessage());
        }
        
	    MBeanServer mbserver = getServer();
	    ObjectName objName = new ObjectName(tckPluginMBObjName);
	    Logger stdErrLogger = Logger.getLogger("STDERR");
	    Level oldLevel = stdErrLogger.getLevel();
	    stdErrLogger.setLevel(Level.OFF);
	    SleeTCKPlugin tckPlugin = new SleeTCKPlugin(rmiRegistryPort, sleeProviderImpl);
	    tckPluginMBean = mbserver.registerMBean(tckPlugin, objName);
	    stdErrLogger.setLevel(oldLevel);
	    initializeTCKResourceAdaptor();
    }
    
    public void stopService() throws Exception
    {
	    MBeanServer mbserver = getServer();
	    mbserver.unregisterMBean(tckPluginMBean.getObjectName());
    }

    public void setTCKPluginClassName(String newClName)
    {
        tckPluginClassName = newClName;
    }

    public String getTCKPluginClassName()
    {
        return tckPluginClassName;
    }

    public void setRMIRegistryPort(int port)
    {
        rmiRegistryPort = port;
    }

    public int getRMIRegistryPort()
    {
        return rmiRegistryPort;
    }

    public void setSleeProviderImpl(String provider)
    {
        sleeProviderImpl = provider;
    }

    public String getSleeProviderImpl()
    {
        return sleeProviderImpl;
    }

    public void setTCKPluginMBeanObjectName(String newMBObjectName)
    {
        tckPluginMBObjName = newMBObjectName;
    }

    public String getTCKPluginMBeanObjectName()
    {
        return tckPluginMBObjName;
    }
    
    /**
     * This is scaffolding code that will be removed after ResourceAdaptor
     * installation is completed.
     *  
     */
    protected void initializeTCKResourceAdaptor() {
        ResourceAdaptorTypeDescriptorImpl ratDescr = new ResourceAdaptorTypeDescriptorImpl();
        ratDescr.parseTckRA();

        ResourceAdaptorType raType = new ResourceAdaptorType(ratDescr);
        ResourceAdaptorTypeID key = raType.getResourceAdaptorTypeID();
        if(logger.isDebugEnabled()) {
        	logger.debug("Installing RAType " + key);
        }

        SleeContainer slee = SleeContainer.lookupFromJndi();
        slee.addResourceAdaptorType(key, raType);

        ResourceAdaptorDescriptorImpl raDescr = new ResourceAdaptorDescriptorImpl();

        raDescr.parseTckRA();
        
        try {
            slee.install(raDescr, null);

            ResourceAdaptorIDImpl raID = new ResourceAdaptorIDImpl(raDescr
                    .getName(), raDescr.getVendor(), raDescr.getVersion());

            Properties p = new Properties();
            ResourceAdaptorEntity raEntity = slee.createResourceAdaptorEntity(raID, "tck", p);
            slee.activateResourceAdaptorEntity("tck");
            // FRANCESCO -- Why do you need a name for the entity and another
            // entity links?
            slee.createResourceAdaptorEntityLink("slee/resources/tck", "tck");

            // Add a resource adaptor entity to the installed resource adaptor.
            InstalledResourceAdaptor ira = slee.getInstalledResourceAdaptor(raID);
            ira.addResourceAdaptorEntity(raEntity);
        } catch (Exception e) {
           // e.printStackTrace();
           logger.error("Class for RA not found", e);
           throw new RuntimeException("Something bad happened while installing RA ", e);
        } 
        /*
         * Install the ActivityContextInterface Factory
         */
        TCKActivityContextInterfaceFactoryImpl acif = 
            new TCKActivityContextInterfaceFactoryImpl(
                slee, "tck");
        
        SleeContainer.registerWithJndi("slee/resources", "tckacif", acif);
        slee.addActivityContextInterfaceFactory(key,acif);
    }

    private static Logger logger = Logger.getLogger(SleeTCKPluginWrapper.class);
    
}
