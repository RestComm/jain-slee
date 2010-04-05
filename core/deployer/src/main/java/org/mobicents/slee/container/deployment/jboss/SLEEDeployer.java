package org.mobicents.slee.container.deployment.jboss;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.vfs.spi.deployer.AbstractSimpleVFSRealDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.system.ServiceController;
import org.jboss.virtual.VFSUtils;

/**
 * JAIN SLEE Real Deployer for JBoss AS 5.x
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SLEEDeployer extends AbstractSimpleVFSRealDeployer<SLEEDeploymentMetaData>
{
  private static Logger logger = Logger.getLogger( SLEEDeployer.class );

  private ArrayList<URL> waitingList = new ArrayList<URL>();

  private ArrayList<URL> deployableUnits = new ArrayList<URL>();

  private HashMap<URL, VFSDeploymentUnit> vfsDUs = new HashMap<URL, VFSDeploymentUnit>(); 
  private HashMap<URL, SLEEDeploymentMetaData> metadata = new HashMap<URL, SLEEDeploymentMetaData>(); 

  private final SleeStateJMXMonitor jmxMonitor;

  /**
   * Create a new SleeDeploymentMetaData.
   */
  public SLEEDeployer(ServiceController serviceController) {
    super(SLEEDeploymentMetaData.class);
    setOutput(SLEEDeploymentMetaData.class);

    jmxMonitor = new SleeStateJMXMonitor(this);
    jmxMonitor.getSleeStateAsInt();

    logger.info("Mobicents SLEE Real Deployer initialized.");    
  }

  @Override
  public void deploy( VFSDeploymentUnit vfsDU, SLEEDeploymentMetaData sdmd ) throws DeploymentException {
    if(logger.isTraceEnabled()) {
      logger.trace("SLEEParserDeployer 'deploy' called:");
      logger.trace("vfsDeploymentUnit.." + vfsDU);
      logger.trace("sdmd..............." + sdmd);
    }

    if(sdmd != null)
    {
      try {
        //DeployableUnitWrapper duWrapper = new DeployableUnitWrapper(VFSUtils.getCompatibleURL(vfsDU.getRoot()));
        //DeployableUnitWrapper duWrapper = new DeployableUnitWrapper(vfsDU, sdmd);
        URL deployableUnitURL = VFSUtils.getCompatibleURL(vfsDU.getRoot());

        vfsDUs.put(deployableUnitURL, vfsDU);
        metadata.put(deployableUnitURL, sdmd);

        if(!jmxMonitor.isSleeRunning()) {
          logger.warn("Unable to INSTALL " + vfsDU.getSimpleName() + " right now. Waiting for SLEE to be in RUNNING state.");
          this.waitingList.add(deployableUnitURL);
          return;
        }
        else {
          callSubDeployer(deployableUnitURL);
        }
      }
      catch ( Exception e ) {
        logger.error( "Failure while deploying " + vfsDU.getName(), e );
      }
    }
  }

  @Override
  public void undeploy(VFSDeploymentUnit vfsDU, SLEEDeploymentMetaData sdmd) {
    if(logger.isTraceEnabled()) {
      logger.trace("SLEEParserDeployer 'undeploy' called:");
      logger.trace("vfsDeploymentUnit.." + vfsDU);
      logger.trace("sdmd..............." + sdmd);
    }

    if(sdmd != null) {
      try {
        doUndeployActions(VFSUtils.getCompatibleURL(vfsDU.getRoot()));
      }
      catch ( Exception e ) {
        logger.error( "Failure while undeploying " + vfsDU.getName(), e );
      }
    }
  }

  private void doUndeployActions(URL deployableUnitURL) throws Exception {
    SLEESubDeployer ssdeployer = SLEESubDeployer.INSTANCE;

    ssdeployer.stop( deployableUnitURL );
  }

  private void callSubDeployer(URL deployableUnitURL) throws Exception {

    SLEESubDeployer ssdeployer = SLEESubDeployer.INSTANCE;

    ssdeployer.accepts( deployableUnitURL );

    ssdeployer.init( deployableUnitURL );

    SLEEDeploymentMetaData sdmd = metadata.remove(deployableUnitURL);

    VFSDeploymentUnit vfsDU = vfsDUs.get(deployableUnitURL);

    for (String componentJar : sdmd.duContents) {
      URL componentURL = null;
      
      try {
        componentURL = VFSUtils.getCompatibleURL(vfsDU.getFile(componentJar));
      }
      catch (Exception e) {
        throw new IllegalArgumentException("Failed to locate " + componentJar + " in DU. Does it exists?", e);
      }

      ssdeployer.accepts( componentURL );

      ssdeployer.init( componentURL );

      ssdeployer.start( componentURL );
    }

    ssdeployer.start( deployableUnitURL );
  }

  public void sleeIsRunning() {
    int failCount = 0;
    for(URL deployableUnitURL : this.waitingList) {
      try {
        callSubDeployer(deployableUnitURL);
        deployableUnits.add(deployableUnitURL);
      }
      catch (Exception e) {
        failCount++;
        logger.error("Failure during deployment procedures.", e);
      }
    }

    if(this.waitingList.size() > 0) {
    	if(logger.isInfoEnabled())
    	{
    		logger.info("><><><><><><><><>< Deployment of JAIN SLEE deployable-units complete. " + (failCount == 0 ? "No" : failCount) + " failures. ><><><><><><><><><");
    	}
    }

    // This should suffice for sync deployments
    this.waitingList.clear();
  }

}