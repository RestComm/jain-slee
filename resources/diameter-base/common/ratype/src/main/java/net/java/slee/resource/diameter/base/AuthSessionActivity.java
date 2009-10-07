package net.java.slee.resource.diameter.base;

/**
 *
 * Superinterface for authorization activities
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AuthSessionActivity extends DiameterActivity {

  /**
   * Return current auth session state - it can have values as follows: Idle,Pending,Open,Disconnected.<br>
   * Disconnected value implies that activity is ending
   * @return
   */
  AuthSessionState getSessionState();

}
