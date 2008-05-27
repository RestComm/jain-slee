package org.csapi.cm;
/**
 *	Generated from IDL definition of enum "TpSiteOrSap"
 *	@author JacORB IDL compiler 
 */

public final class TpSiteOrSap
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _SITE = 0;
	public static final TpSiteOrSap SITE = new TpSiteOrSap(_SITE);
	public static final int _SAP = 1;
	public static final TpSiteOrSap SAP = new TpSiteOrSap(_SAP);
	public int value()
	{
		return value;
	}
	public static TpSiteOrSap from_int(int value)
	{
		switch (value) {
			case _SITE: return SITE;
			case _SAP: return SAP;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpSiteOrSap(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
