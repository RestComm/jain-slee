package net.java.slee.resources.smpp.pdu;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MessageState {
	public static final int SCHEDULED = 0;
	public static final int ENROUTE = 1;
	public static final int DELIVERED = 2;
	public static final int EXPIRED = 3;
	public static final int DELETED = 4;
	public static final int UNDELIVERABLE = 5;

	public static final int ACCEPTED = 6;
	public static final int UNKNOWN = 7;
	public static final int REJECTED = 8;
	public static final int SKIPPED = 9;

}
