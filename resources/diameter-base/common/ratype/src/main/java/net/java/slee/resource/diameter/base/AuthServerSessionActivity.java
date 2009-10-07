package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.ReAuthRequestType;

public interface AuthServerSessionActivity extends AuthSessionActivity {

  /**
   * Create an Abort-Session-Request message populated with the following AVPs:
   * 
   *  * Auth-Application-Id: the value 0 as specified by RFC3588 
   * 
   * @return a new AbortSessionRequest
   */
  AbortSessionRequest createAbortSessionRequest();

  /**
   * Send session abort session request to client
   * 
   * @param request
   * @throws IOException 
   */
  void sendAbortSessionRequest(AbortSessionRequest request) throws IOException;

  /**
   * Create an Re-Auth-Request message populated with the following AVPs:
   * 
   *  * Auth-Application-Id: the value 0 as specified by RFC3588
   *  * Re-Auth-Request-Type: as per reAuthRequestType parameter
   * 
   * @param terminationCause
   * @return a new ReAuthRequest
   */
  ReAuthRequest createReAuthRequest(ReAuthRequestType reAuthRequestType);

  /**
   * Send Re-Auth-Request
   * 
   * @param request
   * @throws IOException 
   */
  void sendReAuthRequest(ReAuthRequest request) throws IOException;

  /**
   * Create an Session-Termination-Answer with the Auth-Application-Id set to 0.
   * 
   * @return
   */
  SessionTerminationAnswer createSessionTerminationAnswer();

  /**
   * Create an Session-Termination-Answer with some AVPs populated from the provided Session-Termination-Request.
   * 
   * @param sessionTerminationRequest
   * @return
   */
  SessionTerminationAnswer createSessionTerminationAnswer(SessionTerminationRequest sessionTerminationRequest);

  /**
   * Send session termination answer to client
   * 
   * @param request
   * @throws IOException 
   */
  void sendSessionTerminationAnswer(SessionTerminationAnswer request) throws IOException;

}
