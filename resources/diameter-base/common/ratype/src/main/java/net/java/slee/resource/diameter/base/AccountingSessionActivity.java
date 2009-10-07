package net.java.slee.resource.diameter.base;

/**
 * 
 * Superinterface for accounting activities.
 *
 * @author OpenCloud
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AccountingSessionActivity extends DiameterActivity{

  /**
   * Returns accounting session state of underlying session. Valid values are: Idle,PendingS,PendingE,PendingB,Open,PendingI,PendingL
   * {@link AccountingSessionState}
   * 
   * @return
   */
  AccountingSessionState getAccountingSessionState();

}
