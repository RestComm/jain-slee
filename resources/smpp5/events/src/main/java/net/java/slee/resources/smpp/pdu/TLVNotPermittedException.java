package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 * 
 */
public class TLVNotPermittedException extends Exception {
	private Tag tag;

	public TLVNotPermittedException(Tag tag, String message) {
		super(message);
		this.tag = tag;
	}

	public TLVNotPermittedException(Tag tag) {
		this.tag = tag;
	}

	public Tag getTag() {
		return this.tag;
	}

}
