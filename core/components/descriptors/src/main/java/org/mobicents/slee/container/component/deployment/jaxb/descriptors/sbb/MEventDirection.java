package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

public enum MEventDirection {

	Fire,Receive,FireAndReceive;
	
	public static MEventDirection fromString(String s)
	{
		if(s.compareTo("Fire")==0)
		{
			return Fire;
		}else if(s.compareTo("Receive")==0)
		{
			return Receive;
		}else if(s.compareTo("FireAndReceive")==0)
		{
			return FireAndReceive;
		}else
		{
			return null;
		}
	}
	
}
