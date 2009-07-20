package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorTypeBinding;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;

/**
 * This class represents a SLEE Deployable Component such as a Profile Specification,
 * Event Type Definition, RA Type, Resource Adaptor, SBB or Service. It also contains
 * the dependencies and the install/uninstall actions needed for that component.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public class DeployableComponent
{
  // The logger.
  private static Logger logger = Logger.getLogger( DeployableComponent.class );
  
  public final static int PROFILESPEC_COMPONENT = 1;
  public final static int EVENTTYPE_COMPONENT   = 2;
  public final static int RATYPE_COMPONENT      = 3;
  public final static int RA_COMPONENT          = 4;
  public final static int SBB_COMPONENT         = 5;
  public final static int SERVICE_COMPONENT     = 6;
  public final static int LIBRARY_COMPONENT     = 7;
  
  // The DeploymentInfo short name
  private String diShortName; 
  
  // The DeploymentInfo URL object
  private URL diURL;
  
  // The ID of the component.
  private ComponentID componentID;
  
  // The dependencies for this Component.
  private Collection<String> dependencies = new ArrayList<String>();
  
  // The actions needed to perform installation for this component.
  private Collection<Object[]> installActions = new ArrayList<Object[]>();
  
  // The actions needed to perform installation for this component.
  private Collection<Object[]> uninstallActions = new ArrayList<Object[]>();
  
  // The key identifying the component (type[name#vendor#version]).
  private String componentKey;
  
  // An indicator for the component type.
  private int componentType = -1;
  
  private DeployableUnitWrapper duWrapper;
  
  // The components inside this component
  private Collection<DeployableComponent> subComponents = new ArrayList<DeployableComponent>();
  
  /**
   * Private constructor for the sub-components.
   * @param dc the base DeployableComponent
   * @throws Exception
   */
  private DeployableComponent( DeployableComponent dc ) throws Exception
  {
    this.diShortName = dc.diShortName;
    this.diURL = dc.diURL;
    
    // We want no sub-sub-components...
    this.subComponents = null;
  }
  
  /**
   * Constructor.
   * @param di the DeploymentInfo for this component.
   * @throws Exception
   */
  public DeployableComponent( URL url, String fileName ) throws Exception
  {
    this.diShortName = fileName;
    this.diURL = url;
    
    // Parse the component descriptor to obtain dependencies.
    this.subComponents = parseDescriptor();
  }

  public DeployableComponent( DeployableUnitWrapper duWrapper, URL url, String fileName ) throws Exception
  {
    this.duWrapper = duWrapper;
    this.diShortName = fileName;
    this.diURL = url;
    
    // Parse the component descriptor to obtain dependencies.
    this.subComponents = parseDescriptor();
  }

  /**
   * Getter for this component dependencies.
   * @return a Collection<String> with dependencies IDs.
   */
  public Collection<String> getDependencies()
  {
    return this.dependencies;
  }
  

  /**
   * Method for checking if the component is deployable, ie, meets the pre-reqs.
   * @param deployedComponents a Collection<String> with the IDs of the already deployed components.
   * @return true if it can be deployed.
   */
  public boolean isDeployable(Collection<String> deployedComponents)
  {
    return deployedComponents.containsAll( dependencies );
  }
  
  /**
   * Getter for the Component Key String.
   * @return a String with the component key (type[name#vendor#version]).
   */
  public String getComponentKey()
  {
    return this.componentKey;
  }
  
  /**
   * Parser for the deployment descriptor. Minimal version obtained from Container.
   * @return a String containing the Component ID. 
   * @throws IOException
   */
  private Collection<DeployableComponent> parseDescriptor() throws IOException
  {
    if( logger.isTraceEnabled() )
      logger.trace( "Parsing Descriptor for " + this.diURL.toString() );
    
    Collection<DeployableComponent> deployableComponents = new ArrayList<DeployableComponent>();
    
    // Special case for the services...
    if( this.diShortName.endsWith( ".xml" ) )
    {
      if( logger.isTraceEnabled() )
        logger.trace( "Parsing Service Descriptor." );
      
      InputStream is = null;
      
      try
      {
        is = diURL.openStream();
        
        ServiceDescriptorFactory sdf = new ServiceDescriptorFactory();
        List<ServiceDescriptorImpl> serviceDescriptors = sdf.parse( is );
        
        for(ServiceDescriptorImpl sd : serviceDescriptors)
        {
          DeployableComponent dc = new DeployableComponent( this );
          
          dc.componentID = sd.getServiceID();
          
          dc.componentKey = getComponentIdAsString( dc.componentID );
          
          dc.componentType = SERVICE_COMPONENT;
          
          if( logger.isTraceEnabled() )
            logger.trace( "Component ID: " + dc.componentKey );

          String rootSbb = getComponentIdAsString( sd.getRootSbbID() );

          if( logger.isTraceEnabled() )
          {
            logger.trace( "------------------------------ Dependencies ------------------------------" );
            logger.trace( rootSbb );
            logger.trace( "--------------------------- End of Dependencies --------------------------" );
          }

          dc.dependencies.add( rootSbb );
          
          dc.installActions.add( new Object[] {"activate", dc.componentID} );

          dc.uninstallActions.add( new Object[] {"deactivate", dc.componentID} );
          
          deployableComponents.add( dc );
        }

        return deployableComponents;
      }
      catch ( Exception e )
      {
        logger.error( "", e );
        return null;
      }
      finally
      {
        // Clean up!
        if( is != null )
        {
          try
          {
            is.close();
          }
          finally
          {
            is = null;
          }
        }
      }
    }
    
    try
    {
      URL descriptorXML = null;
      
      // Determine whether the type of this instance is an sbb, event, RA type, etc.
      if( ( descriptorXML = duWrapper.getEntry( "META-INF/sbb-jar.xml" ) ) != null )
      {
        if( logger.isTraceEnabled() )
          logger.trace( "Parsing SBB Descriptor." );
  
        InputStream is = null;
        
        try
        {
          is = descriptorXML.openStream();
          
          // Parse the descriptor using the factory
          SbbDescriptorFactory sbbdf = new SbbDescriptorFactory();
          List<SbbDescriptorImpl> sbbDescriptors = sbbdf.parse( is );
  
          if( sbbDescriptors.size() == 0 )
          {
            logger.warn( "The " + duWrapper.getFileName() + " deployment descriptor contains no sbb definitions" );
            return null;
          }
  
          for( SbbDescriptorImpl sbbDescriptor : sbbDescriptors)
          {
            DeployableComponent dc = new DeployableComponent( this );
            
            dc.componentType = SBB_COMPONENT;
                      
            // Get the Component ID
            dc.componentID = sbbDescriptor.getSbbID();
  
            // Get the Component Key
            dc.componentKey = getComponentIdAsString( dc.componentID );
  
            if( logger.isTraceEnabled() )
            {
              logger.trace( "Component ID: " + dc.componentKey );
  
              logger.trace( "------------------------------ Dependencies ------------------------------" );
            }
            
            // Get the set of this sbb dependencies
            Set<ComponentID> sbbDependencies = sbbDescriptor.getDependenciesSet();
            
            // Iterate through dependencies set
            for( ComponentID dependencyId : sbbDependencies )
            {
              // Add the dependency
              dc.dependencies.add( getComponentIdAsString( dependencyId ) );
              
              if( logger.isTraceEnabled() )
              {
                logger.trace( getComponentIdAsString( dependencyId ) );
              }
            }
            
            // FIXME: This is special case for Links. Maybe it should be treated in SbbDescriptorImpl?
            for(MResourceAdaptorTypeBinding raTypeBinding : sbbDescriptor.getResourceAdaptorTypeBindings())
            {
              for(MResourceAdaptorEntityBinding raEntityBinding : raTypeBinding.getResourceAdaptorEntityBinding())
              {
                String raLink = raEntityBinding.getResourceAdaptorEntityLink() + "_@_" + getComponentIdAsString( raTypeBinding.getResourceAdaptorTypeRef() );
                
                // Add the dependency
                dc.dependencies.add( raLink );
                
                if( logger.isTraceEnabled() )
                  logger.trace( raLink );
              }
            }

            if( logger.isTraceEnabled() )
              logger.trace( "--------------------------- End of Dependencies --------------------------" );
            
            deployableComponents.add( dc );
          }
        }
        catch ( Exception e )
        {
          logger.error( "", e );
        }
        finally
        {
          // Clean up!
          if( is != null )
          {
            try
            {
              is.close();
            }
            finally
            {
              is = null;
            }
          }
        }
      }
      else if( ( descriptorXML = duWrapper.getEntry( "META-INF/profile-spec-jar.xml" ) ) != null )
      {
        if( logger.isTraceEnabled() )
          logger.trace( "Parsing Profile Specification Descriptor." );
  
        InputStream is = null;
        
        try
        {
          // Get the InputStream
          is = descriptorXML.openStream();
          
          // Parse the descriptor using the factory
          ProfileSpecificationDescriptorFactory psdf = new ProfileSpecificationDescriptorFactory();
          List<ProfileSpecificationDescriptorImpl> psDescriptors = psdf.parse( is );

          // Get a list of the profile specifications in the deployable unit.
          if( psDescriptors.size() == 0 )
          {
            logger.warn( "The " + duWrapper.getFileName() + " deployment descriptor contains no profile-spec definitions" );
            return null;
          }
  
          // Iterate through the profile spec nodes
          for( ProfileSpecificationDescriptorImpl psDescriptor : psDescriptors )
          {
            DeployableComponent dc = new DeployableComponent( this );
            
            // Set Component Type
            dc.componentType = PROFILESPEC_COMPONENT;
            
            // Get the Component ID
            dc.componentID = psDescriptor.getProfileSpecificationID();
            
            // Get the Component Key
            dc.componentKey = getComponentIdAsString(dc.componentID);
  
            if( logger.isTraceEnabled() )
            {
              logger.trace( "Component ID: " + dc.componentKey );
  
              logger.trace( "------------------------------ Dependencies ------------------------------" );
            }
            
            // Get the set of this sbb dependencies
            Set<ComponentID> psDependencies = psDescriptor.getDependenciesSet();
            
            // Iterate through dependencies set
            for( ComponentID dependencyId : psDependencies )
            {
              // Add the dependency
              dc.dependencies.add( getComponentIdAsString( dependencyId ) );
              
              if( logger.isTraceEnabled() )
                logger.trace( getComponentIdAsString( dependencyId ) );
            }

            if( logger.isTraceEnabled() )
              logger.trace( "--------------------------- End of Dependencies --------------------------" );
            
            deployableComponents.add( dc );
          }
        }
        catch ( Exception e )
        {
          logger.error( "", e );
        }
        finally
        {
          // Clean up!
          if( is != null )
          {
            try
            {
              is.close();
            }
            finally
            {
              is = null;
            }
          }
        }
      }
      else if( ( descriptorXML = duWrapper.getEntry( "META-INF/event-jar.xml" ) ) != null )
      {
        if( logger.isTraceEnabled() )
          logger.trace( "Parsing Event Definition Descriptor." );
  
        InputStream is = null;
        
        try
        {
          // Get the InputStream
          is = descriptorXML.openStream();
  
          // Parse the descriptor using the factory
          EventTypeDescriptorFactory etdf = new EventTypeDescriptorFactory();
          List<EventTypeDescriptorImpl> etDescriptors = etdf.parse( is );
  
          if( etDescriptors == null || etDescriptors.size() == 0 )
          {
            logger.warn( "The " + duWrapper.getFileName() + " deployment descriptor contains no event-type definitions" );
            return null;
          }

          for( EventTypeDescriptorImpl etDescriptor : etDescriptors )
          {
            DeployableComponent dc = new DeployableComponent( this );
            
            // Set Component Type
            dc.componentType = EVENTTYPE_COMPONENT;
            
            // Get the Component ID
            dc.componentID = etDescriptor.getEventTypeID();
            
            // Get the Component Key
            // FIXME: Does the ComponentID still comes with the , ## ? dc.componentKey = dc.componentID.toString().substring( 0, dc.componentID.toString().indexOf( ',' ) );
            dc.componentKey = getComponentIdAsString( dc.componentID );

            if( logger.isTraceEnabled() )
            {
              logger.trace( "Component ID: " + dc.componentKey );
  
              logger.trace( "------------------------------ Dependencies ------------------------------" );
            }
            
            // Get the set of this sbb dependencies
            Set<ComponentID> etDependencies = etDescriptor.getDependenciesSet();
            
            // Iterate through dependencies set
            for( ComponentID dependencyId : etDependencies )
            {
              // Add the dependency
              dc.dependencies.add( getComponentIdAsString( dependencyId ) );
              
              if( logger.isTraceEnabled() )
                logger.trace( getComponentIdAsString( dependencyId ) );
            }

            if( logger.isTraceEnabled() )
              logger.trace( "--------------------------- End of Dependencies --------------------------" );

            deployableComponents.add( dc );
          }
  
        }
        catch ( Exception e )
        {
          logger.error( "", e );
        }
        finally
        {
          // Clean up!
          if( is != null )
          {
            try
            {
              is.close();
            }
            finally
            {
              is = null;
            }
          }
        }
      }
      else if( ( descriptorXML = duWrapper.getEntry( "META-INF/resource-adaptor-type-jar.xml" ) ) != null )
      {
        if( logger.isTraceEnabled() )
          logger.trace( "Parsing Resource Adaptor Type Descriptor." );
  
        InputStream is = null;
        
        try
        {
          // Get the InputStream
          is = descriptorXML.openStream();
  
          // Parse the descriptor using the factory
          ResourceAdaptorTypeDescriptorFactory ratdf = new ResourceAdaptorTypeDescriptorFactory();
          List<ResourceAdaptorTypeDescriptorImpl> ratDescriptors = ratdf.parse( is );
  
          if( ratDescriptors == null || ratDescriptors.size() == 0 )
          {
            logger.warn( "The " + duWrapper.getFileName() + " deployment descriptor contains no resource-adaptor-type definitions" );
            return null;
          }

          // Go through all the Resource Adaptor Type Elements          
          for( ResourceAdaptorTypeDescriptorImpl ratDescriptor : ratDescriptors )
          {
            DeployableComponent dc = new DeployableComponent( this );
            
            // Set Component Type
            dc.componentType = RATYPE_COMPONENT;        

            // Get the Component ID
            dc.componentID = ratDescriptor.getResourceAdaptorTypeID();
   
            // Get the Component Key
            dc.componentKey = getComponentIdAsString(dc.componentID);

            if( logger.isTraceEnabled() )
            {
              logger.trace( "Component ID: " + dc.componentKey );
  
              logger.trace( "------------------------------ Dependencies ------------------------------" );
            }
            
            // Get the set of this sbb dependencies
            Set<ComponentID> ratDependencies = ratDescriptor.getDependenciesSet();
            
            // Iterate through dependencies set
            for( ComponentID dependencyId : ratDependencies )
            {
              // Add the dependency
              dc.dependencies.add( getComponentIdAsString( dependencyId ) );
              
              if( logger.isTraceEnabled() )
                logger.trace( getComponentIdAsString( dependencyId ) );
            }

            if( logger.isTraceEnabled() )
              logger.trace( "--------------------------- End of Dependencies --------------------------" );
            
            deployableComponents.add( dc );
          }
        }
        catch ( Exception e )
        {
          logger.error( "", e );
        }
        finally
        {
          // Clean up!
          if( is != null )
          {
            try
            {
              is.close();
            }
            finally
            {
              is = null;
            }
          }
        }
      }
      else if( ( descriptorXML = duWrapper.getEntry( "META-INF/resource-adaptor-jar.xml" ) ) != null )
      {
        if( logger.isTraceEnabled() )
          logger.trace( "Parsing Resource Adaptor Descriptor." );
  
        InputStream is = null;
        
        try
        {
          // Get the InputStream
          is = descriptorXML.openStream();
  
          // Parse the descriptor using the factory
          ResourceAdaptorDescriptorFactory radf = new ResourceAdaptorDescriptorFactory();
          List<ResourceAdaptorDescriptorImpl> raDescriptors = radf.parse( is );
  
          // Go through all the Resource Adaptor Elements
          for( ResourceAdaptorDescriptorImpl raDescriptor : raDescriptors )
          {
            DeployableComponent dc = new DeployableComponent( this );
            
            // Set Component Type
            dc.componentType = RA_COMPONENT;
            
            // Set the Component ID
            dc.componentID = raDescriptor.getResourceAdaptorID();
            
            // Set the Component Key
            dc.componentKey = getComponentIdAsString(dc.componentID);
  
            if( logger.isTraceEnabled() )
            {
              logger.trace( "Component ID: " + dc.componentKey );
  
              logger.trace( "------------------------------ Dependencies ------------------------------" );
            }
            
            // Get the set of this sbb dependencies
            Set<ComponentID> raDependencies = raDescriptor.getDependenciesSet();
            
            // Iterate through dependencies set
            for( ComponentID dependencyId : raDependencies )
            {
              // Add the dependency
              dc.dependencies.add( getComponentIdAsString( dependencyId ) );
              
              if( logger.isTraceEnabled() )
                logger.trace( getComponentIdAsString( dependencyId ) );
            }

            if( logger.isTraceEnabled() )
              logger.trace( "--------------------------- End of Dependencies --------------------------" );
  
            deployableComponents.add( dc );
          }
        }
        catch ( Exception e )
        {
          logger.error( "", e );
        }
        finally
        {
          // Clean up!
          if( is != null )
          {
            try
            {
              is.close();
            }
            finally
            {
              is = null;
            }
          }
        }
      }
      else if( ( descriptorXML = duWrapper.getEntry( "META-INF/library-jar.xml" ) ) != null )
      {
        if( logger.isTraceEnabled() )
          logger.trace( "Parsing Library Descriptor." );
  
        InputStream is = null;
        
        try
        {
          // Get the InputStream
          is = descriptorXML.openStream();
  
          // Parse the descriptor using the factory
          LibraryDescriptorFactory ldf = new LibraryDescriptorFactory();
          List<LibraryDescriptorImpl> libraryDescriptors = ldf.parse( is );
  
          // Go through all the Resource Adaptor Elements
          for( LibraryDescriptorImpl libraryDescriptor : libraryDescriptors )
          {
            DeployableComponent dc = new DeployableComponent( this );
            
            // Set Component Type
            dc.componentType = LIBRARY_COMPONENT;
            
            // Set the Component ID
            dc.componentID = libraryDescriptor.getLibraryID();
            
            // Set the Component Key
            dc.componentKey = getComponentIdAsString(dc.componentID);
  
            if( logger.isTraceEnabled() )
            {
              logger.trace( "Component ID: " + dc.componentKey );
  
              logger.trace( "------------------------------ Dependencies ------------------------------" );
            }
            
            // Get the set of this sbb dependencies
            Set<ComponentID> libraryDependencies = libraryDescriptor.getDependenciesSet();
            
            // Iterate through dependencies set
            for( ComponentID dependencyId : libraryDependencies )
            {
              // Add the dependency
              dc.dependencies.add( getComponentIdAsString( dependencyId ) );
              
              if( logger.isTraceEnabled() )
                logger.trace( getComponentIdAsString( dependencyId ) );
            }

            if( logger.isTraceEnabled() )
              logger.trace( "--------------------------- End of Dependencies --------------------------" );
  
            deployableComponents.add( dc );
          }
        }
        catch ( Exception e )
        {
          logger.error( "", e );
        }
        finally
        {
          // Clean up!
          if( is != null )
          {
            try
            {
              is.close();
            }
            finally
            {
              is = null;
            }
          }
        }
      }
      else
      {
        logger.warn( "\r\n--------------------------------------------------------------------------------\r\n"
            + "No Component Descriptor found in '" + duWrapper.getFileName() + "'.\r\n"
            + "--------------------------------------------------------------------------------");
        
        return new ArrayList<DeployableComponent>();
      }
    }
    finally
    {
    }

    return deployableComponents;
  }

  /**
   * Getter for Install Actions.
   * @return a Collection of Object[] with the actions needed to install this component.
   */
  public Collection<Object[]> getInstallActions()
  {
    return installActions;
  }

  /**
   * Getter for Uninstall Actions.
   * @return a Collection of Object[] with the actions needed to uninstall this component.
   */
  public Collection<Object[]> getUninstallActions()
  {
    return uninstallActions;
  }

  /**
   * Getter for component type.
   * @return an int identifying the component type.
   */
  public int getComponentType()
  {
    return componentType;
  }

  /**
   * Getter for component id.
   * @return the ComponentID for the component.
   */
  public ComponentID getComponentID()
  {
    return componentID;
  }
  
  /**
   * Getter for the sub components.
   * @return Collection of DeployableComponents
   */
  public Collection<DeployableComponent> getSubComponents()
  {
    return this.subComponents;
  }

  /**
   * Perform extra verifications for undeployment.
   * @param du the containing Deployable Unit
   * @return true if the the component can be removed.
   */
  public boolean isUndeployable(DeployableUnit du)
  {
    boolean result = false;

    switch( componentType )
    {
    case PROFILESPEC_COMPONENT:
      result = true;
      break;
    case EVENTTYPE_COMPONENT:
      result = true;
      break;
    case RATYPE_COMPONENT:
      // FIXME: Check if this is still needed in SLEE 2.x
      //SleeContainer sC = SleeContainer.lookupFromJndi();
      // Get the DU componennts
      //Collection<String> duComponents = du.getComponents();
      
      // Obtaining the RAs using this RA Type 
      //HashSet<ResourceAdaptorIDImpl> raIDs = sC.getResourceManagement().getResourceAdaptorType( (ResourceAdaptorTypeID) componentID ).getResourceAdaptorIDs();

      // Check if the referring RAs are in the same DU
      //for( ResourceAdaptorIDImpl raID : raIDs )
      //  if( !duComponents.contains( raID.getAsText() ) )
      //    return false;

      // All aboard! Move on..
      result = true;
      break;
    case RA_COMPONENT:
      result = true;
      break;
    case SBB_COMPONENT:
      result = true;
      break;
    case SERVICE_COMPONENT:
      result = true;
      break;
    case LIBRARY_COMPONENT:
      result = true;
      break;
    }

    return result;
  }
  
  private String getComponentIdAsString(ComponentID componentId)
  {
    ComponentIDPropertyEditor cidPropertyEditor = new ComponentIDPropertyEditor();
    cidPropertyEditor.setValue( componentId );

    return cidPropertyEditor.getAsText();
   
  }
}