package org.mobicents.slee.resource.rules.ra;

import org.mobicents.slee.resource.rules.ratype.CallFact;

public class CallFactImpl implements CallFact {
	private String fromUri;

	private String toUri;
	
	private String subMenu = "NULL";
	
	private String dtmf = "NULL";	
	
	public CallFactImpl(){
		
	}
	
	public CallFactImpl(String fromUri, String toUri){
		this.fromUri = fromUri;
		this.toUri = toUri;
		
	}

	public String getFromUri() {
		return fromUri;
	}

	public void setFromUri(String fromUri) {
		this.fromUri = fromUri;
	}

	public String getToUri() {
		return toUri;
	}

	public void setToUri(String toUri) {
		this.toUri = toUri;
	}
	
	public String getSubMenu() {
		return subMenu;
	}

	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}

	public String getDtmf() {
		return dtmf;
	}

	public void setDtmf(String dtmf) {
		this.dtmf = dtmf;
	}	

}
