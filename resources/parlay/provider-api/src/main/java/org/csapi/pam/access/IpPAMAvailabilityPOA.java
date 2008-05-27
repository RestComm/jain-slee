package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpPAMAvailability"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMAvailabilityPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.access.IpPAMAvailabilityOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "getPreference", new java.lang.Integer(0));
		m_opsHash.put ( "getAvailability", new java.lang.Integer(1));
		m_opsHash.put ( "setPreference", new java.lang.Integer(2));
	}
	private String[] ids = {"IDL:org/csapi/pam/access/IpPAMAvailability:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.access.IpPAMAvailability _this()
	{
		return org.csapi.pam.access.IpPAMAvailabilityHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpPAMAvailability _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpPAMAvailabilityHelper.narrow(_this_object(orb));
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
			case 0: // getPreference
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.pam.TpPAMContext _arg1=org.csapi.pam.TpPAMContextHelper.read(_input);
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMPreferenceDataHelper.write(_out,getPreference(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 1: // getAvailability
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.pam.TpPAMContext _arg1=org.csapi.pam.TpPAMContextHelper.read(_input);
				java.lang.String[] _arg2=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAvailabilityProfileListHelper.write(_out,getAvailability(_arg0,_arg1,_arg2,_arg3));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // setPreference
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.pam.TpPAMContext _arg1=org.csapi.pam.TpPAMContextHelper.read(_input);
				java.lang.String _arg2=_input.read_string();
				org.csapi.pam.TpPAMPreferenceData _arg3=org.csapi.pam.TpPAMPreferenceDataHelper.read(_input);
				byte[] _arg4=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				setPreference(_arg0,_arg1,_arg2,_arg3,_arg4);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_IDENTITY _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_IDENTITYHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex2);
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
