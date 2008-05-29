package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpVPrP"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpVPrPPOATie
	extends IpVPrPPOA
{
	private IpVPrPOperations _delegate;

	private POA _poa;
	public IpVPrPPOATie(IpVPrPOperations delegate)
	{
		_delegate = delegate;
	}
	public IpVPrPPOATie(IpVPrPOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpVPrP _this()
	{
		return org.csapi.cm.IpVPrPHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpVPrP _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpVPrPHelper.narrow(_this_object(orb));
	}
	public IpVPrPOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpVPrPOperations delegate)
	{
		_delegate = delegate;
	}
	public POA _default_POA()
	{
		if (_poa != null)
		{
			return _poa;
		}
		else
		{
			return super._default_POA();
		}
	}
	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public org.csapi.cm.TpProvisionedQoSInfo getProvisionedQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_QOS_INFO
	{
		return _delegate.getProvisionedQoSInfo();
	}

	public org.csapi.cm.TpVprpStatus getStatus() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_STATUS
	{
		return _delegate.getStatus();
	}

	public org.csapi.cm.TpDsCodepoint getDsCodepoint() throws org.csapi.cm.P_UNKNOWN_DSCODEPOINT,org.csapi.TpCommonExceptions
	{
		return _delegate.getDsCodepoint();
	}

	public java.lang.String getVPrPID() throws org.csapi.cm.P_UNKNOWN_VPRP_ID,org.csapi.TpCommonExceptions
	{
		return _delegate.getVPrPID();
	}

	public org.csapi.cm.TpValidityInfo getValidityInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VALIDITY_INFO
	{
		return _delegate.getValidityInfo();
	}

	public java.lang.String getSlaID() throws org.csapi.cm.P_UNKNOWN_SLA_ID,org.csapi.TpCommonExceptions
	{
		return _delegate.getSlaID();
	}

	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public org.csapi.cm.TpPipeQoSInfo getPipeQoSInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_PIPEQOSINFO
	{
		return _delegate.getPipeQoSInfo();
	}

}
