package org.restcomm.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.*;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.msc.service.ServiceName;
import org.jboss.vfs.VirtualFile;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

public class SleeDeploymentParseProcessor implements DeploymentUnitProcessor {

	Logger log = Logger.getLogger(SleeDeploymentParseProcessor.class);

	/**
	 * See {@link Phase} for a description of the different phases
	 */
	public static final Phase PHASE = Phase.PARSE;

	/**
	 * The relative order of this processor within the {@link #PHASE}. The
	 * current number is large enough for it to happen after all the standard
	 * deployment unit processors that come with JBoss AS.
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
		if (!deploymentRoot.getRootName().toLowerCase(Locale.ENGLISH).endsWith(".jar")) {
			return;
		}
		final VirtualFile descriptor = deploymentRoot.getRoot().getChild("META-INF/deployable-unit.xml");
		if (descriptor == null) {
			return;
		}

		SleeDeploymentMarker.mark(deploymentUnit, false);
	}
}
