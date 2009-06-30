package org.mobicents.slee.container.deployment.jboss;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.deployer.AbstractSimpleVFSRealDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.system.ServiceController;
import org.jboss.virtual.VFSUtils;

public class SLEEDeployer extends AbstractSimpleVFSRealDeployer<SLEEDeploymentMetaData>
{
  private static Logger logger = Logger.getLogger( SLEEDeployer.class );
  
  /**
   * Create a new SleeDeploymentMetaData.
   */
  public SLEEDeployer(ServiceController serviceController)
  {
    super(SLEEDeploymentMetaData.class);
    setOutput(SLEEDeploymentMetaData.class);
    
    logger.info("Mobicents SLEE Real Deployer initialized.");    
  }

  @Override
  public void deploy( VFSDeploymentUnit vfsDU, SLEEDeploymentMetaData sdmd ) throws DeploymentException
  {
    //logger.info("»»» SLEEDeployer »» public void deploy( VFSDeploymentUnit unit, SLEEDeploymentMetaData sdmd )");

    if(logger.isTraceEnabled())
    {
      logger.trace("SLEEParserDeployer 'deploy' called:");
      logger.trace("vfsDeploymentUnit.." + vfsDU);
      logger.trace("sdmd..............." + sdmd);
    }
    
    if(sdmd != null)
    {
      try
      {

        SLEESubDeployer ssdeployer = SLEESubDeployer.INSTANCE;

        DeployableUnitWrapper duWrapper = new DeployableUnitWrapper(VFSUtils.getCompatibleURL(vfsDU.getRoot()));

        ssdeployer.accepts( duWrapper );

        ssdeployer.init( duWrapper );

        for (String componentJar : sdmd.duContents)
        {

          DeployableUnitWrapper duWrapperComp = new DeployableUnitWrapper(VFSUtils.getCompatibleURL(vfsDU.getFile(componentJar)));

          ssdeployer.accepts( duWrapperComp );

          ssdeployer.init( duWrapperComp );

          ssdeployer.create( duWrapperComp );

          ssdeployer.start( duWrapperComp );
        }

        ssdeployer.create( duWrapper );

        ssdeployer.start( duWrapper );

      }
      catch ( Exception e )
      {
        logger.error( "Failure while deploying " + vfsDU.getName(), e );
      }
    }
  }
  
  public void undeploy(VFSDeploymentUnit vfsDU, SLEEDeploymentMetaData sdmd)
  {
    //logger.info("»»» SLEEDeployer »» public void undeploy(VFSDeploymentUnit unit, SLEEDeploymentMetaData sdmd)");

    if(logger.isTraceEnabled())
    {
      logger.trace("SLEEParserDeployer 'undeploy' called:");
      logger.trace("vfsDeploymentUnit.." + vfsDU);
      logger.trace("sdmd..............." + sdmd);
    }

    if(sdmd != null)
    {
      try
      {
        SLEESubDeployer ssdeployer = SLEESubDeployer.INSTANCE;

        DeployableUnitWrapper duWrapper = new DeployableUnitWrapper(VFSUtils.getCompatibleURL(vfsDU.getRoot()));

        ssdeployer.stop( duWrapper );

        ssdeployer.destroy( duWrapper );
      }
      catch ( Exception e )
      {
        logger.error( "Failure while undeploying " + vfsDU.getName(), e );
      }
    }
  }

  
}