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

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class JBossCallbackHandler implements CallbackHandler {
  private String username;
  private char[] password;

  public JBossCallbackHandler(String username, String password)
  {
      this.username = username;
      this.password = password.toCharArray();
  }

  public void handle(Callback[] callbacks) throws
          IOException, UnsupportedCallbackException
  {
      for (Callback callback : callbacks)
      {
          //System.out.println("Handling Callback [" + callback + "]...");
          if (callback instanceof NameCallback)
          {

              NameCallback nameCallback = (NameCallback)callback;
              nameCallback.setName(this.username);
          }
          else if (callback instanceof PasswordCallback)
          {
              PasswordCallback passwordCallback = (PasswordCallback)callback;
              passwordCallback.setPassword(this.password);
          }
          else
          {
              throw new UnsupportedCallbackException(callback, "Unrecognized Callback: " + callback);
          }
      }
  }
}