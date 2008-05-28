package org.csapi.cs;

/**
 *	Generated from IDL interface "IpAppChargingManager"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppChargingManagerPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.cs.IpAppChargingManagerOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "sessionAborted", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:org/csapi/cs/IpAppChargingManager:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.cs.IpAppChargingManager _this()
	{
		return org.csapi.cs.IpAppChargingManagerHelper.narrow(_this_object());
	}
	public org.csapi.cs.IpAppChargingManager _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.cs.IpAppChargingManagerHelper.narrow(_this_object(orb));
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
			case 0: // sessionAborted
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				sessionAborted(_arg0);
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
