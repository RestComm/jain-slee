package org.telestax.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.*;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceController;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceRegistry;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.jboss.SleeContainerDeployerImpl;
import org.telestax.slee.container.build.as7.service.SleeServiceNames;

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
        log.info("deploymentUnit "+deploymentUnit);

        final URL deployableUnitJarURL = SleeDeploymentMarker.getDeployableUnitJarURL(deploymentUnit);
        log.info("deployableUnitJarURL "+deployableUnitJarURL);
        if (deployableUnitJarURL == null) {
            return;
        }

        final SleeDeploymentMetaData deployableUnitJarMetaData = SleeDeploymentMarker.getDeployableUnitJarMetaData(deploymentUnit);
        log.info("deployableUnitJarMetaData "+deployableUnitJarMetaData);
        if (deployableUnitJarMetaData == null) {
            return;
        }

        SleeContainer container = getSleeContainer(phaseContext.getServiceRegistry(), SleeServiceNames.SLEE_CONTAINER);

        getExternalDeployer(container).deploy(deploymentUnit, deployableUnitJarURL, deployableUnitJarMetaData);
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
    	final URL deployableUnitJarURL = SleeDeploymentMarker.getDeployableUnitJarURL(deploymentUnit);
        if (deployableUnitJarURL == null) {
        	return;
        }
        // TODO: undeploy from SLEE
    }

    private SleeContainer getSleeContainer(ServiceRegistry registry, ServiceName serviceName) {
        ServiceController<?> service = registry.getService(serviceName);
        log.info("getSleeContainerService service: " + service);
        if (service != null) {
            SleeContainer container = (SleeContainer) service.getValue();
            log.info("getSleeContainer container: " + container);
            return container;
        }
        return null;
    }

    private ExternalDeployerImpl getExternalDeployer(SleeContainer container) {
        if (container != null) {
            SleeContainerDeployerImpl internalDeployer = (SleeContainerDeployerImpl)container.getDeployer();
            ExternalDeployerImpl externalDeployer = (ExternalDeployerImpl)internalDeployer.getExternalDeployer();
            log.info("service internalDeployer " + internalDeployer);
            log.info("service externalDeployer " + externalDeployer);
            return externalDeployer;
        }
        return null;
    }
}
