package net.java.slee.resource.diameter.utils;

public class EventHandle {

    private boolean request=true;
    private int commandCode=-1;
    
    
    
    public EventHandle(boolean req,int commCode)
    {
        request=req;
        commandCode=commCode;
    }
    
    public boolean equals(Object o)
    {
    	if (o != null && o.getClass() == this.getClass()) {
    		EventHandle other=(EventHandle)o;
			return this.request == other.request && this.commandCode == other.commandCode;		
		}
		else {
			return false;
		}
    }
    
    public int hashCode()
    {
        int tmp=(request?0:1);
        //IT SHOULD BE ENOUGH 
        return commandCode*31+tmp;
    }
    public String toString()
    {
        return super.toString()+"(CODE:"+commandCode+"--REQ: "+request+")";
    }
}
