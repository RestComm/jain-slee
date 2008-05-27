package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpAppHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpAppHeartBeatMgmtPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmtOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "disableAppHeartBeat", new java.lang.Integer(0));
		m_opsHash.put ( "enableAppHeartBeat", new java.lang.Integer(1));
		m_opsHash.put ( "changeInterval", new java.lang.Integer(2));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpAppHeartBeatMgmt:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmt _this()
	{
		return org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmtHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmt _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpAppHeartBeatMgmtHelper.narrow(_this_object(orb));
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
			case 0: // disableAppHeartBeat
			{
				_out = handler.createReply();
				disableAppHeartBeat();
				break;
			}
			case 1: // enableAppHeartBeat
			{
				int _arg0=_input.read_long();
				org.csapi.fw.fw_application.integrity.IpHeartBeat _arg1=org.csapi.fw.fw_application.integrity.IpHeartBeatHelper.read(_input);
				_out = handler.createReply();
				enableAppHeartBeat(_arg0,_arg1);
				break;
			}
			case 2: // changeInterval
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				changeInterval(_arg0);
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
