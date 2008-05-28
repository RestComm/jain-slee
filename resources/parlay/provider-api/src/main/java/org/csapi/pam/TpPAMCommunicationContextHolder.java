package org.csapi.pam;

/**
 *	Generated from IDL definition of struct "TpPAMCommunicationContext"
 *	@author JacORB IDL compiler 
 */

public final class TpPAMCommunicationContextHolder
	implements org.omg.CORBA.portable.Streamable
{
	public org.csapi.pam.TpPAMCommunicationContext value;

	public TpPAMCommunicationContextHolder ()
	{
	}
	public TpPAMCommunicationContextHolder(final org.csapi.pam.TpPAMCommunicationContext initial)
	{
		value = initial;
	}
	public org.omg.CORBA.TypeCode _type ()
	{
		return org.csapi.pam.TpPAMCommunicationContextHelper.type ();
	}
	public void _read(final org.omg.CORBA.portable.InputStream _in)
	{
		value = org.csapi.pam.TpPAMCommunicationContextHelper.read(_in);
	}
	public void _write(final org.omg.CORBA.portable.OutputStream _out)
	{
		org.csapi.pam.TpPAMCommunicationContextHelper.write(_out, value);
	}
}
