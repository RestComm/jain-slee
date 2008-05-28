package org.csapi.cm;

/**
 *	Generated from IDL definition of struct "TpProvisionedQoSInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpProvisionedQoSInfoHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.cm.TpProvisionedQoSInfo value;

	public TpProvisionedQoSInfoHolder ()
	{
	}
	public TpProvisionedQoSInfoHolder(final org.csapi.cm.TpProvisionedQoSInfo initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.cm.TpProvisionedQoSInfoHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.cm.TpProvisionedQoSInfoHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.cm.TpProvisionedQoSInfoHelper.write(_out, value);
	}
}
