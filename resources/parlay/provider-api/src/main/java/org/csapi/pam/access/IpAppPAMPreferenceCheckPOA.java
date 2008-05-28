package org.csapi.pam.access;

/**
 *	Generated from IDL interface "IpAppPAMPreferenceCheck"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppPAMPreferenceCheckPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.pam.access.IpAppPAMPreferenceCheckOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "computeAvailability", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:org/csapi/pam/access/IpAppPAMPreferenceCheck:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.pam.access.IpAppPAMPreferenceCheck _this()
	{
		return org.csapi.pam.access.IpAppPAMPreferenceCheckHelper.narrow(_this_object());
	}
	public org.csapi.pam.access.IpAppPAMPreferenceCheck _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.pam.access.IpAppPAMPreferenceCheckHelper.narrow(_this_object(orb));
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
			case 0: // computeAvailability
			{
				java.lang.String _arg0=_input.read_string();
				org.csapi.pam.TpPAMContext _arg1=org.csapi.pam.TpPAMContextHelper.read(_input);
				java.lang.String[] _arg2=org.csapi.TpStringListHelper.read(_input);
				byte[] _arg3=org.csapi.TpOctetSetHelper.read(_input);
				_out = handler.createReply();
				org.csapi.pam.TpPAMAvailabilityProfileListHelper.write(_out,computeAvailability(_arg0,_arg1,_arg2,_arg3));
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
