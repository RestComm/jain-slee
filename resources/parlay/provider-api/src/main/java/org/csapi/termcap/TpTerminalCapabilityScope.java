package org.csapi.termcap;

/**
 *	Generated from IDL definition of struct "TpTerminalCapabilityScope"
 *	@author JacORB IDL compiler 
 */

public final class TpTerminalCapabilityScope
	implements org.omg.CORBA.portable.IDLEntity
{
	public TpTerminalCapabilityScope(){}
	public org.csapi.termcap.TpTerminalCapabilityScopeType ScopeType;
	public java.lang.String Scope;
	public TpTerminalCapabilityScope(org.csapi.termcap.TpTerminalCapabilityScopeType ScopeType, java.lang.String Scope)
	{
		this.ScopeType = ScopeType;
		this.Scope = Scope;
	}
}
