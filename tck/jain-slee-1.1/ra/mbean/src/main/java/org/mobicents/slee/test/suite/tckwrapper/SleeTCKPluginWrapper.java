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

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;

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
	    //Logger stdErrLogger = Logger.getLogger("STDERR");
	    //Level oldLevel = stdErrLogger.getLevel();
	    //stdErrLogger.setLevel(Level.OFF);
	    SleeTCKPlugin tckPlugin = new SleeTCKPlugin(rmiRegistryPort, sleeProviderImpl);
	    tckPluginMBean = mbserver.registerMBean(tckPlugin, objName);
	    //stdErrLogger.setLevel(oldLevel);
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

    private static Logger logger = Logger.getLogger(SleeTCKPluginWrapper.class);
    
}
