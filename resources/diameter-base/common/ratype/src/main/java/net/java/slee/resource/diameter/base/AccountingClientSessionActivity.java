package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;

/**
 * 
 * An AccountingClientSessionActivity represents an accounting session for Diameter accounting clients.
 * 
 * All requests for the session must be sent via the same AccountingClientSessionActivity.
 * 
 * All responses related to the session will be received as events fired on the same AccountingClientSessionActivity.
 * 
 * @author OpenCloud 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AccountingClientSessionActivity extends AccountingSessionActivity {

  /**
   * Create an Accounting-Request message populated with the following AVPs:
   *
   * * Accounting-Record-Type: as per accountingRecordType parameter
   * * Acct-Application-Id: the value 3 as specified by RFC3588 
   * 
   * @param accountingRecordType value for the Accounting-Record-Type AVP
   * @return a new AccountingRequest
   */
  AccountingRequest createAccountingRequest(AccountingRecordType accountingRecordType);

  /**
   * Send an Accounting Request. An event containing the answer will be fired on this activity. 
   * 
   * @param accountingRequest the Accounting-Request message to send
   * @throws IOException if the message could not be sent
   * @throws IllegalArgumentException if accountingRequest is missing any required AVPs
   */
  void sendAccountRequest(AccountingRequest accountingRequest) throws IOException, IllegalArgumentException;

}
