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

package org.rhq.plugins.jslee.utils;

import java.util.Properties;

import javax.management.MBeanServerConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.rhq.plugins.jslee.utils.jaas.JBossCallbackHandler;
import org.rhq.plugins.jslee.utils.jaas.JBossConfiguration;

public class MBeanServerUtils {
	private Properties prop = null;
  private LoginContext loginContext;

	public MBeanServerUtils(String namingURL, String principal, String credentials) {
		this.prop = new Properties();
		prop.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		prop.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		prop.put(Context.PROVIDER_URL, namingURL);

		if(principal != null) {
	    prop.put(Context.SECURITY_PRINCIPAL, principal);
	    prop.put(Context.SECURITY_CREDENTIALS, credentials);

	    CallbackHandler jaasCallbackHandler = new JBossCallbackHandler(principal, "admin");
      javax.security.auth.login.Configuration jaasConfig = new JBossConfiguration();
      try {
        loginContext = new LoginContext(JBossConfiguration.JBOSS_ENTRY_NAME, null, jaasCallbackHandler, jaasConfig);
      }
      catch (LoginException e) {
        throw new RuntimeException(e);
      }
		}
	}

	public MBeanServerConnection getConnection() throws NamingException {
		InitialContext ctx = new InitialContext(this.prop);
		MBeanServerConnection server = (MBeanServerConnection) ctx.lookup("jmx/invoker/RMIAdaptor");
		return server;
	}
	
  public void login() throws LoginException {
    if(loginContext != null) {
      loginContext.login();
    }
  }

  public void logout() throws LoginException {
    if(loginContext != null) {
      loginContext.logout();
    }
  }

}
