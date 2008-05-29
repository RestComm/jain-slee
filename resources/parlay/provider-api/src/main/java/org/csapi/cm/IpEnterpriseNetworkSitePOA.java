package org.csapi.cm;

/**
 *	Generated from IDL interface "IpEnterpriseNetworkSite"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpEnterpriseNetworkSitePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cm.IpEnterpriseNetworkSiteOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "getSAPIPSubnet", new java.lang.Integer(1));
		m_opsHash.put ( "getSiteLocation", new java.lang.Integer(2));
		m_opsHash.put ( "getVPrN", new java.lang.Integer(3));
		m_opsHash.put ( "getIPSubnet", new java.lang.Integer(4));
		m_opsHash.put ( "getSiteList", new java.lang.Integer(5));
		m_opsHash.put ( "getSiteID", new java.lang.Integer(6));
		m_opsHash.put ( "getSite", new java.lang.Integer(7));
		m_opsHash.put ( "getSAPList", new java.lang.Integer(8));
		m_opsHash.put ( "getSiteDescription", new java.lang.Integer(9));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(10));
	}
	private String[] ids = {"IDL:org/csapi/cm/IpEnterpriseNetworkSite:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/cm/IpEnterpriseNetwork:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.cm.IpEnterpriseNetworkSite _this()
	{
		return org.csapi.cm.IpEnterpriseNetworkSiteHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpEnterpriseNetworkSite _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpEnterpriseNetworkSiteHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		// quick lookup of operation
		java.lang.Integer opsIndex = (java.lang.Integer)m_opsHash.get ( method );
		if ( null == opsIndex )
			throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
		switch ( opsIndex.intValue() )
		{
			case 0: // setCallback
			{
			try
			{
				org.csapi.IpInterface _arg0=org.csapi.IpInterfaceHelper.read(_input);
				_out = handler.createReply();
				setCallback(_arg0);
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
				break;
			}
			case 1: // getSAPIPSubnet
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.cm.TpIPSubnetHelper.write(_out,getSAPIPSubnet(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_SAP _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SAPHelper.write(_out, _ex1);
			}
			catch(org.csapi.cm.P_ILLEGAL_SITE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_SITE_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.cm.P_UNKNOWN_IPSUBNET _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.write(_out, _ex3);
			}
				break;
			}
			case 2: // getSiteLocation
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getSiteLocation());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_SITE_LOCATION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SITE_LOCATIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 3: // getVPrN
			{
			try
			{
				_out = handler.createReply();
				org.csapi.IpInterfaceHelper.write(_out,getVPrN());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_VPRN _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_VPRNHelper.write(_out, _ex1);
			}
				break;
			}
			case 4: // getIPSubnet
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cm.TpIPSubnetHelper.write(_out,getIPSubnet());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_IPSUBNET _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_IPSUBNETHelper.write(_out, _ex1);
			}
				break;
			}
			case 5: // getSiteList
			{
			try
			{
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,getSiteList());
			}
			catch(org.csapi.cm.P_UNKNOWN_SITES _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SITESHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
				break;
			}
			case 6: // getSiteID
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getSiteID());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_SITE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SITE_IDHelper.write(_out, _ex1);
			}
				break;
			}
			case 7: // getSite
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				org.csapi.IpInterfaceHelper.write(_out,getSite(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_ILLEGAL_SITE_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_SITE_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.cm.P_UNKNOWN_SITE_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SITE_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 8: // getSAPList
			{
			try
			{
				_out = handler.createReply();
				org.csapi.TpStringListHelper.write(_out,getSAPList());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_SAPS _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SAPSHelper.write(_out, _ex1);
			}
				break;
			}
			case 9: // getSiteDescription
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getSiteDescription());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTION _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_SITE_DESCRIPTIONHelper.write(_out, _ex1);
			}
				break;
			}
			case 10: // setCallbackWithSessionID
			{
			try
			{
				org.csapi.IpInterface _arg0=org.csapi.IpInterfaceHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				setCallbackWithSessionID(_arg0,_arg1);
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
				break;
			}
		}
		return _out;
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
