package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.SubDeployerSupport;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This is the Deployer main class where the AS will invoke the lifecycle methods
 * for new deployments.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
public class SLEESubDeployer extends SubDeployerSupport implements SLEESubDeployerMBean, NotificationListener
{
  // Constants -----------------------------------------------------

  // The suffixes to be accepted by this deployer (other than jars).
  public static final String DEPLOYMENT_EXTENSION = ".mar";

  public static final String BASE_SCRIPT_OBJECT_NAME = "jboss.scripts:type=BeanShell";

  // The Logger.
  private static Logger logger = Logger.getLogger( SLEESubDeployer.class );

  private static Timer timer = new Timer();
  
  // Attributes ----------------------------------------------------
  
  // The manager instance.
  private DeploymentManager dm = new DeploymentManager();

  // The DIs to be accepted by this deployer.
  private ConcurrentHashMap<String, DeploymentInfo> toAccept = new ConcurrentHashMap<String, DeploymentInfo>();

  // Deployable Units present.
  private ConcurrentHashMap<String, DeployableUnit> deployableUnits = new ConcurrentHashMap<String, DeployableUnit>();
  
  private boolean isNotificationEnabled = false;
  
  private boolean isServerShuttingDown = false;
  
  // Constructors -------------------------------------------------------------

  /**
   * Default Constructor.
   */
  public SLEESubDeployer()
  {
    super();
    
    setSuffixes( new String[] { DEPLOYMENT_EXTENSION, "xml", "jar" } );
    
    logger.info( "##### SLEE Deployer Initialized. #####" );
  }

  // SubDeployerSupport overrides ---------------------------------------------

  /**
   * Method for processing nested deployments. 
   */
  @Override
  protected void processNestedDeployments( DeploymentInfo di ) throws DeploymentException
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method processNestedDeployments called for " + di.url );
    
    // We'd like some to pass (mar with DUs inside.. we'll check it at accept).
    super.processNestedDeployments( di );
  }

  /**
   * Method for defining what is and what is not deployable.
   * We use it because otherwise we could lose service xmls.
   */
  protected boolean isDeployable(String name, URL url)
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method isDeployable called for [" + name + "] " + url );
    
    boolean isServiceXML = false;
    
    // Service XML should be an xml and not in the META-INF folder...
    if( name.endsWith( ".xml" ) && !name.contains( "META-INF/" ) )
    {
      InputStream is = null;
      
      try
      {
        // Get the stream
        is = url.openStream();
        
        // Parse the XML
        Document doc = XMLUtils.parseDocument( is, true );

        // Is the root element a <service-xml>
        isServiceXML = doc.getDocumentElement().getNodeName().equals( "service-xml" );
      }
      catch ( Exception ignore )
      {
        // ignore... wouldn't be a (good) service xml anyway.
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
          catch ( IOException ignore )
          {
            // Do nothing
          }
          finally
          {
            is = null;
          }
        }
      }
    }

    // Default isDeopoyable or special for service xml
    return super.isDeployable(name, url) || isServiceXML;
  }  
  
  /**
   * Method for deciding whether or not to accept the file.
   */
  @Override
  public boolean accepts( DeploymentInfo di )
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method accepts called for " + di.url );
    
    try
    {
      // Handle directory deployments
      if( di.isDirectory )
      {
        // We only care about MAR directories... for now.
        if( di.shortName.endsWith( DEPLOYMENT_EXTENSION ) )
        {
          
          if( logger.isDebugEnabled() )
            logger.debug( "Accepting MAR DIRECTORY " + di.url.toString() + "." );
        
          return true;
        }
      }
      // Is it in the toAccept list or is a MAR ? Direct accept.
      else if( toAccept.containsKey( di.shortName ) || di.shortName.endsWith( DEPLOYMENT_EXTENSION ) )
      {
        if( logger.isDebugEnabled() )
          logger.debug( "Accepting " + di.url.toString() + "." );

        return true;
      }
      // If not it the accept list but it's a jar might be a DU jar...
      else if( di.shortName.endsWith( ".jar" ) )
      {
        JarFile duJarFile = null; 
        
        try
        {
          // Obtain the reference to the file
          duJarFile = new JarFile( di.url.toString().replaceFirst( "file:", "" ) );
          
          // Try to obtain the DU descriptor...
          JarEntry duXmlEntry = duJarFile.getJarEntry( "META-INF/deployable-unit.xml" );
          
          // If we got it, we're accepting it!
          if( duXmlEntry != null )
          {
            if( logger.isDebugEnabled() )
              logger.debug( "Accepting " + di.url.toString() + "." );
  
            return true;
          }
        }
        finally
        {
          // Clean up!
          if( duJarFile != null )
          {
            try
            {
              duJarFile.close();
            }
            catch ( IOException ignore )
            {
            }
            finally
            {
              duJarFile = null;
            }
          }
        }
      }
    }
    catch ( Exception ignore )
    {
      // Ignore.. will reject.
    }
    
    // Uh-oh.. looks like it will stay outside.
    return false;
  }

  /**
   * Initializer method for accepted files. Will parse descriptors at this point.
   */
  @Override
  public void init( DeploymentInfo di ) throws DeploymentException
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method init called for " + di.url );

    if(server != null && !isNotificationEnabled)
    {
      try
      {
        server.addNotificationListener( new ObjectName("jboss.system:type=Server"), this, null, "" );
        isNotificationEnabled = true;
      }
      catch ( Exception e )
      {
        logger.error( "Failed to register notification listener.", e );
      }
    }
    
    try
    {
      // Get the filename for this di
      String fileName = di.shortName;
      
      DeploymentInfo duDeploymentInfo = null;
      
      // If we're able to remove it from toAccept was because it was there!
      if( (duDeploymentInfo = toAccept.remove( di.shortName )) != null )
      {
        // Create a new Deployable Component from this DI.
        DeployableComponent dc = new DeployableComponent( di ); 
        
        // Also get the deployable unit for this (it exists, we've checked!)
        DeployableUnit deployerDU = deployableUnits.get( duDeploymentInfo.shortName );
        
        for( DeployableComponent subDC : dc.getSubComponents() )
        {
          // Add the sub-component to the DU object.
          deployerDU.addComponent( subDC );
        }
      }
      // If the DU for this component doesn't exists.. it's a new DU!
      else if( fileName.endsWith( ".jar" ) )
      {
        JarFile duJarFile = null;
        
        try
        {
          // Get a reference to the DU jar file
          duJarFile = new JarFile( di.url.toString().replaceFirst( "file:", "" ) );
  
          // Try to get the Deployable Unit descriptor
          JarEntry duXmlEntry = duJarFile.getJarEntry( "META-INF/deployable-unit.xml" );    
          
          // Got descriptor?
          if( duXmlEntry != null )
          {
            // Create a new Deployable Unit object.
            DeployableUnit deployerDU = new DeployableUnit( di, dm );
            
            // Let's parse the descriptor to see what we've got...
            DeployableUnitDescriptorImpl duDesc = parseDUDescriptor( duJarFile );
  
            // Add it to the deployable units map.
            deployableUnits.put( di.shortName, deployerDU );
  
            // Go through each jar entry in the DU descriptor
            for( Element elem : (Collection<Element>)duDesc.getJarNodes() )
            {
              // Get the name of the jar
              String componentJarName = elem.getTextContent().trim();
  
              // Might have path... strip it!
              int beginIndex;
  
              if( ( beginIndex = componentJarName.lastIndexOf( '/' ) ) == -1 )
                beginIndex = componentJarName.lastIndexOf( '\\' );
  
              beginIndex++;
  
              // Got a clean jar name, no paths.
              componentJarName = componentJarName.substring( beginIndex, componentJarName.length() );
  
              // Put it in the accept list.
              toAccept.put( componentJarName, di );
            }
            
            // Do the same as above... but for services
            for( Element elem : (Collection<Element>)duDesc.getServiceNodes() )
            {
              // Get the name of the service XML
              String serviceXMLName = elem.getTextContent().trim();
              
              // Might have path... strip it!
              int beginIndex;
  
              if( ( beginIndex = serviceXMLName.lastIndexOf( '/' ) ) == -1 )
                beginIndex = serviceXMLName.lastIndexOf( '\\' );
  
              beginIndex++;
  
              // Got a clean XML filename
              serviceXMLName = serviceXMLName.substring( beginIndex, serviceXMLName.length() );
              
              // Add it to the accept list.
              toAccept.put( serviceXMLName, di );
            }
          }
        }
        finally
        {
          // Clean up!
          if( duJarFile != null )
          {
            try
            {
              duJarFile.close();
            }
            catch ( IOException ignore )
            {
            }
            finally
            {
              duJarFile = null;
            }
          }
        }
      }
    }
    catch ( Exception e )
    {
      // Something went wrong...
      if( logger.isDebugEnabled() )
        logger.debug( "Deployment of " + di.shortName + " failed.", e );
      
      logger.error( "Deployment of " + di.shortName + " failed. ", e );
      
      return;
    }

    super.init( di );
  }

  /**
   * Nothing to do here.
   */
  @Override
  public void create( DeploymentInfo di ) throws DeploymentException
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method create called for " + di.url );

    super.create( di );
  }

  /**
   * This is where the fun begins. Time to deploy!
   */
  @Override
  public void start( DeploymentInfo di ) throws DeploymentException
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method start called for " + di.url );

    try
    {
      // Get the deployable unit object 
      DeployableUnit du = deployableUnits.get( di.shortName );
      
      // If it exists, install it.
      if( du != null)
        dm.installDeployableUnit( du );

    }
    catch (Exception e)
    {
      logger.error( "", e );
    }
    
    super.start( di );
  }

  /**
   * Fun has ended. Time to undeploy.
   */
  @Override
  public void stop( DeploymentInfo di ) throws DeploymentException
  {
	  
	  DeployableUnit du = null;
		if ((du = deployableUnits.get(di.shortName)) != null) {
			// We get here when we have components depending on this...
			try {
				// Let's store the 'old' UCL.
				dm.addReplacedUCL(du, di.ucl);
				// Just to make sure we won't lose our classes, we 'hide' the
				// UCL
				di.ucl = null;

				// If the above fails, might think about using this :)
				// di.createClassLoaders();

			} catch (Exception e1) {
				logger.debug("Failed to add old UCL to list.", e1);
			}
  
    if( isServerShuttingDown )
      doStop( di );
    else {
			
        
        // Schedule removal in a recurring task
				timer.scheduleAtFixedRate(new UndeploymentTask(di), 0,
						getWaitTimeBetweenOperations());
			}
		}
	}

  private boolean doStop( DeploymentInfo di )
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method stop called for " + di.url );

    DeployableUnit du = null;
    
    try
    {
      // Get and remove deployable unit object
      if( (du = deployableUnits.get( di.shortName )) != null )
      {
        // Uninstall it
        dm.uninstallDeployableUnit( du );
        
        // Remove it from list if successful
        deployableUnits.remove( di.shortName );
        
        // Make it null. clean.
        du = null;
      }
    }
    catch (Exception e)
    {
      return false;
    }
    
    return true;
    
    //FIXME: alexandre: Hope this is not needed... worked previously on the stop method.
    //super.stop( di );
  }
  
  /**
   * Nothing left to do here.
   */
  @Override
  public void destroy( DeploymentInfo di ) throws DeploymentException
  {
    if( logger.isDebugEnabled() )
      logger.debug( "Method destroy called for " + di.url );

    // Should we clean temporary files here?
    //FIXME: alexandre: Hope this is not needed... remove due to the new stop method.
    //super.destroy( di );
  }

  // MBean Operations ---------------------------------------------------------
  
  /**
   * MBean operation for getting Deployer status.
   */
  public String showStatus() throws DeploymentException
  {
    String output = "";
    
    output += "<p>Deployable Units List:</p>";
    
    for( String key : deployableUnits.keySet() )
    {
      output += "&lt;" + key + "&gt; [" + deployableUnits.get( key ) + "]<br>";
      for( String duComponent : deployableUnits.get( key ).getComponents() )
      {
        output += "+-- " + duComponent + "<br>";
      }
    }

    output += "<p>To Accept List:</p>";
    
    for( String key : toAccept.keySet() )
    {
      output += "&lt;" + key + "&gt; [" + toAccept.get( key ) + "]<br>";   
    }

    output += "<p>Deployment Manager Status</p>";
    
    output += dm.showStatus();   

    return output;
  }

  /**
   * MBean WaitTimeBetweenOperations property getter
   */
  public long getWaitTimeBetweenOperations()
  {
    return dm.waitTimeBetweenOperations;
  }

  /**
   * MBean WaitTimeBetweenOperations property getter
   */
  public void setWaitTimeBetweenOperations(long waitTime)
  {
    dm.waitTimeBetweenOperations = waitTime;
  }
  
  // Aux Functions ------------------------------------------------------------

  /**
   * Method for parsing a Deployable Unit descriptor. Gotten from DeployableUnitDeployer.
   */
  private DeployableUnitDescriptorImpl parseDUDescriptor( JarFile unitJarFile ) throws DeploymentException
  {
    // Get the deployable unit descriptor entry
    JarEntry duXmlEntry = unitJarFile.getJarEntry( "META-INF/deployable-unit.xml" );
    
    // Don't have one? Go away!
    if( duXmlEntry == null )
      throw new DeploymentException( "No DeployableUnitDeploymentDescriptor descriptor (META-INF/deployable-unit.xml) was found in deployable unit" + unitJarFile.getName() );

    // The Document
    Document doc = null;

    InputStream is = null;
    
    try
    {
      // Get the InputStream
      is = unitJarFile.getInputStream( duXmlEntry );
      
      // Parse the descriptor
      doc = XMLUtils.parseDocument( is, false );
    }
    catch ( IOException ex )
    {
      throw new DeploymentException( "Failed to extract the DU depl " + "descriptor from " + unitJarFile.getName() );
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
        catch ( IOException ignore )
        {
        }
        finally
        {
          is = null;
        }
      }
    }

    Element duNode = doc.getDocumentElement();
    DeployableUnitDescriptorImpl deployableUnitDescriptor = new DeployableUnitDescriptorImpl( unitJarFile.getName(), new Date() ); // getDescriptor();

    try
    {
      // Get the description, if any.
      String description = XMLUtils.getElementTextValue( duNode, XMLConstants.DESCRIPTION_ND );
      
      // Store it.
      if( description != null )
        deployableUnitDescriptor.setDescription( description );
    }
    catch ( Exception ex )
    {
      throw new DeploymentException( ex.getMessage() );
    }

    // Get a list of the jars in the deployable unit.
    List<Element> jarNodes = XMLUtils.getAllChildElements( duNode, XMLConstants.JAR_ND );
    deployableUnitDescriptor.setJarNodes( jarNodes );
    
    // Same for the services 
    List<Element> serviceNodes = XMLUtils.getAllChildElements( duNode, XMLConstants.SERVICE_XML_ND );
    deployableUnitDescriptor.setServiceNodes( serviceNodes );
    
    // Got nothing on this DU?
    if( jarNodes.size() == 0 && serviceNodes.size() == 0 )
    {
      throw new DeploymentException( "The " + unitJarFile.getName() + " deployable unit contains no jars or services" );
    }

    // All good. Return the parsed descriptor.
    return deployableUnitDescriptor;
  }

  private class UndeploymentTask extends TimerTask
  {
    DeploymentInfo di;
    long startTime;
    
    public UndeploymentTask(DeploymentInfo di)
    {
      this.di = di;
      this.startTime = System.currentTimeMillis();
    }
    
    public void run()
    {
      try
      {
        long elapsedTime = System.currentTimeMillis() - this.startTime;
        
        if( doStop(di) || elapsedTime > 60 * 60 * 1000 + MobicentsManagement.entitiesRemovalDelay )
          this.cancel();
        
      }
      catch (Exception ignore) 
      {
        // do nothing...
      }
    }
    
  }

  public void handleNotification( Notification notification, Object o )
  {
    if( notification.getType().equals( "org.jboss.system.server.stopped" ) )
      isServerShuttingDown = true;
  }
  
}