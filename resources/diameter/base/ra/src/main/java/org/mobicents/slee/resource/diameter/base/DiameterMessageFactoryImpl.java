package org.mobicents.slee.resource.diameter.base;

import org.jdiameter.api.ApplicationId;
import org.jdiameter.api.Message;
import org.jdiameter.api.RawSession;
import org.jdiameter.api.Session;
import org.jdiameter.api.Stack;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AbortSessionRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.AccountingRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.CapabilitiesExchangeAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.CapabilitiesExchangeRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DeviceWatchdogAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DeviceWatchdogRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.DisconnectPeerAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.DisconnectPeerRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.ExtensionDiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.ReAuthRequestImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationAnswerImpl;
import org.mobicents.slee.resource.diameter.base.events.SessionTerminationRequestImpl;

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.AbortSessionAnswer;
import net.java.slee.resource.diameter.base.events.AbortSessionRequest;
import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeAnswer;
import net.java.slee.resource.diameter.base.events.CapabilitiesExchangeRequest;
import net.java.slee.resource.diameter.base.events.DeviceWatchdogAnswer;
import net.java.slee.resource.diameter.base.events.DeviceWatchdogRequest;
import net.java.slee.resource.diameter.base.events.DiameterCommand;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.DisconnectPeerAnswer;
import net.java.slee.resource.diameter.base.events.DisconnectPeerRequest;
import net.java.slee.resource.diameter.base.events.ExtensionDiameterMessage;
import net.java.slee.resource.diameter.base.events.ReAuthAnswer;
import net.java.slee.resource.diameter.base.events.ReAuthRequest;
import net.java.slee.resource.diameter.base.events.SessionTerminationAnswer;
import net.java.slee.resource.diameter.base.events.SessionTerminationRequest;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;

/**
 * Diameter Base Message Factory
 * 
 * <br>Super project:  mobicents
 * <br>6:52:13 PM May 9, 2008 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author Erick Svenson
 */
public class DiameterMessageFactoryImpl implements DiameterMessageFactory
{

  RawSession session;
  
  public DiameterMessageFactoryImpl(Session session, DiameterIdentityAvp ... avps ) {
    // TODO Auto-generated constructor stub
  }

  public DiameterMessageFactoryImpl(Stack stack) {
    try
    {
      this.session = stack.getSessionFactory().getNewRawSession();
    }
    catch ( Exception e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public AbortSessionAnswer createAbortSessionAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ABORT_SESSION_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    AbortSessionAnswer asa = new AbortSessionAnswerImpl( msg );
    
    return asa;
  }

  public AbortSessionAnswer createAbortSessionAnswer()
  {
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ABORT_SESSION_ANSWER, aid );
    
    AbortSessionAnswer asa = new AbortSessionAnswerImpl( msg );
    
    return asa;
  }

  public AbortSessionRequest createAbortSessionRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ABORT_SESSION_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    AbortSessionRequest asr = new AbortSessionRequestImpl( msg );
    
    return asr;
  }

  public AbortSessionRequest createAbortSessionRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ABORT_SESSION_REQUEST, aid );
    
    AbortSessionRequest asr = new AbortSessionRequestImpl( msg );
    
    return asr;
  }

  public AccountingAnswer createAccountingAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ACCOUNTING_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    AccountingAnswer aca = new AccountingAnswerImpl( msg );
    
    return aca;
  }

  public AccountingAnswer createAccountingAnswer()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ACCOUNTING_ANSWER, aid );
    
    AccountingAnswer aca = new AccountingAnswerImpl( msg );
    
    return aca;
  }

  public AccountingRequest createAccountingRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ACCOUNTING_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    AccountingRequest acr = new AccountingRequestImpl( msg );
    
    return acr;
  }

  public AccountingRequest createAccountingRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.ACCOUNTING_REQUEST, aid );
    
    AccountingRequest acr = new AccountingRequestImpl( msg );
    
    return acr;
  }

  public CapabilitiesExchangeAnswer createCapabilitiesExchangeAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.CAPABILITIES_EXCHANGE_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    CapabilitiesExchangeAnswer cea = new CapabilitiesExchangeAnswerImpl( msg );
    
    return cea;
  }

  public CapabilitiesExchangeAnswer createCapabilitiesExchangeAnswer()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.CAPABILITIES_EXCHANGE_ANSWER, aid );
        
    CapabilitiesExchangeAnswer cea = new CapabilitiesExchangeAnswerImpl( msg );
    
    return cea;
  }

  public CapabilitiesExchangeRequest createCapabilitiesExchangeRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.CAPABILITIES_EXCHANGE_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    CapabilitiesExchangeRequest cer = new CapabilitiesExchangeRequestImpl( msg );
    
    return cer;
  }

  public CapabilitiesExchangeRequest createCapabilitiesExchangeRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.CAPABILITIES_EXCHANGE_REQUEST, aid );
    
    CapabilitiesExchangeRequest cer = new CapabilitiesExchangeRequestImpl( msg );
    
    return cer;
  }

  public DeviceWatchdogAnswer createDeviceWatchdogAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DEVICE_WATCHDOG_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    DeviceWatchdogAnswer dwa = new DeviceWatchdogAnswerImpl( msg );
    
    return dwa;
  }

  public DeviceWatchdogAnswer createDeviceWatchdogAnswer()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DEVICE_WATCHDOG_ANSWER, aid );
    
    DeviceWatchdogAnswer dwa = new DeviceWatchdogAnswerImpl( msg );
    
    return dwa;
  }

  public DeviceWatchdogRequest createDeviceWatchdogRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DEVICE_WATCHDOG_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    DeviceWatchdogRequest dwr = new DeviceWatchdogRequestImpl( msg );
    
    return dwr;
  }

  public DeviceWatchdogRequest createDeviceWatchdogRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DEVICE_WATCHDOG_REQUEST, aid );
    
    DeviceWatchdogRequest dwr = new DeviceWatchdogRequestImpl( msg );
    
    return dwr;
  }

  public DisconnectPeerAnswer createDisconnectPeerAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DISCONNECT_PEER_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    DisconnectPeerAnswer dpa = new DisconnectPeerAnswerImpl( msg );
    
    return dpa;
  }

  public DisconnectPeerAnswer createDisconnectPeerAnswer()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DISCONNECT_PEER_ANSWER, aid );
    
    DisconnectPeerAnswer dpa = new DisconnectPeerAnswerImpl( msg );
    
    return dpa;
  }

  public DisconnectPeerRequest createDisconnectPeerRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DISCONNECT_PEER_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    DisconnectPeerRequest dpr = new DisconnectPeerRequestImpl( msg );
    
    return dpr;
  }

  public DisconnectPeerRequest createDisconnectPeerRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.DISCONNECT_PEER_REQUEST, aid );
    
    DisconnectPeerRequest dpr = new DisconnectPeerRequestImpl( msg );
    
    return dpr;
  }

  public ExtensionDiameterMessage createMessage( DiameterCommand command, DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    return new ExtensionDiameterMessageImpl( session.createMessage(command.getCode(), aid) );
  }

  public DiameterMessage createMessage( DiameterHeader header, DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    int commandCode = header.getCommandCode();
    long endToEndId = header.getEndToEndId();
    long hopByHopId = header.getHopByHopId();
    ApplicationId aid = ApplicationId.createByAccAppId( header.getApplicationId() );
    
    Message msg = session.createMessage(commandCode, aid, endToEndId, hopByHopId );
    
    //FIXME: alexandre: Can't instantiate Diameter message... should we check msg type?!
    return new ExtensionDiameterMessageImpl( msg );
  }

  public ReAuthAnswer createReAuthAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.RE_AUTH_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    ReAuthAnswer raa = new ReAuthAnswerImpl( msg );
    
    return raa;
  }

  public ReAuthAnswer createReAuthAnswer()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.RE_AUTH_ANSWER, aid );
    
    ReAuthAnswer raa = new ReAuthAnswerImpl( msg );
    
    return raa;
  }

  public ReAuthRequest createReAuthRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.RE_AUTH_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    ReAuthRequest rar = new ReAuthRequestImpl( msg );
    
    return rar;
  }

  public ReAuthRequest createReAuthRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.RE_AUTH_REQUEST, aid );
    
    ReAuthRequest rar = new ReAuthRequestImpl( msg );
    
    return rar;
  }

  public SessionTerminationAnswer createSessionTerminationAnswer( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.SESSION_TERMINATION_ANSWER, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    SessionTerminationAnswer sta = new SessionTerminationAnswerImpl( msg );
    
    return sta;
  }

  public SessionTerminationAnswer createSessionTerminationAnswer()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.SESSION_TERMINATION_ANSWER, aid );
    
    SessionTerminationAnswer sta = new SessionTerminationAnswerImpl( msg );
    
    return sta;
  }

  public SessionTerminationRequest createSessionTerminationRequest( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.SESSION_TERMINATION_REQUEST, aid );
    
    for(DiameterAvp avp : avps)
    {
      // FIXME: alexandre: Should we look at the types and add them with proper function?
      msg.getAvps().addAvp( avp.getCode(), avp.byteArrayValue() );
    }
    
    SessionTerminationRequest str = new SessionTerminationRequestImpl( msg );
    
    return str;
  }

  public SessionTerminationRequest createSessionTerminationRequest()
  {
    // FIXME: alexandre: What should be used here?
    ApplicationId aid = ApplicationId.createByAccAppId( ApplicationId.Standard.DIAMETER_COMMON_MESSAGE );
    
    Message msg = session.createMessage( Message.SESSION_TERMINATION_REQUEST, aid );
    
    SessionTerminationRequest str = new SessionTerminationRequestImpl( msg );
    
    return str;
  }
}