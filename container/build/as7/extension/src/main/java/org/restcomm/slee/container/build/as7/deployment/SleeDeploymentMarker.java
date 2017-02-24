package org.restcomm.slee.container.build.as7.deployment;

import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.logging.Logger;
import org.jboss.vfs.VFSUtils;
import org.jboss.vfs.VirtualFile;

import java.net.URL;

/**
 *
 * @author Eduardo Martins
 *
 */
public class SleeDeploymentMarker {

    static Logger log = Logger.getLogger(SleeDeploymentMarker.class);

    private static final AttachmentKey<URL> URL_KEY = AttachmentKey.create(URL.class);
    private static final AttachmentKey<SleeDeploymentMetaData> SLEEMETADATA_KEY = AttachmentKey.create(SleeDeploymentMetaData.class);

    public static void mark(final DeploymentUnit deploymentUnit, final boolean isDeploy) {
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT);
        final VirtualFile root = deploymentRoot.getRoot();
        URL rootURL = null;
        try {
            rootURL = VFSUtils.getRootURL(root);
        } catch (Exception ex) {
            log.error("Cannot get URL for deployable unit: " + ex.getLocalizedMessage());
        }
        setDeployableUnitJarURL(deploymentUnit, rootURL);

        // to parse deployment unit
        if(log.isTraceEnabled()) {
            log.trace("Deployment unit parsing: " + deploymentUnit);
        }
        SleeDeploymentMetaData metaData = new SleeDeploymentMetaData(deploymentUnit, isDeploy);

        // SergeyLee: ClassLoadingMetaData migrating
        /*
        if(metaData.componentType == SleeDeploymentMetaData.ComponentType.DU) {

            ClassLoadingMetaData classLoadingMetaData = du.getAttachment(ClassLoadingMetaData.class);

            if(log.isTraceEnabled()) {
                log.trace("Got Classloading MetaData: " + classLoadingMetaData);
            }

            classLoadingMetaData = new ClassLoadingMetaData10();
            classLoadingMetaData.setName(du.getSimpleName());
            classLoadingMetaData.setIncludedPackages("");

            if(log.isTraceEnabled()) {
                log.trace("Set Classloading MetaData: " + classLoadingMetaData);
            }

            du.addAttachment(ClassLoadingMetaData.class, classLoadingMetaData);

            return _sdmd;
        }
        */

        setDeployableUnitJarMetaData(deploymentUnit, metaData);
    }

    public static void setDeployableUnitJarURL(final DeploymentUnit deploymentUnit, final URL duURL) {
        deploymentUnit.putAttachment(URL_KEY, duURL);
    }

    public static URL getDeployableUnitJarURL(final DeploymentUnit deploymentUnit) {
        return deploymentUnit.getAttachment(URL_KEY);
    }

    public static void setDeployableUnitJarMetaData(final DeploymentUnit deploymentUnit, final SleeDeploymentMetaData duMetaData) {
        deploymentUnit.putAttachment(SLEEMETADATA_KEY, duMetaData);
    }

    public static SleeDeploymentMetaData getDeployableUnitJarMetaData(final DeploymentUnit deploymentUnit) {
        return deploymentUnit.getAttachment(SLEEMETADATA_KEY);
    }
}
