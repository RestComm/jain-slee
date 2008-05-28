package org.csapi;

/**
 *	Generated from IDL interface "IpInterface"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */


public abstract class IpInterfacePOA
	extends org.omg.PortableServer.Servant
	implements org.omg.CORBA.portable.InvokeHandler, org.csapi.IpInterfaceOperations
{
	private String[] ids = {"IDL:org/csapi/IpInterface:1.0"};
	public org.csapi.IpInterface _this()
	{
		return org.csapi.IpInterfaceHelper.narrow(_this_object());
	}
	public org.csapi.IpInterface _this(org.omg.CORBA.ORB orb)
	{
		return org.csapi.IpInterfaceHelper.narrow(_this_object(orb));
	}
	public org.omg.CORBA.portable.OutputStream _invoke(String method, org.omg.CORBA.portable.InputStream _input, org.omg.CORBA.portable.ResponseHandler handler)
		throws org.omg.CORBA.SystemException
	{
		org.omg.CORBA.portable.OutputStream _out = null;
		// do something
		throw new org.omg.CORBA.BAD_OPERATION(method + " not found");
	}

	public String[] _all_interfaces(org.omg.PortableServer.POA poa, byte[] obj_id)
	{
		return ids;
	}
}
