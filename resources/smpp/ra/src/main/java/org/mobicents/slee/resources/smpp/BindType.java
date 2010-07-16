package org.mobicents.slee.resources.smpp;

/**
 * 
 * @author amit bhayani
 *
 */
public enum BindType {
	TRANSMITTER("TRANSMITTER"), RECEIVER("RECEIVER"), TRANSCEIVER("TRANSCEIVER");

	private String type;

	private BindType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public static BindType getBindType(String type) {
		if (type == null) {
			return null;
		} else if (type.compareTo(TRANSMITTER.getType()) == 0) {
			return TRANSMITTER;
		} else if (type.compareTo(RECEIVER.getType()) == 0) {
			return RECEIVER;
		} else if (type.compareTo(TRANSCEIVER.getType()) == 0) {
			return TRANSCEIVER;
		}

		return null;
	}
}
