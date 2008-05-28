package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMIdentityPresence"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMIdentityPresencePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.access.IpPAMIdentityPresenceOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setIdentityPresence", new java.lang.Integer(0));
		m_opsHash.put ( "getIdentityPresence", new java.lang.Integer(1));
		m_opsHash.put ( "setIdentityPresenceExpiration", new java.lang.Integer(2));
	}
	private String[] ids = {"IDL:org/csapi/pam/access/IpPAMIdentityPresence:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.access.IpPAMIdentityPresence _this()
	{
		return org.csapi.pam.access.IpPAMIdentityPresenceHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpPAMIdentityPresence _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpPAMIdentityPresenceHelper.narrow(_this_object(orb));
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
			case 0: // setIdentityPresence
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				org.csapi.pam.TpPAMAttribute[] _arg2=org.csapi.pam.TpPAMAttributeListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				setIdentityPresence(_arg0,_arg1,_arg2,_arg3);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex4);
			}
				break;
			}
			case 1: // getIdentityPresence
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				java.lang.String[] _arg2=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAttributeListHelper.write(_out,getIdentityPresence(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex4);
			}
				break;
			}
			case 2: // setIdentityPresenceExpiration
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				java.lang.String[] _arg2=org.csapi.TpStringListHelper.read(_input);
				long _arg3=_input.read_longlong();
				byte[] _arg4=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				setIdentityPresenceExpiration(_arg0,_arg1,_arg2,_arg3,_arg4);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_TYPE _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_TYPEHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex2);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTE _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ATTRIBUTEHelper.write(_out, _ex4);
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
