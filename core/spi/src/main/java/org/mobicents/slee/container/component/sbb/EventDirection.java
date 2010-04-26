/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

/**
 * @author martins
 *
 */

public enum EventDirection {

	Fire(0), Receive(1), FireAndReceive(2);

	private int value = -1;

	EventDirection(int v) {
		this.value = v;
	}

	public int getValue() {
		return value;
	}

	public static EventDirection fromString(String s) {
		if ("Fire".equalsIgnoreCase(s)) {
			return Fire;
		} else if ("Receive".equalsIgnoreCase(s)) {
			return Receive;
		} else if ("FireAndReceive".equalsIgnoreCase(s)) {
			return FireAndReceive;
		} else {
			return null;
		}
	}

}