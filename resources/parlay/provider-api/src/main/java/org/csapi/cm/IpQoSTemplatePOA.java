package org.csapi.cm;

/**
 *	Generated from IDL interface "IpQoSTemplate"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpQoSTemplatePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cm.IpQoSTemplateOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "getProvisionedQoSInfo", new java.lang.Integer(1));
		m_opsHash.put ( "setProvisionedQoSInfo", new java.lang.Integer(2));
		m_opsHash.put ( "getDsCodepoint", new java.lang.Integer(3));
		m_opsHash.put ( "setValidityInfo", new java.lang.Integer(4));
		m_opsHash.put ( "setPipeQoSInfo", new java.lang.Integer(5));
		m_opsHash.put ( "getDescription", new java.lang.Integer(6));
		m_opsHash.put ( "getValidityInfo", new java.lang.Integer(7));
		m_opsHash.put ( "getTemplateType", new java.lang.Integer(8));
		m_opsHash.put ( "setSlaID", new java.lang.Integer(9));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(10));
		m_opsHash.put ( "getPipeQoSInfo", new java.lang.Integer(11));
	}
	private String[] ids = {"IDL:org/csapi/cm/IpQoSTemplate:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.cm.IpQoSTemplate _this()
	{
		return org.csapi.cm.IpQoSTemplateHelper.narrow(_this_object());
	}
	public org.csapi.cm.IpQoSTemplate _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cm.IpQoSTemplateHelper.narrow(_this_object(orb));
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
			case 1: // getProvisionedQoSInfo
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cm.TpProvisionedQoSInfoHelper.write(_out,getProvisionedQoSInfo());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_QOS_INFO _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_QOS_INFOHelper.write(_out, _ex1);
			}
				break;
			}
			case 2: // setProvisionedQoSInfo
			{
			try
			{
				org.csapi.cm.TpProvisionedQoSInfo _arg0=org.csapi.cm.TpProvisionedQoSInfoHelper.read(_input);
				_out = handler.createReply();
				setProvisionedQoSInfo(_arg0);
			}
			catch(org.csapi.cm.P_ILLEGAL_TAG _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_TAGHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.cm.P_ILLEGAL_VALUE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_VALUEHelper.write(_out, _ex2);
			}
			catch(org.csapi.cm.P_ILLEGAL_COMBINATION _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.write(_out, _ex3);
			}
				break;
			}
			case 3: // getDsCodepoint
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cm.TpDsCodepointHelper.write(_out,getDsCodepoint());
			}
			catch(org.csapi.cm.P_UNKNOWN_DSCODEPOINT _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_DSCODEPOINTHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
				break;
			}
			case 4: // setValidityInfo
			{
			try
			{
				org.csapi.cm.TpValidityInfo _arg0=org.csapi.cm.TpValidityInfoHelper.read(_input);
				_out = handler.createReply();
				setValidityInfo(_arg0);
			}
			catch(org.csapi.cm.P_ILLEGAL_TAG _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_TAGHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.cm.P_ILLEGAL_VALUE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_VALUEHelper.write(_out, _ex2);
			}
			catch(org.csapi.cm.P_ILLEGAL_COMBINATION _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.write(_out, _ex3);
			}
				break;
			}
			case 5: // setPipeQoSInfo
			{
			try
			{
				org.csapi.cm.TpPipeQoSInfo _arg0=org.csapi.cm.TpPipeQoSInfoHelper.read(_input);
				_out = handler.createReply();
				setPipeQoSInfo(_arg0);
			}
			catch(org.csapi.cm.P_ILLEGAL_TAG _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_TAGHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.cm.P_ILLEGAL_VALUE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_VALUEHelper.write(_out, _ex2);
			}
			catch(org.csapi.cm.P_ILLEGAL_COMBINATION _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_COMBINATIONHelper.write(_out, _ex3);
			}
				break;
			}
			case 6: // getDescription
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getDescription());
			}
			catch(org.csapi.cm.P_UNKNOWN_DESCRIPTION _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_DESCRIPTIONHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
				break;
			}
			case 7: // getValidityInfo
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cm.TpValidityInfoHelper.write(_out,getValidityInfo());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_VALIDITY_INFO _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_VALIDITY_INFOHelper.write(_out, _ex1);
			}
				break;
			}
			case 8: // getTemplateType
			{
			try
			{
				_out = handler.createReply();
				_out.write_string(getTemplateType());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_TEMPLATE_TYPEHelper.write(_out, _ex1);
			}
				break;
			}
			case 9: // setSlaID
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				_out = handler.createReply();
				setSlaID(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_ILLEGAL_SLA_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_ILLEGAL_SLA_IDHelper.write(_out, _ex1);
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
			case 11: // getPipeQoSInfo
			{
			try
			{
				_out = handler.createReply();
				org.csapi.cm.TpPipeQoSInfoHelper.write(_out,getPipeQoSInfo());
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.cm.P_UNKNOWN_PIPEQOSINFO _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.cm.P_UNKNOWN_PIPEQOSINFOHelper.write(_out, _ex1);
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
