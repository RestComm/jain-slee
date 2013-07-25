package org.telestax.slee.container.build.as7.deployment;

import java.util.Locale;

import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.Phase;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.vfs.VirtualFile;

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
        if(!deploymentRoot.getRootName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
            return;
        }
        final VirtualFile descriptor = deploymentRoot.getRoot().getChild("META-INF/deployable-unit.xml");
        if (descriptor == null) {
            return;
        }
        // TODO mark deployment as SLEE by storing the jar URL
        //SleeDeploymentMarker.mark(deploymentUnit);
        // deploy
        try {
            log.info("deploying "+deploymentUnit.getName());
            log.info("deploymentRoot root name: "+deploymentRoot.getRootName());
            log.info("deploymentRoot canonical path: "+deploymentRoot.getRoot().getPhysicalFile().getCanonicalPath());
            log.info("deploymentRoot parent canonical path: "+deploymentRoot.getRoot().getParent().getPhysicalFile().getCanonicalPath());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
        // nothing todo
    }

}
