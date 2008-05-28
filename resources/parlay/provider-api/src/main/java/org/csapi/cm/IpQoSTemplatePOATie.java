package org.csapi.cm;

import org.omg.PortableServer.POA;

/**
 *	Generated from IDL interface "IpQoSTemplate"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public class IpQoSTemplatePOATie
	extends IpQoSTemplatePOA
{
	private IpQoSTemplateOperations _delegate;

	private POA _poa;
	public IpQoSTemplatePOATie(IpQoSTemplateOperations delegate)
	{
		_delegate = delegate;
	}
	public IpQoSTemplatePOATie(IpQoSTemplateOperations delegate, POA poa)
	{
		_delegate = delegate;
		_poa = poa;
	}
	public org.csapi.cm.IpQoSTemplate _this()
	{
		return org.csapi.cm.IpQoSTemplateHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpQoSTemplate _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpQoSTemplateHelper.narrow(_this_object(orb));
	}
	public IpQoSTemplateOperations _delegate()
	{
		return _delegate;
	}
	public void _delegate(IpQoSTemplateOperations delegate)
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

	public void setProvisionedQoSInfo(org.csapi.cm.TpProvisionedQoSInfo provisionedQoSInfo) throws org.csapi.cm.P_ILLEGAL_TAG,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION
	{
_delegate.setProvisionedQoSInfo(provisionedQoSInfo);
	}

	public org.csapi.cm.TpDsCodepoint getDsCodepoint() throws org.csapi.cm.P_UNKNOWN_DSCODEPOINT,org.csapi.TpCommonExceptions
	{
		return _delegate.getDsCodepoint();
	}

	public void setValidityInfo(org.csapi.cm.TpValidityInfo validityInfo) throws org.csapi.cm.P_ILLEGAL_TAG,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION
	{
_delegate.setValidityInfo(validityInfo);
	}

	public void setPipeQoSInfo(org.csapi.cm.TpPipeQoSInfo pipeQoSInfo) throws org.csapi.cm.P_ILLEGAL_TAG,org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_VALUE,org.csapi.cm.P_ILLEGAL_COMBINATION
	{
_delegate.setPipeQoSInfo(pipeQoSInfo);
	}

	public java.lang.String getDescription() throws org.csapi.cm.P_UNKNOWN_DESCRIPTION,org.csapi.TpCommonExceptions
	{
		return _delegate.getDescription();
	}

	public org.csapi.cm.TpValidityInfo getValidityInfo() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_VALIDITY_INFO
	{
		return _delegate.getValidityInfo();
	}

	public java.lang.String getTemplateType() throws org.csapi.TpCommonExceptions,org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPE
	{
		return _delegate.getTemplateType();
	}

	public void setSlaID(java.lang.String slaID) throws org.csapi.TpCommonExceptions,org.csapi.cm.P_ILLEGAL_SLA_ID
	{
_delegate.setSlaID(slaID);
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
