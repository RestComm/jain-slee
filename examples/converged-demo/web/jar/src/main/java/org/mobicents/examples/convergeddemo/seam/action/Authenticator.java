package org.mobicents.examples.convergeddemo.seam.action;

import javax.ejb.Local;

@Local
public interface Authenticator
{
  boolean authenticate();
}
