package org.mobicents.slee.container.deployment.jboss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.DeployableUnitID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorEntity;

/**
 * This class represents the Manager responsible for executing deployment actions
 * and for controlling dependencies and monitoring new deployments using the deployer.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public class DeploymentManager
{
  // The Logger.
  private static Logger logger = Logger.getLogger( DeploymentManager.class );
  
  // The DUs waiting to be installed.
  private Collection<DeployableUnit> waitingForInstallDUs = new ArrayList<DeployableUnit>();
  
  // The DUs waiting for being uninstalled.
  private Collection<DeployableUnit> waitingForUninstallDUs = new ArrayList<DeployableUnit>();
  
  // The components already deployed to SLEE
  private Collection<String> deployedComponents = new ArrayList<String>();
  
  // Actions using DeploymentMBean
  private Collection<String> deploymentActions = Arrays.asList( new String[] { "install", "uninstall" } );
  
  // Actions using ResourceManagementMBean
  private Collection<String> resourceAdaptorActions = Arrays.asList( 
      new String[] { "bindLinkName", "unbindLinkName", 
      "createResourceAdaptorEntity", "removeResourceAdaptorEntity",
      "activateResourceAdaptorEntity", "deactivateResourceAdaptorEntity" } );
  
  // Actions using ServiceManagementMBean
  private Collection<String> serviceActions = Arrays.asList( new String[] { "activate", "deactivate" } );
  
  public long waitTimeBetweenOperations = 250;
  
  /**
   * Constructor.
   */
  public DeploymentManager() {}
  
  /**
   * Method for adding a new deployable unit to the manager.
   * @param du the DeployableUnit object to add.
   * @throws Exception
   */
  public void addDeployableUnit(DeployableUnit du) throws Exception
  {
    // Get a fresh copy of the deployed components. 
    updateDeployedComponents();
    
    // Check if there aren't already deployed components in this DU...
    if( !deployedComponents.containsAll( du.getComponents() ) )
    {
      // Add it to the deploy list.
      waitingForInstallDUs.add( du );
    }
    else
    {
      logger.warn( "Trying to deploy a duplicate DU (" + du.getDeploymentInfoShortName() + ")." );
    }
  }
  
  /**
   * Updates the list of components already deployed to SLEE.
   */
  public void updateDeployedComponents()
  {
    try
    {
      // Get the SLEE Container from JNDI
      SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
      
      // First we'll put the components in a temp Collection
      ArrayList<String> newDeployedComponents = new ArrayList<String>();
      
      // Get the deployed Profile Specifications
      ProfileSpecificationID[] sleeProfileSpecifications = sleeContainer.getProfileSpecificationIDs();
      
      for( int i = 0; i < sleeProfileSpecifications.length; i++ )
      {
        newDeployedComponents.add( sleeProfileSpecifications[i].toString() );
      }
      
      // Get the deployed Event Types
      EventTypeID[] sleeEventTypes = sleeContainer.getEventTypes();
      
      for( int i = 0; i < sleeEventTypes.length; i++ )
      {
        String eventTypeIDwithDUnit = sleeEventTypes[i].toString();
        
        newDeployedComponents.add( eventTypeIDwithDUnit.substring( 0, eventTypeIDwithDUnit.indexOf( "," ) ) );
      }
      
      // Get the deployed Resource Adaptor Types
      ResourceAdaptorTypeID[] sleeRATypes = sleeContainer.getResourceAdaptorTypeIDs();
      
      for( int i = 0; i < sleeRATypes.length; i++ )
      {
        newDeployedComponents.add( sleeRATypes[i].toString() );
      }
      
      // Get the deployed Resource Adaptors
      ResourceAdaptorID[] sleeResourceAdaptors = sleeContainer.getResourceAdaptorIDs();

      for( int i = 0; i < sleeResourceAdaptors.length; i++ )
      {
        newDeployedComponents.add( sleeResourceAdaptors[i].toString() );
      }
      
      // Get the deployed Service Building Blocks (SBBs)
      SbbID[] sleeSBBs = sleeContainer.getSbbIDs();
      
      for( int i = 0; i < sleeSBBs.length; i++ )
      {
        newDeployedComponents.add( sleeSBBs[i].toString() );
      }
      
      // Get the deployed Services
      ServiceID[] sleeServices = sleeContainer.getServiceIDs();

      for( int i = 0; i < sleeServices.length; i++ )
      {
        newDeployedComponents.add( sleeServices[i].toString() );
      }
      
      // Get the existing Resource Adaptor Entity links
      String[] entityNames = sleeContainer.getResourceAdaptorEntityNames();
      
      for( int i = 0; i < entityNames.length; i++ )
      {
        ResourceAdaptorEntity rae = sleeContainer.getResourceAdaptorEntity( entityNames[i] );
        
        String raTypeId = rae == null ? "NULL" : rae.getInstalledResourceAdaptor().getRaType().getResourceAdaptorTypeID().toString();
        
        newDeployedComponents.add( entityNames[i] + "_@_" + raTypeId );
      }

      // All good.. Make the temp the good one.
      deployedComponents = newDeployedComponents;
    }
    catch ( Exception e )
    {
      logger.warn( "Failure while updating deployed components.", e );
    }
  }

  /**
   * Method for installing a Deployable Unit into SLEE.
   * @param du the Deployable Unit to install.
   * @throws Exception
   */
  public void installDeployableUnit( DeployableUnit du ) throws Exception
  {
    // Check if the DU is ready to be installed
    if( du.isReadyToInstall( true ) )
    {
      // Get and Run the actions needed for installing this DU
      sciAction( du.getInstallActions() );
      
      // Set the DU as installed
      du.setInstalled( true );
      
      // Update the deployed components from SLEE
      updateDeployedComponents();
      
      // Go through the remaining DUs waiting for installation
      Iterator<DeployableUnit> duIt = waitingForInstallDUs.iterator();
      
      while( duIt.hasNext() )
      {
        DeployableUnit waitingDU = duIt.next();
        
        // If it is ready for installation, follow the same procedure
        if( waitingDU.isReadyToInstall( false ) )
        {
          // Get and Run the actions needed for installing this DU
          sciAction( waitingDU.getInstallActions() );
          
          // Set the DU as installed
          waitingDU.setInstalled( true );
          
          // Update the deployed components from SLEE
          updateDeployedComponents();

          // Remove the DU from the waiting list.
          waitingForInstallDUs.remove( waitingDU );
          
          // Let's start all over.. :)
          duIt = waitingForInstallDUs.iterator();
        }
      }
    }
    else
    {
      logger.info( "Unable to INSTALL " + du.getDeploymentInfoShortName() + " right now. Waiting for dependencies to be resolved." );
      
      // The DU can't be installed now, let's wait...
      waitingForInstallDUs.add( du );
    }
  }
  
  /**
   * Method for uninstalling a Deployable Unit into SLEE.
   * @param du the Deployable Unit to install.
   * @throws Exception
   */
  public void uninstallDeployableUnit( DeployableUnit du ) throws Exception
  {
    // It isn't installed?
    if( !du.isInstalled() )
    {
      // Then it should be in the waiting list... remove and we're done.
      waitingForInstallDUs.remove( du );
      
      logger.info( du.getDeploymentInfoShortName() + " wasn't deployed. Removing from waiting list." );
    }
    // Check if the DU is ready to be uninstalled
    else if( du.isReadyToUninstall() )
    { 
      // Get and Run the actions needed for uninstalling this DU
      sciAction( du.getUninstallActions() );
      
      // Set the DU as not installed
      du.setInstalled( false );
      
      // Update the deployed components from SLEE
      updateDeployedComponents();
      
      // Go through the remaining DUs waiting for uninstallation
      Iterator<DeployableUnit> duIt = waitingForUninstallDUs.iterator();
      
      while( duIt.hasNext() )
      {
        DeployableUnit waitingDU = duIt.next();
        
        // If it is ready for being uninstalled, follow the same procedure
        if( waitingDU.isReadyToUninstall() )
        {
          // Get and Run the actions needed for uninstalling this DU
          sciAction( waitingDU.getUninstallActions() );
          
          // Set the DU as not installed
          waitingDU.setInstalled( false );
          
          // Update the deployed components from SLEE
          updateDeployedComponents();
          
          // Remove the DU from the waiting list.
          waitingForUninstallDUs.remove( waitingDU );
          
          // Let's start all over.. :)
          duIt = waitingForUninstallDUs.iterator();
        }
      }
    }
    else
    {
      logger.info( "Unable to UNINSTALL " + du.getDeploymentInfoShortName() + " right now. Waiting for dependents to be removed." );
      
      // Add it to the waiting list.
      waitingForUninstallDUs.add( du );
    }
  }
  
  /**
   * Method for performing the actions needed for (un)deployment.
   * @param actions the array of strings containing the actions to perform.
   * @throws Exception
   */
  private void sciAction( Collection<Object[]> actions ) throws Exception
  {
    // Get the MBeanServer
    MBeanServer ms = SleeContainer.lookupFromJndi().getMBeanServer();

    // For each action, get the params..
    for( Object[] params : actions )
    {
      // The ObjectName for the MBean invocations
      ObjectName objectName = null;
      
      // The arguments and their signature
      Object[] arguments = new Object[params.length-1];;
      String[] signature = new String[params.length-1];
      
      // Get the action to perform
      String action = (String)params[0];

      // Get the parameters and the signature
      for( int i = 1; i < params.length; i++ )
      {
        arguments[i-1] = params[i];
        signature[i-1] = params[i].getClass().getName();
      }

      // Get the correct object name for the action
      if( deploymentActions.contains( action ) )
      {
        // Set the corresponding ObjectName
        objectName = new ObjectName("slee:name=DeploymentMBean");
        
        if(action.equals( "uninstall" ))
        {
          // Need to convert from file to DeployableUnitID
          DeployableUnitID  duID = (DeployableUnitID) ms.invoke( objectName, "getDeployableUnit", 
              new Object[]{params[1]}, new String[]{params[1].getClass().getName()} );
          
          // Update arguments and signature
          arguments[0] = duID;
          signature[0] = "javax.slee.management.DeployableUnitID";
        }
      }
      else if( resourceAdaptorActions.contains( action ) )
      {
        // Set the corresponding ObjectName
        objectName = new ObjectName("slee:name=ResourceManagementMBean");

        // Special case as arg is ResourceAdaptorIDImpl..
        if( action.equals( "createResourceAdaptorEntity" ) )
          signature[0] = "javax.slee.resource.ResourceAdaptorID";
      }
      else if( serviceActions.contains( action ) )
      {
        // Set the corresponding ObjectName
        objectName = new ObjectName("slee:name=ServiceManagementMBean");
        
        // Special case as arg is ServiceIDImpl..
        signature[0] = "javax.slee.ServiceID";
      }

      if( logger.isDebugEnabled() )
        logger.debug( "Invoking " + action + "(" + Arrays.toString( signature ) + ") on " + objectName );
      
      // Invoke it.
      ms.invoke( objectName, action, arguments, signature );
      
      // Wait a little while just to make sure it finishes
      Thread.sleep( waitTimeBetweenOperations );
    }
  }

  /**
   * Getter for the Deployed Components collection.
   * @return a Collection of Strings with the deployed components IDs.
   */
  public Collection<String> getDeployedComponents()
  {
    return deployedComponents;
  }
  
  /**
   * Method for showing current status of the Deployment Manager.
   * @return a HTML string with the status.
   */
  public String showStatus()
  {
    // Update the currently deployed components.
    updateDeployedComponents();
    
    String output = "";
    
    output += "<p>Deployable Units Waiting For Install:</p>";
    for( DeployableUnit waitingDU : waitingForInstallDUs )
    {
      output += "+-- " + waitingDU.getDeploymentInfoShortName() + "<br>";
      for( String dependency : waitingDU.getExternalDependencies() )
      {
        if( !deployedComponents.contains( dependency ) )
          dependency += " <strong>MISSING!</strong>";
        
        output += "  +-- depends on " + dependency + "<br>";
      }
    }
   
    output += "<p>Deployable Units Waiting For Uninstall:</p>";
    for( DeployableUnit waitingDU : waitingForUninstallDUs )
    {
      output += "+-- " + waitingDU.getDeploymentInfoShortName() + "<br>";
    }
   
    return output;
  }
}