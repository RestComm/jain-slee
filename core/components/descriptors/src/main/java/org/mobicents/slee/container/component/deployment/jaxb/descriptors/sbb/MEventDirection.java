package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

public enum MEventDirection {

	Fire(0), Receive(1), FireAndReceive(2);

	private int value = -1;

	MEventDirection(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

	public static MEventDirection fromString(String s) {
		if (s.compareTo("Fire") == 0) {
			return Fire;
		} else if (s.compareTo("Receive") == 0) {
			return Receive;
		} else if (s.compareTo("FireAndReceive") == 0) {
			return FireAndReceive;
		} else {
			return null;
		}
	}

}
