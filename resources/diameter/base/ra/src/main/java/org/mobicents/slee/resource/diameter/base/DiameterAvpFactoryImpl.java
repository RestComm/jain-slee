package org.mobicents.slee.resource.diameter.base;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.NoSuchAvpException;
import net.java.slee.resource.diameter.base.events.DiameterCommand;
import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvp;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

import org.jdiameter.api.Avp;
import org.jdiameter.client.impl.parser.MessageParser;
import org.mobicents.slee.resource.diameter.base.events.DiameterCommandImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.DiameterAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.FailedAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ProxyInfoAvpImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvpImpl;

/**
 * 
 * <br>Super project:  mobicents
 * <br>7:52:06 PM May 13, 2008 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 * @author Erick Svenson
 */
public class DiameterAvpFactoryImpl implements DiameterAvpFactory
{
  private final long DEFAULT_VENDOR_ID = 0L;
  private final int DEFAULT_MANDATORY = 0;
  private final int DEFAULT_PROTECTED = 0;
  
  protected MessageParser parser = new MessageParser(null);
  
  private class AvpCodeAndVendor
  {
    private int code;
    private long vendorID;
    
    public AvpCodeAndVendor(int code, long vendorID)
    {
      this.code = code;
      this.vendorID = vendorID;
    }

    public int getCode()
    {
      return code;
    }

    public long getVendorID()
    {
      return vendorID;
    }
    
    @Override
    public boolean equals( Object obj )
    {
      if(obj instanceof AvpCodeAndVendor)
      {
        AvpCodeAndVendor other = (AvpCodeAndVendor)obj;
        return (this.code == other.code && this.vendorID == other.vendorID);
      }
      
      return false;
    }
    
    @Override
    public int hashCode()
    {
      return this.code * 31 + (int)vendorID; 
    }
  }
  
  private DiameterAvpType getAvpType(int code, long vendorID) throws NoSuchAvpException
  {
    // Should use Ethereal like dictionary:
    // <avp name="Session-Id" code="263" mandatory="must" protected="mustnot" vendor-bit="mustnot">
    //   <type type-name="UTF8String"/>
    // </avp>
    // <avp name="Origin-Host" code="264" mandatory="must" may-encrypt="no" protected="mustnot" vendor-bit="mustnot">
    //   <type type-name="DiameterIdentity"/>
    // </avp>
    // <avp name="Supported-Vendor-Id" code="265" mandatory="must" may-encrypt="no" protected="mustnot" vendor-bit="mustnot">
    //   <type type-name="VendorId"/>
    // </avp>
    // <avp name="Vendor-Id" code="266" mandatory="must" may-encrypt="no" protected="mustnot" vendor-bit="mustnot">
    //   <type type-name="VendorId"/>
    // </avp>
    
    // FIXME: We should figure a way to get the AVP type from code/vendor pair 
    Map<AvpCodeAndVendor, DiameterAvpType> diameterTypes = new HashMap();
    
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACCT_INTERIM_INTERVAL, 0L), DiameterAvpType.UNSIGNED_32);
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACCOUNTING_REALTIME_REQUIRED, 0L), DiameterAvpType.ENUMERATED);
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACC_MULTI_SESSION_ID, 0L), DiameterAvpType.UTF8_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACC_RECORD_NUMBER, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACC_RECORD_TYPE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACC_SESSION_ID, 0L), DiameterAvpType.OCTET_STRING);
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACC_SUB_SESSION_ID, 0L), DiameterAvpType.UNSIGNED_64 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ACCT_APPLICATION_ID, 0L), DiameterAvpType.UNSIGNED_32);
    diameterTypes.put( new AvpCodeAndVendor(Avp.AUTH_APPLICATION_ID, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.AUTH_REQUEST_TYPE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.AUTHORIZATION_LIFETIME, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.AUTH_GRACE_PERIOD, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.AUTH_SESSION_STATE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.RE_AUTH_REQUEST_TYPE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.CLASS, 0L), DiameterAvpType.OCTET_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.DESTINATION_HOST, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.DESTINATION_REALM, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.DISCONNECT_CAUSE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.E2E_SEQUENCE_AVP, 0L), DiameterAvpType.GROUPED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ERROR_MESSAGE, 0L), DiameterAvpType.UTF8_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ERROR_REPORTING_HOST, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.EVENT_TIMESTAMP, 0L), DiameterAvpType.TIME );
    diameterTypes.put( new AvpCodeAndVendor(Avp.EXPERIMENTAL_RESULT, 0L), DiameterAvpType.GROUPED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.EXPERIMENTAL_RESULT_CODE, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.FAILED_AVP, 0L), DiameterAvpType.GROUPED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.FIRMWARE_REVISION, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.HOST_IP_ADDRESS, 0L), DiameterAvpType.ADDRESS );
    diameterTypes.put( new AvpCodeAndVendor(Avp.INBAND_SECURITY_ID, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.MULTI_ROUND_TIMEOUT, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ORIGIN_HOST, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ORIGIN_REALM, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ORIGIN_STATE_ID, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.PRODUCT_NAME, 0L), DiameterAvpType.UTF8_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.PROXY_HOST, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.PROXY_INFO, 0L), DiameterAvpType.GROUPED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.PROXY_STATE, 0L), DiameterAvpType.OCTET_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.REDIRECT_HOST, 0L), DiameterAvpType.DIAMETER_URI );
    diameterTypes.put( new AvpCodeAndVendor(Avp.REDIRECT_HOST_USAGE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.REDIRECT_MAX_CACHE_TIME, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.RESULT_CODE, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.ROUTE_RECORD, 0L), DiameterAvpType.DIAMETER_IDENTITY );
    diameterTypes.put( new AvpCodeAndVendor(Avp.SESSION_ID, 0L), DiameterAvpType.UTF8_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.SESSION_TIMEOUT, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.SESSION_BINDING, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.SESSION_SERVER_FAILOVER, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.SUPPORTED_VENDOR_ID, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.TERMINATION_CAUSE, 0L), DiameterAvpType.ENUMERATED );
    diameterTypes.put( new AvpCodeAndVendor(Avp.USER_NAME, 0L), DiameterAvpType.UTF8_STRING );
    diameterTypes.put( new AvpCodeAndVendor(Avp.VENDOR_ID, 0L), DiameterAvpType.UNSIGNED_32 );
    diameterTypes.put( new AvpCodeAndVendor(Avp.VENDOR_SPECIFIC_APPLICATION_ID, 0L), DiameterAvpType.GROUPED );
    
    
    DiameterAvpType avpType = diameterTypes.get( new AvpCodeAndVendor(code, vendorID) );
    
    if(avpType == null)
    {
      throw new NoSuchAvpException("No such AVP for Code[" + code + "], Vendor[" + vendorID + "].");
    }
    
    return avpType;
  }
  
  public DiameterAvp createAvp( int avpCode, DiameterAvp[] avps ) throws NoSuchAvpException, AvpNotAllowedException
  {
    GroupedAvpImpl avp = new GroupedAvpImpl(avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    avp.setExtensionAvps( avps );
    
    return avp;
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, DiameterAvp[] avps ) throws NoSuchAvpException, AvpNotAllowedException
  {
    GroupedAvpImpl avp = new GroupedAvpImpl(avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    avp.setExtensionAvps( avps );
    
    return avp;
  }

  public DiameterAvp createAvp( int avpCode, byte[] value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, value, avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, byte[] value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, value, avpType );
  }

  public DiameterAvp createAvp( int avpCode, int value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.int32ToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, int value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );

    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.int32ToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int avpCode, long value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    byte[] byteValue = null;
    
    if( avpType.getType() ==  DiameterAvpType._INTEGER_64 )
      byteValue = parser.int64ToBytes(value);
    else if ( avpType.getType() ==  DiameterAvpType._UNSIGNED_32 )
      byteValue = parser.intU32ToBytes(value);
    else
      throw new NoSuchAvpException("Unrecongnized type");
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, byteValue, avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, long value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    byte[] byteValue = null;
    
    if( avpType.getType() ==  DiameterAvpType._INTEGER_64 )
      byteValue = parser.int64ToBytes(value);
    else if ( avpType.getType() ==  DiameterAvpType._UNSIGNED_32 )
      byteValue = parser.intU32ToBytes(value);
    else
      throw new NoSuchAvpException("Unrecongnized type");
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, byteValue, avpType );
  }

  public DiameterAvp createAvp( int avpCode, float value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.float32ToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, float value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.float32ToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int avpCode, double value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.float64ToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, double value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.float64ToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int avpCode, InetAddress value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.addressToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, InetAddress value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.addressToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int avpCode, Date value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.dateToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, Date value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.dateToBytes(value), avpType );
  }

  public DiameterAvp createAvp( int avpCode, String value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    byte[] byteValue = null;
      
    try
    {
      if( avpType.getType() ==  DiameterAvpType._OCTET_STRING )
        byteValue = parser.octetStringToBytes(value);
      else if ( avpType.getType() ==  DiameterAvpType._UTF8_STRING )
        byteValue = parser.utf8StringToBytes(value);
      else
        throw new NoSuchAvpException("Unrecongnized type");
    }
    catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }
    
    return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, byteValue, avpType );
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, String value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    byte[] byteValue = null;
      
    try
    {
      if( avpType.getType() ==  DiameterAvpType._OCTET_STRING )
        byteValue = parser.octetStringToBytes(value);
      else if ( avpType.getType() ==  DiameterAvpType._UTF8_STRING )
        byteValue = parser.utf8StringToBytes(value);
      else
        throw new NoSuchAvpException("Unrecongnized type");
    }
    catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }
    
    return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, byteValue, avpType );
  }

  public DiameterAvp createAvp( int avpCode, Enumerated value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, 0L );
    
    try
    {
      // FIXME: alexandre: Should we use objectToBytes?
      return new DiameterAvpImpl( avpCode, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.objectToBytes(value), avpType );
    }
    catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }
  }

  public DiameterAvp createAvp( int vendorID, int avpCode, Enumerated value ) throws NoSuchAvpException
  {
    DiameterAvpType avpType = getAvpType( avpCode, vendorID );
    
    try
    {
      // FIXME: alexandre: Should we use objectToBytes?
      return new DiameterAvpImpl( avpCode, vendorID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, parser.objectToBytes(value), avpType );
    }
    catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
      return null;
    }
  }

  public DiameterCommand createCommand( int commandCode, int applicationId, String shortName, String longName, boolean isRequest, boolean isProxiable )
  {
    return new DiameterCommandImpl( commandCode, applicationId, shortName, longName, isRequest, isProxiable );
  }

  public ExperimentalResultAvp createExperimentalResult( long vendorId, long experimentalResultCode )
  {
    // TODO Auto-generated method stub
    return null;
  }

  public ExperimentalResultAvp createExperimentalResult()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public ExperimentalResultAvp createExperimentalResult( DiameterAvp avp ) throws AvpNotAllowedException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public ExperimentalResultAvp createExperimentalResult( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public FailedAvp createFailedAvp()
  {
    return new FailedAvpImpl(DiameterAvpCodes.FAILED_AVP, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
  }

  public FailedAvp createFailedAvp( DiameterAvp avp )
  {
    FailedAvp favp = new FailedAvpImpl(DiameterAvpCodes.FAILED_AVP, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    try
    {
      // FIXME: alexandre: is this correct? are they considered as extensions?
      favp.setExtensionAvps( new DiameterAvp[]{ avp } );
    }
    catch ( AvpNotAllowedException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    
    return favp;
  }

  public FailedAvp createFailedAvp( DiameterAvp[] avps )
  {
    FailedAvp favp = new FailedAvpImpl(DiameterAvpCodes.FAILED_AVP, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    try
    {
      // FIXME: alexandre: is this correct? are they considered as extensions?
      favp.setExtensionAvps( avps );
    }
    catch ( AvpNotAllowedException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    
    return favp;
  }

  public ProxyInfoAvp createProxyInfo( DiameterIdentityAvp proxyHost, byte[] proxyState )
  {
    ProxyInfoAvp proxyInfo = new ProxyInfoAvpImpl(DiameterAvpCodes.PROXY_INFO, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    proxyInfo.setProxyHost( proxyHost );
    proxyInfo.setProxyState( proxyState );
    
    return proxyInfo;
  }

  public ProxyInfoAvp createProxyInfo()
  {
    return new ProxyInfoAvpImpl(DiameterAvpCodes.PROXY_INFO, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
  }

  public ProxyInfoAvp createProxyInfo( DiameterAvp avp )
  {
    ProxyInfoAvp proxyInfo = new ProxyInfoAvpImpl(DiameterAvpCodes.PROXY_INFO, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
   
    try
    {
      // FIXME: alexandre: is this correct? are they considered as extensions?
      proxyInfo.setExtensionAvps( new DiameterAvp[]{avp} );
    }
    catch ( AvpNotAllowedException e )
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }
    
    return proxyInfo;
  }

  public ProxyInfoAvp createProxyInfo( DiameterAvp[] avps )
  {
    ProxyInfoAvp proxyInfo = new ProxyInfoAvpImpl(DiameterAvpCodes.PROXY_INFO, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    try
    {
      // FIXME: alexandre: is this correct? are they considered as extensions?
      proxyInfo.setExtensionAvps( avps );
    }
    catch ( AvpNotAllowedException e )
    {
      // TODO handle exception
      e.printStackTrace();
      return null;
    }
    
    return proxyInfo;
  }

  public VendorSpecificApplicationIdAvp createVendorSpecificApplicationId( long vendorId )
  {
    return new VendorSpecificApplicationIdAvpImpl(DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID, vendorId, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
  }

  public VendorSpecificApplicationIdAvp createVendorSpecificApplicationId()
  {
    return new VendorSpecificApplicationIdAvpImpl(DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
  }

  public VendorSpecificApplicationIdAvp createVendorSpecificApplicationId( DiameterAvp avp ) throws AvpNotAllowedException
  {
    VendorSpecificApplicationIdAvp vsaid = new VendorSpecificApplicationIdAvpImpl(DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    // FIXME: alexandre: is this correct? are they considered as extensions?
    vsaid.setExtensionAvps( new DiameterAvp[]{ avp } );
    
    return vsaid;
  }

  public VendorSpecificApplicationIdAvp createVendorSpecificApplicationId( DiameterAvp[] avps ) throws AvpNotAllowedException
  {
    VendorSpecificApplicationIdAvp vsaid = new VendorSpecificApplicationIdAvpImpl(DiameterAvpCodes.VENDOR_SPECIFIC_APPLICATION_ID, DEFAULT_VENDOR_ID, DEFAULT_MANDATORY, DEFAULT_PROTECTED, new byte[]{});
    
    // FIXME: alexandre: is this correct? are they considered as extensions?
    vsaid.setExtensionAvps( avps );
    
    return vsaid;
  }

}
