package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpVPrN"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpVPrNPOATie
	extends IpVPrNPOA
{
	private IpVPrNOperations _delegate;

	private POA _poa;
	public IpVPrNPOATie(IpVPrNOperations delegate)
	{
		_delegate = delegate;
	}
	public IpVPrNPOATie(IpVPrNOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpVPrN _this()
	{
		return org.csapi.cm.IpVPrNHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpVPrN _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpVPrNHelper.narrow(_this_object(orb));
	}
	public IpVPrNOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpVPrNOperations delegate)
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
	public void setCallbackWithSessionID(org.csapi.IpInterface appInterface, int sessionID) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions,org.csapi.P_INVALID_SESSION_ID
	{
_delegate.setCallbackWithSessionID(appInterface,sessionID);
	}

	public org.csapi.IpInterface getVPrP(java.lang.String vPrPID) throws org.csapi.cm.P_UNKNOWN_VPRP_ID,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VPRPID
	{
		return _delegate.getVPrP(vPrPID);
	}

	public org.csapi.IpInterface createVPrP(org.csapi.IpInterface templateRef) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_INTERFACE,org.csapi.cm.P_ILLEGAL_REF_VALUE
	{
		return _delegate.createVPrP(templateRef);
	}

	public java.lang.String[] getVPrPList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VPRP
	{
		return _delegate.getVPrPList();
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public void deleteVPrP(java.lang.String vPrPID) throws org.csapi.cm.P_UNKNOWN_VPRP_ID,org.csapi.cm.P_CANT_DELETE_VPRP,org.csapi.TpCommonExceptions
	{
_delegate.deleteVPrP(vPrPID);
	}

}
