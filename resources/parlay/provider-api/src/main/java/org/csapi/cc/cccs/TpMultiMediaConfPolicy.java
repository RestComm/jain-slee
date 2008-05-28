package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpMultiMediaConfPolicy"
 *	@author JacORB IDL compiler 
 */

public final class TpMultiMediaConfPolicy
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpMultiMediaConfPolicy(){}
	public boolean JoinAllowed;
	public int MediaAllowed;
	public boolean Chaired;
	public org.csapi.cc.cccs.TpVideoHandlingType VideoHandling;
	public TpMultiMediaConfPolicy(boolean JoinAllowed, int MediaAllowed, boolean Chaired, org.csapi.cc.cccs.TpVideoHandlingType VideoHandling)
	{
		this.JoinAllowed = JoinAllowed;
		this.MediaAllowed = MediaAllowed;
		this.Chaired = Chaired;
		this.VideoHandling = VideoHandling;
	}
}
