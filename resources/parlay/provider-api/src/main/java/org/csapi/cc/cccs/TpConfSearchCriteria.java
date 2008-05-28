package org.csapi.cc.cccs;

/**
 *	Generated from IDL definition of struct "TpConfSearchCriteria"
 *	@author JacORB IDL compiler 
 */

public final class TpConfSearchCriteria
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpConfSearchCriteria(){}
	public java.lang.String StartSearch;
	public java.lang.String StopSearch;
	public int RequestedResources;
	public int RequestedDuration;
	public TpConfSearchCriteria(java.lang.String StartSearch, java.lang.String StopSearch, int RequestedResources, int RequestedDuration)
	{
		this.StartSearch = StartSearch;
		this.StopSearch = StopSearch;
		this.RequestedResources = RequestedResources;
		this.RequestedDuration = RequestedDuration;
	}
}
