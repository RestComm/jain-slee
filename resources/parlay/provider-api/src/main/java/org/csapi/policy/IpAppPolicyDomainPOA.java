package org.csapi.policy;

/**
 *	Generated from IDL interface "IpAppPolicyDomain"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppPolicyDomainPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.policy.IpAppPolicyDomainOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "reportNotification", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:org/csapi/policy/IpAppPolicyDomain:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.policy.IpAppPolicyDomain _this()
	{
		return org.csapi.policy.IpAppPolicyDomainHelper.narrow(_this_object());
	}
	public org.csapi.policy.IpAppPolicyDomain _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.policy.IpAppPolicyDomainHelper.narrow(_this_object(orb));
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
			case 0: // reportNotification
			{
				int _arg0=_input.read_long();
				org.csapi.policy.TpPolicyEvent _arg1=org.csapi.policy.TpPolicyEventHelper.read(_input);
				_out = handler.createReply();
				reportNotification(_arg0,_arg1);
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
