package org.mobicents.slee.resource.sip;

import java.util.HashSet;
import java.util.Set;

import javax.sip.ListeningPoint;
import javax.sip.SipProvider;
import javax.sip.address.AddressFactory;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;


/**
 * <p>Title: SIP_RA</p>
 * <p>Description: JAIN SIP Resource Adaptor</p>
 * @author mvera@lucent.com
 * @version 1.0
 */
public class SipFactoryProvider
    implements SipResourceAdaptorSbbInterface, java.io.Serializable {
  private transient AddressFactory addressFactory;
  private transient MessageFactory messageFactory;
  private transient HeaderFactory headerFactory;
  private transient SipProviderProxy sipProvider;
  private transient String hostAddress=null;
  private transient String[] transports=null;
  private transient int port=-1;
  
  
  private void check() {
    if ( sipProvider == null )
      throw new java.lang.IllegalStateException(
        "The resource adaptor has already been stopped"
      );
  }

  public void release() {
    if ( sipProvider != null ) {
      sipProvider.release();
    }
    addressFactory=null;
    messageFactory=null;
    headerFactory=null;
    sipProvider=null;
  }

  public SipFactoryProvider(AddressFactory addressFactory,
          MessageFactory messageFactory, HeaderFactory headerFactory,
          SipProvider sipProvider, SipResourceAdaptor sipResourceAdaptor) {
      this.addressFactory = addressFactory;
      this.messageFactory = messageFactory;
      this.headerFactory = headerFactory;
      this.sipProvider = new SipProviderProxy(sipProvider, sipResourceAdaptor);
      Set transports=new HashSet();

      ListeningPoint[] lps=sipProvider.getListeningPoints();
      
      for(int i=0;i<lps.length;i++)
    	  transports.add(lps[i].getTransport());
      
      this.transports=new String[transports.size()];
      this.transports=(String[]) transports.toArray(this.transports);
      this.hostAddress=lps[0].getIPAddress();
      this.port=lps[0].getPort();
  }

  /**
   *
   * @return Returns the addressFactory.
   */
  public AddressFactory getAddressFactory() {
    check();
    return addressFactory;
  }

  /**
   *
   * @return Returns the headerFactory.
   */
  public HeaderFactory getHeaderFactory() {
    check();
    return headerFactory;
  }

  /**
   *
   * @return Returns the messageFactory.
   */
  public MessageFactory getMessageFactory() {
    check();
    return messageFactory;
  }

  /**
   *
   * @return Returns the sipProvider.
   */
  public SipProvider getSipProvider() {
    check();
    return sipProvider;
  }
  /**
   * @return ip address to which ra has bound its sip stack
   */
  public String getHostAddress()
  {
	  return hostAddress;
  }
  
  public int getHostPort()
  {
	  return port;
  }
  public String[] getTransports()
  {
	  return transports;
  }
  
}
