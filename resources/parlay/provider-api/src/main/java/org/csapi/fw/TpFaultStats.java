package org.csapi.fw;

/**
 *	Generated from IDL definition of struct "TpFaultStats"
 *	@author JacORB IDL compiler 
 */

public final class TpFaultStats
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpFaultStats(){}
	public org.csapi.fw.TpInterfaceFault Fault;
	public int Occurrences;
	public int MaxDuration;
	public int TotalDuration;
	public int NumberOfClientsAffected;
	public TpFaultStats(org.csapi.fw.TpInterfaceFault Fault, int Occurrences, int MaxDuration, int TotalDuration, int NumberOfClientsAffected)
	{
		this.Fault = Fault;
		this.Occurrences = Occurrences;
		this.MaxDuration = MaxDuration;
		this.TotalDuration = TotalDuration;
		this.NumberOfClientsAffected = NumberOfClientsAffected;
	}
}
