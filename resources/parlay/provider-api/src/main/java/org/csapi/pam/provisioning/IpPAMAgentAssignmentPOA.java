package org.csapi.pam.provisioning;

/**
 *	Generated from IDL interface "IpPAMAgentAssignment"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpPAMAgentAssignmentPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.provisioning.IpPAMAgentAssignmentOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "listAssignedAgentsByCapability", new java.lang.Integer(0));
		m_opsHash.put ( "unassignAgent", new java.lang.Integer(1));
		m_opsHash.put ( "listCapabilitiesOfIdentity", new java.lang.Integer(2));
		m_opsHash.put ( "assignAgent", new java.lang.Integer(3));
		m_opsHash.put ( "listAssignedAgents", new java.lang.Integer(4));
		m_opsHash.put ( "isIdentityCapableOf", new java.lang.Integer(5));
		m_opsHash.put ( "listAssociatedIdentitiesOfAgent", new java.lang.Integer(6));
	}
	private String[] ids = {"IDL:org/csapi/pam/provisioning/IpPAMAgentAssignment:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.provisioning.IpPAMAgentAssignment _this()
	{
		return org.csapi.pam.provisioning.IpPAMAgentAssignmentHelper.narrow(_this_object());
	}
	public org.csapi.pam.provisioning.IpPAMAgentAssignment _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.provisioning.IpPAMAgentAssignmentHelper.narrow(_this_object(orb));
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
			case 0: // listAssignedAgentsByCapability
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listAssignedAgentsByCapability(_arg0,_arg1,_arg2));
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
			case 1: // unassignAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				unassignAgent(_arg0,_arg1,_arg2);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENT _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_ASSIGNMENTHelper.write(_out, _ex3);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex4);
			}
				break;
			}
			case 2: // listCapabilitiesOfIdentity
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMCapabilityListHelper.write(_out,listCapabilitiesOfIdentity(_arg0,_arg1));
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
			case 3: // assignAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				assignAgent(_arg0,_arg1,_arg2);
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
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex3);
			}
				break;
			}
			case 4: // listAssignedAgents
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listAssignedAgents(_arg0,_arg1));
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
			case 5: // isIdentityCapableOf
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				java.lang.String _arg1=_input.read_string();
				byte[] _arg2=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				_out.write_boolean(isIdentityCapableOf(_arg0,_arg1,_arg2));
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
			case 6: // listAssociatedIdentitiesOfAgent
			{
			try
			{
				java.lang.String _arg0=_input.read_string();
				byte[] _arg1=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMFQNameListHelper.write(_out,listAssociatedIdentitiesOfAgent(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.pam.P_PAM_INVALID_CREDENTIAL _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_INVALID_CREDENTIALHelper.write(_out, _ex1);
			}
			catch(org.csapi.pam.P_PAM_UNKNOWN_AGENT _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.pam.P_PAM_UNKNOWN_AGENTHelper.write(_out, _ex2);
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
