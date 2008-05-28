package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAddressPresentation"
 *	@author JacORB IDL compiler 
 */

public final class TpAddressPresentation
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_ADDRESS_PRESENTATION_UNDEFINED = 0;
	public static final TpAddressPresentation P_ADDRESS_PRESENTATION_UNDEFINED = new TpAddressPresentation(_P_ADDRESS_PRESENTATION_UNDEFINED);
	public static final int _P_ADDRESS_PRESENTATION_ALLOWED = 1;
	public static final TpAddressPresentation P_ADDRESS_PRESENTATION_ALLOWED = new TpAddressPresentation(_P_ADDRESS_PRESENTATION_ALLOWED);
	public static final int _P_ADDRESS_PRESENTATION_RESTRICTED = 2;
	public static final TpAddressPresentation P_ADDRESS_PRESENTATION_RESTRICTED = new TpAddressPresentation(_P_ADDRESS_PRESENTATION_RESTRICTED);
	public static final int _P_ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE = 3;
	public static final TpAddressPresentation P_ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE = new TpAddressPresentation(_P_ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE);
	public int value()
	{
		return value;
	}
	public static TpAddressPresentation from_int(int value)
	{
		switch (value) {
			case _P_ADDRESS_PRESENTATION_UNDEFINED: return P_ADDRESS_PRESENTATION_UNDEFINED;
			case _P_ADDRESS_PRESENTATION_ALLOWED: return P_ADDRESS_PRESENTATION_ALLOWED;
			case _P_ADDRESS_PRESENTATION_RESTRICTED: return P_ADDRESS_PRESENTATION_RESTRICTED;
			case _P_ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE: return P_ADDRESS_PRESENTATION_ADDRESS_NOT_AVAILABLE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAddressPresentation(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
