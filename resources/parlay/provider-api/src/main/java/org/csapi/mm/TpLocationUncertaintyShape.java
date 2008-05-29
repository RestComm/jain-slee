package org.csapi.mm;
/**
 *	Generated from IDL definition of enum "TpLocationUncertaintyShape"
 *	@author JacORB IDL compiler 
 */

public final class TpLocationUncertaintyShape
	implements org.omg.CORBA.portable.IDLEntity
{
	private int value = -1;
	public static final int _P_M_SHAPE_NONE = 0;
	public static final TpLocationUncertaintyShape P_M_SHAPE_NONE = new TpLocationUncertaintyShape(_P_M_SHAPE_NONE);
	public static final int _P_M_SHAPE_CIRCLE = 1;
	public static final TpLocationUncertaintyShape P_M_SHAPE_CIRCLE = new TpLocationUncertaintyShape(_P_M_SHAPE_CIRCLE);
	public static final int _P_M_SHAPE_CIRCLE_SECTOR = 2;
	public static final TpLocationUncertaintyShape P_M_SHAPE_CIRCLE_SECTOR = new TpLocationUncertaintyShape(_P_M_SHAPE_CIRCLE_SECTOR);
	public static final int _P_M_SHAPE_CIRCLE_ARC_STRIPE = 3;
	public static final TpLocationUncertaintyShape P_M_SHAPE_CIRCLE_ARC_STRIPE = new TpLocationUncertaintyShape(_P_M_SHAPE_CIRCLE_ARC_STRIPE);
	public static final int _P_M_SHAPE_ELLIPSE = 4;
	public static final TpLocationUncertaintyShape P_M_SHAPE_ELLIPSE = new TpLocationUncertaintyShape(_P_M_SHAPE_ELLIPSE);
	public static final int _P_M_SHAPE_ELLIPSE_SECTOR = 5;
	public static final TpLocationUncertaintyShape P_M_SHAPE_ELLIPSE_SECTOR = new TpLocationUncertaintyShape(_P_M_SHAPE_ELLIPSE_SECTOR);
	public static final int _P_M_SHAPE_ELLIPSE_ARC_STRIPE = 6;
	public static final TpLocationUncertaintyShape P_M_SHAPE_ELLIPSE_ARC_STRIPE = new TpLocationUncertaintyShape(_P_M_SHAPE_ELLIPSE_ARC_STRIPE);
	public int value()
	{
		return value;
	}
	public static TpLocationUncertaintyShape from_int(int value)
	{
		switch (value) {
			case _P_M_SHAPE_NONE: return P_M_SHAPE_NONE;
			case _P_M_SHAPE_CIRCLE: return P_M_SHAPE_CIRCLE;
			case _P_M_SHAPE_CIRCLE_SECTOR: return P_M_SHAPE_CIRCLE_SECTOR;
			case _P_M_SHAPE_CIRCLE_ARC_STRIPE: return P_M_SHAPE_CIRCLE_ARC_STRIPE;
			case _P_M_SHAPE_ELLIPSE: return P_M_SHAPE_ELLIPSE;
			case _P_M_SHAPE_ELLIPSE_SECTOR: return P_M_SHAPE_ELLIPSE_SECTOR;
			case _P_M_SHAPE_ELLIPSE_ARC_STRIPE: return P_M_SHAPE_ELLIPSE_ARC_STRIPE;
			default: throw new org.omg.CORBA.BAD_PARAM();
		}
	}
	protected TpLocationUncertaintyShape(int i)
	{
		value = i;
	}
	java.lang.Object readResolve()
	throws java.io.ObjectStreamException
	{
		return from_int(value());
	}
}
