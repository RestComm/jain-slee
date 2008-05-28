package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpConfSearchResult"
 *	@author JacORB IDL compiler 
 */

public final class TpConfSearchResult
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpConfSearchResult(){}
	public boolean MatchFound;
	public java.lang.String ActualStartTime;
	public int ActualResources;
	public int ActualDuration;
	public TpConfSearchResult(boolean MatchFound, java.lang.String ActualStartTime, int ActualResources, int ActualDuration)
	{
		this.MatchFound = MatchFound;
		this.ActualStartTime = ActualStartTime;
		this.ActualResources = ActualResources;
		this.ActualDuration = ActualDuration;
	}
}
