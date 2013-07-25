package org.telestax.slee.container.build.as7.deployment;

import java.net.URL;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;

public class SleeDeploymentInstallProcessor implements DeploymentUnitProcessor {

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
        final URL deployableUnitJarURL = SleeDeploymentMarker.getDeployableUnitJarURL(deploymentUnit);
        if (deployableUnitJarURL == null) {
        	return;
        }
        // TODO deploy in SLEE
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
    	final URL deployableUnitJarURL = SleeDeploymentMarker.getDeployableUnitJarURL(deploymentUnit);
        if (deployableUnitJarURL == null) {
        	return;
        }
        // TODO undeploy from SLEE
    }

}
