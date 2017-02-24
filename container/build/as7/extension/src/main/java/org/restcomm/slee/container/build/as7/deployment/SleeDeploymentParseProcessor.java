package org.restcomm.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.*;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.vfs.VirtualFile;

import java.util.Locale;

public class SleeDeploymentParseProcessor implements DeploymentUnitProcessor {

    Logger log = Logger.getLogger(SleeDeploymentParseProcessor.class);

    /**
     * See {@link Phase} for a description of the different phases
     */
    public static final Phase PHASE = Phase.PARSE;

    /**
     * The relative order of this processor within the {@link #PHASE}.
     * The current number is large enough for it to happen after all
     * the standard deployment unit processors that come with JBoss AS.
     */
    public static final int PRIORITY = 0x4000;

    @Override
    public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);

        if (!deploymentRoot.getRootName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
            return;
        }
        final VirtualFile descriptor = deploymentRoot.getRoot().getChild("META-INF/deployable-unit.xml");
        if (descriptor == null) {
            return;
        }

        SleeDeploymentMarker.mark(deploymentUnit, true);
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
        if(!deploymentRoot.getRootName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
            return;
        }
        final VirtualFile descriptor = deploymentRoot.getRoot().getChild("META-INF/deployable-unit.xml");
        if (descriptor == null) {
            return;
        }

        SleeDeploymentMarker.mark(deploymentUnit, false);
    }

}
