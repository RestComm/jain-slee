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

package org.rhq.plugins.jslee.jbossas5.util;

import java.io.File;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jetbrains.annotations.Nullable;

import org.rhq.core.util.exception.ThrowableUtil;

/**
 * @author Ian Springer
 */
public class JBossASDiscoveryUtils {
    private static final Log LOG = LogFactory.getLog(JBossASDiscoveryUtils.class);

    @Nullable
    public static UserInfo getJmxInvokerUserInfo(File configDir) {
        String securityDomain = getJmxInvokerSecurityDomain(configDir);
        if (securityDomain == null) {
            LOG.debug("The JMX invoker service is not configured to require authentication.");
            return null;
        }

        LOG.debug("The JMX invoker service is configured to use the '" + securityDomain
                + "' security domain for authentication.");
        File usersPropsFile = new File(configDir, "conf/props/" + securityDomain + "-users.properties");
        if (!usersPropsFile.exists()) {
            LOG.debug("Could not find users configuration for security domain '" + securityDomain
                    + "' - " + usersPropsFile + " does not exist.");
        }
        File rolesPropsFile = new File(configDir, "conf/props/" + securityDomain + "-roles.properties");
        if (!rolesPropsFile.exists()) {
            LOG.debug("Could not find roles configuration for security domain '" + securityDomain
                    + "' - " + rolesPropsFile + " does not exist.");
        }
        if (usersPropsFile.exists() && rolesPropsFile.exists()) {
            try {
                SecurityDomainInfo securityDomainInfo = new SecurityDomainInfo(usersPropsFile, rolesPropsFile);
                Set<String> adminUsers = securityDomainInfo.getUsers("JBossAdmin");
                if (!adminUsers.isEmpty()) {
                    // Use the first one - it's as good as any.
                    String adminUser = adminUsers.iterator().next();
                    String adminPassword = securityDomainInfo.getPassword(adminUser);
                    LOG.debug("Discovered principal (" + adminUser
                            + ") and credentials for connecting to the JMX invoker service.");
                    return new UserInfo(adminUser, adminPassword);
                }
            }
            catch (Exception e) {
                LOG.error("Could not determine username and password of admin user - failed to parse users and/or roles configuration file.");
            }
        }
        return null;
    }

    @Nullable
    private static String getJmxInvokerSecurityDomain(File configDir) {
        File deployDir = new File(configDir, "deploy");
        File jmxInvokerServiceXmlFile = new File(deployDir, "jmx-invoker-service.xml");
        String securityDomain = null;
        try {
            JmxInvokerServiceConfiguration jmxInvokerConfig = new JmxInvokerServiceConfiguration(jmxInvokerServiceXmlFile);
            securityDomain = jmxInvokerConfig.getSecurityDomain();
        }
        catch (Exception e) {
            LOG.debug("Failed to read " + jmxInvokerServiceXmlFile
                    + " - unable to determine if authentication is enabled on the JMX invoker. Cause: "
                    + ThrowableUtil.getAllMessages(e));
        }
        return securityDomain;
    }

    public static class UserInfo {
        private final String username;
        private final String password;

        public UserInfo(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }

    private JBossASDiscoveryUtils() {
    }
}
