/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.logging;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.xml.DOMConfigurator;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.MainDeployer;
import org.jboss.deployment.MainDeployerConstants;
import org.jboss.deployment.MainDeployerMBean;
import org.jboss.deployment.SubDeployer;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.system.ServiceMBeanSupport;

/**
 * This MBean register itself to the SubDeployers in order to receive
 * notifications from the new deployments.
 *
 * Each deployment scanned for the acceptable log4j configuration. A new
 * LoggerRepository will be created for the given Deployment.
 *
 *
 * TODO: Watch the config files
 *
 * @version <tt>$Revision:  $</tt>
 * @author <a href="mailto:smil@dev.hu">Tamas Cserveny</a>
 */
public class Log4jRepositorySelector
        extends ServiceMBeanSupport
        implements RepositorySelector, NotificationListener, Log4jRepositorySelectorMBean
{
   
   private static final String CONFIG_XML  = "META-INF/log4j.xml";
   private static final String CONFIG_PROP = "META-INF/log4j.properties";
   
   /** Default logger repository */
   private static LoggerRepository defaultRepository;
   
   /** A proxy to the MainDeployer. */
   protected MainDeployerMBean mainDeployer;
   
   /** List of notification handlers by type. */
   private Map handlers = new HashMap();
   
   private DeploymentTracker tracker;
   
   /** Creates a new instance of Log4jRepositorySelector */
   public Log4jRepositorySelector()
   {
      // Handlers to track deployments
      handlers.put( SubDeployer.INIT_NOTIFICATION,     new InitSubDeployerListener()         );
      handlers.put( SubDeployer.DESTROY_NOTIFICATION,  new DestorySubDeployerListener()      );
      
      // Handlers for debugging
      handlers.put( SubDeployer.CREATE_NOTIFICATION,   new StatSubDeployerListener("Create") );
      handlers.put( SubDeployer.STOP_NOTIFICATION,     new StatSubDeployerListener("Stop")   );
      handlers.put( SubDeployer.START_NOTIFICATION,    new StatSubDeployerListener("Start")  );
      
      // Handlers to track deployers
      handlers.put( MainDeployerConstants.ADD_DEPLOYER,    new AddDeployerListener() );
      handlers.put( MainDeployerConstants.REMOVE_DEPLOYER, new RemoveDeployerListener() );
   }
   
   /**
    * This is the implementation of the RepositorySelector interface, it will be
    * invoked upon all Logger creation in order to find the correct repository.
    */
   public LoggerRepository getLoggerRepository()
   {
      LoggerRepository repository = (LoggerRepository)tracker.get();
      
      if (repository == null)
      {
         return defaultRepository;
      }
      else
      {
         return repository;
      }
   }
   
   /**
    * This is the implementation of the NotificationListener interface.
    * All watched services send their notifications to this dispatcher.
    */
   public void handleNotification(Notification notification, Object handback)
   {
      NotificationListener listener =
              (NotificationListener) handlers.get( notification.getType() );
      
      if ( listener != null )
      {
         listener.handleNotification( notification, handback );
      }
   }
   
   /**
    * Create the external references.
    */
   protected void createService() throws Exception
   {
      // Setup the proxy to mainDeployer
      mainDeployer = (MainDeployerMBean)
      MBeanProxyExt.create(MainDeployerMBean.class,
              MainDeployerMBean.OBJECT_NAME,
              server);
   }
   
   /**
    * Starts the service.
    */
   protected void startService() throws Exception
   {
      // Get the default repository created by the Log4jService.
      defaultRepository = LogManager.getLoggerRepository();
      
      tracker = new DeploymentTracker( getServer(), log );
      
      LogManager.setRepositorySelector( this, LogManager.getRootLogger() );
      
      // Get notifications from the deployers
      getServer().addNotificationListener( MainDeployer.OBJECT_NAME, this, null, null );
      
      // Register to all already started deployers
      Collection co = mainDeployer.listDeployers();
      for (Iterator i = co.iterator(); i.hasNext();)
      {
         ObjectName name = (ObjectName) i.next();
         registerListener( name );
      }
   }
   
   /**
    * Stops the service.
    */
   protected void stopService() throws Exception
   {
      // Nothing can be done here
      // This service should be deployed only once at startup and undeployed at
      // the very end.
   }
   
   /**
    * Deploys the logger if needed.
    */
   private void deployLogger( DeploymentInfo di )
   throws IOException
   {
      Hierarchy hierarchy = null;
      ClassLoader cl = di.localCl;
      
      // Only 'root' deployments are considered
      if ( di.parent != null )
      {
         log.debug("Skip nested deployments, as they cannot have an own logger.");
         return;
      }
      
      InputStream input = cl.getResourceAsStream( CONFIG_XML );
      
      if ( log.isDebugEnabled() )
      {
         log.debug("Search for log4j config in deployment " + di.shortName );
      }
      
      // Try load XML first
      if ( input != null )
      {
         log.info("Found log4j XML configuration in deployment " + di.shortName );
         
         DOMConfigurator conf = new DOMConfigurator();
         hierarchy = new Hierarchy( new RootCategory( Level.INFO ) );
         conf.doConfigure( input, hierarchy );
      }
      else
      {
         // Try load properties
         input = cl.getResourceAsStream( CONFIG_PROP );
         
         if ( input != null )
         {
            log.info("Found log4j property configuration in deployment " + di.shortName );
            
            PropertyConfigurator conf = new PropertyConfigurator();
            hierarchy = new Hierarchy( new RootCategory( Level.INFO ) );
            
            Properties prop = new Properties();
            prop.load( input );
            conf.doConfigure( prop, hierarchy );
         }
      }
      
      // Register the hierarchy
      if ( hierarchy != null )
      {
         tracker.trackDeployment( di, hierarchy );
      }
   }
   
   /**
    * Undelpoys the logger from a given deployment. Does nothing on deployments
    * without custom logger.
    */
   private void undeployLogger( DeploymentInfo di )
   {
      // Remove deployment from the tracker
      LoggerRepository repository = (LoggerRepository)tracker.removeDeployment( di );
      
      // If there was a custom repository, it's time to close it.
      if ( repository != null && repository != defaultRepository)
      {
         repository.shutdown();
      }
   }
   
   /**
    * Register for notifications.
    */
   private void registerListener( ObjectName name )
   {
      try
      {
         getServer().addNotificationListener( name, this, null, name );
      }
      catch (InstanceNotFoundException ex)
      {
         // Should not happen, but log it though
         log.error("Object not found. Should not happen.", ex);
      }
   }
   
   /**
    * Registers a new deployer.
    * Invoked upon MainDeployerConstants.ADD_DEPLOYER notification.
    */
   private class AddDeployerListener implements NotificationListener
   {
      public void handleNotification(Notification notification, Object handback)
      {
         ObjectName name = (ObjectName)notification.getUserData();
         registerListener( name );
      }
   }
   
   /**
    * Unregisters a destroyed deployer.
    * Invoked upon MainDeployerConstants.REMOVE_DEPLOYER notification.
    */
   private class RemoveDeployerListener implements NotificationListener
   {
      public void handleNotification(Notification notification, Object handback)
      {
         ObjectName name = (ObjectName)notification.getUserData();
         try
         {
            getServer().removeNotificationListener( name, this );
         }
         catch (InstanceNotFoundException ex)
         {
            // Should not happen, but log it though
            log.error("Object not found. Should not happen.", ex);
         }
         catch (ListenerNotFoundException ignore)
         {
            // Ignore.
         }
      }
   }
   
   /**
    * This listener adds some markers into the debug log on SubDeployer
    * notifications.
    */
   private class StatSubDeployerListener implements NotificationListener
   {
      private String info;
      
      public StatSubDeployerListener( String info )
      {
         this.info = info;
      }
      
      public void handleNotification(Notification notification, Object handback)
      {
         DeploymentInfo di = (DeploymentInfo)notification.getUserData();
         
         if ( log.isDebugEnabled() )
         {
            log.debug( info + " " + di.shortName );
            tracker.printStats();
         }
      }
   }
   
   /**
    * Invoked upon all new deployments, tries to deploy the logger.
    *
    * Invoked upon SubDeployer.INIT_NOTIFICATION notifications.
    */
   private class InitSubDeployerListener implements NotificationListener
   {
      public void handleNotification(Notification notification, Object handback)
      {
         DeploymentInfo di = (DeploymentInfo)notification.getUserData();
         try
         {
            deployLogger( di );
            
            tracker.printStats();
            
         }
         catch (IOException ioe )
         {
            log.error("Failed to configure deployment-local log4j!", ioe );
         }
      }
   }
   
   /**
    * Invoked upon all undeploy events, tries to remove and shutdown the logger.
    *
    * Invoked upon SubDeployer.DESTORY_NOTIFICATION notifications.
    */
   private class DestorySubDeployerListener implements NotificationListener
   {
      public void handleNotification(Notification notification, Object handback)
      {
         DeploymentInfo di = (DeploymentInfo)notification.getUserData();
         
         undeployLogger( di );
         
         tracker.printStats();
      }
   }
}
