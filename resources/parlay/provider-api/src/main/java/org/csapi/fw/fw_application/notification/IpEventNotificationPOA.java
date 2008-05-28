package org.csapi.fw.fw_application.notification;

/**
 *	Generated from IDL interface "IpEventNotification"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpEventNotificationPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.fw.fw_application.notification.IpEventNotificationOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "createNotification", new java.lang.Integer(0));
		m_opsHash.put ( "destroyNotification", new java.lang.Integer(1));
	}
	private String[] ids = {"IDL:org/csapi/fw/fw_application/notification/IpEventNotification:1.0","IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.fw.fw_application.notification.IpEventNotification _this()
	{
		return org.csapi.fw.fw_application.notification.IpEventNotificationHelper.narrow(_this_object());
	}
	public org.csapi.fw.fw_application.notification.IpEventNotification _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.fw.fw_application.notification.IpEventNotificationHelper.narrow(_this_object(orb));
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
			case 0: // createNotification
			{
			try
			{
				org.csapi.fw.TpFwEventCriteria _arg0=org.csapi.fw.TpFwEventCriteriaHelper.read(_input);
				_out = handler.createReply();
				_out.write_long(createNotification(_arg0));
			}
			catch(org.csapi.P_INVALID_EVENT_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_EVENT_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
			}
			catch(org.csapi.P_INVALID_CRITERIA _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_CRITERIAHelper.write(_out, _ex3);
			}
				break;
			}
			case 1: // destroyNotification
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				destroyNotification(_arg0);
			}
			catch(org.csapi.P_INVALID_ASSIGNMENT_ID _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_ASSIGNMENT_IDHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.fw.P_ACCESS_DENIED _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.fw.P_ACCESS_DENIEDHelper.write(_out, _ex2);
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
