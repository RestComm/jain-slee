package org.csapi;
/**
 *	Generated from IDL definition of enum "TpAttributeTagInfo"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeTagInfo
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_SIMPLE_TYPE = 0;
	public static final TpAttributeTagInfo P_SIMPLE_TYPE = new TpAttributeTagInfo(_P_SIMPLE_TYPE);
	public static final int _P_STRUCTURED_TYPE = 1;
	public static final TpAttributeTagInfo P_STRUCTURED_TYPE = new TpAttributeTagInfo(_P_STRUCTURED_TYPE);
	public static final int _P_XML_TYPE = 2;
	public static final TpAttributeTagInfo P_XML_TYPE = new TpAttributeTagInfo(_P_XML_TYPE);
	public int value()
	{
		return value;
	}
	public static TpAttributeTagInfo from_int(int value)
	{
		switch (value) {
			case _P_SIMPLE_TYPE: return P_SIMPLE_TYPE;
			case _P_STRUCTURED_TYPE: return P_STRUCTURED_TYPE;
			case _P_XML_TYPE: return P_XML_TYPE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpAttributeTagInfo(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
