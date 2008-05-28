package org.csapi.fw.fw_service.integrity;

/**
 *	Generated from IDL interface "IpFwHeartBeat"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpFwHeartBeatPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_service.integrity.IpFwHeartBeatOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "pulse", new java.lang.Integer(0));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_service/integrity/IpFwHeartBeat:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_service.integrity.IpFwHeartBeat _this()
	{
		return org.csapi.fw.fw_service.integrity.IpFwHeartBeatHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_service.integrity.IpFwHeartBeat _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_service.integrity.IpFwHeartBeatHelper.narrow(_this_object(orb));
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
			case 0: // pulse
			{
			try
			{
				_out = handler.createReply();
				pulse();
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
