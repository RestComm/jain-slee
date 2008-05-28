package org.csapi.policy;


/**
 *	Generated from IDL interface "IpPolicyIterator"
 *	@author JacORB IDL compiler V 2.1, 16-Feb-2004
 */

public final class IpPolicyIteratorHelper
{
	public static void insert (final org.omg.CORBA.Any any, final org.csapi.policy.IpPolicyIterator s)
	{
			any.insert_Object(s);
	}
	public static org.csapi.policy.IpPolicyIterator extract(final org.omg.CORBA.Any any)
	{
		return narrow(any.extract_Object()) ;
	}
	public static org.omg.CORBA.TypeCode type()
	{
		return org.omg.CORBA.ORB.init().create_interface_tc("IDL:org/csapi/policy/IpPolicyIterator:1.0", "IpPolicyIterator");
	}
	public static String id()
	{
		return "IDL:org/csapi/policy/IpPolicyIterator:1.0";
	}
	public static IpPolicyIterator read(final org.omg.CORBA.portable.InputStream in)
	{
		return narrow(in.read_Object());
	}
	public static void write(final org.omg.CORBA.portable.OutputStream _out, final org.csapi.policy.IpPolicyIterator s)
	{
		_out.write_Object(s);
	}
	public static org.csapi.policy.IpPolicyIterator narrow(final java.lang.Object obj)
	{
		if (obj instanceof org.csapi.policy.IpPolicyIterator)
		{
			return (org.csapi.policy.IpPolicyIterator)obj;
		}
		else if (obj instanceof org.omg.CORBA.Object)
		{
			return narrow((org.omg.CORBA.Object)obj);
		}
		throw new org.omg.CORBA.BAD_PARAM("Failed to narrow in helper");
	}
	public static org.csapi.policy.IpPolicyIterator narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.policy.IpPolicyIterator)obj;
		}
		catch (ClassCastException c)
		{
			if (obj._is_a("IDL:org/csapi/policy/IpPolicyIterator:1.0"))
			{
				org.csapi.policy._IpPolicyIteratorStub stub;
				stub = new org.csapi.policy._IpPolicyIteratorStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
			}
		}
		throw new org.omg.CORBA.BAD_PARAM("Narrow failed");
	}
	public static org.csapi.policy.IpPolicyIterator unchecked_narrow(final org.omg.CORBA.Object obj)
	{
		if (obj == null)
			return null;
		try
		{
			return (org.csapi.policy.IpPolicyIterator)obj;
		}
		catch (ClassCastException c)
		{
				org.csapi.policy._IpPolicyIteratorStub stub;
				stub = new org.csapi.policy._IpPolicyIteratorStub();
				stub._set_delegate(((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate());
				return stub;
		}
	}
}
