package net.java.slee.resource.diameter.base;

/**
 * Enumeration of Accounting session states. 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public enum AccountingSessionState {
  Idle, PendingS, PendingE, PendingB, Open, PendingI, PendingL;
}
