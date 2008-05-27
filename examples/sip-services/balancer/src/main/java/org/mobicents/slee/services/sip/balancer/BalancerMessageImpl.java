package org.mobicents.slee.services.sip.balancer;

import java.io.Serializable;

/**
 * @author baranowb
 *
 */
public class BalancerMessageImpl implements BalancerMessage {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2237779561165576445L;
	private Object content;
	private BalancerMessageType type;
	
	public Object getContent() {
		
		return content;
	}

	public BalancerMessageType getType() {

		return type;
	}

	public BalancerMessageImpl(Object content, BalancerMessageType type) {
		super();
		this.content = content;
		this.type = type;
	}

	public String toString()
	{
		return "BalancerMessageImpl[  Type["+this.type+"] Content["+this.content+"]   ]";
	}
	
}
