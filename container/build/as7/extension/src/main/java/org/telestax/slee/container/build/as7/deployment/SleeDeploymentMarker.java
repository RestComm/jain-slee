package org.telestax.slee.container.build.as7.deployment;

import java.net.URL;

import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.DeploymentUnit;

/**
 *
 * @author Eduardo Martins
 *
 */
public class SleeDeploymentMarker {

    private static final AttachmentKey<URL> ATTACHMENT_KEY = AttachmentKey.create(URL.class);

    public static void setDeployableUnitJarURL(final DeploymentUnit deployment, final URL duURL) {
        deployment.putAttachment(ATTACHMENT_KEY, duURL);
    }

    public static URL getDeployableUnitJarURL(final DeploymentUnit deploymentUnit) {
        return deploymentUnit.getAttachment(ATTACHMENT_KEY);        
    }
}
