package org.csapi.fw.fw_application.integrity;

/**
 *	Generated from IDL interface "IpHeartBeatMgmt"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpHeartBeatMgmtPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_application.integrity.IpHeartBeatMgmtOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "enableHeartBeat", new java.lang.Integer(0));
		m_opsHash.put ( "disableHeartBeat", new java.lang.Integer(1));
		m_opsHash.put ( "changeInterval", new java.lang.Integer(2));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_application/integrity/IpHeartBeatMgmt:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_application.integrity.IpHeartBeatMgmt _this()
	{
		return org.csapi.fw.fw_application.integrity.IpHeartBeatMgmtHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.integrity.IpHeartBeatMgmt _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.integrity.IpHeartBeatMgmtHelper.narrow(_this_object(orb));
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
			case 0: // enableHeartBeat
			{
			try
			{
				int _arg0=_input.read_long();
				org.csapi.fw.fw_application.integrity.IpAppHeartBeat _arg1=org.csapi.fw.fw_application.integrity.IpAppHeartBeatHelper.read(_input);
				_out = handler.createReply();
				enableHeartBeat(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 1: // disableHeartBeat
			{
			try
			{
				_out = handler.createReply();
				disableHeartBeat();
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
				break;
			}
			case 2: // changeInterval
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				changeInterval(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
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
