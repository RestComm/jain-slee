package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 * 
 */
public class TLVNotPermittedException extends Exception {
	private Tag tag;

	protected TLVNotPermittedException(Tag tag, String message) {
		super(message);
		this.tag = tag;
	}

	protected TLVNotPermittedException(Tag tag) {
		this.tag = tag;
	}

	public Tag getTag() {
		return this.tag;
	}

}
