package org.csapi;

/**
 *	Generated from IDL definition of union "TpAttributeValue"
 *	@author JacORB IDL compiler 
 */

public final class TpAttributeValue
	implements org.omg.CORBA.portable.IDLEntity
{
	private org.csapi.TpAttributeTagInfo discriminator;
	private org.csapi.TpSimpleAttributeValue SimpleValue;
	private org.csapi.TpStructuredAttributeValue StructuredValue;
	private java.lang.String XMLValue;

	public TpAttributeValue ()
	{
	}

	public org.csapi.TpAttributeTagInfo discriminator ()
	{
		return discriminator;
	}

	public org.csapi.TpSimpleAttributeValue SimpleValue ()
	{
		if (discriminator != org.csapi.TpAttributeTagInfo.P_SIMPLE_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return SimpleValue;
	}

	public void SimpleValue (org.csapi.TpSimpleAttributeValue _x)
	{
		discriminator = org.csapi.TpAttributeTagInfo.P_SIMPLE_TYPE;
		SimpleValue = _x;
	}

	public org.csapi.TpStructuredAttributeValue StructuredValue ()
	{
		if (discriminator != org.csapi.TpAttributeTagInfo.P_STRUCTURED_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return StructuredValue;
	}

	public void StructuredValue (org.csapi.TpStructuredAttributeValue _x)
	{
		discriminator = org.csapi.TpAttributeTagInfo.P_STRUCTURED_TYPE;
		StructuredValue = _x;
	}

	public java.lang.String XMLValue ()
	{
		if (discriminator != org.csapi.TpAttributeTagInfo.P_XML_TYPE)
			throw new org.omg.CORBA.BAD_OPERATION();
		return XMLValue;
	}

	public void XMLValue (java.lang.String _x)
	{
		discriminator = org.csapi.TpAttributeTagInfo.P_XML_TYPE;
		XMLValue = _x;
	}

}
