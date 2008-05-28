package org.csapi.gms;

/**
 *	Generated from IDL interface "IpMailbox"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpMailboxPOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.gms.IpMailboxOperations
{
	static private final java.util.Hashtable m_opsHash = new java.util.Hashtable();
	static
	{
		m_opsHash.put ( "setCallback", new java.lang.Integer(0));
		m_opsHash.put ( "lock", new java.lang.Integer(1));
		m_opsHash.put ( "createFolder", new java.lang.Integer(2));
		m_opsHash.put ( "openFolder", new java.lang.Integer(3));
		m_opsHash.put ( "remove", new java.lang.Integer(4));
		m_opsHash.put ( "unlock", new java.lang.Integer(5));
		m_opsHash.put ( "close", new java.lang.Integer(6));
		m_opsHash.put ( "setInfoProperties", new java.lang.Integer(7));
		m_opsHash.put ( "getInfoProperties", new java.lang.Integer(8));
		m_opsHash.put ( "setCallbackWithSessionID", new java.lang.Integer(9));
		m_opsHash.put ( "getInfoAmount", new java.lang.Integer(10));
	}
	private String[] ids = {"IDL:org/csapi/gms/IpMailbox:1.0","IDL:org/csapi/IpInterface:1.0","IDL:org/csapi/IpService:1.0"};
	public org.csapi.gms.IpMailbox _this()
	{
		return org.csapi.gms.IpMailboxHelper.narrow(_this_object());
	}
	public org.csapi.gms.IpMailbox _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.gms.IpMailboxHelper.narrow(_this_object(orb));
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
			case 0: // setCallback
			{
			try
			{
				org.csapi.IpInterface _arg0=org.csapi.IpInterfaceHelper.read(_input);
				_out = handler.createReply();
				setCallback(_arg0);
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
				break;
			}
			case 1: // lock
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				lock(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOX _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_LOCKING_LOCKED_MAILBOXHelper.write(_out, _ex2);
			}
				break;
			}
			case 2: // createFolder
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				createFolder(_arg0,_arg1);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_INVALID_FOLDER_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_FOLDER_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.gms.P_GMS_MAILBOX_LOCKED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.write(_out, _ex3);
			}
				break;
			}
			case 3: // openFolder
			{
			try
			{
				int _arg0=_input.read_long();
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				org.csapi.gms.TpMailboxFolderIdentifierHelper.write(_out,openFolder(_arg0,_arg1));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_INVALID_FOLDER_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_FOLDER_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.gms.P_GMS_FOLDER_IS_OPEN _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_FOLDER_IS_OPENHelper.write(_out, _ex3);
			}
			catch(org.csapi.gms.P_GMS_MAILBOX_LOCKED _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.write(_out, _ex4);
			}
				break;
			}
			case 4: // remove
			{
			try
			{
				org.csapi.TpAddress _arg0=org.csapi.TpAddressHelper.read(_input);
				java.lang.String _arg1=_input.read_string();
				_out = handler.createReply();
				remove(_arg0,_arg1);
			}
			catch(org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATION _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_AUTHENTICATION_INFORMATIONHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_INVALID_MAILBOX _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INVALID_MAILBOXHelper.write(_out, _ex1);
			}
			catch(org.csapi.TpCommonExceptions _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex2);
			}
			catch(org.csapi.gms.P_GMS_MAILBOX_OPEN _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_MAILBOX_OPENHelper.write(_out, _ex3);
			}
			catch(org.csapi.gms.P_GMS_MAILBOX_LOCKED _ex4)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.write(_out, _ex4);
			}
			catch(org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGE _ex5)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_INSUFFICIENT_PRIVILEGEHelper.write(_out, _ex5);
			}
				break;
			}
			case 5: // unlock
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				unlock(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOX _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_UNLOCKING_UNLOCKED_MAILBOXHelper.write(_out, _ex2);
			}
			catch(org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOX _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_CANNOT_UNLOCK_MAILBOXHelper.write(_out, _ex3);
			}
				break;
			}
			case 6: // close
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				close(_arg0);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
			}
				break;
			}
			case 7: // setInfoProperties
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				org.csapi.gms.TpMailboxInfoProperty[] _arg2=org.csapi.gms.TpMailboxInfoPropertySetHelper.read(_input);
				_out = handler.createReply();
				setInfoProperties(_arg0,_arg1,_arg2);
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.gms.P_GMS_PROPERTY_NOT_SET _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_PROPERTY_NOT_SETHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
			catch(org.csapi.gms.P_GMS_MAILBOX_LOCKED _ex3)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_MAILBOX_LOCKEDHelper.write(_out, _ex3);
			}
				break;
			}
			case 8: // getInfoProperties
			{
			try
			{
				int _arg0=_input.read_long();
				int _arg1=_input.read_long();
				int _arg2=_input.read_long();
				_out = handler.createReply();
				org.csapi.gms.TpMailboxInfoPropertySetHelper.write(_out,getInfoProperties(_arg0,_arg1,_arg2));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
			}
			catch(org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVE _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.gms.P_GMS_NUMBER_NOT_POSITIVEHelper.write(_out, _ex2);
			}
				break;
			}
			case 9: // setCallbackWithSessionID
			{
			try
			{
				org.csapi.IpInterface _arg0=org.csapi.IpInterfaceHelper.read(_input);
				int _arg1=_input.read_long();
				_out = handler.createReply();
				setCallbackWithSessionID(_arg0,_arg1);
			}
			catch(org.csapi.P_INVALID_INTERFACE_TYPE _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_INTERFACE_TYPEHelper.write(_out, _ex0);
			}
			catch(org.csapi.TpCommonExceptions _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex1);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex2)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex2);
			}
				break;
			}
			case 10: // getInfoAmount
			{
			try
			{
				int _arg0=_input.read_long();
				_out = handler.createReply();
				_out.write_long(getInfoAmount(_arg0));
			}
			catch(org.csapi.TpCommonExceptions _ex0)
			{
				_out = handler.createExceptionReply();
				org.csapi.TpCommonExceptionsHelper.write(_out, _ex0);
			}
			catch(org.csapi.P_INVALID_SESSION_ID _ex1)
			{
				_out = handler.createExceptionReply();
				org.csapi.P_INVALID_SESSION_IDHelper.write(_out, _ex1);
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
