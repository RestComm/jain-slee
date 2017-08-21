package org.mobicents.slee.container.component.deployment;

import java.util.List;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.deployment.classloading.URLClassLoaderDomainImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;

/**
 * Looks for an env-entry with list of packages where classloder order will be
 * inverted. First look into dependencies and local, then in parent.
 *
 * fixes https://github.com/RestComm/jain-slee/issues/49
 *
 */
public class PreferredPackagesBuilder {

    private static final String PREFERRED_ENV_ENTRY = "org.restcomm.slee.preferred-package-list";
    private static final Logger logger = Logger
            .getLogger(PreferredPackagesBuilder.class);
    private static final String LIST_SEPARATOR = ",";

    public static String[] parsePackageList(String commaSeparatedList) {
        String[] split = commaSeparatedList.split(LIST_SEPARATOR);
        return split;
    }
    
    public static void buildPreferredPackages(String[] packages, URLClassLoaderDomainImpl classLoader) {
        for (String pack : packages) {
            classLoader.getPreferredPackages().add(pack);
            if (logger.isInfoEnabled()) {
                logger.info("Added preferred package:" + pack);
            }
        }
    }    

    public static void buildPreferredPackages(SbbDescriptorImpl descriptor, URLClassLoaderDomainImpl classLoader) {
        List<EnvEntryDescriptor> envEntries = descriptor.getEnvEntries();
        boolean found = false;
        for (EnvEntryDescriptor entry : envEntries) {
            if (entry.getEnvEntryName().equals(PREFERRED_ENV_ENTRY)) {
                if (logger.isInfoEnabled()) {
                    logger.info("Using env-entry to set preferredPacakges:" + entry.getEnvEntryValue());
                }
                found = true;
                String[] split = parsePackageList(entry.getEnvEntryValue());
                buildPreferredPackages(split, classLoader);
            }
        }
        if (!found) {
            logger.info("No preferred package entry found. Applying defaults.");
        }
    }
}
