package org.rhq.plugins.jslee.utils.jaas;

import java.util.Map;
import java.util.HashMap;

import javax.security.auth.login.Configuration;
import javax.security.auth.login.AppConfigurationEntry;

/**
 * A JAAS configuration for a JBoss client. This is the programmatic equivalent of the following auth.conf file:
 *
 * <code>
 * jboss
 * {
 *   org.jboss.security.ClientLoginModule required
 *     multi-threaded=true;
 * };
 * </code>
 *
 * @author Ian Springer
 */
public class JBossConfiguration extends Configuration {
    public static final String JBOSS_ENTRY_NAME = "jboss";

    private static final String JBOSS_LOGIN_MODULE_CLASS_NAME = "org.jboss.security.ClientLoginModule";
    private static final String MULTI_THREADED_OPTION = "multi-threaded";

    public AppConfigurationEntry[] getAppConfigurationEntry(String name) {
        if (JBOSS_ENTRY_NAME.equals(name)) {
            Map options = new HashMap(1);
            options.put(MULTI_THREADED_OPTION, Boolean.TRUE.toString());
            AppConfigurationEntry appConfigurationEntry =
                new AppConfigurationEntry(JBOSS_LOGIN_MODULE_CLASS_NAME,
                    AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options);
            return new AppConfigurationEntry[] {appConfigurationEntry};
        } else {
            throw new IllegalArgumentException("Unknown entry name: " + name);
        }
    }

    public void refresh() {
        return;
    }
}