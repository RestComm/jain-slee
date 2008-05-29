package org.csapi.mm;

/**
 *	Generated from IDL definition of struct "TpGeographicalPosition"
 *	@author JacORB IDL compiler 
 */

public final class TpGeographicalPosition
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpGeographicalPosition(){}
	public float Longitude;
	public float Latitude;
	public org.csapi.mm.TpLocationUncertaintyShape TypeOfUncertaintyShape;
	public float UncertaintyInnerSemiMajor;
	public float UncertaintyOuterSemiMajor;
	public float UncertaintyInnerSemiMinor;
	public float UncertaintyOuterSemiMinor;
	public int AngleOfSemiMajor;
	public int SegmentStartAngle;
	public int SegmentEndAngle;
	public TpGeographicalPosition(float Longitude, float Latitude, org.csapi.mm.TpLocationUncertaintyShape TypeOfUncertaintyShape, float UncertaintyInnerSemiMajor, float UncertaintyOuterSemiMajor, float UncertaintyInnerSemiMinor, float UncertaintyOuterSemiMinor, int AngleOfSemiMajor, int SegmentStartAngle, int SegmentEndAngle)
	{
		this.Longitude = Longitude;
		this.Latitude = Latitude;
		this.TypeOfUncertaintyShape = TypeOfUncertaintyShape;
		this.UncertaintyInnerSemiMajor = UncertaintyInnerSemiMajor;
		this.UncertaintyOuterSemiMajor = UncertaintyOuterSemiMajor;
		this.UncertaintyInnerSemiMinor = UncertaintyInnerSemiMinor;
		this.UncertaintyOuterSemiMinor = UncertaintyOuterSemiMinor;
		this.AngleOfSemiMajor = AngleOfSemiMajor;
		this.SegmentStartAngle = SegmentStartAngle;
		this.SegmentEndAngle = SegmentEndAngle;
	}
}
