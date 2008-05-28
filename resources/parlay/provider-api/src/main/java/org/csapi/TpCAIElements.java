package org.csapi;

/**
 *	Generated from IDL definition of struct "TpCAIElements"
 *	@author JacORB IDL compiler 
 */

public final class TpCAIElements
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCAIElements(){}
	public int UnitsPerInterval;
	public int SecondsPerTimeInterval;
	public int ScalingFactor;
	public int UnitIncrement;
	public int UnitsPerDataInterval;
	public int SegmentsPerDataInterval;
	public int InitialSecsPerTimeInterval;
	public TpCAIElements(int UnitsPerInterval, int SecondsPerTimeInterval, int ScalingFactor, int UnitIncrement, int UnitsPerDataInterval, int SegmentsPerDataInterval, int InitialSecsPerTimeInterval)
	{
		this.UnitsPerInterval = UnitsPerInterval;
		this.SecondsPerTimeInterval = SecondsPerTimeInterval;
		this.ScalingFactor = ScalingFactor;
		this.UnitIncrement = UnitIncrement;
		this.UnitsPerDataInterval = UnitsPerDataInterval;
		this.SegmentsPerDataInterval = SegmentsPerDataInterval;
		this.InitialSecsPerTimeInterval = InitialSecsPerTimeInterval;
	}
}
