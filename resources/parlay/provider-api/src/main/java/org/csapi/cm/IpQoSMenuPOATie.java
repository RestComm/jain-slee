package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpQoSMenu"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpQoSMenuPOATie
	extends IpQoSMenuPOA
{
	private IpQoSMenuOperations _delegate;

	private POA _poa;
	public IpQoSMenuPOATie(IpQoSMenuOperations delegate)
	{
		_delegate = delegate;
	}
	public IpQoSMenuPOATie(IpQoSMenuOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpQoSMenu _this()
	{
		return org.csapi.cm.IpQoSMenuHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpQoSMenu _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpQoSMenuHelper.narrow(_this_object(orb));
	}
	public IpQoSMenuOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpQoSMenuOperations delegate)
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

	public java.lang.String[] getTemplateList() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_TEMPLATES
	{
		return _delegate.getTemplateList();
	}

	public void setCallback(org.csapi.IpInterface appInterface) throws org.csapi.P_INVALID_INTERFACE_TYPE,org.csapi.TpCommonExceptions
	{
_delegate.setCallback(appInterface);
	}

	public org.csapi.IpInterface getTemplate(java.lang.String templateType) throws org.csapi.TpCommonExceptions
	{
		return _delegate.getTemplate(templateType);
	}

}
