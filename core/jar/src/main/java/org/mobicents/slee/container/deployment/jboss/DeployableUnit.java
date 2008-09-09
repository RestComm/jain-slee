package org.mobicents.slee.container.deployment.jboss;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.slee.ComponentID;
import javax.slee.resource.ResourceAdaptorID;

import org.jboss.deployment.DeploymentInfo;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentIDImpl;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class represents a SLEE Deployable Unit, represented by a collection of
 * Deployable Components. Contains all the DU dependencies, install/uninstall
 * actions needed for the DU and post-install and pre-uninstall actions.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public class DeployableUnit
{
  // The logger.
  private static Logger logger = Logger.getLogger( DeployableUnit.class );
  
  // The DeploymentInfo short name
  private String diShortName; 
  
  // The DeploymentInfo URL object
  private URL diURL;
  
  // The deployment manager in charge of this DU
  private DeploymentManager deploymentManager;
  
  // A collection of the Deployable Components in this DU
  private Collection<DeployableComponent> components = new ArrayList<DeployableComponent>();
  
  // A collection of the IDs of the components in this DU.
  private Collection<String> componentIDs = new ArrayList<String>();
  
  // A collection of the IDs of the components that this DU depends on. 
  private Collection<String> dependencies = new ArrayList<String>();
  
  // The install actions needed to install/activate this DU components.
  private Collection<Object[]> installActions = new ArrayList<Object[]>();

  // The post-install actions needed to install/activate this DU components.
  private HashMap<String, Collection<Object[]>> postInstallActions = new HashMap<String, Collection<Object[]>>();

  // The pre-uninstall actions needed to deactivate/uninstall this DU components.
  private HashMap<String, Collection<Object[]>> preUninstallActions = new HashMap<String, Collection<Object[]>>();

  // The install actions needed to deactivate/uninstall this DU components.
  private Collection<Object[]> uninstallActions = new ArrayList<Object[]>();

  // A flag indicating wether this DU is installed
  private boolean isInstalled = false;
  
  /**
   * Constructor.
   * @param duDeploymentInfo this DU deployment info.
   * @param deploymentManager the DeploymentManager in charge of this DU.
   * @throws Exception 
   */
  public DeployableUnit( DeploymentInfo duDeploymentInfo, DeploymentManager deploymentManager ) throws Exception
  {
    this.diShortName = duDeploymentInfo.shortName;
    this.diURL = duDeploymentInfo.url;
    
    this.deploymentManager = deploymentManager;
    
    // First action for the DU is always install.
    installActions.add( new Object[] {"install", duDeploymentInfo.url.toString()} );
    
    // Parse the deploy-config.xml to obtain post-install/pre-uninstall actions
    parseDeployConfig();
  }

  /**
   * Adder method for a Deployable Component.
   * @param dc the deployable component object.
   */
  public void addComponent( DeployableComponent dc )
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Adding Component " + dc.getComponentKey() );
    
    // Add the component ..
    components.add( dc );
    
    // .. the key ..
    componentIDs.add( dc.getComponentKey() );
    
    // .. the dependencies ..
    dependencies.addAll( dc.getDependencies() );
    
    // .. the install actions to be taken ..
    installActions.addAll( dc.getInstallActions() );
    
    // .. post-install actions (if any) ..
    Collection<Object[]> postInstallActionsStrings = postInstallActions.remove( dc.getComponentKey() );
    
    if( postInstallActionsStrings != null && postInstallActionsStrings.size() > 0 )
    {
      installActions.addAll( postInstallActionsStrings );
    }
    else if( dc.getComponentType() == DeployableComponent.RA_COMPONENT )
    {
      ComponentID cid = dc.getComponentID();
      
      String raID = dc.getComponentKey();
      
      logger.warn( "\r\n------------------------------------------------------------" +
          "\r\nNo RA Entity and Link config for " + raID + " found. Using default values!" +
          "\r\n------------------------------------------------------------" );

      String raName = raID.substring( raID.indexOf( '[' ) + 1, raID.indexOf( '#' ) );
      
      // Add the default Create and Activate RA Entity actions to the Install Actions
      installActions.add( new Object[] { "createResourceAdaptorEntity", cid, raName, new Properties() } );
      installActions.add( new Object[] { "activateResourceAdaptorEntity", raName } );
      
      // Create default link
      installActions.add( new Object[] { "bindLinkName", raName, raName } );
      
      // Remove default link
      uninstallActions.add( new Object[] { "unbindLinkName", raName } );
      
      // Add the default Deactivate and Remove RA Entity actions to the Uninstall Actions
      uninstallActions.add( new Object[] { "deactivateResourceAdaptorEntity", raName } );
      uninstallActions.add( new Object[] { "removeResourceAdaptorEntity", raName } );
    }
    
    // .. pre-uninstall actions (if any) ..
    Collection<Object[]> preUninstallActionsStrings = preUninstallActions.remove( dc.getComponentKey() );
    
    if( preUninstallActionsStrings != null )
      uninstallActions.addAll( preUninstallActionsStrings );
    
    // .. and finally the uninstall actions to the DU.
    uninstallActions.addAll( dc.getUninstallActions() );
  }
  
  /**
   * Method for checking if DU is self-sufficient.
   * @return true if the DU has no external dependencies.
   */
  public boolean isSelfSufficient()
  {
    // All dependencies in the DU components?
    return componentIDs.containsAll( dependencies );
  }
  
  /**
   * Method for obtaining the external dependencies for this DU, if any.
   * @return a Collection of external dependencies identifiers.
   */
  public Collection<String> getExternalDependencies()
  {
    // Take all dependencies...
    Collection<String> externalDependencies = dependencies;
    
    // Remove those which are contained in this DU
    externalDependencies.removeAll( componentIDs );
    
    // Return what's left.
    return externalDependencies;
  }
  
  /**
   * Method for checking if the DU has all the dependencies needed to be deployed.
   * @param showMissing param to set whether to show or not missing dependencies.
   * @return true if all the dependencies are satisfied.
   */
  public boolean hasDependenciesSatisfied( boolean showMissing )
  {
    // First of all check if it is self-sufficient
    if( isSelfSufficient() )
      return true;
    
    // If not self-sufficient, get the remaining dependencies
    Collection<String> externalDependencies = getExternalDependencies();
    
    // Remove those that are already installed...
    externalDependencies.removeAll( deploymentManager.getDeployedComponents() );
    
    // Some remaining?
    if( externalDependencies.size() > 0 )
    {
      if( showMissing )
      {
        // List them to the user...
        String missingDepList = "";
        
        for( String missingDep : externalDependencies )
          missingDepList += "\r\n +-- " + missingDep;
        
        logger.info( "Missing dependencies for " + this.diShortName + ":" + missingDepList );
      }
      
      // Return dependencies not satified.
      return false;
    }
    
    // OK, dependencies satisfied!
    return true;
  }
  
  /**
   * Method for checking if this DU contains any component that is already deployed.
   * @return true if there's a component that is already deployed.
   */
  public boolean hasDuplicates()
  {
    ArrayList<String> duplicates = new ArrayList<String>();
    
    // For each component in the DU ..
    for( String componentId : componentIDs )
    {
      // Check if it is already deployed
      if( deploymentManager.getDeployedComponents().contains( componentId ) )
      {
        duplicates.add( componentId );
      }
    }

    if(duplicates.size() > 0)
    {
      logger.warn("The deployable unit '" + this.diShortName + "' contains components that are already deployed. The following are already installed:");
      
      for(String dupComponent : duplicates)
        logger.warn( " - " + dupComponent );
      
      return true;
    }

    // If we got here, there's no dups.
    return false;
  }
  
  /**
   * Method for doing all the checking to make sure it is ready to be installed.
   * @param showMissing param to set whether to show or not missing dependencies.
   * @return true if all the pre-reqs are met.
   */
  public boolean isReadyToInstall( boolean showMissing )
  {
    // Check if the deps are satisfied and there are no dups.
    return hasDependenciesSatisfied( showMissing ) && !hasDuplicates();
  }

  /**
   * Getter for the Install Actions.
   * @return a Collection of actions.
   */
  public Collection<Object[]> getInstallActions()
  {
    ArrayList<Object[]> iActions = new ArrayList<Object[]>();
    
    // Let's check if we have some remaining install actions
    if(postInstallActions.values().size() > 0)
    {
      for(String componentId : postInstallActions.keySet())
      {
        iActions.addAll( postInstallActions.get( componentId ) );          
      }
    }
    
    iActions.addAll( installActions );
    
    return iActions;
  }

  /**
   * Getter for the Uninstall Actions.
   * @return a Collection of actions.
   */
  public Collection<Object[]> getUninstallActions()
  {
    Collection<Object[]> uActions = new ArrayList(uninstallActions);
    
    // Let's check if we have some remaining install actions
    if(preUninstallActions.values().size() > 0)
    {
      for(String componentId : preUninstallActions.keySet())
      {
        uActions.addAll( preUninstallActions.get( componentId ) );          
      }
    }
    
    // To make sure uninstall is the last action, we add it just when we return them.
    uActions.add( new Object[] {"uninstall", diURL.toString()} );
    
    return uActions;
  }

  /**
   * Getter for this DU components.
   * @return a Collection of component identifiers.
   */
  public Collection<String> getComponents()
  {
    return componentIDs;
  }

  /**
   * Method for doing all the checking to make sure it is ready to be uninstalled.
   * @return true if all the pre-reqs are met.
   * @throws Exception
   */
  public boolean isReadyToUninstall() throws Exception
  {
    // Check DU for readiness ..
    if( isInstalled && !hasReferringDU() )
    {
      // Check each DU for it's readiness also
      for( DeployableComponent dc : components )
      {
        if( !dc.isUndeployable( this ) )
          return false;
      }
    }
    else
    {
      return false;
    }
    
    // It's good to go.
    return true;
  }
  
  /**
   * Method for checking if this DU components are referred by any others.
   * @return true if there are other DUs installed referring this.
   * @throws Exception
   */
  private boolean hasReferringDU() throws Exception
  {
    // Get transaction manager
    SleeTransactionManager tm = SleeContainer.getTransactionManager();

    try
    {
      // Get SleeContainer instance from JNDI
      SleeContainer sC = SleeContainer.lookupFromJndi();

      // Beging transaction (needed)
      tm.begin();

      // Get this DU Descriptor
      DeployableUnitDescriptorImpl dudesc = (DeployableUnitDescriptorImpl) sC.getDeploymentManager().getDeployableUnitIDtoDescriptorMap().get( sC.getDeployableUnitIDFromUrl( diURL.toString() ) );

      // Get the Components IDs from the descriptor
      ComponentID[] cid = dudesc.getComponents();
      
      // For each component...
      for( int i = 0; i < cid.length; i++ )
      {
        // Get the DUs related to the component
        Set hset = (Set) sC.getDeploymentManager().getComponentIDToDeployableUnitIDMap().get( cid[i] );
        
        // If we get null, there's none.
        if( hset == null )
        {
          return false;
        }
        else if( hset.size() != 1 )
        {
          // See if there's another DU that refers directly to me.
          return dudesc.hasInstalledComponent( cid[i] );
        }
        else
        {
          // I'm the only one that references this component.
          ComponentID[] referringComponents = sC.getReferringComponents( cid[i] );
          
          // See if there's any other component that refers to me that belongs to another DU.
          if( referringComponents != null )
          {
            for( int k = 0; k < referringComponents.length; k++ )
            {
              if( !( (Set) sC.getDeploymentManager().getComponentIDToDeployableUnitIDMap().get( referringComponents[k] ) ).contains( dudesc.getDeployableUnit() ) )
              {
                return true;
              }
            }
          }
        }
      }
      
      // If we got here
      return false;
    }
    catch ( Exception ex )
    {
      tm.setRollbackOnly();
      throw ex;
    }
    finally
    {
      tm.commit();
    }
  }

  /**
   * Getter for the DeploymentInfo short name
   * @return a String containing the filename
   */
  public String getDeploymentInfoShortName()
  {
    return this.diShortName;
  }

  /**
   * Setter for the isInstalled flag.
   * @return a boolean indicating if the DU is already installed.
   */
  public boolean isInstalled()
  {
    return isInstalled;
  }
  
  /**
   * Setter for the isInstalled flag.
   * @param isInstalled the isInstalled flag indicating that the DU is already installed.
   */
  public void setInstalled( boolean isInstalled )
  {
    this.isInstalled = isInstalled;
  }
  
  /**
   * Parser for the deployment config xml.
   * @throws Exception
   */
  private void parseDeployConfig() throws Exception
  {
    JarFile componentJarFile = null;
    
    InputStream is = null;
    
    try
    {
      // Create a JarFile object
      componentJarFile = new JarFile( diURL.toString().replaceAll( "file:", "" ) );
      
      // Get the JarEntry for the deploy-config.xml
      JarEntry deployInfoXML = componentJarFile.getJarEntry( "META-INF/deploy-config.xml" );
        
      // If it exists, set an Input Stream on it 
      is = deployInfoXML != null ? componentJarFile.getInputStream( deployInfoXML ) : null;
  
      if( is != null )
      {
        // Read the file into a Document
        Document doc = XMLUtils.parseDocument( is, true );
        
        // By now we only care about <ra-entitu> nodes
        NodeList raEntities = doc.getElementsByTagName( "ra-entity" );
        
        // The RA identifier
        String raId = null;
        
        // The collection of Post-Install Actions
        Collection<Object[]> cPostInstallActions = new ArrayList<Object[]>();
        
        // The collection of Pre-Uninstall Actions
        Collection<Object[]> cPreUninstallActions = new ArrayList<Object[]>();
        
        // Iterate through each ra-entity node
        for( int i = 0; i < raEntities.getLength(); i++ )
        {
          Element raEntity = (Element) raEntities.item(i);
          
          // Get the component ID
          raId = ComponentIDImpl.RESOURCE_ADAPTOR_ID + "[" + raEntity.getAttribute( "resource-adaptor-id" ) + "]";
          
          // The RA Entity Name
          String entityName = raEntity.getAttribute( "entity-name" );
          
          // Select the properties node
          NodeList propsNodeList = raEntity.getElementsByTagName("properties");
          
          if( propsNodeList.getLength() > 1 )
            logger.warn( "Invalid ra-entity element, has more than one properties child. Reading only first." );
          
          Element propsNode = (Element) propsNodeList.item(0);
          
          // The properties for this RA
          Properties props = new Properties(); 
            
          // Do we have any properties at all?
          if( propsNode != null )
          {
            String propsFilename;
            
            // Do we have a properties file to load?
            if( ( propsFilename = ((Element)propsNode).getAttribute( "file" ) ) != null && !propsFilename.equals( "" ) )
            {
              // Get the entry from the jar
              JarEntry propsFile = componentJarFile.getJarEntry( "META-INF/" + propsFilename );
              
              // Load it.
              props.load( componentJarFile.getInputStream( propsFile ) );
            }
            
            // Select the property elements
            NodeList propsList = propsNode.getElementsByTagName( "property" );
            
            // For each element, add it to the Properties object
            for( int j = 0; j < propsList.getLength(); j++ )
            {
              Element property = (Element) propsList.item(j);
              
              // If the property already exists, it will be overwritten.
              props.put( property.getAttribute( "name" ), property.getAttribute( "value" ) );
            }
          }
          
          // Create the Resource Adaptor ID
          ResourceAdaptorID componentID = new ResourceAdaptorIDImpl(new ComponentKey( raEntity.getAttribute( "resource-adaptor-id" )));
          
          // Add the Create and Activate RA Entity actions to the Post-Install Actions
          cPostInstallActions.add( new Object[] { "createResourceAdaptorEntity", componentID, entityName, props } );
          cPostInstallActions.add( new Object[] { "activateResourceAdaptorEntity", entityName } );
          
          // Each RA might have zero or more links.. get them
          NodeList links = raEntity.getElementsByTagName( "ra-link" );
          
          for( int j = 0; j < links.getLength(); j++ )
          {
            String linkName = ((Element) links.item(j)).getAttribute( "name" );
            
            cPostInstallActions.add( new Object[] { "bindLinkName", entityName, linkName } );
            
            cPreUninstallActions.add( new Object[] { "unbindLinkName", linkName } );
            
            HashMap existingRATypes = SleeContainer.lookupFromJndi().getResourceAdaptorTypes();
            
            String raTypeFromRa = null;
            
            for(Object raTypeObject : existingRATypes.values())
            {
              ResourceAdaptorType raType = (ResourceAdaptorType)raTypeObject;

              if( raType.getResourceAdaptorIDs().contains( componentID ) )
                raTypeFromRa = raType.getResourceAdaptorTypeID().toString();
            }
            
            if(raTypeFromRa != null)
              this.componentIDs.add( linkName + "_@_" + raTypeFromRa );
          }
          
          // Add the Deactivate and Remove RA Entity actions to the Pre-Uninstall Actions
          cPreUninstallActions.add( new Object[] { "deactivateResourceAdaptorEntity", entityName } );
          cPreUninstallActions.add( new Object[] { "removeResourceAdaptorEntity", entityName } );
          
          // Finally add the actions to the respective hashmap.
          if( raId != null )
          {
            // We need to check if we are updating or adding new ones.
            if(postInstallActions.containsKey( raId ))
              postInstallActions.get( raId ).addAll( cPostInstallActions );
            else
              postInstallActions.put( raId, cPostInstallActions );  
            
            // Same here...
            if(preUninstallActions.containsKey( raId ))
              preUninstallActions.get( raId ).addAll( cPreUninstallActions );
            else
              preUninstallActions.put( raId, cPreUninstallActions );
          }
          
          // Now we clean the lists for the next round (might come a new RA ID)...
          cPostInstallActions = new ArrayList<Object[]>();
          cPreUninstallActions = new ArrayList<Object[]>();
          
          raId = null;
          
        }
      }
    }
    finally
    {
      // Clean depoy-config.xml inputstream
      try
      {
        if(is != null)
          is.close();
      }
      finally
      {
        is = null;
      }
      
      // Clean jar input streams
      try
      {
        if(componentJarFile != null)
          componentJarFile.close();
      }
      finally
      {
        componentJarFile = null;
      }
    }
  }
}