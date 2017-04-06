package org.restcomm.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.*;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.restcomm.slee.container.build.as7.service.SleeContainerService;
import org.restcomm.slee.container.build.as7.service.SleeServiceNames;

import java.net.URL;

public class SleeDeploymentInstallProcessor implements DeploymentUnitProcessor {

    Logger log = Logger.getLogger(SleeDeploymentInstallProcessor.class);

    /**
     * See {@link Phase} for a description of the different phases
     */
    public static final Phase PHASE = Phase.INSTALL;

    /**
     * The relative order of this processor within the {@link #PHASE}.
     * The current number is large enough for it to happen after all
     * the standard deployment unit processors that come with JBoss AS.
     */
    public static final int PRIORITY = 0x4000;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        log.info("Deploy: "+deploymentUnit);

        final URL deployableUnitJarURL = SleeDeploymentMarker.getDeployableUnitJarURL(deploymentUnit);
        if (deployableUnitJarURL == null) {
            return;
        }

        final SleeDeploymentMetaData deployableUnitJarMetaData = SleeDeploymentMarker.getDeployableUnitJarMetaData(deploymentUnit);
        if (deployableUnitJarMetaData == null) {
            return;
        }

        ExternalDeployerImpl externalDeployer = getExternalDeployer(phaseContext.getServiceRegistry(), SleeServiceNames.SLEE_CONTAINER);
        if (externalDeployer != null) {
            externalDeployer.deploy(deploymentUnit, deployableUnitJarURL, deployableUnitJarMetaData, null);
        }
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
        log.info("Undeploy: "+deploymentUnit);

    	final URL deployableUnitJarURL = SleeDeploymentMarker.getDeployableUnitJarURL(deploymentUnit);
        if (deployableUnitJarURL == null) {
        	return;
        }
        final SleeDeploymentMetaData deployableUnitJarMetaData = SleeDeploymentMarker.getDeployableUnitJarMetaData(deploymentUnit);
        if (deployableUnitJarMetaData == null) {
            return;
        }

        //SleeDeploymentMarker.setDeployableUnitJarURL(deploymentUnit, null);
        //SleeDeploymentMarker.setDeployableUnitJarMetaData(deploymentUnit, null);

        ExternalDeployerImpl externalDeployer = getExternalDeployer(deploymentUnit.getServiceRegistry(), SleeServiceNames.SLEE_CONTAINER);
        if (externalDeployer != null) {
            externalDeployer.undeploy(deploymentUnit, deployableUnitJarURL, deployableUnitJarMetaData);
        }
    }

    private ExternalDeployerImpl getExternalDeployer(ServiceRegistry registry, ServiceName serviceName) {
        ServiceController<?> serviceController = registry.getService(serviceName);
        SleeContainerService service = (SleeContainerService) serviceController.getService();
        if (service != null) {
            ExternalDeployerImpl externalDeployer = (ExternalDeployerImpl) service.getExternalDeployer();
            return externalDeployer;
        }
        return null;
    }
}
