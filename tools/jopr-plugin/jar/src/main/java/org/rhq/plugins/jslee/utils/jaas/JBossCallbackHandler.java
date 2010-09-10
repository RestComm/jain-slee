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