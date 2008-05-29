package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpSignatureAndServiceMgr"
 *	@author JacORB IDL compiler 
 */

public final class TpSignatureAndServiceMgrHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.fw.TpSignatureAndServiceMgr value;

	public TpSignatureAndServiceMgrHolder ()
	{
	}
	public TpSignatureAndServiceMgrHolder(final org.csapi.fw.TpSignatureAndServiceMgr initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.fw.TpSignatureAndServiceMgrHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.fw.TpSignatureAndServiceMgrHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.fw.TpSignatureAndServiceMgrHelper.write(_out, value);
	}
}
