package org.csapi.cc.mmccs;

/**
 *	Generated from IDL definition of struct "TpCallSuperviseVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpCallSuperviseVolume
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpCallSuperviseVolume(){}
	public int VolumeQuantity;
	public int VolumeUnit;
	public TpCallSuperviseVolume(int VolumeQuantity, int VolumeUnit)
	{
		this.VolumeQuantity = VolumeQuantity;
		this.VolumeUnit = VolumeUnit;
	}
}
