package org.csapi.dsc;

/**
 *	Generated from IDL definition of struct "TpDataSessionSuperviseVolume"
 *	@author JacORB IDL compiler 
 */

public final class TpDataSessionSuperviseVolume
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpDataSessionSuperviseVolume(){}
	public int VolumeQuantity;
	public int VolumeUnit;
	public TpDataSessionSuperviseVolume(int VolumeQuantity, int VolumeUnit)
	{
		this.VolumeQuantity = VolumeQuantity;
		this.VolumeUnit = VolumeUnit;
	}
}
