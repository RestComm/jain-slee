/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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